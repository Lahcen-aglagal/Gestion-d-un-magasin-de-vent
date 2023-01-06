package com.AML.entities;


public class LignePanier {
    private Produits art;
    private int qte;

    public LignePanier(Produits art, int qte) {
        this.art = art;
        this.qte = qte;
    }

    public Produits getArt() {
        return art;
    }

    public void setArt(Produits art) {
        this.art = art;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    @Override
    public String toString() {
        return "LignePanier{" + "art=" + art + ", qte=" + qte + '}';
    }
    
}
