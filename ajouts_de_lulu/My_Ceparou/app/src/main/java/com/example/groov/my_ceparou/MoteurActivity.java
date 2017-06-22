package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static com.example.groov.my_ceparou.R.id.result;

/**
 * Created by groov on 31/05/2017.
 */

public class MoteurActivity extends AppCompatActivity {

    SendRequest request = new SendRequest();
    Gson gson = new GsonBuilder().create();
    IALocationManager mLocationManager;
    double latitude = 0;
    double longitude = 0;

    IALocationListener mLocationListener = new IALocationListener() {
        @Override
        public void onLocationChanged(IALocation iaLocation) {
            latitude = iaLocation.getLatitude();
            longitude = iaLocation.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moteur);

        mLocationManager = IALocationManager.create(this);

        Bundle extras = getIntent().getExtras();
        final int id_client = extras.getInt("id_client");

        Button bouton_page_parametres_from_moteur = (Button) findViewById(R.id.bouton_page_parametres_from_moteur);
        bouton_page_parametres_from_moteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoteurActivity.this, ParametresActivity.class);
                intent.putExtra("id_client", id_client);
                startActivity(intent);
            }
        });

        Button bouton_calcul = (Button) findViewById(R.id.bouton_calcul);
        bouton_calcul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText recherche = (EditText) findViewById(R.id.edit_text_recherche);
                String arrive = recherche.getText().toString();
                final RadioGroup choix = (RadioGroup) findViewById(R.id.radio_option_affichage);
                int selectedId = choix.getCheckedRadioButtonId();

                System.out.println("idListe:"+R.id.radio_option_affichage_liste);
                System.out.println("idPlan:"+R.id.radio_option_affichage_plan);
                System.out.println("id sélectionnée:"+selectedId);
                System.out.println("Calcul à effectuer avec arrive='"+arrive+"'");

                if(selectedId==R.id.radio_option_affichage_liste){
                    System.out.println("Affichage choisi:Liste ");
                }else{
                    System.out.println("Affichage choisi:Plan ");
                }
                Intent intent = new Intent(MoteurActivity.this,GMClientActivity.class);
                intent.putExtra("id_client", id_client);
                startActivity(intent);
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

        MyAsynTask task = new MyAsynTask();
        task.execute(latitude, longitude);
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

    public class MyAsynTask extends AsyncTask<Double, Integer, String> {

        @Override
        protected String doInBackground(Double... arg0) {
            double lat = arg0[0];
            double lon = arg0[1];
            String place = "";
            try {
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/selectIdP/" + lat + "/" + lon);

                InputStream inputStream = request.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    place = gson.fromJson(reader, String.class);
                    System.out.println(place);
                    if(place.equals(null)){
                        place="(en recherche de localisation)";
                    }
                    return place;

                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            return "(en recherche de localisation)";
        }

        @Override
        protected void onPostExecute(String result) {
            TextView affichage_position = (TextView) findViewById(R.id.lat_lng);
            affichage_position.setText("Vous êtes en " + result + "\n(" + latitude + ", " + longitude + ")");
        }
    }
}
