package com.AML.controllers;


import com.AML.Conn.connection;
import com.AML.alerts.AlertsBuilder;
import com.AML.entities.Produits;
import com.AML.notifications.NotificationsFactory;
import com.AML.validations.TextFieldValidation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class ItemDetailController implements Initializable {

    @FXML
    private Label ProductName;
    @FXML
    private Label ProductPrice;
    @FXML
    private ImageView ProductImg;
    @FXML
    private Text ProductDescription;
    @FXML
    private JFXTextField txtQuantity;
    @FXML
    private JFXButton addCartBtn;
    @FXML
    private Circle exit_circle;
    @FXML
    private FontAwesomeIconView exit_font;
    private Produits produit;
    @FXML
    private Label stockLabel;
    private ClientMainController clientMainController;
    private JFXDialog dialog;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        exit_font.setVisible(false);
        txtQuantity.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal && txtQuantity.getText().isEmpty()){
                txtQuantity.setText("1");
            }
        });
    }    

    public Circle getExit_circle() {
        return exit_circle;
    }

    public FontAwesomeIconView getExit_font() {
        return exit_font;
    }

    void setProduit(Produits p) {
        this.produit = p;
        ProductName.setText(p.getDesignation());
        ProductDescription.setText(p.getDescription());
        ProductPrice.setText("$" + p.getPrix());
        if(p.getStock()==0)
            stockLabel.setText(p.getStock() + " in stock");
        else
            stockLabel.setText("Only " + p.getStock() + " left in stock");
        ProductImg.setImage(GetImageById(p.getCodeArticle()));
        TextFieldValidation.onlyNumbersRange(txtQuantity, 1, p.getStock());
        
    }
    
    public Image GetImageById(int id){
        Image img=new Image("file:empty-image.jpg");
        try {
            String sql = "SELECT Photo FROM produits WHERE Codeproduits=?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet res = preparedStatement.executeQuery();
            if(res.next()){
                if(res.getBinaryStream("Photo")!=null)
                    img=SwingFXUtils.toFXImage(ImageIO.read(res.getBinaryStream("Photo")), null);
            }
        } catch (SQLException | IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return img;
    }

    @FXML
    private void AddToCart(ActionEvent event) {
        int qtecde = Integer.parseInt(txtQuantity.getText());
//        System.out.println(this.clientMainController);
        if(this.dialog!=null)
            this.dialog.close();
        if(IsProductExistInCart(this.produit.getCodeArticle())){
            if(CheckQuantity(this.produit.getCodeArticle(), this.produit.getStock(),qtecde)){
                if(ChangeQuantity(this.produit.getCodeArticle(),qtecde)){
                    this.clientMainController.showWindows("CartView");
                    AlertsBuilder.create("success",this.clientMainController.getStkClientRoot(), "Product added seccussfully to cart.");
                }
            }
            else{
                NotificationsFactory.create("Error", "You have reach max quantity.");
            }
        }
        else{
            if(InsertLignePanier(this.produit.getCodeArticle(),qtecde)){
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
    
    public boolean ChangeQuantity(int codeproduit ,int qte){
        try {
            String sql = "UPDATE panier set qtecde = qtecde + ? where codeproduit = ? ";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,qte);
            preparedStatement.setInt(2,codeproduit);
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
    
    public boolean CheckQuantity(int codeproduit,int stock,int plusQteCde){
        try {
            String sql = "SELECT qtecde FROM panier where codeproduit = ? ";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,codeproduit);
            ResultSet res= preparedStatement.executeQuery();
            if(res.next()){
                int qtecde = res.getInt("qtecde");
                if(qtecde+plusQteCde<=stock){
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

    void setDialog(JFXDialog dialog) {
        this.dialog = dialog;
    }
    
}
