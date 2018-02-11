package com.example.htmx.pro.emploi;

/**
 * Created by htmx on 26/10/16.
 */

public class User_emploi {
    public String nom;
    public String prenom;
    public String password;
    public String email;
    public User_emploi(){
    }

    public User_emploi(String nome, String prenom, String password,String email) {
        this.nom = nome;
        this.prenom = prenom;
        this.password=password;
        this.email=email;
    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

