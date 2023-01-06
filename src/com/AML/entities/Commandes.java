package com.AML.entities;

import java.sql.Date;

/**
 *
 * @author ay0ub
 */
public class Commandes {
    private int NumCommande;
    private Date DateCommande;
    private int CodeClient;

    public Commandes() {
    }

    public Commandes(Date DateCommande, int CodeClient) {
        this.DateCommande = DateCommande;
        this.CodeClient = CodeClient;
    }

    public int getNumCommande() {
        return NumCommande;
    }

    public void setNumCommande(int NumCommande) {
        this.NumCommande = NumCommande;
    }

    public Date getDateCommande() {
        return DateCommande;
    }

    public void setDateCommande(Date DateCommande) {
        this.DateCommande = DateCommande;
    }

    public int getCodeClient() {
        return CodeClient;
    }

    public void setCodeClient(int CodeClient) {
        this.CodeClient = CodeClient;
    }

    
}
