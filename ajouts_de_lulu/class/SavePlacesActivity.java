package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by groov on 06/06/2017.
 */

public class SavePlacesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_places);

        Button bouton_placement = (Button) findViewById(R.id.bouton_placer);
        bouton_placement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText lieu = (EditText) findViewById(R.id.edit_text_place);
                String place = lieu.getText().toString();
                //voir ce qu'il faut faire
                startActivity(new Intent(SavePlacesActivity.this, GMActivity.class));
            }
        });
        Button bouton_retour = (Button) findViewById(R.id.bouton_page_admin_from_save_places);
        bouton_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavePlacesActivity.this, AdminActivity.class));
            }
        });
    }
}
