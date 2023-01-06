package com.AML.alerts;

import animatefx.animation.FadeIn;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author ay0ub
 */
public class ConfirmAlert {
    private static ConfirmAlert c = new ConfirmAlert();
    
    public String getStyle(String name){
        return getClass().getResource(name).toExternalForm();
    }
    
    public static ButtonType create(String title,String header,String body){
        
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.initStyle(StageStyle.UNDECORATED);
        ((Stage)a.getDialogPane().getScene().getWindow()).centerOnScreen();
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(body);
        a.getDialogPane().getStylesheets().add(c.getStyle("style.css"));
        Optional<ButtonType> o =a.showAndWait();
        new FadeIn(a.getDialogPane()).play();
        return o.get();
        
    }
}
