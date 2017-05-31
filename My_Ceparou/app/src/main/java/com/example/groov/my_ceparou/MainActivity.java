package com.example.groov.my_ceparou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bouton_connexion = (Button) findViewById(R.id.bouton_connexion);
        bouton_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText identifiant = (EditText) findViewById(R.id.edit_text_identifiant);
                String id = identifiant.getText().toString();
                final EditText motdepasse = (EditText) findViewById(R.id.edit_text_mdp);
                String mdp = motdepasse.getText().toString();
                final CheckBox accepte = (CheckBox) findViewById(R.id.checkbox_accepte);

                if (id.equals("lucas") && (mdp.equals("lucas")) && (accepte.isChecked() == true)) {
                    //lancer une session
                    System.out.println("Connexion réussie");
                    startActivity(new Intent(MainActivity.this, MoteurActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, PopConnexionActivity.class));
                    System.out.println("Connexion ratée");
                }
            }
        });

        Button bouton_page_oublis_infos = (Button) findViewById(R.id.bouton_page_oublis_infos);
        bouton_page_oublis_infos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OublisActivity.class));
            }
        });

        Button bouton_page_inscription = (Button) findViewById(R.id.bouton_page_inscription);
        bouton_page_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InscriptionActivity.class));
            }
        });
    }
}