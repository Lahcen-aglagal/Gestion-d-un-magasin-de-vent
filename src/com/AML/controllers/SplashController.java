package com.AML.controllers;


import animatefx.animation.FadeIn;
import animatefx.animation.ZoomIn;
import animatefx.animation.ZoomOut;
import com.AML.animations.Animations;
import com.AML.resources.Constants;
import com.jfoenix.controls.JFXProgressBar;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class SplashController implements Initializable {

    @FXML
    private Text txt;
    @FXML
    private Text statusb;
    @FXML
    private JFXProgressBar progressb;
    @FXML
    private AnchorPane ap;
    @FXML
    private ImageView spiral;

    
    class ShowSplashScreen extends Thread{

        @Override
        public void run(){
            FadeIn f=new FadeIn(statusb);
              try{
                for (int i = 0 ; i <= 10 ; i++){
                    double x = i * (0.1);
                    Animations.progressAnimation(progressb, x);
                    if(i==2){
                        statusb.setText("conneccting to database...");
                        f.play();
                    }
                    if(i==5){
                        statusb.setText("loading modules...");
                        f.play();
                    }
                    if(i==8){
                        statusb.setText("restoring sessions...");
                        f.play();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i==0){
                        txt.setVisible(true);
                        ZoomIn z=new ZoomIn(txt);
                        z.play();
                        spiral.setVisible(true);
                        ZoomIn z1 = new ZoomIn(spiral);
                        z1.play();
                    }
                }

                Platform.runLater(() -> {
                    Stage stage = new Stage();
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource(Constants.VIEWS_PACKAGE + "login.fxml"));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.getIcons().add(new Image("/resources/aml-logo.jpg"));
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();
                    ap.getScene().getWindow().hide();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    class spiralRotate extends Thread{
        @Override
        public void run(){
            RotateTransition rot=new RotateTransition();
            rot.setNode(spiral);
            rot.setCycleCount(10);
            rot.setAutoReverse(false);
            rot.setDuration(Duration.millis(1000));
            rot.setByAngle(360);
            rot.setRate(0.5d);
            rot.play();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt.setVisible(false);
        spiral.setVisible(false);
        new ShowSplashScreen().start();
        new spiralRotate().start();
    }    
    
}
