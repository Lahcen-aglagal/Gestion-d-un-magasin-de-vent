package com.AML.entities;

import com.jfoenix.controls.JFXButton;
import java.sql.Date;

/**
 *
 * @author ay0ub
 */
public class CommandeTable {
    private int NumCommande;
    private Date DateCommande;
    private int TotalItems;
    private String ClientName;
    private double TotalPrice;
    private JFXButton deletebtn;

    public CommandeTable() {
    }

    public CommandeTable(int NumCommande, Date DateCommande, int TotalItems, String ClientName, double TotalPrice, JFXButton deletebtn) {
        this.NumCommande = NumCommande;
        this.DateCommande = DateCommande;
        this.TotalItems = TotalItems;
        this.ClientName = ClientName;
        this.TotalPrice = TotalPrice;
        this.deletebtn = deletebtn;
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

    public int getTotalItems() {
        return TotalItems;
    }

    public void setTotalItems(int TotalItems) {
        this.TotalItems = TotalItems;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String ClientName) {
        this.ClientName = ClientName;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public JFXButton getDeletebtn() {
        return deletebtn;
    }

    public void setDeletebtn(JFXButton deletebtn) {
        this.deletebtn = deletebtn;
    }
    
    
}
