package com.example.groov.ceparou;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by groov on 03/05/2017.
 */

public class Connexion_BDD {
    Statement state;
    Connection connection;
    public Connexion_BDD(){
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:postgresql://localhost:5432/Ecole";
            String user = "postgres";
            String passwd = "010395";

            Connection connection = DriverManager.getConnection(url, user, passwd);
            Statement state = connection.createStatement();
            System.out.println("Connexion BDD effective !");

        } catch (Exception e) {
            System.out.println("Connexion BDD ECHEC !");
            e.printStackTrace();
        }

    }
    public Statement getState(){
        return state;
    }
    public Connection getConnection(){
        return connection;
    }
}

