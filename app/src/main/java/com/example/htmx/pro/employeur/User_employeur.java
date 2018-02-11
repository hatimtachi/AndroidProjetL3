package com.example.htmx.pro.employeur;

/**
 * Created by htmx on 13/12/16.
 */
public class User_employeur {
    String nom;
    String prenom;
    String email;
    String societe;
    String password;

    public User_employeur(){}
    public User_employeur(String nom,String prenom,String societe,String password,String email){
        this.nom=nom;
        this.prenom=prenom;
        this.societe=societe;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
