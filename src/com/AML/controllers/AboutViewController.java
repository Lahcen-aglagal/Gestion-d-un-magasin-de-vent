package com.AML.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import animatefx.animation.Flip;
import animatefx.animation.ZoomIn;
import com.AML.DBUtil.UserSessionDB;
import com.AML.animations.Animations;
import javafx.application.Platform;
import javafx.scene.control.Label;
/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class AboutViewController implements Initializable {

    @FXML
    private StackPane stackHome;
    @FXML
    private Pane titlePane;
    @FXML
    private VBox info;
    @FXML
    private Label AyoubNouri;
    @FXML
    private Label aous;
    @FXML
    private Label AyoubDerdori;
    @FXML
    private Label LahsenAglagal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAnimations();
        Platform.runLater(()->{
            UserSessionDB.checkLogin(AyoubNouri);
        });
    }   
    
    public void setAnimations(){
        Animations.zoomIn(titlePane);
        Animations.zoomIn(info);
        Animations.zoomIn(AyoubNouri);
        Animations.zoomIn(aous);
        Animations.zoomIn(AyoubDerdori);
        Animations.zoomIn(LahsenAglagal);
        Animations.Textglow(AyoubNouri);
        Animations.Textglow(aous);
        Animations.Textglow(AyoubDerdori);
        Animations.Textglow(LahsenAglagal);
    }
    
}
