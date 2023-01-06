package com.AML.notifications;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author ay0ub
 */
public class NotificationsFactory {
    public static Image icon;
    public static String title;
    
    public static void create(String NotificationType,String message){
        setType(NotificationType);
        Notifications n=Notifications.create();
        n.darkStyle();
        n.title(title);
        n.text(message);
        n.graphic(new ImageView(icon));
        n.hideAfter(Duration.seconds(5));
        n.position(Pos.BASELINE_RIGHT);
        n.show();
    }

    private static void setType(String NotificationType) {
        switch(NotificationType){
            case "Error":
                icon=new Image("/resources/error.png");
                title = "Error!";
            break;
            case "Information":
                icon=new Image("/resources/information.png");
                title = "Information!";
            break;
            case "Success":
                icon=new Image("/resources/success.png");
                title = "Success!";
            break;
            case "Invalid_Action":
                icon=new Image("/resources/error.png");
                title = "Invalid action!";
            break;
        }
    }
}
