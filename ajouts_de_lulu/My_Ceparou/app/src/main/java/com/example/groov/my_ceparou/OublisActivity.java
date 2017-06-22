package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by groov on 31/05/2017.
 */

public class OublisActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oublis_infos);

        Button bouton_recuperation = (Button) findViewById(R.id.bouton_recuperation);
        bouton_recuperation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Vérifier si mail existe dans BDD
                //Envoyer les identifiants liés à ce compte
                startActivity(new Intent(OublisActivity.this, PopOublisActivity.class));
            }
        });

        Button bouton_page_connexion_from_oublis = (Button) findViewById(R.id.bouton_page_connexion_from_oublis);
        bouton_page_connexion_from_oublis.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(OublisActivity.this, MainActivity.class));
            }
        });
    }
}
