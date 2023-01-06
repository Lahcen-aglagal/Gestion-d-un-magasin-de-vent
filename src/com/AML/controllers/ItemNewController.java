package com.AML.controllers;


import com.AML.Conn.connection;
import com.AML.alerts.AlertsBuilder;
import com.AML.entities.ClickListener;
import com.AML.entities.Produits;
import com.AML.notifications.NotificationsFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class ItemNewController implements Initializable {

    @FXML
    private Label nameLabel;
    @FXML
    private Label priceLable;
    @FXML
    private ImageView img;
    private Produits produit;
    private ClickListener cl;
    @FXML
    private VBox vboxContainer;
    @FXML
    private JFXButton btnAddCart;
    private ClientMainController clientMainController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setData(Produits p, ClickListener cl) {
        this.produit = p;
        this.cl = cl;
        nameLabel.setText(p.getDesignation());
        priceLable.setText("$" + p.getPrix());
        Image image=new Image("/resources/empty-image.jpg");
        try {
            if(p.getPhoto()!=null)
                image=SwingFXUtils.toFXImage(ImageIO.read(p.getPhoto()), null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        img.setImage(image);
    }
    public Produits getProduit(){
        return this.produit;
    }

    public VBox getVboxContainer() {
        return vboxContainer;
    }

    @FXML
    private void clickHandle(MouseEvent event) {
        this.cl.onClick(this.produit);
    }

    @FXML
    private void AddToCart(ActionEvent event) {
        if(IsProductExistInCart(this.produit.getCodeArticle())){
            if(CheckQuantity(this.produit.getCodeArticle(), this.produit.getStock())){
                if(IncrementQuantity(this.produit.getCodeArticle())){
                    this.clientMainController.showWindows("CartView");
                    AlertsBuilder.create("success",this.clientMainController.getStkClientRoot(), "Product added seccussfully to cart.");
                }
            }
            else{
                NotificationsFactory.create("Error", "You have reach max quantity.");
            }
        }
        else{
            if(InsertLignePanier(this.produit.getCodeArticle(),1)){
                this.clientMainController.showWindows("CartView");
                AlertsBuilder.create("success",this.clientMainController.getStkClientRoot(), "Product added seccussfully to cart.");
            }
            else{
                NotificationsFactory.create("Error", "please check your connection.");
            }
        }
    }
    
    public boolean InsertLignePanier(int codeproduit,int qte){
        try {
            String sql = "INSERT INTO panier values (?,?)";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,codeproduit);
            preparedStatement.setInt(2,qte);
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean ChangeQuantity(int qte){
        try {
            String sql = "UPDATE panier set qtecde = ? where codeproduit = ? ";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,qte);
            preparedStatement.setInt(2,this.produit.getCodeArticle());
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean IncrementQuantity(int codeproduit){
        try {
            String sql = "UPDATE panier set qtecde = qtecde+1 where codeproduit = ? ";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,codeproduit);
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean CheckQuantity(int codeproduit,int stock){
        try {
            String sql = "SELECT qtecde FROM panier where codeproduit = ? ";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,codeproduit);
            ResultSet res= preparedStatement.executeQuery();
            if(res.next()){
                int qtecde = res.getInt("qtecde");
                if(qtecde<stock){
                    return true;
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public boolean IsProductExistInCart(int codeProduit){
        try {
            String sql = "SELECT codeproduit FROM panier where codeproduit = ?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1, codeProduit);
            ResultSet res = preparedStatement.executeQuery();
            return res.next();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public int GetCodeClient(){
        int code = -1;
        try {
            String sql = "SELECT CodeClient FROM UserSession";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            ResultSet res = preparedStatement.executeQuery();
            if(res.next()){
                code = res.getInt("CodeClient");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return code;
    }

    void setClientMainController(ClientMainController clientMainController) {
        this.clientMainController = clientMainController;
    }
    
}
