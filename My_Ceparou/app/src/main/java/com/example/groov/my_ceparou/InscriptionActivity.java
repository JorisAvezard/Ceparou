package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;



/**
 * Created by groov on 31/05/2017.
 */

public class InscriptionActivity extends AppCompatActivity {

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

                Client client = Client.create(new DefaultClientConfig());
                ClientResponse rep2 = client.resource(getBaseURI()).path("insertUser").path(nom).path(mdp).get(ClientResponse.class);
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

}
