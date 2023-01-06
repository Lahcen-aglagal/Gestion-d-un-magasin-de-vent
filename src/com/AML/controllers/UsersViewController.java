package com.AML.controllers;


import com.AML.Conn.connection;
import com.AML.DBUtil.UserSessionDB;
import com.AML.alerts.AlertsBuilder;
import com.AML.alerts.ConfirmAlert;
import com.AML.animations.Animations;
import com.AML.entities.Clients;
import com.AML.notifications.NotificationsFactory;
import com.AML.validations.RequieredFields;
import com.AML.validations.TextFieldValidation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class UsersViewController implements Initializable {

    private ObservableList<Clients> listClients;

    private ObservableList<Clients> filterClients=FXCollections.observableArrayList();
    @FXML
    private AnchorPane topAp;
    @FXML
    private AnchorPane crudPane;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private HBox searchPane;
    @FXML
    private TableView<Clients> tblUsers;
    @FXML
    private TableColumn<Clients, Integer> colId;
    @FXML
    private TableColumn<Clients, String> colFullName;
    @FXML
    private TableColumn<Clients, String> colAdresse;
    @FXML
    private TableColumn<Clients, Integer> colPostalCode;
    @FXML
    private TableColumn<Clients, String> colCity;
    @FXML
    private TableColumn<Clients, Integer> colPhoneNumber;
    @FXML
    private TableColumn<Clients, String> colEmail;
    @FXML
    private TableColumn<Clients, String> colPassword;
    @FXML
    private JFXTextField txtFirstName;
    @FXML
    private JFXTextField txtLastName;
    @FXML
    private JFXTextField txtPostalCode;
    @FXML
    private JFXTextField txtCity;
    @FXML
    private JFXTextField txtPhoneNumber;
    @FXML
    private JFXTextField txtEmail;
    @FXML
    private JFXTextField txtPassowrd;
    @FXML
    private JFXTextArea txtAdresse;
    @FXML
    private JFXTextField searchUsers;
    @FXML
    private StackPane stkUsers;
    @FXML
    private AnchorPane rootUsers;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAnimations();
        loadData();
        setValidations();
        selectText();
        tblUsers.getSelectionModel().selectedIndexProperty().addListener((o,oldItem,newItem)->{
            Clients clt = tblUsers.getSelectionModel().getSelectedItem();
            if(clt!=null){
                txtFirstName.setText(clt.getNom());
                txtLastName.setText(clt.getPrenom());
                txtPostalCode.setText(String.valueOf(clt.getCodePostal()));
                txtCity.setText(clt.getVille());
                txtPhoneNumber.setText(String.valueOf(clt.getTel()));
                txtEmail.setText(clt.getEmail());
                txtPassowrd.setText(clt.getMotDePasse());
                txtAdresse.setText(clt.getAdresse());
            }
        });
        searchUsers.textProperty().addListener((o,oldVal,newVal)->{
            if (newVal.isEmpty()) {
                tblUsers.setItems(listClients);
            } else {
                filterClients.clear();
                for (Clients c : listClients) {
                    if (c.getNom().toLowerCase().contains(newVal.toLowerCase())
                            || c.getPrenom().toLowerCase().contains(newVal.toLowerCase())
                            ) {
                        filterClients.add(c);
                    }
                }
                tblUsers.setItems(filterClients);
            }
        });
        Platform.runLater(()->{
            UserSessionDB.checkLogin(txtAdresse);
        });
    }
    
    private void setValidations() {
        RequieredFields.forJfxTextField(txtFirstName);
        RequieredFields.forJfxTextField(txtLastName);
        RequieredFields.forJfxTextField(txtPostalCode);
        RequieredFields.forJfxTextField(txtCity);
        RequieredFields.forJfxTextField(txtPhoneNumber);
        RequieredFields.forJfxTextField(txtEmail);
        RequieredFields.forJfxTextField(txtPassowrd);
        RequieredFields.forJFXTextArea(txtAdresse);
        // validate input 
        TextFieldValidation.onlyNumbers(txtPostalCode);
        TextFieldValidation.onlyNumbers(txtPhoneNumber);
    }


    private void selectText() {
        TextFieldValidation.selectText(txtFirstName);
        TextFieldValidation.selectText(txtLastName);
        TextFieldValidation.selectText(txtPostalCode);
        TextFieldValidation.selectText(txtCity);
        TextFieldValidation.selectText(txtPhoneNumber);
        TextFieldValidation.selectText(txtEmail);
        TextFieldValidation.selectText(txtPassowrd);
        TextFieldValidation.selectTextforJFXTextArea(txtAdresse);
    }
    
    public void setAnimations(){
        Animations.fadeIn(stkUsers);
        Animations.fadeIn(rootUsers);
        Animations.fadeIn(topAp);
        Animations.fadeIn(crudPane);
    }

    private void loadData() {
        loadTable();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("CodePostal"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("Ville"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("Tel"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("MotDePasse"));
    }

    private void loadTable()  {
        ArrayList<Clients> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM clients where isAdmin=0";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String nom = resultSet.getString("Nom");
                String prenom = resultSet.getString("Prenom");
                String adresse = resultSet.getString("Adresse");
                int CodePostal = resultSet.getInt("CodePostal");
                String Ville = resultSet.getString("Ville");
                int Tel = resultSet.getInt("Tel");
                String email = resultSet.getString("Email");
                String pass = resultSet.getString("MotPasse");
                Clients clt = new Clients(id, nom + " " + prenom, adresse, CodePostal, Ville, Tel, email, pass);
                clt.setNom(nom);
                clt.setPrenom(prenom);
                list.add(clt);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            AlertsBuilder.create("error", stkUsers, "Please check your connection to MYSQL");
        }
        listClients = FXCollections.observableArrayList(list);
        tblUsers.getItems().clear();
        tblUsers.setItems(listClients);
        tblUsers.setFixedCellSize(30);
    }

    @FXML
    private void AddUsers(ActionEvent event) {
        String nom     = txtFirstName.getText().trim();
        String prenom  = txtLastName.getText().trim();
        String adresse = txtAdresse.getText().trim();
        String Ville   = txtCity.getText().trim();
        String email   = txtEmail.getText().trim();
        String pass    = txtPassowrd.getText().trim();
        if(nom.isEmpty() || prenom.isEmpty() 
                || adresse.isEmpty()  || txtPostalCode.getText().trim().isEmpty()
                || Ville.isEmpty() || txtPhoneNumber.getText().trim().isEmpty()
                || email.isEmpty() || pass.isEmpty() 
            ){
            Animations.shake(txtFirstName);
            Animations.shake(txtLastName);
            Animations.shake(txtAdresse);
            Animations.shake(txtPostalCode);
            Animations.shake(txtCity);
            Animations.shake(txtPhoneNumber);
            Animations.shake(txtEmail);
            Animations.shake(txtPassowrd);
            return;
        }
        if(nom.isEmpty()){
            txtFirstName.requestFocus();
            Animations.shake(txtFirstName);
            return;
        }
        if(prenom.isEmpty()){
            txtLastName.requestFocus();
            Animations.shake(txtLastName);
            return;
        }
        if(adresse.isEmpty()){
            txtAdresse.requestFocus();
            Animations.shake(txtAdresse);
            return;
        }
        if(Ville.isEmpty()){
            txtCity.requestFocus();
            Animations.shake(txtCity);
            return;
        }
        if(email.isEmpty()){
            txtEmail.requestFocus();
            Animations.shake(txtEmail);
            return;
        }
        if(pass.isEmpty()){
            txtPassowrd.requestFocus();
            Animations.shake(txtPassowrd);
            return;
        }
        if(txtPostalCode.getText().trim().isEmpty()){
            txtPostalCode.requestFocus();
            Animations.shake(txtPostalCode);
            return;
        }
        if(txtPhoneNumber.getText().trim().isEmpty()){
            txtPhoneNumber.requestFocus();
            Animations.shake(txtPhoneNumber);
            return;
        }
        if(!TextFieldValidation.ValidateEmail(email)){
            txtEmail.requestFocus();
            Animations.shake(txtEmail);
            NotificationsFactory.create("Error", "Email not valid\nPlease enter a valid Email");
            return;
        }
        if(!TextFieldValidation.ValidatePassword(pass)){
            txtPassowrd.requestFocus();
            Animations.shake(txtPassowrd);
            NotificationsFactory.create("Error", "Invalid password!\n enter a strong password.");
            return;
        }
        if(EmailExists(email)){
            txtEmail.requestFocus();
            Animations.shake(txtEmail);
            NotificationsFactory.create("Error", "Email already exists!!");
            return;
        }
        int CodePostal = Integer.parseInt(txtPhoneNumber.getText().trim());
        int Tel        = Integer.parseInt(txtPhoneNumber.getText().trim());
        Clients c = new Clients(adresse, CodePostal, Ville, Tel, nom, prenom, email, pass);
        boolean res = InsertClient(c);
        if(res){
            loadTable();
            AlertsBuilder.create("success", stkUsers, "user added seccussfully");
        }
        else{
            NotificationsFactory.create("error", "please check your connection");
        }
    }
    
    public boolean EmailExists(String e){
        try{
            String sql="select * from clients where Email=?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, e);
            return preparedStatement.executeQuery().next();
        }
        catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    private boolean InsertClient(Clients c) {
        try {
            String sql = "INSERT INTO clients  "
                    + "(Id,Email,Nom,Prenom,Adresse,CodePostal,Ville,Tel,MotPasse)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1, c.getId());
            preparedStatement.setString(2, c.getEmail());
            preparedStatement.setString(3, c.getNom());
            preparedStatement.setString(4, c.getPrenom());
            preparedStatement.setString(5, c.getAdresse());
            preparedStatement.setInt(6, c.getCodePostal());
            preparedStatement.setString(7, c.getVille());
            preparedStatement.setInt(8, c.getTel());
            preparedStatement.setString(9, c.getMotDePasse());
            preparedStatement.execute();
            listClients.add(c);
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @FXML
    private void UpdateUsers(ActionEvent event) {
        Clients clt = tblUsers.getSelectionModel().getSelectedItem();
        if(clt==null){
            NotificationsFactory.create("Error", "please select an item from the table");
        }
        else{
            String nom     = txtFirstName.getText().trim();
            String prenom  = txtLastName.getText().trim();
            String adresse = txtAdresse.getText().trim();
            String Ville   = txtCity.getText().trim();
            String email   = txtEmail.getText().trim();
            String pass    = txtPassowrd.getText().trim();
            if(nom.isEmpty() || prenom.isEmpty() 
                || adresse.isEmpty()  || txtPostalCode.getText().trim().isEmpty()
                || Ville.isEmpty() || txtPhoneNumber.getText().trim().isEmpty()
                || email.isEmpty() || pass.isEmpty() 
            ){
                Animations.shake(txtFirstName);
                Animations.shake(txtLastName);
                Animations.shake(txtAdresse);
                Animations.shake(txtPostalCode);
                Animations.shake(txtCity);
                Animations.shake(txtPhoneNumber);
                Animations.shake(txtEmail);
                Animations.shake(txtPassowrd);
                return;
            }
            if(nom.isEmpty()){
                txtFirstName.requestFocus();
                Animations.shake(txtFirstName);
                return;
            }
            if(prenom.isEmpty()){
                txtLastName.requestFocus();
                Animations.shake(txtLastName);
                return;
            }
            if(adresse.isEmpty()){
                txtAdresse.requestFocus();
                Animations.shake(txtAdresse);
                return;
            }
            if(Ville.isEmpty()){
                txtCity.requestFocus();
                Animations.shake(txtCity);
                return;
            }
            if(email.isEmpty()){
                txtEmail.requestFocus();
                Animations.shake(txtEmail);
                return;
            }
            if(pass.isEmpty()){
                txtPassowrd.requestFocus();
                Animations.shake(txtPassowrd);
                return;
            }
            if(txtPostalCode.getText().trim().isEmpty()){
                txtPostalCode.requestFocus();
                Animations.shake(txtPostalCode);
                return;
            }
            if(txtPhoneNumber.getText().trim().isEmpty()){
                txtPhoneNumber.requestFocus();
                Animations.shake(txtPhoneNumber);
                return;
            }
            if(!TextFieldValidation.ValidateEmail(email)){
                txtEmail.requestFocus();
                Animations.shake(txtEmail);
                NotificationsFactory.create("Error", "Email not valid\nPlease enter a valid Email");
                return;
            }
            if(!TextFieldValidation.ValidatePassword(pass)){
                txtPassowrd.requestFocus();
                Animations.shake(txtPassowrd);
                NotificationsFactory.create("Error", "Invalid password!\n enter a strong password.");
                return;
            }
//            if(EmailExists(email)){
//                txtEmail.requestFocus();
//                Animations.shake(txtEmail);
//                NotificationsFactory.create("Error", "Email already exists!!");
//                return;
//            }
            int CodePostal = Integer.parseInt(txtPhoneNumber.getText().trim());
            int Tel        = Integer.parseInt(txtPhoneNumber.getText().trim());
            Clients c = new Clients(adresse, CodePostal, Ville, Tel, nom, prenom, email, pass);
            c.setId(clt.getId());
            boolean res = updateUser(c);
            if(res){
                loadTable();
                AlertsBuilder.create("success", stkUsers, "user updated seccussfully");
            }
            else{
                NotificationsFactory.create("error", "please check your connection");
            }
        }
    }
    
    public static boolean updateUser(Clients c) {
        try {
            String sql = "UPDATE clients SET "
                    + "Email=?,Nom=?,Prenom=?,Adresse=?,CodePostal=?,Ville=?,Tel=?,MotPasse=?"
                    + "WHERE Id=?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, c.getEmail());
            preparedStatement.setString(2, c.getNom());
            preparedStatement.setString(3, c.getPrenom());
            preparedStatement.setString(4, c.getAdresse());
            preparedStatement.setInt(5, c.getCodePostal());
            preparedStatement.setString(6, c.getVille());
            preparedStatement.setInt(7, c.getTel());
            preparedStatement.setString(8, c.getMotDePasse());
            preparedStatement.setInt(9, c.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @FXML
    private void DeleteUsers(ActionEvent event) {
        Clients c = tblUsers.getSelectionModel().getSelectedItem();
        if(c==null){
            NotificationsFactory.create("Error", "please select an item from the table");
        }
        else{
            ButtonType bt = ConfirmAlert.create("Confirm Delete", "Attention!!!", "if you delete this item other item can be deleted.\nAre you sure you want to delete it?");
            if(bt==ButtonType.OK){
                boolean res = DeleteUser(c);
                if(res){
                    loadTable();
                    clearNode();
                    AlertsBuilder.create("success", stkUsers, "user deleted seccussfully");
                }
                else{
                    NotificationsFactory.create("Error", "please check your connection");
                }
            }
        }
    }
    
    public boolean DeleteUser(Clients c){
        try {
            String sql = "DELETE FROM clients "
                    + "WHERE Id=?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1, c.getId());
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public void clearNode(){
        txtAdresse.clear();
        txtCity.clear();
        txtEmail.clear();
        txtFirstName.clear();
        txtLastName.clear();
        txtPassowrd.clear();
        txtPhoneNumber.clear();
        txtPostalCode.clear();
    }
    
}
