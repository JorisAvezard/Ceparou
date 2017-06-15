package com.example.groov.my_ceparou;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by groov on 06/06/2017.
 */

public class SavePlacesActivity extends AppCompatActivity {

    Spinner spinner;
    SendRequest request = new SendRequest();
    Gson gson = new GsonBuilder().create();
    List<Building> list = new ArrayList<Building>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_places);
        Button bouton_placement = (Button) findViewById(R.id.bouton_placer);
        bouton_placement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText lieu = (EditText) findViewById(R.id.edit_text_place);
                String place = lieu.getText().toString();
                Building building = (Building) spinner.getSelectedItem();
                String id_building = String.valueOf(building.getId_building());
                Intent intent = new Intent(SavePlacesActivity.this,GMActivity.class);
                intent.putExtra(place, place);
                intent.putExtra(id_building, id_building);
                startActivity(intent);
            }
        });

        Button bouton_building = (Button) findViewById(R.id.bouton_building);
        bouton_building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //function entrée dans BDD du building
                String name_main = ((EditText) findViewById(R.id.edit_name_main)).getText().toString();
                String name_specific = ((EditText) findViewById(R.id.edit_name_specific)).getText().toString();
                System.out.println(name_main + " - " + name_specific);
                MyAsynTaskBuilding myAsynTaskBuilding = new MyAsynTaskBuilding();
                myAsynTaskBuilding.execute(name_main, name_specific);
            }
        });

        Button bouton_retour = (Button) findViewById(R.id.bouton_page_admin_from_save_places);
        bouton_retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SavePlacesActivity.this, AdminActivity.class));
            }
        });


        spinner = (Spinner) findViewById(R.id.spinner);
        MyAsynTask myAsynTask = new MyAsynTask();
        myAsynTask.execute();
    }

    public class MyAsynTask extends AsyncTask<Void, Integer, ArrayList<Building>> {

        ArrayList<Building> list_building = new ArrayList<Building>();
        Type listType = new TypeToken<ArrayList<Building>>(){}.getType();

        @Override
        protected ArrayList doInBackground(Void... arg0) {
            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/building");

                InputStream inputStream = request.sendRequest(url);

                // Vérification de l'inputStream
                if (inputStream != null) {
                    // Lecture de l'inputStream dans un reader

                    InputStreamReader reader = new InputStreamReader(inputStream);
                    list_building = gson.fromJson(reader, listType);
                    return list_building;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Building> result) {
            for(int i=0; i < result.size(); i++) {
                list.add(result.get(i));
            }
            ArrayAdapter adapter = new ArrayAdapter(SavePlacesActivity.this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }

    }

    public class MyAsynTaskBuilding extends AsyncTask<String, Integer, Void> {

        @Override
        protected Void doInBackground(String... arg0) {
            String name_main = arg0[0];
            String name_specific = arg0[1];
            System.out.println(name_main + " / " + name_specific);
            try {
                //URL POUR afficher les building
                URL url = new URL("http://192.168.137.1:8080/Ceparou/service/insertBuilding/" + name_main + "/" + name_specific);

                InputStream inputStream = request.sendRequest(url);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
