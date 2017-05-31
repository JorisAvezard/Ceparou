package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by groov on 31/05/2017.
 */

public class ParametresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);


        Button bouton_page_moteur_from_parametres = (Button) findViewById(R.id.bouton_page_moteur_from_parametres);
        bouton_page_moteur_from_parametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParametresActivity.this, MoteurActivity.class));
            }
        });

        Button bouton_page_modification_mail = (Button) findViewById(R.id.bouton_page_modification_mail);
        bouton_page_modification_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParametresActivity.this, ModificationMailActivity.class));
            }
        });

        Button bouton_page_modification_num = (Button) findViewById(R.id.bouton_page_modification_num);
        bouton_page_modification_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParametresActivity.this, ModificationNumActivity.class));
            }
        });

        Button bouton_page_modification_mdp = (Button) findViewById(R.id.bouton_page_modification_mdp);
        bouton_page_modification_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParametresActivity.this, ModificationMdpActivity.class));
            }
        });

        Button bouton_suppression_compte = (Button) findViewById(R.id.bouton_suppression_compte);
        bouton_suppression_compte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ParametresActivity.this, SuppressionCompteActivity.class));
            }
        });
    }
}
