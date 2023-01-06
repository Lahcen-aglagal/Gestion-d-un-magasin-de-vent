package com.AML.DBUtil;

import com.AML.Conn.connection;
import com.AML.entities.Clients;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ay0ub
 */
public class ClientsDB {
    public static boolean InsertClient(Clients c) {
        try {
            String sql = "INSERT INTO clients "
                    + "(Nom,Prenom,Email,Adresse,CodePostal,Ville,Tel,MotPasse) "
                    + "VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.getInstance().prepareStatement(sql);
            preparedStatement.setString(1, c.getNom());
            preparedStatement.setString(2, c.getPrenom());
            preparedStatement.setString(3, c.getEmail());
            preparedStatement.setString(4, c.getAdresse());
            preparedStatement.setInt(5, c.getCodePostal());
            preparedStatement.setString(6, c.getVille());
            preparedStatement.setInt(7, c.getTel());
            preparedStatement.setString(8, c.getMotDePasse());
            preparedStatement.execute();
            return true;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public static boolean EmailExists(String e) {
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
}
