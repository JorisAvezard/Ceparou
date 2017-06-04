package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;



/**
 * Created by groov on 31/05/2017.
 */

public class InscriptionActivity extends AppCompatActivity {

    SendRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Button bouton_inscription = (Button) findViewById(R.id.bouton_inscription);
        bouton_inscription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final EditText motdepasse = (EditText) findViewById(R.id.edit_text_mdp);
                String mdp = motdepasse.getText().toString();
                final EditText confirmation = (EditText) findViewById(R.id.edit_text_confirmation_mdp);
                String conf = confirmation.getText().toString();
                final EditText identifiant = (EditText) findViewById(R.id.edit_text_identifiant);
                String nom = identifiant.getText().toString();
                final EditText email = (EditText) findViewById(R.id.edit_text_mail);
                String mail = email.getText().toString();

                MyAsynTask myAsyncTask = new  MyAsynTask();
                //EN ATTENDANT DE RAJOUTER LES CHAMPS NECESSAIRES
                //myAsyncTask.execute(nom, mdp, conf, firstname, lastname, mail);


            }
        });

        Button bouton_page_connexion_from_inscription = (Button) findViewById(R.id.bouton_page_connexion_from_inscription);
        bouton_page_connexion_from_inscription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(InscriptionActivity.this,MainActivity.class));
            }
        });
    }



    private static URI getBaseURI() {

        return UriBuilder.fromUri("http://192.168.137.1:8080/Ceparou/service").build();

    }

    public class MyAsynTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... arg0) {
            String nom = arg0[0];
            String mdp = arg0[1];
            String conf = arg0[2];
            String firstname = arg0[3];
            String lastname = arg0[4];
            String email = arg0[5];

            if(mdp.equals(conf)) {
                try {
                    URL url_name = new URL("http://192.168.137.1:8080/Ceparou/service/pseudo/" + nom);
                    InputStream inputStream_name = request.sendRequest(url_name);
                    if (inputStream_name != null) {
                        String result_name = "";
                        ByteArrayOutputStream baos_name = new ByteArrayOutputStream();
                        byte[] buffer_name = new byte[1024];
                        int length_name;
                        while ((length_name = inputStream_name.read(buffer_name)) != -1) {
                            baos_name.write(buffer_name, 0, length_name);
                        }
                        result_name = baos_name.toString("UTF-8");

                        if (!result_name.equals(nom)) {
                            URL url_mail = new URL("http://192.168.137.1:8080/Ceparou/service/mail/" + email);
                            InputStream inputStream_mail = request.sendRequest(url_mail);
                            if (inputStream_mail != null) {
                                String result_mail = "";
                                ByteArrayOutputStream baos_mail = new ByteArrayOutputStream();
                                byte[] buffer_mail = new byte[1024];
                                int length_mail;
                                while ((length_mail = inputStream_mail.read(buffer_mail)) != -1) {
                                    baos_mail.write(buffer_mail, 0, length_mail);
                                }
                                result_mail = baos_mail.toString("UTF-8");

                                if (!result_mail.equals(email)) {
                                    URL url = new URL("http://192.168.137.1:8080/Ceparou/service/inscription/" + nom + "/" + mdp + "/" + firstname + "/" + lastname + "/" + email);
                                    InputStream inputStream = request.sendRequest(url);
                                    //UNE FENETRE QUI DIT QUE C'EST REUSSI ??
                                    startActivity(new Intent(InscriptionActivity.this,PopInscriptionActivity.class));
                                }
                                else {
                                    startActivity(new Intent(InscriptionActivity.this,PopInscriptionEchecMailActivity.class));
                                }
                            }
                            else {
                                startActivity(new Intent(InscriptionActivity.this,PopInscriptionEchecIDActivity.class));
                            }
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                startActivity(new Intent(InscriptionActivity.this,PopInscriptionEchecMDPActivity.class));
            }
            return null;
        }
    }

}
