package com.AML.controllers;

import animatefx.animation.FadeIn;
import com.AML.Conn.connection;
import com.AML.DBUtil.UserSessionDB;
import com.AML.alerts.AlertsBuilder;
import com.AML.alerts.ConfirmAlert;
import com.AML.animations.Animations;
import com.AML.entities.Categories;
import com.AML.notifications.NotificationsFactory;
import com.AML.resources.Constants;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class ClientMainController implements Initializable {

    @FXML
    private AnchorPane rootSideMenu;
    @FXML
    private JFXButton btnHome;
    @FXML
    private Rectangle rc;
    @FXML
    private Rectangle rc1;
    @FXML
    private Rectangle rc2;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private Rectangle rc4;
    @FXML
    private JFXButton btnCommandes;
    @FXML
    private AnchorPane rootContainer;
    @FXML
    private Circle exit_circle;
    @FXML
    private FontAwesomeIconView exit_font;
    @FXML
    private Circle minus_circle;
    @FXML
    private Label minus_label;
    @FXML
    private JFXButton btnCart;
    @FXML
    private JFXButton btnAbout;
    @FXML
    private Rectangle rc3;
    @FXML
    private Label UserName;
    @FXML
    private StackPane stkClientRoot;
    @FXML
    private AnchorPane anchorClientRoot;

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
        UserName.setText(GetUserName());
        Animations.Textglow(UserName);
        Platform.runLater(()->{
            UserSessionDB.checkLogin(UserName);
        });
    }
    public String GetUserName(){
        try {
            String sql = "select concat(nom ,' ' ,prenom) as 'username' from clients clt , UserSession u where clt.Id = u.CodeClient";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String str = resultSet.getString("username");
                str = str.substring(0, 1).toUpperCase() + str.substring(1);
                return str;
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return "";
    }
    
    public void showHomeInitial(){
        showWindows("ClientHomeView");
    }
    
    public void showWindows(String view){
        rootContainer.getChildren().clear();
        try {
//            Parent root=FXMLLoader.load(getClass().getResource(view+".fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(Constants.VIEWS_PACKAGE + view + ".fxml"));
            Parent root = fxmlLoader.load();
            if(view.equals("ClientHomeView")){
                ClientHomeViewController ch = fxmlLoader.getController();
                ch.setstkroot(stkClientRoot);
                ch.setanchor(anchorClientRoot);
                ch.setClientMainController(this);
            }
            else if(view.equals("CartView")){
                CartViewController cv = fxmlLoader.getController();
                cv.setstkroot(stkClientRoot);
                cv.setanchor(anchorClientRoot);
                cv.setClientMainController(this);
            }
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
        btnHome.hoverProperty().addListener((o,oldVal,NewVal)->{
        if(NewVal){
            rc.setVisible(true);
            Animations.fadeIn(rc);
        }
        else{
            rc.setVisible(false);
            Animations.fadeOut(rc);
        }
    });
    btnCart.hoverProperty().addListener((o,oldVal,NewVal)->{
        if(NewVal){
            rc1.setVisible(true);
            Animations.fadeIn(rc1);
        }
        else{
            rc1.setVisible(false);
            Animations.fadeOut(rc1);
        }
    });
    btnCommandes.hoverProperty().addListener((o,oldVal,NewVal)->{
        if(NewVal){
            rc2.setVisible(true);
            Animations.fadeIn(rc2);
        }
        else{
            rc2.setVisible(false);
            Animations.fadeOut(rc2);
        }
    });
    btnAbout.hoverProperty().addListener((o,oldVal,NewVal)->{
        if(NewVal){
            rc3.setVisible(true);
            Animations.fadeIn(rc3);
        }
        else{
            rc3.setVisible(false);
            Animations.fadeOut(rc3);
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
                    try{
                        UserSessionDB.logout();
                        TruncatePanier();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource(Constants.LOGIN_VIEW));
                        Parent root = loader.load();
                        Stage stage = new Stage();
                        stage.setTitle(Constants.TITLE);
                        stage.getIcons().add(new Image("/resources/aml-logo.jpg"));
                        stage.centerOnScreen();
                        stage.initStyle(StageStyle.UNDECORATED);
                        Scene scene = new Scene(root, 1000, 600);
                        stage.setScene(scene);
                        ((Stage)btnAbout.getScene().getWindow()).close();
                        stage.show();
                        FadeIn f=new FadeIn(root);
                        f.setSpeed(2);
                        f.play();
                        System.exit(0);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        exit_font.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                ButtonType bt = ConfirmAlert.create("Exit AML", "Attention!!!", "Are you sure you want to exit AMLBoutique?");
                if(bt==ButtonType.OK){
                    try{
                        UserSessionDB.logout();
                        TruncatePanier();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource(Constants.LOGIN_VIEW));
                        Parent root = loader.load();
                        Stage stage = new Stage();
                        stage.setTitle(Constants.TITLE);
                        stage.getIcons().add(new Image("/resources/aml-logo.jpg"));
                        stage.centerOnScreen();
                        stage.initStyle(StageStyle.UNDECORATED);
                        Scene scene = new Scene(root, 1000, 600);
                        stage.setScene(scene);
                        ((Stage)btnAbout.getScene().getWindow()).close();
                        stage.show();
                        FadeIn f=new FadeIn(root);
                        f.setSpeed(2);
                        f.play();
                        System.exit(0);
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
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
        showWindows("ClientHomeView");
    }


    @FXML
    private void LogoutWindows(ActionEvent event) {
        try {
            UserSessionDB.logout();
            TruncatePanier();
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
            ((Stage)btnAbout.getScene().getWindow()).close();
            stage.show();
            FadeIn f=new FadeIn(root);
            f.setSpeed(2);
            f.play();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void TruncatePanier(){
        try {
            String sql = "truncate table panier";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void CommandesWindows(ActionEvent event) {
        showWindows("ClientCommandesView");
    }

    @FXML
    private void CartWindows(ActionEvent event) {
        showWindows("CartView");
    }

    @FXML
    private void AboutWindows(ActionEvent event) {
        showWindows("AboutView");
    }

    public AnchorPane getRootContainer() {
        return rootContainer;
    }

    public StackPane getStkClientRoot() {
        return stkClientRoot;
    }
    
}
