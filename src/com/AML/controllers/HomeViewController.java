package com.AML.controllers;

import com.AML.Conn.connection;
import com.AML.DBUtil.UserSessionDB;
import com.AML.animations.Animations;
import com.jfoenix.controls.JFXRippler;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class HomeViewController implements Initializable {

    @FXML
    private StackPane stackHome;
    @FXML
    private Label totalUsers;
    @FXML
    private Label totalProducts;
    @FXML
    private Label totalCategories;
    @FXML
    private AnchorPane rootwelcome;
    @FXML
    private AnchorPane rootTotalUsers;
    @FXML
    private AnchorPane rootTotalProducts;
    @FXML
    private AnchorPane rootTotalCategories;
    @FXML
    private AnchorPane rootTotalOrders;
    @FXML
    private Label totalCommandes;
    @FXML
    private FontAwesomeIconView fontUsers;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAnimations();
        CountRecords();
        Platform.runLater(()->{
            UserSessionDB.checkLogin(totalCategories);
        });
    }

    public void setAnimations(){
        Animations.fadeIn(rootwelcome);
        Animations.fadeIn(rootTotalUsers);
        Animations.fadeIn(rootTotalProducts);
        Animations.fadeIn(rootTotalCategories);
        Animations.fadeIn(rootTotalOrders);
    }
    private void CountRecords() {
        try {
            String sql = "SELECT (SELECT COUNT(*) FROM clients) AS clients, "
                    + "(SELECT COUNT(*) FROM produits) AS produits, "
                    + "(SELECT COUNT(*) FROM categories) AS categories,"
                    + "(SELECT COUNT(*) FROM commandes) AS commandes";
            PreparedStatement preparedStatetent = connection.getInstance().prepareStatement(sql);
            ResultSet rs = preparedStatetent.executeQuery();
            while (rs.next()) {
                totalUsers.setText(String.valueOf(rs.getInt(1)));
                totalProducts.setText(String.valueOf(rs.getInt(2)));
                totalCategories.setText(String.valueOf(rs.getInt(3)));
                totalCommandes.setText(String.valueOf(rs.getInt(4)));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
}
