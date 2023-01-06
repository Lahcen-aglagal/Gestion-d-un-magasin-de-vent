package com.AML.DBUtil;

import animatefx.animation.FadeIn;
import com.AML.Conn.connection;
import com.AML.notifications.NotificationsFactory;
import com.AML.resources.Constants;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author AML
 */
public class UserSessionDB {
    private static UserSessionDB us=new UserSessionDB();
    public static boolean insertUserSession(int id) throws ClassNotFoundException{
        try {
            String sql = "insert into UserSession (CodeClient) values (?)";
            PreparedStatement pt = connection.getInstance().prepareStatement(sql);
            pt.setInt(1, id);
            pt.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    public static void logout(){
        try {
            String sql = "TRUNCATE TABLE UserSession";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void checkLogin(Node node){
        try{
            if(!us.checkIfLoggedIn()){
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(us.getResource());
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle(Constants.TITLE);
                stage.centerOnScreen();
                stage.getIcons().add(new Image("/resources/aml-logo.jpg"));
                stage.initStyle(StageStyle.UNDECORATED);
                Scene scene = new Scene(root, 1000, 600);
                stage.setScene(scene);
                ((Stage)node.getScene().getWindow()).close();
                stage.show();
                FadeIn f=new FadeIn(root);
                f.setSpeed(2);
                f.play();
                NotificationsFactory.create("Error", "please log in first!!!");
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public boolean checkIfLoggedIn(){
        try {
            String sql = "SELECT * FROM UserSession";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            ResultSet res =  preparedStatement.executeQuery();
            return res.next();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private URL getResource() {
        return getClass().getResource((Constants.LOGIN_VIEW));
    }
    
}
