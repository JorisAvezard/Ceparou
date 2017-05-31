package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by groov on 29/05/2017.
 */

public class SuppressionCompteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppression_compte);

        Button bouton_suppression_compte = (Button) findViewById(R.id.bouton_suppression_compte);
        bouton_suppression_compte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText pwd = (EditText) findViewById(R.id.edit_text_mdp);
                String password = pwd.getText().toString();
                final EditText conf = (EditText) findViewById(R.id.edit_text_confirmation_mdp);
                String confirmation = conf.getText().toString();

                if(pwd.equals(conf)){

                }
                //Vérifier si les mdp sont identique si oui aller voir dans bdd s'ils correspondent
                //Vérifier si le mdp correspond
                //Requête à la BDD pour changement
                startActivity(new Intent(SuppressionCompteActivity.this, SuppressionCompteActivity.class));
            }
        });

        Button bouton_page_parametres_from_suppression_compte = (Button) findViewById(R.id.bouton_page_parametres_from_suppression_compte);
        bouton_page_parametres_from_suppression_compte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuppressionCompteActivity.this, ParametresActivity.class));
            }
        });
    }
}
