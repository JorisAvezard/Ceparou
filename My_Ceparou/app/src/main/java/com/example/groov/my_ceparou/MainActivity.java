package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    SendRequest request = new SendRequest();
    Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bouton_connexion = (Button) findViewById(R.id.bouton_connexion);
        bouton_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText identifiant = (EditText) findViewById(R.id.edit_text_identifiant);
                String pseudo = identifiant.getText().toString();
                final EditText motdepasse = (EditText) findViewById(R.id.edit_text_mdp);
                String mdp = motdepasse.getText().toString();
                final CheckBox accepte = (CheckBox) findViewById(R.id.checkbox_accepte);
                String coche = "";
                if(accepte.isChecked()) {
                    coche = "1";
                }
                else { coche = "0"; }
                MyAsynTask myAsyncTask = new  MyAsynTask();
                myAsyncTask.execute(pseudo, mdp, coche);
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

    public class MyAsynTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... arg0) {
            String pseudo = arg0[0];
            String mdp = arg0[1];
            String acc = arg0[2];
            User user;
            try {
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/connexion/" + pseudo + "/" + mdp);

                InputStream inputStream = request.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader

                    InputStreamReader reader = new InputStreamReader(inputStream);

                    // Retourne la liste désérialisée par le moteur GSON

                    user = gson.fromJson(reader, User.class);
                    if (user.getPseudo().equals(pseudo) && (user.getPassword().equals(mdp)) && acc.equals("1")) {
                        //lancer une session
                        /*Rajouter un if pour si user.getGrade_user().equals("admin") alors lancer AdminActivity,
                        * Sinon lancer ActivityNormale (nom à définir ultérieurement). Faire des pops !*/
                        System.out.println("Connexion réussie");
                        startActivity(new Intent(MainActivity.this, MoteurActivity.class));
                    } else {
                        System.out.println("Connexion ratée");
                        startActivity(new Intent(MainActivity.this, PopConnexionActivity.class));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}