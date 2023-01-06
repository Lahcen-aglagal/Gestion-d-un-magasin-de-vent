package com.AML.controllers;


import animatefx.animation.FadeIn;
import com.AML.DBUtil.UserSessionDB;
import com.AML.alerts.ConfirmAlert;
import com.AML.animations.Animations;
import com.AML.resources.Constants;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane rootSideMenu;
    @FXML
    private JFXButton btnDashboard;
    @FXML
    private JFXButton btnCategories;
    @FXML
    private JFXButton btnProducts;
    @FXML
    private Circle exit_circle;
    @FXML
    private FontAwesomeIconView exit_font;
    @FXML
    private Circle minus_circle;
    @FXML
    private Label minus_label;
    @FXML
    private JFXButton btnUsers;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private Rectangle rc;
    @FXML
    private Rectangle rc1;
    @FXML
    private Rectangle rc2;
    @FXML
    private Rectangle rc3;
    @FXML
    private Rectangle rc4;
    @FXML
    private AnchorPane rootContainer;
    @FXML
    private JFXButton btnCommandes;
    @FXML
    private Rectangle rc5;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnLogout.requestFocus();
        rc.setVisible(false);
        hoverRectangle();
        exit_minus();
        showHomeInitial();
        Platform.runLater(()->{
            UserSessionDB.checkLogin(btnCategories);
        });
    }
    
    public void showHomeInitial(){
        showWindows("HomeView");
    }
    
    public void showWindows(String view){
        rootContainer.getChildren().clear();
        try {
            Parent root=FXMLLoader.load(getClass().getResource(Constants.VIEWS_PACKAGE + view+".fxml"));
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, 0.0);
            rootContainer.getChildren().setAll(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void hoverRectangle(){
        btnDashboard.hoverProperty().addListener((o,oldVal,NewVal)->{
        if(NewVal){
            rc.setVisible(true);
            Animations.fadeIn(rc);
        }
        else{
            rc.setVisible(false);
            Animations.fadeOut(rc);
        }
    });
    btnCategories.hoverProperty().addListener((o,oldVal,NewVal)->{
        if(NewVal){
            rc1.setVisible(true);
            Animations.fadeIn(rc1);
        }
        else{
            rc1.setVisible(false);
            Animations.fadeOut(rc1);
        }
    });
    btnProducts.hoverProperty().addListener((o,oldVal,NewVal)->{
        if(NewVal){
            rc2.setVisible(true);
            Animations.fadeIn(rc2);
        }
        else{
            rc2.setVisible(false);
            Animations.fadeOut(rc2);
        }
    });
    btnUsers.hoverProperty().addListener((o,oldVal,NewVal)->{
        if(NewVal){
            rc3.setVisible(true);
            Animations.fadeIn(rc3);
        }
        else{
            rc3.setVisible(false);
            Animations.fadeOut(rc3);
        }
    });
    btnCommandes.hoverProperty().addListener((o,oldVal,NewVal)->{
        if(NewVal){
            rc5.setVisible(true);
            Animations.fadeIn(rc5);
        }
        else{
            rc5.setVisible(false);
            Animations.fadeOut(rc5);
        }
    });
    btnLogout.hoverProperty().addListener((o,oldVal,NewVal)->{
        if(NewVal){
            rc4.setVisible(true);
            Animations.fadeIn(rc4);
        }
        else{
            rc4.setVisible(false);
            Animations.fadeOut(rc4);
        }
    });
    }
    
    private void exit_minus() {
        minus_label.setVisible(false);
        exit_font.setVisible(false);
        exit_circle.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleFont(true);
            }
        });
        exit_circle.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleFont(false);
            }            
        });
        exit_font.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleFont(true);
            }
            
        });
        exit_font.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleFont(false);
            }            
        });
        exit_circle.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                ButtonType bt = ConfirmAlert.create("Exit AML", "Attention!!!", "Are you sure you want to exit AMLBoutique?");
                if(bt==ButtonType.OK){
                    UserSessionDB.logout();
                    LoadLogin();
                    System.exit(0);
                }
            }
        });
        exit_font.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                ButtonType bt = ConfirmAlert.create("Exit AML", "Attention!!!", "Are you sure you want to exit AMLBoutique?");
                if(bt==ButtonType.OK){
                    UserSessionDB.logout();
                    LoadLogin();
                    System.exit(0);
                }
            }
        });
        
        
        
        minus_circle.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleLabel(true);
            }
        });
        minus_circle.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleLabel(false);
            }            
        });
        minus_label.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleLabel(true);
            }
            
        });
        minus_label.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleLabel(false);
            }            
        });
        minus_circle.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                ((Stage)minus_circle.getScene().getWindow()).setIconified(true);
            }
        });
        minus_label.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                ((Stage)minus_circle.getScene().getWindow()).setIconified(true);
            }
        });
    }
    public void setvisibleFont(boolean b){
        exit_font.setVisible(b);
    }
    public void setvisibleLabel(boolean b){
        minus_label.setVisible(b);
    }

    @FXML
    private void homeWindows(ActionEvent event) {
        showWindows("HomeView");
    }

    @FXML
    private void CategoriesWindows(ActionEvent event) {
        showWindows("CategoriesView");
    }

    @FXML
    private void ProductsWindows(ActionEvent event) {
        showWindows("ProductsView");
    }

    @FXML
    private void UsersWindows(ActionEvent event) {
        showWindows("UsersView");
    }

    @FXML
    private void LogoutWindows(ActionEvent event) {
        UserSessionDB.logout();
        LoadLogin();
    }

    @FXML
    private void CommandesWindows(ActionEvent event) {
        showWindows("CommandesView");
    }
    
    private void LoadLogin(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(Constants.LOGIN_VIEW));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(Constants.TITLE);
            stage.centerOnScreen();
            stage.getIcons().add(new Image("/resources/aml-logo.jpg"));
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            ((Stage)btnCategories.getScene().getWindow()).close();
            stage.show();
            FadeIn f=new FadeIn(root);
            f.setSpeed(2);
            f.play();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
