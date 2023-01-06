/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.AML.validations;

import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.scene.control.TextField;

/**
 *
 * @author ay0ub
 */
public class TextFieldValidation1 {
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
    
    public static void onlyNumbers(TextField txt) {
        txt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txt.setText(oldValue);
            }
        });
    }
}
