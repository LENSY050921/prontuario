/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

/**
 *
 * @author carlos
 */
import java.sql.*;
import javax.swing.*;


public class Connect {
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    public static Connection ConnectionDB(){
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/bdProntuario_Eletronico","postgres","123456");
            return conn;
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
        
        
    }
    
    
}
