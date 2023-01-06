package com.AML.entities;

import java.io.InputStream;

/**
 *
 * @author ay0ub
 */
public class Produits {
    private int CodeArticle;
    private String Designation;
    private String Description;
    private double Prix;
    private int Stock;
    private InputStream Photo;
    private int RefCat;

    public Produits() {
    }

    public Produits(int CodeArticle, String Designation, String Description, double Prix, int Stock, int RefCat) {
        this.CodeArticle = CodeArticle;
        this.Designation = Designation;
        this.Description = Description;
        this.Prix = Prix;
        this.Stock = Stock;
        this.RefCat = RefCat;
    }

    public int getCodeArticle() {
        return CodeArticle;
    }

    public void setCodeArticle(int CodeArticle) {
        this.CodeArticle = CodeArticle;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String Designation) {
        this.Designation = Designation;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public double getPrix() {
        return Prix;
    }

    public void setPrix(double Prix) {
        this.Prix = Prix;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int Stock) {
        this.Stock = Stock;
    }

    public InputStream getPhoto() {
        return Photo;
    }

    public void setPhoto(InputStream Photo) {
        this.Photo = Photo;
    }

    public int getRefCat() {
        return RefCat;
    }

    public void setRefCat(int RefCat) {
        this.RefCat = RefCat;
    }

    @Override
    public String toString() {
        return "Produits{" + "CodeArticle=" + CodeArticle + ", Designation=" + Designation + ", Description=" + Description + ", Prix=" + Prix + ", Stock=" + Stock + ", Photo=" + Photo + ", RefCat=" + RefCat + '}';
    }
    
    
}
