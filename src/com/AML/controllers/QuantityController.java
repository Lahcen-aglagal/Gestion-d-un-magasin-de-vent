package com.AML.controllers;

import com.AML.Conn.connection;
import com.AML.alerts.AlertsBuilder;
import com.AML.entities.LignePanier;
import com.AML.notifications.NotificationsFactory;
import com.AML.validations.TextFieldValidation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class QuantityController implements Initializable {

    @FXML
    private JFXButton btnMinus;
    @FXML
    private JFXTextField txtQuantity;
    @FXML
    private JFXButton btnPlus;
    
    private LignePanier lignepanier;
    private StackPane stkCart;
    private CartViewController CartController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtQuantity.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent k) {
                if(k.getCode()==KeyCode.ENTER){
                    if(Integer.parseInt(txtQuantity.getText()) != lignepanier.getQte()){
                        if(ChangeQuantity(Integer.parseInt(txtQuantity.getText()))){
                            lignepanier.setQte(Integer.parseInt(txtQuantity.getText()));
                            CartController.loadTable();
                            AlertsBuilder.create("success", stkCart, "Quantity updated seccussfully");
                        }
                        else{
                            NotificationsFactory.create("Error", "please check your connection");
                        }
                    }
                }
            }
        });
    }
    
    public void setProduit(LignePanier lp){
        this.lignepanier=lp;
    }

    @FXML
    private void minusQuantity(ActionEvent event) {
        int tmp = Integer.parseInt(txtQuantity.getText())-1;
        if(tmp<1){
            NotificationsFactory.create("Error", "Invalid quantity.\nquantity must be between 1 and " + this.lignepanier.getArt().getStock() + ".");
        }
        else
            txtQuantity.setText(String.valueOf(tmp));
    }

    @FXML
    private void PlusQuantity(ActionEvent event) {
        int tmp = Integer.parseInt(txtQuantity.getText())+1;
        if(tmp>this.lignepanier.getArt().getStock()){
            NotificationsFactory.create("Error", "Invalid quantity.\nquantity must be between 1 and " + this.lignepanier.getArt().getStock() + ".");
        }
        else
            txtQuantity.setText(String.valueOf(tmp));
//        txtQuantity.setText(String.valueOf(Integer.parseInt(txtQuantity.getText())-1));
    }

    public void setQuantity() {
        txtQuantity.setText(String.valueOf(this.lignepanier.getQte()));
        TextFieldValidation.onlyNumbersRange(txtQuantity, 1, this.lignepanier.getArt().getStock());
        txtQuantity.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal){
                txtQuantity.setText(String.valueOf(this.lignepanier.getQte()));
            }
        });
    }
    
    public boolean ChangeQuantity(int qte){
        try {
            String sql = "UPDATE panier set qtecde = ? where codeproduit = ? ";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,qte);
            preparedStatement.setInt(2,this.lignepanier.getArt().getCodeArticle());
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    void setStkCart(StackPane stkCart) {
        this.stkCart=stkCart;
    }

    void setCartController(CartViewController CartController) {
        this.CartController=CartController;
    }
    
}
