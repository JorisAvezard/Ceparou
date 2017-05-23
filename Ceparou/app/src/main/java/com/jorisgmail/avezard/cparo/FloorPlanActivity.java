package com.jorisgmail.avezard.cparo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.resources.IAResourceManager;

/**
 * Created by Joris on 03/05/2017.
 */

public class FloorPlanActivity extends AppCompatActivity {
    private IALocationManager mLocationManager;
    private IAResourceManager mResourceManager;
    private ImageView mFloorPlanImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_plan);
        mFloorPlanImage = (ImageView) findViewById(R.id.image);
        // ...
        // Create instance of IAFloorPlanManager class
        mResourceManager = IAResourceManager.create(this);
    }
}