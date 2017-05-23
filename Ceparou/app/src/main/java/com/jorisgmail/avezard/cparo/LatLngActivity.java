package com.jorisgmail.avezard.cparo;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;

/**
 * Created by Joris on 01/05/2017.
 */

public class LatLngActivity extends AppCompatActivity {

    private final int CODE_PERMISSIONS = 1;
    IALocationManager mLocationManager;

    IALocationListener mLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {
            TextView text = (TextView) findViewById(R.id.textView_latlng);
            text.setText(String.valueOf(iaLocation.getLatitude() + ", " + iaLocation.getLongitude()));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

    @Override
    protected void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_latlng);
        String[] neededPermissions = {
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(this, neededPermissions, CODE_PERMISSIONS);

        mLocationManager = IALocationManager.create(this);
    }

    protected void onResume() {
        super.onResume();
        mLocationManager.requestLocationUpdates(IALocationRequest.create(), mLocationListener);
    }

    protected void onPause() {
        mLocationManager.removeLocationUpdates(mLocationListener);
        super.onPause();
    }

    protected void onDestroy() {
        mLocationManager.destroy();
        super.onDestroy();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
