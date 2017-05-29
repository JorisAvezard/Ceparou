package com.example.groov.ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.groov.ceparou.R.id.edit_text_identifiant;
import static com.example.groov.ceparou.R.layout.activity_pop_oublis;

public class ConnexionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        Connexion_BDD connexion = new Connexion_BDD();

        Button bouton_connexion = (Button) findViewById(R.id.bouton_connexion);
        bouton_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText identifiant = (EditText) findViewById(R.id.edit_text_identifiant);
                String id = identifiant.getText().toString();
                final EditText motdepasse = (EditText) findViewById(R.id.edit_text_mdp);
                String mdp = motdepasse.getText().toString();
                final CheckBox accepte = (CheckBox) findViewById(R.id.checkbox_accepte);

                /*Pour test, à enlever plus tard*/
                if(id.equals("lucas")&&(mdp.equals("lucas"))&&(accepte.isChecked()==true)){
                    //lancer une session
                    System.out.println("Connexion réussie");
                    startActivity(new Intent(ConnexionActivity.this, MoteurActivity.class));
                }else{
                    startActivity(new Intent(ConnexionActivity.this, PopConnexionActivity.class));
                    System.out.println("Connexion ratée");
                }

                /*à mettre à la place
                if(accepte.isChecked()==true) {
                    Connexion_BDD connexion = new Connexion_BDD();
                    try {
                        ResultSet result_id = connexion.getState().executeQuery("SELECT id FROM utilisateur");
                        while (result_id.next()) {
                            if (id.equals(result_id.getString("id"))) {
                                ResultSet result_mdp = connexion.getState().executeQuery("SELECT mdp FROM utilisateur WHERE id=" + id);
                                if (mdp.equals(result_mdp.getString("mdp"))) {
                                    System.out.println("Connexion en cours");
                                    startActivity(new Intent(ConnexionActivity.this, MoteurActivity.class));
                                    System.out.println("Connexion réussie");
                                }else {
                                    System.out.println("Échec de connexion");
                                    startActivity(new Intent(ConnexionActivity.this, PopConnexionActivity.class));
                                }
                            } else {
                                System.out.println("Échec de connexion");
                                startActivity(new Intent(ConnexionActivity.this, PopConnexionActivity.class));
                            }
                        }
                        connexion.getState().close();
                        connexion.getConnection().close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    System.out.println("Échec de connexion");
                    startActivity(new Intent(ConnexionActivity.this, PopConnexionActivity.class));
                }*/
            }
        });

        Button bouton_page_oublis_infos = (Button) findViewById(R.id.bouton_page_oublis_infos);
        bouton_page_oublis_infos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(ConnexionActivity.this, OublisActivity.class));
            }
        });

        Button bouton_page_inscription = (Button) findViewById(R.id.bouton_page_inscription);
        bouton_page_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConnexionActivity.this, InscriptionActivity.class));
            }
        });
    }

}