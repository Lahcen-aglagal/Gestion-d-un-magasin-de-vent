package com.AML.validations;


import com.AML.notifications.NotificationsFactory;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author ay0ub
 */
public class TextFieldValidation {
    public static void selectText(TextField txt){
        txt.focusedProperty().addListener((o,oldVal,newVal) -> {
            Platform.runLater(() -> {
                if(newVal && !txt.getText().isEmpty()){
                    txt.selectAll();
                }
            });
        });
    }
    
    public static boolean ValidateEmail(String email){
        String regx="^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(regx).matcher(email).matches();
    }
    /**
     * Validate password Field
     * 1 at least 8 characters and at most 20 characters
     * 2 al least one digit
     * 3 at least one upper case alphabet
     * 4 at least one lower case alphabet
     * 5 at least one special character which includes !@$#%&*()-=^
     *
     * @param  pass
     *         The String to be Validated
     * @return <tt>true</tt> if, and only if, the entire region sequence
     *          matches this matcher's pattern
     */
    public static boolean ValidatePassword(String pass){
        String regx="^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&-+=])"
                + "(?=\\S+$).{8,20}$";
        return Pattern.compile(regx).matcher(pass).matches();
    }
    public static void ShowHidePassword(JFXTextField txtpassword,JFXPasswordField password,FontAwesomeIconView eye){
        // bind the event of pressed eye to the two textfield 
        // so when it is pressed the textfield will be visible and the passwordfield will not be visible 
        // and when it is released the passwordfield will be visible and the textfield will not be visible
        txtpassword.managedProperty().bind(eye.pressedProperty());
        txtpassword.visibleProperty().bind(eye.pressedProperty());
        password.managedProperty().bind(eye.pressedProperty().not());
        password.visibleProperty().bind(eye.pressedProperty().not());
        // now in every change copy the contents of the passwordfield to the textfield
        txtpassword.textProperty().bindBidirectional(password.textProperty());
        // event for the eye to set it to eye when it is pressed and to eye_slash when it is released
        eye.pressedProperty().addListener((o,oldVal,newVal) -> {
            if(newVal){
                eye.setIcon(FontAwesomeIcon.EYE);
                txtpassword.requestFocus();
                txtpassword.deselect();
            }
            else{
                eye.setIcon(FontAwesomeIcon.EYE_SLASH);
                password.requestFocus();
                password.deselect();
            }
        });
    }
    
    public static void onlyNumbers(JFXTextField txt) {
        txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txt.setText(oldValue);
            }
        });
    }
    
    public static void onlyNumbersRange(JFXTextField txt,int from , int to) {
        txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.isEmpty()){
                if(Integer.parseInt(newValue)<from || Integer.parseInt(newValue)>to ){
                    txt.setText(oldValue);
                    NotificationsFactory.create("Error", "Invalid quantity.\nplease enter a quantity between "+from + " and " + to + ".");
                }
            }
//            else{
//                txt.setText(oldValue);
//            }
        });
    }
    
    public static void onlyDoubleNumbers(TextField txt) {
        txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,10}([\\.]\\d{0,2})?")) {
                txt.setText(oldValue);
            }
        });
    }
    public static void selectTextforJFXTextArea(TextArea txt) {
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            Platform.runLater(() -> {
                if (!txt.getText().isEmpty() && newVal) {
                    txt.selectAll();
                }
            });
        });
    }
    
}
