package com.AML.controllers;

import com.AML.Conn.connection;
import com.AML.DBUtil.UserSessionDB;
import com.AML.alerts.AlertsBuilder;
import com.AML.animations.Animations;
import com.AML.entities.CommandeTable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
public class ClientCommandesViewController implements Initializable {

    private ObservableList<CommandeTable> listCommades;

    private ObservableList<CommandeTable> filterCommandes;
    @FXML
    private StackPane stkClientCommandes;
    @FXML
    private AnchorPane rootClientCommandes;
    @FXML
    private TableView<CommandeTable> tblCommandes;
    @FXML
    private TableColumn<CommandeTable, Integer> colCommId;
    @FXML
    private TableColumn<CommandeTable, Date> colCommDate;
    @FXML
    private TableColumn<CommandeTable, Double> colTotalItem;
    @FXML
    private TableColumn<CommandeTable, Double> colTotalPrice;
    @FXML
    private HBox searchPane;
    @FXML
    private JFXTextField searchCommandes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAnimations();
        loadData();
        searchCommandes.textProperty().addListener((o,oldVal,newVal)->{
            if (newVal.isEmpty()) {
                tblCommandes.setItems(listCommades);
            } else {
                filterCommandes=FXCollections.observableArrayList();
                for (CommandeTable c : listCommades) {
                    if (c.getDateCommande().toString().toLowerCase().contains(newVal.toLowerCase())
                            || String.valueOf(c.getTotalItems()).toLowerCase().contains(newVal.toLowerCase())
                            ) {
                        filterCommandes.add(c);
                    }
                }
                tblCommandes.setItems(filterCommandes);
            }
        });
        Platform.runLater(()->{
            UserSessionDB.checkLogin(searchCommandes);
        });
    }    
    
    public void setAnimations(){
        Animations.fadeIn(stkClientCommandes);
        Animations.fadeIn(rootClientCommandes);
        Animations.fadeIn(searchPane);
    }
    
    private void loadData() {
        loadTable();
        colCommId.setCellValueFactory(new PropertyValueFactory<>("NumCommande"));
        colCommDate.setCellValueFactory(new PropertyValueFactory<>("DateCommande"));
        colTotalItem.setCellValueFactory(new PropertyValueFactory<>("TotalItems"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));
    }

    private void loadTable() {
        ArrayList<CommandeTable> list = new ArrayList<>();
        try {
            String sql = "select "
                    + "c.NumCommande,DateCommande,"
                    + "sum(QteCde) as 'Total Items',"
                    + "sum(QteCde*p.prix) as 'Total Price' "
                    + "from commandes c,lignecommandes lgcom,"
                    + "clients clt,produits p "
                    + "where c.NumCommande=lgcom.NumCommande and "
                    + "c.CodeClient=clt.Id and "
                    + "p.codeproduits=lgcom.codeproduits and c.CodeClient = ? "
                    + "group by c.NumCommande;";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1, GetCodeClient());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int NumCommande = resultSet.getInt("NumCommande");
                Date DateCommande = resultSet.getDate("DateCommande");
                int TotalItems = resultSet.getInt("Total Items");
                double TotalPrice = resultSet.getDouble("Total Price");
                CommandeTable cmd = new CommandeTable(NumCommande, DateCommande, TotalItems, null, TotalPrice,null);
                list.add(cmd);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            AlertsBuilder.create("error", stkClientCommandes, "please check your connection.");
        }
        listCommades = FXCollections.observableArrayList(list);
        tblCommandes.getItems().clear();
        tblCommandes.setItems(listCommades);
        tblCommandes.setFixedCellSize(30);
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
