

package com.AML.Conn;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *  create a single instance connection 
 * @author ay0ub
 * 
 */
public class connection {
    private static connection single_instance=null;
    private Connection con=null;
    private connection() throws ClassNotFoundException, SQLException {
//        try {
    
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/magasin","root","lahsen");
//        } catch (ClassNotFoundException | SQLException ex) {
//            ex.printStackTrace();
//        }
    }
    public static Connection getInstance() throws ClassNotFoundException, SQLException{
        if(single_instance==null)
            single_instance=new connection();
        return single_instance.con;
    }
}
