/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.AML.entities;

/**
 *
 * @author ay0ub
 */
public class LigneCommandes {
    private int CodeArticle;
    private int NumCommande;
    private int QteCde;

    public LigneCommandes() {
    }

    public LigneCommandes(int CodeArticle, int NumCommande, int QteCde) {
        this.CodeArticle = CodeArticle;
        this.NumCommande = NumCommande;
        this.QteCde = QteCde;
    }

    public int getCodeArticle() {
        return CodeArticle;
    }

    public void setCodeArticle(int CodeArticle) {
        this.CodeArticle = CodeArticle;
    }

    public int getNumCommande() {
        return NumCommande;
    }

    public void setNumCommande(int NumCommande) {
        this.NumCommande = NumCommande;
    }

    public int getQteCde() {
        return QteCde;
    }

    public void setQteCde(int QteCde) {
        this.QteCde = QteCde;
    }

    
}
