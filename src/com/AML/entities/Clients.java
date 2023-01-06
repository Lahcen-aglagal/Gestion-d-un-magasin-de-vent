package com.AML.entities;

/**
 * 
 * @author ay0ub
 */
public class Clients {
    private int id;
    private String fullName;
    private String adresse;
    private int CodePostal;
    private String Ville;
    private int Tel;
    private String nom;
    private String prenom;
    private String email;
    private String MotDePasse;
    private boolean isAdmin;

    public Clients() {
    }

    public Clients(String adresse, int CodePostal, String Ville, int Tel, String nom, String prenom, String email, String MotDePasse) {
        this.adresse = adresse;
        this.CodePostal = CodePostal;
        this.Ville = Ville;
        this.Tel = Tel;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.MotDePasse = MotDePasse;
    }
    
    public Clients(int id, String fullName, String adresse, int CodePostal, String Ville, int Tel, String email, String MotDePasse) {
        this.id = id;
        this.fullName = fullName;
        this.adresse = adresse;
        this.CodePostal = CodePostal;
        this.Ville = Ville;
        this.Tel = Tel;
        this.email = email;
        this.MotDePasse = MotDePasse;
    }


    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getCodePostal() {
        return CodePostal;
    }

    public void setCodePostal(int CodePostal) {
        this.CodePostal = CodePostal;
    }

    public String getVille() {
        return Ville;
    }

    public void setVille(String Ville) {
        this.Ville = Ville;
    }

    public int getTel() {
        return Tel;
    }

    public void setTel(int Tel) {
        this.Tel = Tel;
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

    public String getMotDePasse() {
        return MotDePasse;
    }

    public void setMotDePasse(String MotDePasse) {
        this.MotDePasse = MotDePasse;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    
    
    
}
