package com.AML;

import animatefx.animation.FadeIn;
import com.AML.resources.Constants;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author AML
 */
public class App extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(Constants.SPLASH_VIEW));
        
        Scene scene = new Scene(root, 700, 450);
        
        primaryStage.setTitle(Constants.TITLE);
        primaryStage.getIcons().add(new Image("/resources/aml-logo.jpg"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.centerOnScreen();
        primaryStage.setScene(scene);
        primaryStage.show();
        FadeIn f=new FadeIn(root);
        f.setSpeed(2);
        f.play();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
