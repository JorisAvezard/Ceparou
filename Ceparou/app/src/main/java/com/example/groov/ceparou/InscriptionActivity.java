package com.example.groov.ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.sql.Statement;

import static com.example.groov.ceparou.R.id.result;

/**
 * Created by groov on 11/04/2017.
 */

public class InscriptionActivity extends AppCompatActivity{

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

            Connexion_BDD connexion = new Connexion_BDD();
            try{
                if(mdp.equals(conf)){
                    System.out.println("mdp = conf");
                    final EditText identifiant = (EditText) findViewById(R.id.edit_text_identifiant);
                    String id = identifiant.getText().toString();
                    boolean id_used = false;
                    ResultSet result_id = connexion.getState().executeQuery("SELECT id FROM utilisateur");
                    while(result_id.next()){
                        if(id.equals(result_id.getString("id"))){
                            id_used=true;
                        }
                    }
                    if(id_used==false){
                        System.out.println("id disponible");
                        final EditText email = (EditText) findViewById(R.id.edit_text_mail);
                        String mail = email.getText().toString();
                        boolean mail_used = false;
                        ResultSet result_mail = connexion.getState().executeQuery("SELECT mail FROM utilisateur");
                        while(result_mail.next()){
                            if(mail.equals(result_mail.getString("mail"))){
                                mail_used=true;
                            }
                        }
                        if(mail_used==false){
                            System.out.println("mail disponible");
                            final EditText numero = (EditText) findViewById(R.id.edit_text_num);
                            String num = numero.getText().toString();
                            boolean num_used = false;
                            ResultSet result_num = connexion.getState().executeQuery("SELECT num FROM utilisateur");
                            while(result_num.next()){
                                if(num.equals(result_num.getString("num"))){
                                    num_used=true;
                                }
                            }
                            if(num_used==false){
                                System.out.println("n° disponible");
                                System.out.println("Inscription en cours");
                                /*requête inscription*/

                                connexion.getState().executeQuery("SELECT num FROM utilisateur");
                                System.out.println("Confirmation inscription");
                                startActivity(new Intent(InscriptionActivity.this,PopInscriptionActivity.class));
                            }else {
                                System.out.println("n° déjà utilisé");
                                startActivity(new Intent(InscriptionActivity.this,PopInscriptionEchecNumActivity.class));
                            }
                            result_num.close();
                        }else {
                            System.out.println("mail déjà utilisé");
                            startActivity(new Intent(InscriptionActivity.this,PopInscriptionEchecMailActivity.class));
                        }
                        result_mail.close();
                    }else {
                        System.out.println("id déjà utilisé");
                        startActivity(new Intent(InscriptionActivity.this,PopInscriptionEchecIDActivity.class));
                    }
                    result_id.close();
                }else {
                    System.out.println("mdp != confirmation");
                    startActivity(new Intent(InscriptionActivity.this,PopInscriptionEchecMDPActivity.class));
                }
                connexion.getState().close();
                connexion.getConnection().close();
            }catch (SQLException e) {
                    e.printStackTrace();
            }
            }
        });

        Button bouton_page_connexion_from_inscription = (Button) findViewById(R.id.bouton_page_connexion_from_inscription);
        bouton_page_connexion_from_inscription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(InscriptionActivity.this,ConnexionActivity.class));
            }
        });
    }
}
