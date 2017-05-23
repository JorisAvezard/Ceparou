package com.jorisgmail.avezard.cparo;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;

/**
 * Created by Joris on 26/04/2017.
 */

public class MapsActivity extends FragmentActivity implements IALocationListener {

    private final int CODE_PERMISSIONS = 1;
    private static final float HUE_IABLUE = 200.0f;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Marker mMarker;
    private IALocationManager mIALocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String[] neededPermissions = {
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(this, neededPermissions, CODE_PERMISSIONS);
        mIALocationManager = IALocationManager.create(this);

        final String floorPlanId = getString(R.string.indooratlas_floor_plan_id);
        if (!TextUtils.isEmpty(floorPlanId)) {
            final IALocation FLOOR_PLAN_ID = IALocation.from(IARegion.floorPlan(floorPlanId));
            mIALocationManager.setLocation(FLOOR_PLAN_ID);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIALocationManager.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIALocationManager != null) {
            mIALocationManager.removeLocationUpdates(this);
        }
    }

    /**
     * Callback for receiving locations.
     * This is where location updates can be handled by moving markers or the camera.
     */
    public void onLocationChanged(IALocation location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (mMarker == null) {
            if (mMap != null) {
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                        .icon(BitmapDescriptorFactory.defaultMarker(HUE_IABLUE)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
            }
        } else {
            mMarker.setPosition(latLng);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // N/A
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}