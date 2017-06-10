package com.example.groov.my_ceparou;

/**
 * Created by Joris on 04/06/2017.
 */

public class User {

    private int id_user;
    private String pseudo;
    private String password;

    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    public User(int id_user, String pseudo, String password) {
        super();
        this.id_user = id_user;
        this.pseudo = pseudo;
        this.password = password;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
