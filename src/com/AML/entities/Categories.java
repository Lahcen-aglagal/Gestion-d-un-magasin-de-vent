package com.AML.entities;

/**
 *
 * @author ay0ub
 */
public class Categories {
    int RefCat;
    String CatTitle;
    String CatDescription;

    public Categories() {
    }

    public Categories(String CatTitle, String CatDescription) {
        this.CatTitle = CatTitle;
        this.CatDescription = CatDescription;
    }

    public Categories(int RefCat, String CatTitle, String CatDescription) {
        this.RefCat = RefCat;
        this.CatTitle = CatTitle;
        this.CatDescription = CatDescription;
    }

    public int getRefCat() {
        return RefCat;
    }

    public void setRefCat(int RefCat) {
        this.RefCat = RefCat;
    }

    public String getCatTitle() {
        return CatTitle;
    }

    public void setCatTitle(String CatTitle) {
        this.CatTitle = CatTitle;
    }

    public String getCatDescription() {
        return CatDescription;
    }

    public void setCatDescription(String CatDescription) {
        this.CatDescription = CatDescription;
    }

    @Override
    public String toString() {
        return "Categories{" + "RefCat=" + RefCat + ", CatTitle=" + CatTitle + ", CatDescription=" + CatDescription + '}';
    }

    

    
}
