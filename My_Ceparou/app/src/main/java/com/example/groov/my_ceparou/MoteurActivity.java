package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by groov on 31/05/2017.
 */

public class MoteurActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moteur);

        Button bouton_page_parametres_from_moteur = (Button) findViewById(R.id.bouton_page_parametres_from_moteur);
        bouton_page_parametres_from_moteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoteurActivity.this, ParametresActivity.class));
            }
        });

        Button bouton_calcul = (Button) findViewById(R.id.bouton_calcul);
        bouton_calcul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText position = (EditText) findViewById(R.id.edit_text_position);
                String depart = position.getText().toString();
                final EditText recherche = (EditText) findViewById(R.id.edit_text_recherche);
                String arrive = recherche.getText().toString();
                final RadioGroup choix = (RadioGroup) findViewById(R.id.radio_option_affichage);
                int selectedId = choix.getCheckedRadioButtonId();

                System.out.println("idListe:"+R.id.radio_option_affichage_liste);
                System.out.println("idPlan:"+R.id.radio_option_affichage_plan);
                System.out.println("id sélectionnée:"+selectedId);
                System.out.println("Calcul à effectuer avec depart='"+depart+"' arrive='"+arrive+"'");

                if(selectedId==R.id.radio_option_affichage_liste){
                    System.out.println("Affichage choisi:Liste ");
                }else{
                    System.out.println("Affichage choisi:Plan ");
                }
                //ouvrir sur une nouvelle fenêtre ?
                startActivity(new Intent(MoteurActivity.this, MoteurActivity.class));
            }
        });

        Button bouton_raz = (Button) findViewById(R.id.bouton_raz);
        bouton_raz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoteurActivity.this, MoteurActivity.class));
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
}
