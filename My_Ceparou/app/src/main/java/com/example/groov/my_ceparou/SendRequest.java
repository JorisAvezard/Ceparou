package com.example.groov.my_ceparou;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Joris on 04/06/2017.
 */

public class SendRequest {

    public SendRequest() {

    }

    public InputStream sendRequest(URL url) throws Exception {
        InputStream inputStream = null;
        try {
            // Ouverture de la connexion
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Connexion à l'URL
            urlConnection.connect();

            // Si le serveur nous répond avec un code OK
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream =  urlConnection.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
