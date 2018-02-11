package com.example.htmx.pro.employeur;

/**
 * Created by htmx on 26/12/16.
 */
public class Favori {
    private String nom;
    private String prenom;
    private String domaine;

    public Favori(String nom, String prenom, String domaine) {
        this.nom = nom;
        this.prenom = prenom;
        this.domaine = domaine;
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

    public String getDomaine() {
        return domaine;
    }

    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }
}
