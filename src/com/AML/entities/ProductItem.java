package com.AML.entities;

import javafx.scene.layout.AnchorPane;

/**
 *
 * @author ay0ub
 */
public class ProductItem {
    private Produits produit;
    private AnchorPane anchopane;

    public ProductItem() {
    }

    public ProductItem(Produits produit, AnchorPane anchopane) {
        this.produit = produit;
        this.anchopane = anchopane;
    }

    public Produits getProduit() {
        return produit;
    }

    public void setProduit(Produits produit) {
        this.produit = produit;
    }

    public AnchorPane getAnchopane() {
        return anchopane;
    }

    public void setAnchopane(AnchorPane anchopane) {
        this.anchopane = anchopane;
    }
    
}
