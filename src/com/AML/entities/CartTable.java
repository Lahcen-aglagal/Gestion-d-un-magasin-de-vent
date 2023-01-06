package com.AML.entities;

import com.jfoenix.controls.JFXButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author ay0ub
 */
public class CartTable {
    private int codeproduit;
    private String Title;
    private double Price;
    private HBox Quantity;
    private double Total;
    private JFXButton view;
    private JFXButton action;

    public CartTable() {
    }

    public CartTable(String Title, double Price, HBox Quantity, double Total, JFXButton view, JFXButton action) {
        this.Title = Title;
        this.Price = Price;
        this.Quantity = Quantity;
        this.Total = Total;
        this.view = view;
        this.action = action;
    }

    public int getCodeproduit() {
        return codeproduit;
    }

    public void setCodeproduit(int codeproduit) {
        this.codeproduit = codeproduit;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public HBox getQuantity() {
        return Quantity;
    }

    public void setQuantity(HBox Quantity) {
        this.Quantity = Quantity;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

    public JFXButton getView() {
        return view;
    }

    public void setView(JFXButton view) {
        this.view = view;
    }

    public JFXButton getAction() {
        return action;
    }

    public void setAction(JFXButton action) {
        this.action = action;
    }
    
    
}
