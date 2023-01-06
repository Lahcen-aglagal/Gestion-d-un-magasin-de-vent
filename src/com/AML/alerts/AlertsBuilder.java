package com.AML.alerts;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AlertsBuilder {

    private static String title;
    
    private static String buttonClass;
    
    private static String titleClass;
    
    private static String bodyClass;
    
    private static JFXDialog dialog;

    public static void create(String type, StackPane dialogContainer, String body) {
        setType(type);

        AnchorPane root = new AnchorPane();
        root.setPrefSize(360, 200);

        JFXButton button = new JFXButton("Ok");
        button.getStyleClass().add(buttonClass);
        button.setCursor(Cursor.HAND);

        HBox buttonContainer = new HBox();
        buttonContainer.setLayoutY(115);
        buttonContainer.setPrefSize(390, 115);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.getChildren().addAll(button);

        Text textTitle = new Text(title);
        textTitle.getStyleClass().add(titleClass);

        Text textBody = new Text(body);
        textBody.getStyleClass().add(bodyClass);

        VBox textContainer = new VBox();
        VBox.setMargin(textBody, new Insets(10));
        textContainer.setSpacing(5);
        textContainer.setPrefSize(390, 115);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        textContainer.setPadding(new Insets(0, 0, 0, 30));
        textContainer.getChildren().addAll(textTitle, textBody);
        root.getChildren().addAll(buttonContainer, textContainer);

        dialog = new JFXDialog();
        dialog.setContent(root);
        dialog.setDialogContainer(dialogContainer);
        dialog.setBackground(Background.EMPTY);
        dialog.getStyleClass().add("jfx-dialog-overlay-pane");
        dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        dialog.show();

        button.setOnMouseClicked(e -> {
            dialog.close();
        });
    }
    
    public static void close() {
        if (dialog != null) {
            dialog.close();
        }
    }

    private static void setType(String type) {
        switch (type) {
            case "success":
                title = "Success!";
                buttonClass = "alert-success-button";
                titleClass = "alert-success-title";
                bodyClass = "alert-success-body";
            break;
            
            case "error":
                title = "Ooops!!";
                buttonClass = "alert-error-button";
                titleClass = "alert-error-title";
                bodyClass = "alert-error-body";
            break;
        }
    }
  
}
