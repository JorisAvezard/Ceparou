package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            public void onClick(View v){
            final EditText motdepasse = (EditText) findViewById(R.id.edit_text_mdp);
            String mdp = motdepasse.getText().toString();
            final EditText confirmation = (EditText) findViewById(R.id.edit_text_confirmation_mdp);
            String conf = confirmation.getText().toString();
            final EditText identifiant = (EditText) findViewById(R.id.edit_text_identifiant);
            String nom = identifiant.getText().toString();
            final EditText email = (EditText) findViewById(R.id.edit_text_mail);
            String mail = email.getText().toString();

            Connexion_BDD connexion = new Connexion_BDD();

            try {
                String insertUsersQuery = "INSERT INTO android.test_user(nom, mdp) VALUES (?,?)";

                Connection dbConnection = connexion.getConnection();
                PreparedStatement preparedStatement = dbConnection.prepareStatement(insertUsersQuery);

                preparedStatement.setString(1, nom);
                preparedStatement.setString(2, mdp);

                preparedStatement.executeUpdate();

                preparedStatement.close();
            } catch(SQLException se) {
                System.err.println(se.getMessage());
            }
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
}
