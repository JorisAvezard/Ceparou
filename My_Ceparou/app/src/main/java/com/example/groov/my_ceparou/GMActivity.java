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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by groov on 06/06/2017.
 */

public class GMActivity extends FragmentActivity {

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
    private String longitude = "0";
    private String latitude = "0";
    private SendRequest request = new SendRequest();
    private int state = 0;
    private int semi_state = 0;
    private List<String> list_longitude_areas = new ArrayList<String>();
    private List<String> list_latitude_areas = new ArrayList<String>();
    private List<String> list_longitude_walls = new ArrayList<String>();
    private List<String> list_latitude_walls = new ArrayList<String>();

    /**
     * Listener that handles location change events.
     */
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

            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
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

        Bundle extras = getIntent().getExtras();
        final TextView type_entree = (TextView) findViewById(R.id.type_entree);
        final Button save_area = (Button) findViewById(R.id.save_area);
        final Button save_walls = (Button) findViewById(R.id.save_walls);
        final Button save_all = (Button) findViewById(R.id.save_all);
        final Button return_save_place = (Button) findViewById(R.id.return_save_place);
        type_entree.setText("Rentrez les points pour l'Area :");
        save_area.setEnabled(true);
        save_walls.setEnabled(false);
        final String name_place = extras.getString("place");
        final String id_building = extras.getString("id_building");
        System.out.println("Place + building : " + name_place + " - " + id_building);

        save_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Rentrez les coordonnees dans une liste
                list_latitude_areas.add(latitude);
                list_longitude_areas.add(longitude);
                state = 0;
                if(semi_state == 0) {
                    semi_state = 1;
                    save_all.setEnabled(false);
                }
                else {
                    semi_state = 0;
                    save_all.setEnabled(true);
                }
            }
        });

        save_walls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Rentrez les coordonnees dans une liste
                list_latitude_walls.add(latitude);
                list_longitude_walls.add(longitude);
                state = 1;
                if(semi_state == 0) {
                    semi_state = 1;
                    save_all.setEnabled(false);
                }
                else {
                    semi_state = 0;
                    save_all.setEnabled(true);
                }
            }
        });

        save_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state == 0) {
                    type_entree.setText("Rentrez les points pour les walls :");
                    save_area.setEnabled(false);
                    save_walls.setEnabled(true);
                    state = 1;
                }
                else if (state == 1) {
                    type_entree.setText("Vous validez la totalité ?");
                    save_walls.setEnabled(false);
                    state = 2;
                }
                else {
                    //Faire la requete puis retour dans la page admin
                    MyAsynTask myAsyncTask = new  MyAsynTask();
                    List<String> list = new ArrayList<String>();
                    list.add(name_place);
                    list.add(id_building);
                    System.out.println(name_place + " " + id_building);
                    System.out.println("Affichage de la place et du building : ");
                    for(int t = 0; t < list.size(); t++) {
                        System.out.println(list.get(t).toString() + " ");
                    }
                    System.out.println("Affichage des longitudes et latitudes : ");
                    if(list_latitude_areas.size() == list_longitude_areas.size() && list_latitude_walls.size() == list_longitude_walls.size()) {
                        for(int s = 0; s < list_latitude_areas.size(); s++) {
                            System.out.println(list_latitude_areas.get(s).toString() + " / " + list_longitude_areas.get(s).toString());
                        }
                        for(int u = 0; u < list_latitude_walls.size(); u++) {
                            System.out.println(list_latitude_walls.get(u).toString() + " / " + list_longitude_walls.get(u).toString());
                        }
                    }
                    myAsyncTask.execute(list, list_latitude_areas, list_longitude_areas, list_latitude_walls, list_longitude_walls);
                    startActivity(new Intent(GMActivity.this, AdminActivity.class));
                }
            }
        });

        return_save_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GMActivity.this, AdminActivity.class));
            }
        });

        //ANCIEN CODE MAIS RESSEMBLE AU NOUVEAU QUI VA ETRE FAIT, DONC MODELE
        /*sauvegarde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_place = getIntent().getStringExtra("place");
                MyAsynTask myAsyncTask = new  MyAsynTask();
                myAsyncTask.execute(name_place, lagitude, lontitude);
                Intent intent = new Intent(GMActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });*/
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

    public class MyAsynTask extends AsyncTask<List<String>, Integer, Void> {

        @Override
        protected Void doInBackground(List<String>... arg0) {
            String area = "POLYGON((";
            String walls = "MULTILINESTRING(";
            String name_place = arg0[0].get(0);
            String building_id = arg0[0].get(1);
            //On transforme tout pour avoir un type Place
            List<Coordinate> list_areas = new ArrayList<Coordinate>();
            List<Coordinate> list_walls = new ArrayList<Coordinate>();
            for(int i = 0; i < arg0[1].size(); i++) {
                Coordinate coordinate_area = new Coordinate(Double.valueOf(arg0[1].get(i)), Double.valueOf(arg0[2].get(i)));
                list_areas.add(coordinate_area);
                Coordinate coordinate_wall = new Coordinate(Double.valueOf(arg0[3].get(i)), Double.valueOf(arg0[4].get(i)));
                list_walls.add(coordinate_wall);
            }
            //On transforme en chaine String
            for(int i = 0; i < list_areas.size() - 1; i++) {
                area = area + list_areas.get(i).getLatitude() + "-" + list_areas.get(i).getLongitude() + ",";
            }
            area = area + list_areas.get(list_areas.size() - 1).getLatitude() + "-" + list_areas.get(list_areas.size() - 1).getLongitude() + "))";

            for(int j = 0; j < list_walls.size(); j+=2) {
                double latitudeX = list_walls.get(j).getLatitude();
                double latitudeY = list_walls.get(j+1).getLatitude();
                double longitudeX = list_walls.get(j).getLongitude();
                double longitudeY = list_walls.get(j+1).getLongitude();
                walls = walls + "(" + latitudeX + "-" + longitudeX + "," + latitudeY + "-" + longitudeY + ")";
                if(j+3 < list_walls.size()) {
                    walls = walls + ",";
                }
            }
            walls = walls + ")";
            System.out.println("name_place : " + name_place);
            System.out.println("area : " + area);
            System.out.println("walls : " + walls);
            System.out.println("building_id : " + building_id);

            try {
                //URL POUR RENTRER UNE PLACE
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/savePlace/" + name_place + "/" + area + "/" + walls + "/" + building_id);

                InputStream inputStream = request.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader

                    InputStreamReader reader = new InputStreamReader(inputStream);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
