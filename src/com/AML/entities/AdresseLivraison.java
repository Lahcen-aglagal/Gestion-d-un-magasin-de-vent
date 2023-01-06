package com.AML.entities;
public class AdresseLivraison {
    private int AdresseLivraisonId;
    private String adresse;
    private int CodePostal;
    private int tel;
    private String ville;
    private String pays;
    private int CodeClient;

    public AdresseLivraison() {
    }

    public AdresseLivraison(String adresse, int CodePostal, int tel, String ville, String pays, int CodeClient) {
        this.adresse = adresse;
        this.CodePostal = CodePostal;
        this.tel = tel;
        this.ville = ville;
        this.pays = pays;
        this.CodeClient = CodeClient;
    }

    public int getAdresseLivraisonId() {
        return AdresseLivraisonId;
    }

    public void setAdresseLivraisonId(int AdresseLivraisonId) {
        this.AdresseLivraisonId = AdresseLivraisonId;
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

    public int getTel() {
        return tel;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public int getCodeClient() {
        return CodeClient;
    }

    public void setCodeClient(int CodeClient) {
        this.CodeClient = CodeClient;
    }
    
}
