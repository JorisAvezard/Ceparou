package com.example.groov.ceparou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by groov on 11/04/2017.
 */

public class ModificationMdpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_mdp);

        Button bouton_modification_mdp = (Button) findViewById(R.id.bouton_modification_mdp);
        bouton_modification_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText anc_mdp = (EditText) findViewById(R.id.edit_text_ancien_mdp);
                String ancien_mdp = anc_mdp.getText().toString();
                final EditText nouv_mdp = (EditText) findViewById(R.id.edit_text_nouveau_mdp);
                String nouveau_mdp = nouv_mdp.getText().toString();
                final EditText conf = (EditText) findViewById(R.id.edit_text_confirmation_mdp);
                String confirmation = conf.getText().toString();

                //Vérifier si ancien mdp correspond à celui dans BDD
                    //Vérifier si le mdp confirmation correspond au nouveau
                        //Requête à la BDD pour changement
                startActivity(new Intent(ModificationMdpActivity.this, ModificationMdpActivity.class));
            }
        });

        Button bouton_page_parametres_from_modif_mdp = (Button) findViewById(R.id.bouton_page_parametres_from_modif_mdp);
        bouton_page_parametres_from_modif_mdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModificationMdpActivity.this, ParametresActivity.class));
            }
        });
    }
}
