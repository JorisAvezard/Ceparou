package com.example.groov.my_ceparou;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

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
 * Created by groov on 31/05/2017.
 */

public class MoteurActivity extends FragmentActivity {

    String building_id = "";
    String id_path = "";
    String id_coord = "";
    String last_idPlace = "";
    String id_place = "";
    String id_client = "";
    SendRequest sendRequest = new SendRequest();
    Gson gson = new GsonBuilder().create();

    IALocationManager mLocationManager;
    double latitude = 0;
    double longitude = 0;

    private static final float HUE_ORANGE = 200.0f;

    /* used to decide when bitmap should be downscaled */
    private static final int MAX_DIMENSION = 2048;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker mMarker;
    private IARegion mOverlayFloorPlan = null;
    private GroundOverlay mGroundOverlay = null;
    private IAResourceManager mResourceManager;
    private IATask<IAFloorPlan> mFetchFloorPlanTask;
    private Target mLoadTarget;
    private IALocationRequest iaRequest = IALocationRequest.create();
    private boolean mCameraPositionNeedsUpdating = true; // update on first location

    IALocationListener mLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {
            if (mMap == null) {
                // location received before map is initialized, ignoring update here
                return;
            }

            LatLng latLng = new LatLng(iaLocation.getLatitude(), iaLocation.getLongitude());

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
            latitude = iaLocation.getLatitude();
            longitude = iaLocation.getLongitude();
            MyAsynTask task = new MyAsynTask();
            task.execute(latitude, longitude);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

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
        setContentView(R.layout.activity_moteur);

        Bundle extras = getIntent().getExtras();
        id_client = String.valueOf(extras.getInt("id_client"));

        mLocationManager = IALocationManager.create(this);
        mResourceManager = IAResourceManager.create(this);

        Button bouton_page_parametres_from_moteur = (Button) findViewById(R.id.bouton_page_parametres_from_moteur);
        bouton_page_parametres_from_moteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoteurActivity.this, ParametresActivity.class);
                intent.putExtra("id_client", id_client);
                startActivity(intent);
            }
        });

        Button bouton_calcul = (Button) findViewById(R.id.bouton_calcul);
        bouton_calcul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText recherche = (EditText) findViewById(R.id.edit_text_recherche);
                String arrive = recherche.getText().toString();
                final RadioGroup choix = (RadioGroup) findViewById(R.id.radio_option_affichage);
                int selectedId = choix.getCheckedRadioButtonId();

                System.out.println("idListe:"+R.id.radio_option_affichage_liste);
                System.out.println("idPlan:"+R.id.radio_option_affichage_plan);
                System.out.println("id sélectionnée:"+selectedId);
                System.out.println("Calcul à effectuer avec arrive='"+arrive+"'");

                if(selectedId==R.id.radio_option_affichage_liste){
                    System.out.println("Affichage choisi:Liste ");
                }else{
                    System.out.println("Affichage choisi:Plan ");
                }
                Intent intent = new Intent(MoteurActivity.this,GMClientActivity.class);
                intent.putExtra("id_client", id_client);
                startActivity(intent);
            }
        });

        Button bouton_deconnexion = (Button) findViewById(R.id.bouton_deconnexion);
        bouton_deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //arrêter la session commencée lors de la connexion
                startActivity(new Intent(MoteurActivity.this, MainActivity.class));
            }
        });
    }

    protected void onResume() {
        super.onResume();
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
        }
        // start receiving location updates & monitor region change
        iaRequest.setFastestInterval(10000);
        mLocationManager.requestLocationUpdates(iaRequest, mLocationListener);

        mLocationManager.registerRegionListener(mRegionListener);
    }

    protected void onPause() {
        super.onPause();
        mLocationManager.removeLocationUpdates(mLocationListener);
        mLocationManager.registerRegionListener(mRegionListener);
    }

    protected void onDestroy() {
        mLocationManager.destroy();
        super.onDestroy();
    }

    public class MyAsynTask extends AsyncTask<Double, Integer, String> {

        @Override
        protected String doInBackground(Double... arg0) {
            double lat = arg0[0];
            double lon = arg0[1];
            String place = "";
            try {
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/selectIdPName/" + lat + "/" + lon);

                InputStream inputStream = sendRequest.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    place = gson.fromJson(reader, String.class);
                    System.out.println(place);
                    if((place == null) || (place.equals(""))){
                        place="(en recherche de localisation)";
                        return place;
                    }
                    return place;


                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            return "(en recherche de localisation)";
        }

        @Override
        protected void onPostExecute(String result) {
            TextView affichage_position = (TextView) findViewById(R.id.lat_lng);
            affichage_position.setText("Vous êtes en " + result + "\n(" + latitude + ", " + longitude + ")");
            MyAsynTaskBuildingId task1 = new MyAsynTaskBuildingId();
            task1.execute(String.valueOf(latitude), String.valueOf(longitude));
        }
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
            MyAsynTaskPathId task2 = new MyAsynTaskPathId();
            task2.execute();
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
            MyAsynTaskCoordId task3 = new MyAsynTaskCoordId();
            task3.execute();
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
            MyAsynTaskPlaceId task4 = new MyAsynTaskPlaceId();
            task4.execute(String.valueOf(latitude), String.valueOf(longitude));
        }
    }

    public class MyAsynTaskPlaceId extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... arg0) {
            String lat = arg0[0];
            String lon = arg0[1];
            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/selectIdPlace/" + lat + "/" + lon);

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
            MyAsynTaskLastId task5 = new MyAsynTaskLastId();
            task5.execute(String.valueOf(id_client));
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
            System.out.println("last_idPlace : " + last_idPlace);
            //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            //String date = String.valueOf(timestamp);
            if(!last_idPlace.equals(id_place)) {
                System.out.println("Nouvelle entrée dans Paths !");
                MyAsynTaskNewPath newPath = new MyAsynTaskNewPath();
                System.out.println("id_path : " + id_path + ", id_coord : " + id_coord +
                        ", latitude : " + String.valueOf(latitude) + ", longitude : " + String.valueOf(longitude) +
                        ", id_client : " + String.valueOf(id_client) +
                        ", building_id : " + building_id);
                newPath.execute(id_path, id_coord, String.valueOf(latitude), String.valueOf(longitude), String.valueOf(id_client), building_id);
            }
            else {
                System.out.println("Pas de nouveau Paths cette fois !");
            }
        }
    }

    public class MyAsynTaskNewPath extends AsyncTask<String, Integer, Void> {


        @Override
        protected Void doInBackground(String... arg0) {
            String idPath = arg0[0];
            String idCoord = arg0[1];
            String lati = arg0[2];
            String longi = arg0[3];
            String idUser = arg0[4];
            String idBuilding = arg0[5];
            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/newPath/" + idPath + "/"
                        + idCoord + "/" + lati + "/" + longi + "/" + idUser + "/" + idBuilding);

                InputStream inputStream = sendRequest.sendRequest(url);

                return null;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void bool) {

        }
    }

    public class MyAsynTaskPrediction extends AsyncTask<Integer, Integer, ArrayList> {


        @Override
        protected ArrayList doInBackground(Integer... arg0) {
            int id_user = arg0[0];
            ArrayList<String> list_predictions = new ArrayList<String>();
            Type listType = new TypeToken<ArrayList<Building>>(){}.getType();

            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/predict/" + id_user);

                InputStream inputStream = sendRequest.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    list_predictions = gson.fromJson(reader, listType);
                    return list_predictions;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            
        }
    }
}
