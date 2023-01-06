    package com.AML.controllers;


import com.AML.Conn.connection;
import com.AML.DBUtil.UserSessionDB;
import com.AML.alerts.AlertsBuilder;
import com.AML.alerts.ConfirmAlert;
import com.AML.animations.Animations;
import com.AML.entities.Produits;
import com.AML.notifications.NotificationsFactory;
import com.AML.validations.RequieredFields;
import com.AML.validations.TextFieldValidation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class ProductsViewController implements Initializable {

    private final ColorAdjust colorAdjust = new ColorAdjust();
    private ObservableList<Produits> listProduits;
    private ObservableList<Produits> filterProduits=FXCollections.observableArrayList();
    @FXML
    private StackPane stkProducts;
    @FXML
    private AnchorPane rootProducts;
    @FXML
    private TableView<Produits> tblProducts;
    @FXML
    private TableColumn<Produits, Integer> colCodeproduits;
    @FXML
    private TableColumn<Produits, String> colDesignation;
    @FXML
    private TableColumn<Produits, String> colDescription;
    @FXML
    private TableColumn<Produits, Double> colPrix;
    @FXML
    private TableColumn<Produits, Integer> colStock;
    @FXML
    private TableColumn<Produits, Integer> colRefCat;
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
    private JFXTextField txtProductName;
    @FXML
    private JFXTextField txtPrice;
    @FXML
    private JFXTextField txtStock;
    @FXML
    private JFXComboBox<String> comboxRefCat;
    @FXML
    private JFXTextArea txtDescription;
    @FXML
    private ImageView ProductImg;
    @FXML
    private HBox searchPane;
    @FXML
    private JFXTextField searchProducts;
    @FXML
    private JFXButton btnChooseImg;
    @FXML
    private Label imgName;
    private File f=new File("/home/ay0ub/Desktop/the fucking project/AML_BOUTIQUE/src/resources/empty-image.jpg");
    @FXML
    private HBox imagebox;
    private Image imgEmpty = new Image("file:../../../resources/empty-image.jpg");
    private final long LIMIT=1000000;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAnimations();
        loadData();
        setValidations();
        selectText();
        setComobox();
        tblProducts.getSelectionModel().selectedIndexProperty().addListener((o,oldItem,newItem)->{
            Produits p = tblProducts.getSelectionModel().getSelectedItem();
            if(p!=null){
                txtProductName.setText(p.getDesignation());
                txtPrice.setText(String.valueOf(p.getPrix()));
                txtStock.setText(String.valueOf(p.getStock()));
                txtDescription.setText(p.getDescription());
                comboxRefCat.getSelectionModel().select(p.getRefCat() % comboxRefCat.getItems().size());
                ProductImg.setImage(GetImageById(p.getCodeArticle()));
            }
        });
        searchProducts.textProperty().addListener((o,oldVal,newVal)->{
            if (newVal.isEmpty()) {
                tblProducts.setItems(listProduits);
            } else {
                filterProduits.clear();
                for (Produits p : listProduits) {
                    if (p.getDesignation().toLowerCase().contains(newVal.toLowerCase())
                            || p.getDescription().toLowerCase().contains(newVal.toLowerCase())
                            ) {
                        filterProduits.add(p);
                    }
                }
                tblProducts.setItems(filterProduits);
            }
        });
        ProductImg.hoverProperty().addListener((o, oldVal, newVal) -> {
            if (newVal) {
                colorAdjust.setBrightness(0.25);
                ProductImg.setEffect(colorAdjust);
            } else {
                ProductImg.setEffect(null);
            }
        });
        Platform.runLater(()->{
            UserSessionDB.checkLogin(txtDescription);
        });
    }    
    
    private void setComobox(){
        comboxRefCat.setItems(getCategories());
    }
    private ObservableList<String> getCategories(){
        List<String> cats = new ArrayList<>();
        try {
            String sql = "SELECT * FROM categories order by RefCat";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String catTitle = resultSet.getString("CatTitle");
                cats.add(catTitle);
            }
            return FXCollections.observableArrayList(cats);
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            AlertsBuilder.create("error", stkProducts, "");
            return null;
        }
    }
    private void setValidations() {
        RequieredFields.forJfxTextField(txtProductName);
        RequieredFields.forJfxTextField(txtPrice);
        RequieredFields.forJfxTextField(txtStock);
        RequieredFields.forJFXTextArea(txtDescription);
        // validate input 
        TextFieldValidation.onlyNumbers(txtStock);
        TextFieldValidation.onlyDoubleNumbers(txtPrice);
    }

    private void selectText() {
        TextFieldValidation.selectText(txtProductName);
        TextFieldValidation.selectText(txtPrice);
        TextFieldValidation.selectText(txtStock);
        TextFieldValidation.selectTextforJFXTextArea(txtDescription);
    }
    
    public void setAnimations(){
        Animations.fadeIn(stkProducts);
        Animations.fadeIn(rootProducts);
        Animations.fadeIn(topAp);
        Animations.fadeIn(crudPane);
    }

    private void loadData() {
        loadTable();
        colCodeproduits.setCellValueFactory(new PropertyValueFactory<>("CodeArticle"));
        colDesignation.setCellValueFactory(new PropertyValueFactory<>("Designation"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("Prix"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("Stock"));
        colRefCat.setCellValueFactory(new PropertyValueFactory<>("RefCat"));
    }

    private void loadTable()  {
        ArrayList<Produits> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM produits";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int Codeproduits = resultSet.getInt("Codeproduits");
                String Designation = resultSet.getString("Designation");
                String Description = resultSet.getString("Description");
                double Prix = resultSet.getDouble("Prix");
                int Stock = resultSet.getInt("Stock");
                int RefCat = resultSet.getInt("RefCat");
                Produits p = new Produits(Codeproduits, Designation, Description, Prix, Stock, RefCat);
                list.add(p);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            AlertsBuilder.create("error", stkProducts, "Please check your connection to MYSQL");
        }
        listProduits = FXCollections.observableArrayList(list);
        tblProducts.getItems().clear();
        tblProducts.setItems(listProduits);
        tblProducts.setFixedCellSize(30);
    }

    @FXML
    private void AddProducts(ActionEvent event) {
        String Designation = txtProductName.getText().trim();
        String Description = txtDescription.getText().trim();
        int RefCat         = getRefCatByTitle(comboxRefCat.getSelectionModel().getSelectedItem());
        if(Designation.isEmpty() || Description.isEmpty()
                || txtPrice.getText().trim().isEmpty() || txtStock.getText().trim().isEmpty()
                ||RefCat==-1 
            ){
            Animations.shake(txtProductName);
            Animations.shake(txtPrice);
            Animations.shake(txtStock);
            Animations.shake(txtDescription);
            Animations.shake(comboxRefCat);
            return;
        }
        if(Designation.isEmpty()){
            txtProductName.requestFocus();
            Animations.shake(txtProductName);
            return;
        }
        if(txtPrice.getText().trim().isEmpty()){
            txtPrice.requestFocus();
            Animations.shake(txtPrice);
            return;
        }
        if(txtStock.getText().trim().isEmpty()){
            txtStock.requestFocus();
            Animations.shake(txtStock);
            return;
        }
        if(RefCat==-1){
            comboxRefCat.requestFocus();
            Animations.shake(comboxRefCat);
            return;
        }
        double Prix = Double.parseDouble(txtPrice.getText().trim());
        int Stock   = Integer.parseInt(txtStock.getText().trim());
        Produits p = new Produits(111, Designation, Description, Prix, Stock, RefCat);
        try {
            p.setPhoto (new FileInputStream(f));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            NotificationsFactory.create("Error", "Coudln't load the image\nplease try again.");
        }
        boolean res = InsertProduit(p);
        if(res){
            loadTable();
            AlertsBuilder.create("success", stkProducts, "product added seccussfully");
        }
        else{
            NotificationsFactory.create("error", "please check your connection");
        }
    }
    public boolean InsertProduit(Produits p){
        try {
            String sql = "INSERT INTO produits  "
                    + "(Designation,Description,Prix,Stock,Photo,RefCat)"
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, p.getDesignation());
            preparedStatement.setString(2, p.getDescription());
            preparedStatement.setDouble(3, p.getPrix());
            preparedStatement.setInt(4, p.getStock());
            preparedStatement.setBinaryStream(5, p.getPhoto());
            preparedStatement.setInt(6, p.getRefCat());
            preparedStatement.execute();
            listProduits.add(p);
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public Image GetImageById(int id){
        Image img=new Image("file:../../../resources/empty-image.jpg");
        try {
            String sql = "SELECT Photo FROM produits WHERE Codeproduits=?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet res = preparedStatement.executeQuery();
            if(res.next()){
                if(res.getBinaryStream("Photo")!=null)
                    img=SwingFXUtils.toFXImage(ImageIO.read(res.getBinaryStream("Photo")), null);
            }
        } catch (SQLException | IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return img;
    }
    
    public boolean CompareImage(Image img1,Image img2){
        if(img1.getWidth()!=img2.getWidth()) return false;
        if(img1.getHeight()!=img2.getHeight()) return false;
        for(int i=0;i<img1.getWidth();i++){
            for(int j=0;j<img1.getHeight();j++){
                if(imgEmpty.getPixelReader().getArgb(i, j)!=img2.getPixelReader().getArgb(i, j)){
                    return false;
                }
            }
        }
        return true;
    }
    
    public int getRefCatByTitle(String title){
        int refcat=-1;
        try{
            String sql="select RefCat from categories where CatTitle=?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, title);
            ResultSet res = preparedStatement.executeQuery();
            if(res.next()){
                refcat = res.getInt("RefCat");
            }
        }
        catch(SQLException | ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return refcat;
    }

    @FXML
    private void UpdateProducts(ActionEvent event) {
        Produits p = tblProducts.getSelectionModel().getSelectedItem();
        if(p==null){
            NotificationsFactory.create("Error", "please select an item from the table");
        }
        else{
            String Designation = txtProductName.getText().trim();
            String Description = txtDescription.getText().trim();
            int RefCat         = getRefCatByTitle(comboxRefCat.getSelectionModel().getSelectedItem());
            if(Designation.isEmpty() || Description.isEmpty()
                    || txtPrice.getText().trim().isEmpty() || txtStock.getText().trim().isEmpty()
                    ||RefCat==-1 
                ){
                Animations.shake(txtProductName);
                Animations.shake(txtPrice);
                Animations.shake(txtStock);
                Animations.shake(txtDescription);
                Animations.shake(comboxRefCat);
                return;
            }
            if(Designation.isEmpty()){
                txtProductName.requestFocus();
                Animations.shake(txtProductName);
                return;
            }
            if(txtPrice.getText().trim().isEmpty()){
                txtPrice.requestFocus();
                Animations.shake(txtPrice);
                return;
            }
            if(txtStock.getText().trim().isEmpty()){
                txtStock.requestFocus();
                Animations.shake(txtStock);
                return;
            }
            if(RefCat==-1){
                comboxRefCat.requestFocus();
                Animations.shake(comboxRefCat);
                return;
            }
            double Prix = Double.parseDouble(txtPrice.getText().trim());
            int Stock   = Integer.parseInt(txtStock.getText().trim());
            Produits pro = new Produits(p.getCodeArticle(), Designation, Description, Prix, Stock, RefCat);
            try {
                pro.setPhoto(new FileInputStream(f));
            } catch (FileNotFoundException ex) {
                NotificationsFactory.create("Error", "Coudln't load the image\nplease try again.");
            }
            boolean res = updateProduit(pro);
            if(res){
                loadTable();
                AlertsBuilder.create("success", stkProducts, "product updated seccussfully");
            }
            else{
                NotificationsFactory.create("error", "please check your connection");
            }
        }
    }
    public static boolean updateProduit(Produits p) {
        try {
            String sql = "UPDATE produits SET "
                    + "Designation=?,Description=?,Prix=?,Stock=?,Photo=?,RefCat=? "
                    + "WHERE Codeproduits=?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, p.getDesignation());
            preparedStatement.setString(2, p.getDescription());
            preparedStatement.setDouble(3, p.getPrix());
            preparedStatement.setInt(4, p.getStock());
            preparedStatement.setBinaryStream(5, p.getPhoto());
            preparedStatement.setInt(6, p.getRefCat());
            preparedStatement.setInt(7, p.getCodeArticle());
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @FXML
    private void DeleteProducts(ActionEvent event) {
        Produits p = tblProducts.getSelectionModel().getSelectedItem();
        if(p==null){
            NotificationsFactory.create("Error", "please select an item from the table");
        }
        else{
            ButtonType bt = ConfirmAlert.create("Confirm Delete", "Attention!!!", "if you delete this item other item can be deleted.\nAre you sure you want to delete it?");
            if(bt==ButtonType.OK){
                boolean res = DeleteProduct(p);
                if(res){
                    loadTable();
                    clearNode();
                    AlertsBuilder.create("success", stkProducts, "product updated seccussfully");
                }
                else{
                    NotificationsFactory.create("error", "please check your connection");
                }
            }
        }
    }
    
    public boolean DeleteProduct(Produits p){
        try {
            String sql = "DELETE FROM produits "
                    + "WHERE Codeproduits=?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1, p.getCodeArticle());
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @FXML
    private void ChooseImage(ActionEvent event) {
        FileChooser file = new FileChooser();
        FileChooser.ExtensionFilter ext1 = new FileChooser.ExtensionFilter("JPG files", "*.jpg");
        FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("PNG files", "*.png");
        file.getExtensionFilters().addAll(ext1,ext2);
        f = file.showOpenDialog(((Stage)stkProducts.getScene().getWindow()));
        if(f!=null && f.length()<LIMIT){
            imgName.setText(f.getName());
            try {
                ProductImg.setImage(new Image(f.toURI().toURL().toString()));
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                NotificationsFactory.create("Error", "Faild while loading the image\nplease try again.");
            }
            return;
        }
        if(f.length()>LIMIT){
            NotificationsFactory.create("Error", "Image too large,must be less then 1MB\nplease choose another one.");
        }
    }
    
    public void clearNode(){
        txtDescription.clear();
        txtPrice.clear();
        txtProductName.clear();
        txtStock.clear();
        comboxRefCat.getSelectionModel().clearSelection();
        try {
            ProductImg.setImage(new Image(f.toURI().toURL().toString()));
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }
    
}
