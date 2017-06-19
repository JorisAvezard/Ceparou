package com.example.groov.my_ceparou;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * Created by Joris on 18/06/2017.
 */

public class GMClientActivity extends AppCompatActivity {

    private static final float HUE_ORANGE = 200.0f;

    /* used to decide when bitmap should be downscaled */
    private static final int MAX_DIMENSION = 2048;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker mMarker;
    private IARegion mOverlayFloorPlan = null;
    private GroundOverlay mGroundOverlay = null;
    private IALocationManager mIALocationManager;
    private IAResourceManager mResourceManager;
    private IATask<IAFloorPlan> mFetchFloorPlanTask;
    private Target mLoadTarget;
    private boolean mCameraPositionNeedsUpdating = true; // update on first location
    private double longitude = 0;
    private double latitude = 0;
    Bundle extras = getIntent().getExtras();
    String building_id = "";
    String id_path = "";
    String id_coord = "";
    String last_idPlace = "";
    String id_place = "";
    boolean ok1 = false;
    boolean ok2 = false;
    boolean ok3 = false;
    boolean ok4 = false;
    boolean ok5 = false;
    boolean test = true;
    SendRequest sendRequest = new SendRequest();
    Gson gson = new GsonBuilder().create();
    final int id_client = extras.getInt("id_client");

    private IALocationListener mListener = new IALocationListenerSupport() {

        /**
         * Location changed, move marker and camera position.
         */
        @Override
        public void onLocationChanged(IALocation location) {

            if (mMap == null) {
                // location received before map is initialized, ignoring update here
                return;
            }

            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

            if (mMarker == null) {
                // first location, add marker
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(HUE_ORANGE)));
            } else {
                // move existing markers position to received location
                mMarker.setPosition(latLng);
            }

            // our camera position needs updating if location has significantly changed
            if (mCameraPositionNeedsUpdating) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5f));
                mCameraPositionNeedsUpdating = false;
            }
            ok1 = false;
            ok2 = false;
            ok3 = false;
            ok4 = false;
            ok5 = false;
            final String user_id = extras.getString("user_id");
            MyAsynTaskBuildingId task1 = new MyAsynTaskBuildingId();
            task1.execute(String.valueOf(latitude), String.valueOf(longitude));
            MyAsynTaskPathId task2 = new MyAsynTaskPathId();
            task2.execute();
            MyAsynTaskCoordId task3 = new MyAsynTaskCoordId();
            task3.execute();
            MyAsynTaskPlaceId task4 = new MyAsynTaskPlaceId();
            task4.execute(String.valueOf(latitude), String.valueOf(longitude));
            MyAsynTaskLastId task5 = new MyAsynTaskLastId();
            task5.execute(user_id);
            test = true;
            if(!last_idPlace.equals(id_place)) {
                while (test == true) {
                    if (ok1 == true && ok2 == true && ok3 == true && ok4 == true && ok5 == true) {
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                        String date = String.valueOf(timestamp);
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        MyAsynTaskNewPath newPath = new MyAsynTaskNewPath();
                        newPath.execute(id_path, id_coord, String.valueOf(latitude), String.valueOf(longitude), date, user_id, building_id);
                        test = false;
                    }
                }
            }
        }
    };

    /**
     * Listener that changes overlay if needed
     */
    private IARegion.Listener mRegionListener = new IARegion.Listener() {

        @Override
        public void onEnterRegion(IARegion region) {
            if (region.getType() == IARegion.TYPE_FLOOR_PLAN) {
                final String newId = region.getId();
                // Are we entering a new floor plan or coming back the floor plan we just left?
                if (mGroundOverlay == null || !region.equals(mOverlayFloorPlan)) {
                    mCameraPositionNeedsUpdating = true; // entering new fp, need to move camera
                    if (mGroundOverlay != null) {
                        mGroundOverlay.remove();
                        mGroundOverlay = null;
                    }
                    mOverlayFloorPlan = region; // overlay will be this (unless error in loading)
                    fetchFloorPlan(newId);
                } else {
                    mGroundOverlay.setTransparency(0.0f);
                }
            }
        }

        @Override
        public void onExitRegion(IARegion region) {
            if (mGroundOverlay != null) {
                // Indicate we left this floor plan but leave it there for reference
                // If we enter another floor plan, this one will be removed and another one loaded
                mGroundOverlay.setTransparency(0.5f);
            }
            /*showInfo("Enter " + (region.getType() == IARegion.TYPE_VENUE
                    ? "VENUE "
                    : "FLOOR_PLAN ") + region.getId());*/
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm);
        // prevent the screen going to sleep while app is on foreground
        findViewById(android.R.id.content).setKeepScreenOn(true);

        // instantiate IALocationManager and IAResourceManager
        mIALocationManager = IALocationManager.create(this);
        mResourceManager = IAResourceManager.create(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // remember to clean up after ourselves
        mIALocationManager.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
        }

        // start receiving location updates & monitor region changes
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mListener);
        mIALocationManager.registerRegionListener(mRegionListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister location & region changes
        mIALocationManager.removeLocationUpdates(mListener);
        mIALocationManager.registerRegionListener(mRegionListener);
    }

    /**
     * Sets bitmap of floor plan as ground overlay on Google Maps
     */
    private void setupGroundOverlay(IAFloorPlan floorPlan, Bitmap bitmap) {

        if (mGroundOverlay != null) {
            mGroundOverlay.remove();
        }

        if (mMap != null) {
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
            IALatLng iaLatLng = floorPlan.getCenter();
            LatLng center = new LatLng(iaLatLng.latitude, iaLatLng.longitude);
            GroundOverlayOptions fpOverlay = new GroundOverlayOptions()
                    .image(bitmapDescriptor)
                    .position(center, floorPlan.getWidthMeters(), floorPlan.getHeightMeters())
                    .bearing(floorPlan.getBearing());

            mGroundOverlay = mMap.addGroundOverlay(fpOverlay);
        }
    }

    /**
     * Download floor plan using Picasso library.
     */
    private void fetchFloorPlanBitmap(final IAFloorPlan floorPlan) {

        //final String url = floorPlan.getUrl();
        final String url = floorPlan.getUrl();

        if (mLoadTarget == null) {
            mLoadTarget = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    setupGroundOverlay(floorPlan, bitmap);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    // N/A
                }

                @Override
                public void onBitmapFailed(Drawable placeHolderDraweble) {
                    //showInfo("Failed to load bitmap");
                    mOverlayFloorPlan = null;
                }
            };
        }

        RequestCreator request = Picasso.with(this).load(url);

        final int bitmapWidth = floorPlan.getBitmapWidth();
        final int bitmapHeight = floorPlan.getBitmapHeight();

        if (bitmapHeight > MAX_DIMENSION) {
            request.resize(0, MAX_DIMENSION);
        } else if (bitmapWidth > MAX_DIMENSION) {
            request.resize(MAX_DIMENSION, 0);
        }

        request.into(mLoadTarget);
    }

    /**
     * Fetches floor plan data from IndoorAtlas server.
     */
    private void fetchFloorPlan(String id) {

        // if there is already running task, cancel it
        cancelPendingNetworkCalls();

        final IATask<IAFloorPlan> task = mResourceManager.fetchFloorPlanWithId(id);

        task.setCallback(new IAResultCallback<IAFloorPlan>() {

            @Override
            public void onResult(IAResult<IAFloorPlan> result) {

                if (result.isSuccess() && result.getResult() != null) {
                    // retrieve bitmap for this floor plan metadata
                    fetchFloorPlanBitmap(result.getResult());
                } else {
                    // ignore errors if this task was already canceled
                    if (!task.isCancelled()) {
                        // do something with error
                        //showInfo("Loading floor plan failed: " + result.getError());
                        mOverlayFloorPlan = null;
                    }
                }
            }
        }, Looper.getMainLooper()); // deliver callbacks using main looper

        // keep reference to task so that it can be canceled if needed
        mFetchFloorPlanTask = task;

    }

    /**
     * Helper method to cancel current task if any.
     */
    private void cancelPendingNetworkCalls() {
        if (mFetchFloorPlanTask != null && !mFetchFloorPlanTask.isCancelled()) {
            mFetchFloorPlanTask.cancel();
        }
    }


    public class MyAsynTaskBuildingId extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... arg0) {
            String lat = arg0[0];
            String lon = arg0[1];
            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/selectIdB/" + lat + "/" + lon);

                InputStream inputStream = sendRequest.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    String id = gson.fromJson(reader, String.class);
                    return id;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            building_id = result;
            System.out.println("id_building : " + result);
        }
    }

    public class MyAsynTaskPathId extends AsyncTask<Void, Integer, String> {


        @Override
        protected String doInBackground(Void... arg0) {
            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/newID");

                InputStream inputStream = sendRequest.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    String id = gson.fromJson(reader, String.class);
                    return id;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            id_path = result;
            System.out.println("id_path : " + result);
        }
    }

    public class MyAsynTaskCoordId extends AsyncTask<Void, Integer, String> {


        @Override
        protected String doInBackground(Void... arg0) {
            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/newCoord");

                InputStream inputStream = sendRequest.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    String id = gson.fromJson(reader, String.class);
                    return id;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            id_coord = result;
            System.out.println("id_coord : " + result);
        }
    }

    public class MyAsynTaskPlaceId extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... arg0) {
            String lat = arg0[0];
            String lon = arg0[1];
            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/selectIdP/" + lat + "/" + lon);

                InputStream inputStream = sendRequest.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    String id = gson.fromJson(reader, String.class);
                    return id;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            last_idPlace = result;
            System.out.println("last_idPlace : " + result);
        }
    }

    public class MyAsynTaskLastId extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... arg0) {
            String place_id = arg0[0];
            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/selectIdP/" + place_id);

                InputStream inputStream = sendRequest.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    String id = gson.fromJson(reader, String.class);
                    return id;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            id_place = result;
            System.out.println("id_place : " + result);
        }
    }

    public class MyAsynTaskNewPath extends AsyncTask<String, Integer, Boolean> {


        @Override
        protected Boolean doInBackground(String... arg0) {
            String idPath = arg0[0];
            String idCoord = arg0[1];
            String lati = arg0[2];
            String longi = arg0[3];
            String date = arg0[4];
            String idUser = arg0[5];
            String idBuilding = arg0[6];
            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/newPath/" + idPath + "/"
                        + idCoord + "/" + lati + "/" + longi + "/" + date + "/" + idUser + "/" + idBuilding);

                InputStream inputStream = sendRequest.sendRequest(url);

                return false;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            test = bool;

        }
    }

    public class MyAsynTaskPrediction extends AsyncTask<Integer, Integer, ArrayList> {


        @Override
        protected ArrayList doInBackground(Integer... arg0) {
            int id_user = arg0[0];
            ArrayList<String> list = new ArrayList<String>();
            Type listType = new TypeToken<ArrayList<Building>>(){}.getType();

            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/predict/" + id_user);

                InputStream inputStream = sendRequest.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    list = gson.fromJson(reader, listType);
                    return list;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            //QUE FAIT-ON DU RESULTAT ?
        }
    }

}
