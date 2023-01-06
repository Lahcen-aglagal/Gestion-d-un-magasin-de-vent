package com.AML.controllers;


import com.AML.Conn.connection;
import com.AML.DBUtil.UserSessionDB;
import com.AML.alerts.AlertsBuilder;
import com.AML.animations.Animations;
import com.AML.entities.ClickListener;
import com.AML.entities.ProductItem;
import com.AML.entities.Produits;
import com.AML.resources.Constants;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author ay0ub
 */
public class ClientHomeViewController implements Initializable {

    List<ProductItem> listProduits = new ArrayList<>();
    
    List<ProductItem> filterProduits = new ArrayList<>();
    @FXML
    private StackPane stkProducts;
    @FXML
    private HBox searchProducts;
    @FXML
    private JFXTextField Searchproduit;
    @FXML
    private Label marketLabel;
    @FXML
    private ScrollPane scrollbar;
    @FXML
    private GridPane grid;
    
    private ClickListener cl;
    @FXML
    private Pane Noproduct;
    @FXML
    private AnchorPane rootContainer;
    private StackPane stkClientRoot;
    private AnchorPane anchorClientRoot;
    private ClientMainController clientMainController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        System.out.println(this.clientMainController);
        setAnimations();
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
        listProduits.addAll(getData());
        LoadData(listProduits);
        Searchproduit.textProperty().addListener((o,oldVal,newVal)->{
            if (newVal.isEmpty()) {
                Noproduct.setVisible(false);
                scrollbar.setVisible(true);
                Animations.fadeIn(scrollbar);
                LoadData(listProduits);
            } else {
                filterProduits.clear();
                for (ProductItem pItem : listProduits) {
                    if (pItem.getProduit().getDesignation().toLowerCase().contains(newVal.toLowerCase())
                            || String.valueOf(pItem.getProduit().getPrix()).toLowerCase().contains(newVal.toLowerCase())
                            ) {
                        filterProduits.add(pItem);
                    }
                }
                if(filterProduits.isEmpty()){
                    if(!Noproduct.isVisible()){
                        scrollbar.setVisible(false);
                        Noproduct.setVisible(true);
                        Animations.fadeIn(Noproduct);
                    }
                }
                else{
                    Noproduct.setVisible(false);
                    scrollbar.setVisible(true);
                    Animations.fadeIn(scrollbar);
                    LoadData(filterProduits);
                }
            }
        });
        Platform.runLater(()->{
            UserSessionDB.checkLogin(marketLabel);
        });
    }    
    
    public void setAnimations(){
        Animations.zoomIn(searchProducts);
        Animations.zoomIn(scrollbar);
        Animations.zoomIn(marketLabel);
    }
    
    public void LoadData(List<ProductItem> list){
        grid.getChildren().clear();
        if (list.size() > 0) {
            cl = new ClickListener() {
                @Override
                public void onClick(Produits p) {
                    System.out.println(p.getDesignation());
                }
            };
        }
        int column = 0;
        int row = 1;
        if(list!=null){
            for (int i = 0; i < list.size(); i++) {

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(list.get(i).getAnchopane(), column++, row);
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(list.get(i).getAnchopane(), new Insets(10));
            }
        }
    }
    
    private List<ProductItem> getData() {
        List<ProductItem> list = new ArrayList<>();
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
                InputStream photo = resultSet.getBinaryStream("Photo");
                Produits p = new Produits(Codeproduits, Designation, Description, Prix, Stock, RefCat);
                p.setPhoto(photo);
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(Constants.VIEWS_PACKAGE + "itemNew.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemNewController itemController = fxmlLoader.getController();
                itemController.setData(p,cl);
//                System.out.println(this.clientMainController);
                Platform.runLater(()->{
                    itemController.setClientMainController(this.clientMainController);
                });
                ProductItem pItem = new ProductItem(p, anchorPane);
                JFXRippler jr = new JFXRippler(itemController.getVboxContainer());
                jr.getStyleClass().add("ayoub");
                jr.setRipplerFill(Paint.valueOf("#9145b6"));
                anchorPane.getChildren().add(jr);
                list.add(pItem);
            }
        } catch (SQLException | IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            AlertsBuilder.create("error", stkProducts, "Please check your connection to MYSQL");
        }
        return list;
    }


    void setstkroot(StackPane stkClientRoot) {
        this.stkClientRoot = stkClientRoot;
    }

    void setanchor(AnchorPane anchorClientRoot) {
        this.anchorClientRoot=anchorClientRoot;
    }

    void setClientMainController(ClientMainController clientMainController) {
        this.clientMainController = clientMainController;
    }
    
}
