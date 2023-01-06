package com.AML.controllers;


import com.AML.Conn.connection;
import com.AML.DBUtil.UserSessionDB;
import com.AML.alerts.AlertsBuilder;
import com.AML.alerts.ConfirmAlert;
import com.AML.animations.Animations;
import com.AML.entities.Categories;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class CategoriesViewController implements Initializable {
    

    private ObservableList<Categories> listCategories;

    private ObservableList<Categories> filterCategories;
    @FXML
    private AnchorPane rootCategories;
    @FXML
    private TableView<Categories> tblCategories;
    @FXML
    private TableColumn<Categories, Integer> colRefCat;
    @FXML
    private TableColumn<Categories, String> colTitle;
    @FXML
    private TableColumn<Categories, String> colDescription;
    @FXML
    private AnchorPane topAp;
    @FXML
    private JFXTextField searchCategorie;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXTextField txtTitle;
    @FXML
    private JFXTextArea txtDescription;
    @FXML
    private StackPane stkCategories;
    @FXML
    private HBox searchPane;
    @FXML
    private AnchorPane crudPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAnimations();
        loadData();
        setValidations();
        selectText();
        tblCategories.getSelectionModel().selectedIndexProperty().addListener((o,oldItem,newItem)->{
            Categories c = tblCategories.getSelectionModel().getSelectedItem();
            if(c!=null){
                txtTitle.setText(c.getCatTitle());
                txtDescription.setText(c.getCatDescription());
            }
        });
        searchCategorie.textProperty().addListener((o,oldVal,newVal)->{
            if (newVal.isEmpty()) {
                tblCategories.setItems(listCategories);
            } else {
                filterCategories=FXCollections.observableArrayList();
                for (Categories c : listCategories) {
                    if (c.getCatTitle().toLowerCase().contains(newVal.toLowerCase())
                            || c.getCatDescription().toLowerCase().contains(newVal.toLowerCase())
                            ) {
                        filterCategories.add(c);
                    }
                }
                tblCategories.setItems(filterCategories);
            }
        });
        Platform.runLater(()->{
            UserSessionDB.checkLogin(txtDescription);
        });
    }
    
    private void setValidations() {
        RequieredFields.forJfxTextField(txtTitle);
        RequieredFields.forJFXTextArea(txtDescription);
    }


    private void selectText() {
        TextFieldValidation.selectText(txtTitle);
        TextFieldValidation.selectTextforJFXTextArea(txtDescription);
    }
    
    public void setAnimations(){
        Animations.fadeIn(stkCategories);
        Animations.fadeIn(rootCategories);
        Animations.fadeIn(topAp);
        Animations.fadeIn(crudPane);
        
    }

    private void loadData() {
        loadTable();
        colRefCat.setCellValueFactory(new PropertyValueFactory<>("RefCat"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("CatTitle"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("CatDescription"));
    }

    private void loadTable() {
        ArrayList<Categories> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM categories";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int refcat = resultSet.getInt("RefCat");
                String catTitle = resultSet.getString("CatTitle");
                String CatDesc = resultSet.getString("CatDescription");
                list.add(new Categories(refcat, catTitle, CatDesc));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            AlertsBuilder.create("error", stkCategories, "");
        }
        listCategories = FXCollections.observableArrayList(list);
        tblCategories.getItems().clear();
        tblCategories.setItems(listCategories);
        tblCategories.setFixedCellSize(30);
    }

    @FXML
    private void AddCategorie(ActionEvent event) {
        String catTitle = txtTitle.getText().trim();
        String CatDesc = txtDescription.getText().trim();
        if(catTitle.isEmpty() || CatDesc.isEmpty()){
            Animations.shake(txtTitle);
            Animations.shake(txtDescription);
            return;
        }
        if(catTitle.isEmpty()){
            txtTitle.requestFocus();
            Animations.shake(txtTitle);
            return;
        }
        if(CatDesc.isEmpty()){
            txtDescription.requestFocus();
            Animations.shake(txtDescription);
            return;
        }
        Categories c = new Categories(catTitle, CatDesc);
        boolean res = InsertCategorie(c);
        if(res){
            loadTable();
            AlertsBuilder.create("success", stkCategories, "categorie added seccussfully");
        }
        else{
            NotificationsFactory.create("error", "please check your connection");
        }
    }

    private boolean InsertCategorie(Categories c)  {
        try {
            String sql = "INSERT INTO categories (CatTitle,CatDescription) "
                    + "VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, c.getCatTitle());
            preparedStatement.setString(2, c.getCatDescription());
            preparedStatement.execute();
            listCategories.add(c);
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    @FXML
    private void UpdateCategorie(ActionEvent event) {
        Categories c = tblCategories.getSelectionModel().getSelectedItem();
        if(c==null){
            NotificationsFactory.create("Error", "please select an item from the table");
        }
        else{
            String catTitle = txtTitle.getText().trim();
            String CatDesc = txtDescription.getText().trim();
            if(catTitle.isEmpty() || CatDesc.isEmpty()){
                Animations.shake(txtTitle);
                Animations.shake(txtDescription);
                return;
            }
            if(catTitle.isEmpty()){
                txtTitle.requestFocus();
                Animations.shake(txtTitle);
                return;
            }
            if(CatDesc.isEmpty()){
                txtDescription.requestFocus();
                Animations.shake(txtDescription);
                return;
            }
            boolean res = updateCategorie(c.getRefCat(),catTitle,CatDesc);
            if(res){
                loadTable();
                AlertsBuilder.create("success", stkCategories, "categorie updated seccussfully");
            }
            else{
                NotificationsFactory.create("Error", "please check your connection");
            }
        }
    }

    public static boolean updateCategorie(int id,String catTitle,String catDesc) {
        try {
            String sql = "UPDATE categories SET CatTitle = ? , CatDescription = ?"
                    + "WHERE  RefCat = ?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, catTitle);
            preparedStatement.setString(2, catDesc);
            preparedStatement.setInt(3, id);
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    @FXML
    private void DeleteCategorie(ActionEvent event) {
        Categories c = tblCategories.getSelectionModel().getSelectedItem();
        if(c==null){
            NotificationsFactory.create("Error", "please select an item from the table");
        }
        else{
            ButtonType bt = ConfirmAlert.create("Confirm Delete", "Attention!!!", "if you delete this item other item can be deleted.\nAre you sure you want to delete it?");
            if(bt==ButtonType.OK){
                boolean res = DeleteCategotie(c);
                if(res){
                    loadTable();
                    clearNode();
                    AlertsBuilder.create("success", stkCategories, "categorie deleted seccussfully");
                }
                else{
                    NotificationsFactory.create("Error", "please check your connection");
                }
            }
        }
    }
    
    public boolean DeleteCategotie(Categories c){
        try {
            String sql = "DELETE FROM categories WHERE RefCat = ?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1, c.getRefCat());
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public void clearNode(){
        txtDescription.clear();
        txtTitle.clear();
    }
}
