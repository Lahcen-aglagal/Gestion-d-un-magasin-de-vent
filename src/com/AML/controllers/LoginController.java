package com.AML.controllers;



import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutLeft;
import animatefx.animation.FadeOutRight;
import com.AML.Conn.connection;
import com.AML.DBUtil.ClientsDB;
import com.AML.DBUtil.UserSessionDB;
import com.AML.animations.Animations;
import com.AML.entities.Clients;
import com.AML.notifications.NotificationsFactory;
import com.AML.resources.Constants;
import com.AML.validations.RequieredFields;
import com.AML.validations.TextFieldValidation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class LoginController implements Initializable {

    private List<Pane> panes = new ArrayList<>();
    private int currentSlide = 0;
    @FXML
    private Pane pane1;
    @FXML
    private Pane pane2;
    @FXML
    private Pane pane3;
    @FXML
    private AnchorPane imgAp;
    @FXML
    private Circle exit_circle;
    @FXML
    private FontAwesomeIconView exit_font;
    @FXML
    private Circle minus_circle;
    @FXML
    private Label minus_label;
    @FXML
    private JFXTextField txtpassword;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private FontAwesomeIconView eye;
    @FXML
    private JFXButton login_btn;
    @FXML
    private AnchorPane leftAp;
    @FXML
    private Pane registre_pane;
    @FXML
    private FontAwesomeIconView arrow;
    @FXML
    private AnchorPane rightAp;
    @FXML
    private Hyperlink registrelink;
    boolean isInLogin=true;
    TranslateTransition tt;
    Timeline t ;
    @FXML
    private ImageView imgv1;
    @FXML
    private Pane step1;
    @FXML
    private JFXTextField FirstName;
    @FXML
    private JFXTextField LastName;
    @FXML
    private JFXTextField Email;
    @FXML
    private JFXPasswordField password_registre;
    @FXML
    private JFXButton btnNextStep1;
    @FXML
    private Pane step2;
    @FXML
    private JFXTextField PostalCode;
    @FXML
    private JFXTextField City;
    @FXML
    private JFXTextField Adress;
    @FXML
    private JFXButton btnNextStep2;
    @FXML
    private JFXTextField PhoneNumber;
    @FXML
    private JFXButton btnBackStep2;
    @FXML
    private Pane step3;
    @FXML
    private Label txtStep3;
    @FXML
    private Label label_progress;
    @FXML
    private JFXProgressBar registre_progress;
    @FXML
    private JFXTextField txtpassword_registre;
    @FXML
    private FontAwesomeIconView eye_registre;
    private Clients clt=new Clients();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        exit_font.setVisible(false);
        minus_label.setVisible(false);
        panes.add(pane1);
        panes.add(pane2);
        panes.add(pane3);
        t= new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            FadeOutLeft fol = new FadeOutLeft(panes.get(currentSlide % 3));
            FadeOutRight fr=new FadeOutRight(panes.get(currentSlide % 3));
            fol.setResetOnFinished(true);
            fr.setResetOnFinished(true);
            if(isInLogin){
                fol.play();
                fol.setOnFinished(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent arg0) {
                        panes.get(currentSlide % 3).setVisible(false);
                        panes.get(++currentSlide % 3).setVisible(true);
                    } 
                });
            }
            else{
                fr.play();
                fr.setOnFinished(new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent arg0) {
                        panes.get(currentSlide % 3).setVisible(false);
                        panes.get(++currentSlide % 3).setVisible(true);
                    }
                });
            }
        }));
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
        tt=new TranslateTransition(Duration.seconds(2), imgAp);
        // events for exit button and minus button
        exit_minus();
        // request focus on username's field when the scene is loaded
        username.requestFocus();
        //show and hide the content of the password field while clicking and releasing the eye 
        ShowHidePassword();
        TextFieldValidation.ShowHidePassword(txtpassword_registre, password_registre, eye_registre);
        // display required field when the field is lived empty
        Validation();
        // select all text in a field when it if on focus
        SelectTextOnfocus();
        // check the movement of the imgAp with axisX
        imgAp.translateXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                if(!isInLogin && arg2.intValue()==500){
                    registre_pane.setVisible(true);
                    new FadeIn(registre_pane).play();
                    return;
                }
                if(isInLogin && arg2.intValue()==0){
                    rightAp.setVisible(true);
                    new FadeIn(rightAp).play();
                }
            }
        });
        // reset the registration pane
        startupRegistre();
        // set a tooltip to the arrow 
        Tooltip tooltip = new Tooltip("Back to login");
        tooltip.setFont(Font.font("Special Elite"));
        Tooltip.install(arrow, tooltip);
        
    }
    public void setvisibleFont(boolean b){
        exit_font.setVisible(b);
    }
    public void setvisibleLabel(boolean b){
        minus_label.setVisible(b);
    }

    private void ShowHidePassword() {
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

    private void Validation() {
        // requierd fields
        RequieredFields.forJfxTextField(username);
        RequieredFields.forJfxTextField(txtpassword);
        RequieredFields.forJfxTextField(FirstName);
        RequieredFields.forJfxTextField(LastName);
        RequieredFields.forJfxTextField(Email);
        RequieredFields.forJfxPasswordTextField(password_registre);
        RequieredFields.forJfxTextField(Adress);
        RequieredFields.forJfxTextField(PostalCode);
        RequieredFields.forJfxTextField(City);
        RequieredFields.forJfxTextField(PhoneNumber);
        RequieredFields.forJfxPasswordTextField(password);
        // validate fields
        TextFieldValidation.onlyNumbers(PostalCode);
        TextFieldValidation.onlyNumbers(PhoneNumber);
    }

    private void SelectTextOnfocus() {
        TextFieldValidation.selectText(txtpassword);
        TextFieldValidation.selectText(password);
        TextFieldValidation.selectText(username);
        TextFieldValidation.selectText(FirstName);
        TextFieldValidation.selectText(LastName);
        TextFieldValidation.selectText(Email);
        TextFieldValidation.selectText(password_registre);
        TextFieldValidation.selectText(Adress);
        TextFieldValidation.selectText(PostalCode);
        TextFieldValidation.selectText(City);
        TextFieldValidation.selectText(PhoneNumber);
    }

    @FXML
    private void login(ActionEvent event)  {
        String user=username.getText().trim();
        String pass=password.getText().trim();
        if(user.isEmpty() && pass.isEmpty()){
            Animations.shake(username);
            Animations.shake(password);
            Animations.shake(eye);
            NotificationsFactory.create("Error", "Missing Data");
            return;
        }
        if(user.isEmpty()){
            username.requestFocus();
            username.validate();
            Animations.shake(username);
            return;
        }
        if(pass.isEmpty()){
            password.requestFocus();
            password.validate();
            Animations.shake(password);
            return;
        }
        if(!TextFieldValidation.ValidateEmail(user)){
            username.requestFocus();
            username.validate();
            Animations.shake(username);
            NotificationsFactory.create("Error", "Email not valid\nPlease enter a valid Email");
            return;
        }
        // interact with the database here
        try{
            String sql = "select * from clients where Email=? and MotPasse=?";
            Connection con = connection.getInstance();
            PreparedStatement pt = con.prepareStatement(sql);
            pt.setString(1, user);
            pt.setString(2, pass);
            ResultSet res = pt.executeQuery();
            if(res.next()){
                int id=res.getInt("Id");
                String fullname=res.getString("Nom") + " " + res.getString("Prenom");
                boolean isAdmin = res.getBoolean("isAdmin");
                boolean result = UserSessionDB.insertUserSession(id);
                if(result){
                    loadMainView(isAdmin);
                    NotificationsFactory.create("Success","Welcome "+fullname);
                }
            }
            else{
                NotificationsFactory.create("Error", "Invalid username or password");
                Animations.shake(txtpassword);
                Animations.shake(password);
                Animations.shake(username);
                Animations.shake(eye);
            }
        }
        catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
            NotificationsFactory.create("Error", "Error when connecting to database.\n please check your connecction");
        }
    }
    public void loadMainView(boolean isAdmin){
        try {
            FXMLLoader loader;
            Parent root;
            if(isAdmin){
                loader= new FXMLLoader();
                loader.setLocation(getClass().getResource(Constants.MAIN_VIEW));
                root = loader.load();
                MainController main = loader.getController();
            }
            else{
                loader = new FXMLLoader();
                loader.setLocation(getClass().getResource(Constants.CLIENT_MAIN_VIEW));
                root = loader.load();
                ClientMainController main = loader.getController();
            }
            Stage stage = new Stage();
            stage.setTitle(Constants.TITLE);
            stage.getIcons().add(new Image("/resources/aml-logo.jpg"));
            stage.centerOnScreen();
            stage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            ((Stage)txtpassword.getScene().getWindow()).close();
            stage.show();
            FadeIn f=new FadeIn(root);
            f.setSpeed(2);
            f.play();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void goTologin(MouseEvent event) {
        isInLogin=true;
        registre_pane.setVisible(false);
        t.stop();
        tt.setByX(-500);
        tt.play();
        t.play();
        startupRegistre();
    }

    @FXML
    private void goToregistre(ActionEvent event) {
        isInLogin=false;
        rightAp.setVisible(false);
        t.stop();
        tt.setByX(500);
        tt.play();
        t.play();
    }

    private void exit_minus() {
        exit_circle.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleFont(true);
            }
        });
        exit_circle.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleFont(false);
            }            
        });
        exit_font.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleFont(true);
            }
            
        });
        exit_font.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleFont(false);
            }            
        });
        exit_circle.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                System.exit(0);
            }
        });
        exit_font.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                System.exit(0);
            }
        });
        
        
        
        minus_circle.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleLabel(true);
            }
        });
        minus_circle.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleLabel(false);
            }            
        });
        minus_label.setOnMouseEntered(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleLabel(true);
            }
            
        });
        minus_label.setOnMouseExited(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                setvisibleLabel(false);
            }            
        });
        minus_circle.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                ((Stage)minus_circle.getScene().getWindow()).setIconified(true);
            }
        });
        minus_label.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                ((Stage)minus_circle.getScene().getWindow()).setIconified(true);
            }
        });
    }

    @FXML
    private void step1Tostep2(ActionEvent event) {
        clt.setNom(FirstName.getText());
        clt.setPrenom(LastName.getText());
        clt.setEmail(Email.getText());
        clt.setMotDePasse(password_registre.getText());
        if(clt.getNom().isEmpty() && clt.getPrenom().isEmpty() 
            && clt.getEmail().isEmpty() && clt.getMotDePasse().isEmpty()){
            Animations.shake(FirstName);
            Animations.shake(LastName);
            Animations.shake(Email);
            Animations.shake(password_registre);
            NotificationsFactory.create("Error", "Missing Data");
            return;
        }
        if(clt.getNom().isEmpty()){
            FirstName.requestFocus();
            FirstName.validate();
            Animations.shake(FirstName);
            return;
        }
        if(clt.getPrenom().isEmpty()){
            LastName.requestFocus();
            LastName.validate();
            Animations.shake(LastName);
            return;
        }
        if(clt.getEmail().isEmpty()){
            Email.requestFocus();
            Email.validate();
            Animations.shake(Email);
            return;
        }
        if(clt.getMotDePasse().isEmpty()){
            password_registre.requestFocus();
            password_registre.validate();
            Animations.shake(password_registre);
            return;
        }
        if(!TextFieldValidation.ValidateEmail(clt.getEmail())){
            Email.requestFocus();
            Animations.shake(Email);
            NotificationsFactory.create("Error", "Email not valid\nPlease enter a valid Email");
            return;
        }
        if(!TextFieldValidation.ValidatePassword(clt.getMotDePasse())){
            password_registre.requestFocus();
            Animations.shake(password_registre);
            NotificationsFactory.create("Error", "Password so weak");
            return;
        }
        if(ClientsDB.EmailExists(clt.getEmail())){
            Email.requestFocus();
            Animations.shake(Email);
            NotificationsFactory.create("Error", "Email already exists!!");
            return;
        }
        FadeOutLeft fol=new FadeOutLeft(step1);
        fol.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                step1.setVisible(false);
                step2.setVisible(true);
                step3.setVisible(false);
            }
        });
        // don't forget this line is so accursed haha
        // the node will not come back and it will not show again ;)
        // so we need to reset it 
        fol.setResetOnFinished(true);
        fol.play();
        label_progress.setText("2 of 3");
        Animations.fadeIn(label_progress);
        Animations.progressAnimation(registre_progress, 0.66);
    }
    

    @FXML
    private void step2Tostep3(ActionEvent event) {
        String pNumber=PhoneNumber.getText();
        String pCode=PostalCode.getText();
        clt.setVille(City.getText());
        clt.setAdresse(Adress.getText());
        if(pNumber.isEmpty() && pCode.isEmpty() 
            && clt.getVille().isEmpty() && clt.getAdresse().isEmpty()){
            Animations.shake(PostalCode);
            Animations.shake(PhoneNumber);
            Animations.shake(City);
            Animations.shake(Adress);
            NotificationsFactory.create("Error", "Missing Data");
            return;
        }
        if(pNumber.isEmpty()){
            PhoneNumber.requestFocus();
            PhoneNumber.validate();
            Animations.shake(PhoneNumber);
            return;
        }
        if(pCode.isEmpty()){
            PostalCode.requestFocus();
            PostalCode.validate();
            Animations.shake(PostalCode);
            return;
        }
        if(clt.getVille().isEmpty()){
            City.requestFocus();
            City.validate();
            Animations.shake(City);
            return;
        }
        if(clt.getAdresse().isEmpty()){
            Adress.requestFocus();
            Adress.validate();
            Animations.shake(Adress);
            return;
        }
        clt.setCodePostal(Integer.parseInt(pCode));
        clt.setTel(Integer.parseInt(pNumber));
        FadeOutLeft fol=new FadeOutLeft(step2);
        fol.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                step2.setVisible(false);
                step3.setVisible(true);
                Timeline time=new Timeline(new KeyFrame(Duration.seconds(5), (e)->{
                    System.out.println(clt.toString());
                    System.out.println(ClientsDB.InsertClient(clt));
                    NotificationsFactory.create("Success", "You are registred successfully");
                }));
                time.setCycleCount(1);
                time.setOnFinished((e)->{
                    isInLogin=true;
                    registre_pane.setVisible(false);
                    t.stop();
                    tt.setByX(-500);
                    tt.play();
                    t.play();
                    startupRegistre();
                });
                time.play();
            }
        });
        // don't forget this line is so accursed haha
        // the node will not come back and it will not show again ;)
        // so we need to reset it 
        fol.setResetOnFinished(true);
        fol.play();
        // set arrow to disable to restrict the user to not back to login 
        arrow.setDisable(true);
        label_progress.setText("3 of 3");
        Animations.fadeIn(label_progress);
        Animations.progressAnimation(registre_progress, 1);
    }

    @FXML
    private void step2Tostep1(ActionEvent event) {
        FadeOutRight fr=new FadeOutRight(step2);
        fr.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                step2.setVisible(false);
                step1.setVisible(true);
                step3.setVisible(false);
            }
        });
        // don't forget this line is so accursed haha
        // the node will not come back and it will not show again ;)
        // so we need to reset it 
        fr.setResetOnFinished(true);
        fr.play();
        label_progress.setText("1 of 3");
        Animations.fadeIn(label_progress);
        Animations.progressAnimation(registre_progress, 0.33);
    }

    private void startupRegistre() {
            step1.setVisible(true);
            step2.setVisible(false);
            step3.setVisible(false);
            registre_progress.setProgress(0.33);
            label_progress.setText("1 of 3");
    }
}