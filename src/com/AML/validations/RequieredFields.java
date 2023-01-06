package com.AML.validations;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 *
 * @author ay0ub
 */
public class RequieredFields {
    public static final FontAwesomeIcon warning_icon = FontAwesomeIcon.EXCLAMATION_TRIANGLE;
    public static final String message = "Required field!";
    public static void forJfxTextField(JFXTextField txt){
        RequiredFieldValidator validator= new RequiredFieldValidator(message);
        validator.setIcon(new FontAwesomeIconView(warning_icon));
        txt.getValidators().add(validator);
        txt.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal){
                txt.validate();
            }
        });
    }
    public static void forJfxPasswordTextField(JFXPasswordField pwd){
        RequiredFieldValidator validator= new RequiredFieldValidator(message);
        validator.setIcon(new FontAwesomeIconView(warning_icon));
        pwd.getValidators().add(validator);
        pwd.focusedProperty().addListener((o,oldVal,newVal)->{
            if(!newVal){
                pwd.validate();
            }
        });
    }
    public static void forJFXTextArea(JFXTextArea txt) {
        RequiredFieldValidator validator = new RequiredFieldValidator(message);
        validator.setIcon(new FontAwesomeIconView(warning_icon));

        txt.getValidators().add(validator);
        txt.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                txt.validate();
            }
        });
    }
    
    public static void forJFXComboBox(JFXComboBox comboBox) {
        RequiredFieldValidator validator = new RequiredFieldValidator(message);
        validator.setIcon(new FontAwesomeIconView(warning_icon));
        
        comboBox.getValidators().add(validator);
        comboBox.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                comboBox.validate();
            }
        });
    }
    
}
