package com.AML.controllers;

import animatefx.animation.FadeInLeft;
import animatefx.animation.FadeInRight;
import animatefx.animation.FadeOutLeft;
import animatefx.animation.FadeOutRight;
import com.AML.Conn.connection;
import com.AML.DBUtil.UserSessionDB;
import com.AML.alerts.AlertsBuilder;
import com.AML.alerts.ConfirmAlert;
import com.AML.animations.Animations;
import com.AML.entities.AdresseLivraison;
import com.AML.entities.CartTable;
import com.AML.entities.ClickListener;
import com.AML.entities.Commandes;
import com.AML.entities.LignePanier;
import com.AML.entities.Produits;
import com.AML.notifications.NotificationsFactory;
import com.AML.resources.Constants;
import com.AML.validations.RequieredFields;
import com.AML.validations.TextFieldValidation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class CartViewController implements Initializable {

    private ObservableList<CartTable> listCart;
    @FXML
    private StackPane stkCart;
    @FXML
    private AnchorPane rootCart;
    @FXML
    private TableView<CartTable> tblCart;
    @FXML
    private TableColumn<CartTable, String> colTitle;
    @FXML
    private TableColumn<CartTable, Double> colPrice;
    @FXML
    private TableColumn<CartTable, Integer> colQuantity;
    @FXML
    private TableColumn<CartTable, Double> colTotal;
    @FXML
    private TableColumn<CartTable, FontAwesomeIconView> colView;
    @FXML
    private TableColumn<CartTable, JFXButton> colAction;
    private ClickListener cl;
    private StackPane stkClientRoot;
    private AnchorPane anchorClientRoot;
    private ClientMainController clientMainController;
    @FXML
    private Label Subtotal;
    @FXML
    private JFXButton checkout;
    @FXML
    private AnchorPane rootCheckout;
    @FXML
    private JFXTextField txtFirstName;
    @FXML
    private JFXTextField txtLastName;
    @FXML
    private JFXTextField txtPhoneNumber;
    @FXML
    private JFXTextField txtPostalCode;
    @FXML
    private JFXComboBox<String> comboxCountry;
    @FXML
    private JFXComboBox<String> comboxCity;
    @FXML
    private JFXTextArea txtAdress;
    @FXML
    private JFXButton Paybtn;
    @FXML
    private FontAwesomeIconView arrow;
    private List<LignePanier> listLignepanier = new ArrayList<>();
    
    public String[] cities = {
        "Zaouit Cheikh",
        "Fes",
        "Meknes",
        "Casablanca",
        "Beni Mellal",
        "Tanger",
        "Essaouira",
        "Asila",
        "Marrakech",
        "Mghila"
    };

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cl = new ClickListener() {
            @Override
            public void onClick(Produits p) {
                try{
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource(Constants.VIEWS_PACKAGE + "ItemDetail.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    ItemDetailController itemDetail = fxmlLoader.getController();
                    itemDetail.setProduit(p);
                    itemDetail.setClientMainController(clientMainController);
                    JFXDialog dialog = new JFXDialog();
                    dialog.setContent(anchorPane);
                    dialog.setDialogContainer(stkClientRoot);
                    dialog.setBackground(Background.EMPTY);
                    dialog.getStyleClass().add("jfx-dialog-overlay-pane");
                    dialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
                    BoxBlur blur_effect = new BoxBlur(3, 3, 3);
                    dialog.show();
                    itemDetail.setDialog(dialog);
                    dialog.setOnDialogOpened(e -> {
                        anchorClientRoot.setEffect(blur_effect);
                    });

                    dialog.setOnDialogClosed(e -> {
                        anchorClientRoot.setEffect(null);
                    });
                    itemDetail.getExit_circle().setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent arg0) {
                            if(dialog!=null)
                                dialog.close();
                        }
                    });
                    Circle exit_cercle =  itemDetail.getExit_circle();
                    FontAwesomeIconView exit_font= itemDetail.getExit_font();
                    exit_cercle.setOnMouseClicked((e)->{
                            if(dialog!=null)
                                dialog.close();
                    });
                    exit_font.setOnMouseClicked((e)->{
                            if(dialog!=null)
                                dialog.close();
                    });
                    exit_font.setOnMouseEntered((e)->exit_font.setVisible(true));
                    exit_font.setOnMouseExited((e)->exit_font.setVisible(false));
                    exit_cercle.setOnMouseEntered((e)->exit_font.setVisible(true));
                    exit_cercle.setOnMouseExited((e)->exit_font.setVisible(false));
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        };
        setAnimations();
        setValidations();
        selectText();
        loadData();
        new FadeOutRight(rootCheckout).play();
        comboxCountry.setItems(FXCollections.observableArrayList("Morroco"));
        comboxCountry.getSelectionModel().select("Morroco");
        comboxCity.setItems(FXCollections.observableArrayList(cities));
        Platform.runLater(()->{
            UserSessionDB.checkLogin(comboxCity);
        });
    }   
    
    private void setValidations() {
        RequieredFields.forJfxTextField(txtFirstName);
        RequieredFields.forJfxTextField(txtLastName);
        RequieredFields.forJfxTextField(txtPostalCode);
        RequieredFields.forJfxTextField(txtPhoneNumber);
        RequieredFields.forJFXTextArea(txtAdress);
        RequieredFields.forJFXComboBox(comboxCity);
        RequieredFields.forJFXComboBox(comboxCountry);
        // validate input 
        TextFieldValidation.onlyNumbers(txtPostalCode);
        TextFieldValidation.onlyNumbers(txtPhoneNumber);
    }


    private void selectText() {
        TextFieldValidation.selectText(txtFirstName);
        TextFieldValidation.selectText(txtLastName);
        TextFieldValidation.selectText(txtPostalCode);
        TextFieldValidation.selectText(txtPhoneNumber);
        TextFieldValidation.selectTextforJFXTextArea(txtAdress);
    }
    
    public double getSubtotal(){
        double s = 0;
        for(CartTable c : listCart){
            s += c.getTotal();
        }
        return s;
    }
    
    public void setAnimations(){
        Animations.fadeIn(stkCart);
        Animations.fadeIn(rootCart);
    }
    
    private void loadData() {
        loadTable();
        colTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));
        colView.setCellValueFactory(new PropertyValueFactory<>("view"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("action"));
    }
    
    public void loadTable() {
        ArrayList<CartTable> list = new ArrayList<>();
        listLignepanier.clear();
        try {
            String sql = "SELECT pa.codeproduit,Designation,Prix,qtecde,qtecde*Prix as 'Total'  "
                    + "FROM panier as pa , produits as p "
                    + "where pa.codeproduit=p.Codeproduits;";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String Designation = resultSet.getString("Designation");
                double Prix = resultSet.getDouble("Prix");
                int Stock = resultSet.getInt("qtecde");
                double TotalPrice = resultSet.getDouble("Total");
                int codeproduit = resultSet.getInt("codeproduit");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(Constants.VIEWS_PACKAGE + "quantity.fxml"));
                HBox hbox = fxmlLoader.load();
                QuantityController quantityController = fxmlLoader.getController();
                Produits p = getProduitById(codeproduit);
                LignePanier lp = new LignePanier(p, Stock);
                listLignepanier.add(lp);
                quantityController.setProduit(lp);
                quantityController.setQuantity();
                quantityController.setStkCart(stkCart);
                quantityController.setCartController(this);
                JFXButton btnView = new JFXButton("View");
                JFXButton btnDelete = new JFXButton("Delete");
                btnView.setOnAction(e -> cl.onClick(p));
                btnDelete.setOnAction(e ->{
                    ButtonType bt = ConfirmAlert.create("Confirm Delete", "Attention!!!", "Are you sure you want to delete this item?");
                        if(bt==ButtonType.OK){
                            boolean res = DeleteLignePanier((JFXButton)e.getSource());
                            if(res){
                                loadTable();
                                AlertsBuilder.create("success", stkCart, "Product deleted seccussfully from the cart.");
                            }
                            else{
                                NotificationsFactory.create("Error", "please check your connection");
                            }
                        }
                });
                btnDelete.getStyleClass().add("btn-delete-commande");
                btnView.getStyleClass().add("btn-delete-commande");
                CartTable cart = new CartTable(Designation, Prix, hbox, TotalPrice, btnView, btnDelete);
                cart.setCodeproduit(codeproduit);
                list.add(cart);
            }
        } catch (SQLException | IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            AlertsBuilder.create("error", stkCart, "faild while loading the table\ntry again.");
        }
        listCart = FXCollections.observableArrayList(list);
        tblCart.getItems().clear();
        tblCart.setItems(listCart);
        tblCart.setFixedCellSize(30);
        if(listCart.size()==0){
            Subtotal.setText("0 MAD");
            checkout.setVisible(false);
        }
        else{
            Subtotal.setText(getSubtotal() + " MAD");
            checkout.setVisible(true);
        }
    }
    
    private boolean DeleteLignePanier(JFXButton btn) {
        boolean res=false;
        for(CartTable c : listCart){
            if(c.getAction().equals(btn)){
                res = DeleteLignePanier(c);
                break;
            }
        }
        return res;
    }
    
    private boolean DeleteLignePanier(CartTable c) {
        try {
            String sql = "DELETE FROM panier WHERE codeproduit = ?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1, c.getCodeproduit());
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public Image GetImageById(int id){
        Image img=new Image("file:empty-image.jpg");
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

    private Produits getProduitById(int codeProduit) {
        Produits p=null;
        try {
            String sql = "SELECT * FROM produits WHERE Codeproduits=?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1,codeProduit);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int Codeproduits = resultSet.getInt("Codeproduits");
                String Designation = resultSet.getString("Designation");
                String Description = resultSet.getString("Description");
                double Prix = resultSet.getDouble("Prix");
                int Stock = resultSet.getInt("Stock");
                int RefCat = resultSet.getInt("RefCat");
                p= new Produits(Codeproduits, Designation, Description, Prix, Stock, RefCat);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return p;
    }

    void setstkroot(StackPane stkClientRoot) {
        this.stkClientRoot = stkClientRoot;
    }

    void setanchor(AnchorPane anchorClientRoot) {
        this.anchorClientRoot = anchorClientRoot;
    }

    void setClientMainController(ClientMainController clientMainController) {
        this.clientMainController = clientMainController;
    }

    @FXML
    private void goToCheckout(ActionEvent event) {
        FadeOutLeft fol=new FadeOutLeft(rootCart);
        fol.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                rootCart.setVisible(false);
                rootCheckout.setVisible(true);
                FadeInRight fir = new FadeInRight(rootCheckout);
                fir.setResetOnFinished(true);
                fir.play();
            }
        });
        // don't forget this line is so accursed haha
        // the node will not come back and it will not show again ;)
        // so we need to reset it 
//        fol.setResetOnFinished(true);
        fol.play();
    }

    @FXML
    private void Pay(ActionEvent event) {
        String nom     = txtFirstName.getText().trim();
        String prenom  = txtLastName.getText().trim();
        String adresse = txtAdress.getText().trim();
        String Ville   = comboxCity.getSelectionModel().getSelectedItem();
        String Country = comboxCountry.getSelectionModel().getSelectedItem();
        if(nom.isEmpty() || prenom.isEmpty() 
                || adresse.isEmpty()  || txtPostalCode.getText().trim().isEmpty()
                || Ville==null || txtPhoneNumber.getText().trim().isEmpty()
            ){
            Animations.shake(txtFirstName);
            Animations.shake(txtLastName);
            Animations.shake(txtAdress);
            Animations.shake(txtPostalCode);
            Animations.shake(comboxCity);
            Animations.shake(txtPhoneNumber);
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
            txtAdress.requestFocus();
            Animations.shake(txtAdress);
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
        int CodePostal = Integer.parseInt(txtPhoneNumber.getText().trim());
        int Tel        = Integer.parseInt(txtPhoneNumber.getText().trim());
        int CodeClient = GetCodeClient();
        AdresseLivraison al = new AdresseLivraison(adresse, CodePostal, Tel, Ville, Country, CodeClient);
        Commandes cmd = new Commandes(new Date(System.currentTimeMillis()), CodeClient);
        if(InsertAdresseLivraison(al) && 
                InsertCommande(cmd, al.getAdresseLivraisonId()) && InsertLigneCommande(cmd)
            ){
            this.clientMainController.showWindows("ClientCommandesView");
            AlertsBuilder.create("success", this.clientMainController.getStkClientRoot(), "Your order is seccussfully passed.\nsee your orders now.");
        }
        else{
            NotificationsFactory.create("error", "please check your connection");
        }
    }
    
    public boolean InsertAdresseLivraison(AdresseLivraison al){
        try {
            String sql = "INSERT INTO AdresseLivraison  "
                    + "(adresse,CodePostal,tel,ville,pays,CodeClient)"
                    + "VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, al.getAdresse());
            preparedStatement.setInt(2, al.getCodePostal());
            preparedStatement.setInt(3, al.getTel());
            preparedStatement.setString(4, al.getVille());
            preparedStatement.setString(5, al.getPays());
            preparedStatement.setInt(6, al.getCodeClient());
            preparedStatement.executeUpdate();
            ResultSet res= preparedStatement.getGeneratedKeys();
            res.next();
            al.setAdresseLivraisonId(res.getInt(1));
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public boolean InsertCommande(Commandes cmd,int adi){
        try {
            String sql = "INSERT INTO commandes  "
                    + "(CodeClient,DateCommande,AdresseLivraisonId)"
                    + "VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, cmd.getCodeClient());
            preparedStatement.setDate(2, cmd.getDateCommande());
            preparedStatement.setInt(3, adi);
            preparedStatement.executeUpdate();
            ResultSet res= preparedStatement.getGeneratedKeys();
            res.next();
            cmd.setNumCommande(res.getInt(1));
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean InsertLigneCommande(Commandes cmd){
        for(LignePanier lp : listLignepanier){
            try {
            String sql = "INSERT INTO lignecommandes  "
                    + "VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1, cmd.getNumCommande());
            preparedStatement.setInt(2, lp.getArt().getCodeArticle());
            preparedStatement.setInt(3, lp.getQte());
            preparedStatement.execute();
            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }
    @FXML
    private void goToCart(MouseEvent event) {
        FadeOutRight fol=new FadeOutRight(rootCheckout);
        fol.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent a) {
                rootCheckout.setVisible(false);
                rootCart.setVisible(true);
                FadeInLeft fil = new FadeInLeft(rootCart);
                fil.setResetOnFinished(true);
                fil.play();
            }
        });
        // don't forget this line is so accursed haha
        // the node will not come back and it will not show again ;)
        // so we need to reset it 
//        fol.setResetOnFinished(true);
        fol.play();
    }
    
    public int GetCodeClient(){
        int code = -1;
        try {
            String sql = "SELECT CodeClient FROM UserSession";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            ResultSet res = preparedStatement.executeQuery();
            if(res.next()){
                code = res.getInt("CodeClient");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return code;
    }
    
}
