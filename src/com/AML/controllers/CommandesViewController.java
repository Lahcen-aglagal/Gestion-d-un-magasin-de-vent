package com.AML.controllers;


import com.AML.Conn.connection;
import com.AML.DBUtil.UserSessionDB;
import com.AML.alerts.AlertsBuilder;
import com.AML.alerts.ConfirmAlert;
import com.AML.animations.Animations;
import com.AML.entities.CommandeTable;
import com.AML.notifications.NotificationsFactory;
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
public class CommandesViewController implements Initializable {

    private ObservableList<CommandeTable> listCommades;

    private ObservableList<CommandeTable> filterCommandes;
    @FXML
    private StackPane stkCommandes;
    @FXML
    private AnchorPane rootCommandes;
    @FXML
    private TableView<CommandeTable> tblCommandes;
    @FXML
    private TableColumn<CommandeTable, Integer> colCommId;
    @FXML
    private TableColumn<CommandeTable, Date> colCommDate;
    @FXML
    private TableColumn<CommandeTable, Integer> colTotalItem;
    @FXML
    private TableColumn<CommandeTable, String> colfullName;
    @FXML
    private TableColumn<CommandeTable, Double> colTotalPrice;
    @FXML
    private TableColumn<CommandeTable, JFXButton> colDelete;
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
                    if (c.getClientName().toLowerCase().contains(newVal.toLowerCase())
                            || c.getDateCommande().toString().toLowerCase().contains(newVal.toLowerCase())
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
        Animations.fadeIn(stkCommandes);
        Animations.fadeIn(rootCommandes);
        Animations.fadeIn(searchPane);
    }
    
    private void loadData() {
        loadTable();
        colCommId.setCellValueFactory(new PropertyValueFactory<>("NumCommande"));
        colCommDate.setCellValueFactory(new PropertyValueFactory<>("DateCommande"));
        colTotalItem.setCellValueFactory(new PropertyValueFactory<>("TotalItems"));
        colfullName.setCellValueFactory(new PropertyValueFactory<>("ClientName"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("deletebtn"));
    }

    private void loadTable() {
        ArrayList<CommandeTable> list = new ArrayList<>();
        try {
            String sql = "select "
                    + "c.NumCommande,DateCommande,"
                    + "sum(QteCde) as 'Total Items',"
                    + "concat(clt.nom,' ',clt.prenom) as 'Full Name',"
                    + "sum(QteCde*p.prix) as 'Total Price' "
                    + "from commandes c,lignecommandes lgcom,"
                    + "clients clt,produits p "
                    + "where c.NumCommande=lgcom.NumCommande and "
                    + "c.CodeClient=clt.Id and "
                    + "p.codeproduits=lgcom.codeproduits "
                    + "group by c.NumCommande;";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int NumCommande = resultSet.getInt("NumCommande");
                Date DateCommande = resultSet.getDate("DateCommande");
                int TotalItems = resultSet.getInt("Total Items");
                String FullName = resultSet.getString("Full Name");
                double TotalPrice = resultSet.getDouble("Total Price");
                JFXButton deletebtn = new JFXButton("Delete");
                CommandeTable cmd = new CommandeTable(NumCommande, DateCommande, TotalItems, FullName, TotalPrice, deletebtn);
                deletebtn.getStyleClass().add("btn-delete-commande");
                deletebtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {
                        ButtonType bt = ConfirmAlert.create("Confirm Delete", "Attention!!!", "if you delete this item other item can be deleted.\nAre you sure you want to delete it?");
                        if(bt==ButtonType.OK){
                            boolean res = DeleteCommande((JFXButton)e.getSource());
                            if(res){
                                loadTable();
                                AlertsBuilder.create("success", stkCommandes, "Command deleted seccussfully");
                            }
                            else{
                                NotificationsFactory.create("Error", "please check your connection");
                            }
                        }
                    }
                });
                list.add(cmd);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            AlertsBuilder.create("error", stkCommandes, "");
        }
        listCommades = FXCollections.observableArrayList(list);
        tblCommandes.getItems().clear();
        tblCommandes.setItems(listCommades);
        tblCommandes.setFixedCellSize(30);
    }
    private boolean DeleteCommande(JFXButton btn) {
        boolean res=false;
        for(CommandeTable c : listCommades){
            if(c.getDeletebtn().equals(btn)){
                res = DeleteCommande(c);
                break;
            }
        }
        return res;
    }

    private boolean DeleteCommande(CommandeTable c)  {
        try {
            String sql = "DELETE FROM commandes WHERE NumCommande = ?";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setInt(1, c.getNumCommande());
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
}
