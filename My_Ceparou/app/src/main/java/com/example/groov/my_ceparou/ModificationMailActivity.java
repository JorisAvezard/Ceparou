package com.example.groov.my_ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by groov on 11/04/2017.
 */

public class ModificationMailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_mail);

        Bundle extras = getIntent().getExtras();
        final int id_client = extras.getInt("id_client");

        Button bouton_modification_mail = (Button) findViewById(R.id.bouton_modification_mail);
        bouton_modification_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText nouv_mail = (EditText) findViewById(R.id.edit_text_nouveau_mail);
                String nouveau_mail = nouv_mail.getText().toString();
                final EditText conf = (EditText) findViewById(R.id.edit_text_confirmation_mdp);
                String confirmation = conf.getText().toString();

                //Vérifier si le mdp correspond
                    //Récupérer la nouvelle adresse
                    //Vérifier si pas déjà utilisée
                        //Requête à la BDD pour changement
                Intent intent = new Intent(ModificationMailActivity.this, ModificationMailActivity.class);
                intent.putExtra("id_client", id_client);
                startActivity(intent);
            }
        });

        Button bouton_page_parametres_from_modif_mail = (Button) findViewById(R.id.bouton_page_parametres_from_modif_mail);
        bouton_page_parametres_from_modif_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModificationMailActivity.this, ParametresActivity.class);
                intent.putExtra("id_client", id_client);
                startActivity(intent);
            }
        });
    }
}
