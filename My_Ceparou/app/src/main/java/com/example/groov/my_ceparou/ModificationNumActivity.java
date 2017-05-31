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

public class ModificationNumActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_num);

        Button bouton_modification_num = (Button) findViewById(R.id.bouton_modification_num);
        bouton_modification_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText nouv_num = (EditText) findViewById(R.id.edit_text_nouveau_num);
                String nouveau_num = nouv_num.getText().toString();
                final EditText conf = (EditText) findViewById(R.id.edit_text_confirmation_mdp);
                String confirmation = conf.getText().toString();


                //Récupérer mdp dans la BDD
                //Vérifier si le mdp correspond
                    //Vérifier le nouveau n° si pas déjà utilisée
                        //Requête à la BDD pour changement
                startActivity(new Intent(ModificationNumActivity.this, ModificationNumActivity.class));
            }
        });

        Button bouton_page_parametres_from_modif_num = (Button) findViewById(R.id.bouton_page_parametres_from_modif_num);
        bouton_page_parametres_from_modif_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModificationNumActivity.this, ParametresActivity.class));
            }
        });
    }
}
