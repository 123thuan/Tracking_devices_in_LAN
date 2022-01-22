/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connect;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class InfoDAO {
    
    Connection con;
    Statement st;
    PreparedStatement ps;
    public InfoDAO(){
        try {     
            con = new ConnectDB().openConnect();
        } catch (Exception ex) {
            Logger.getLogger(InfoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void  inSertUsbInfo(Info usi){
       
        try {
            String sql = "INSERT INTO  usb_info(v_id,p_id,name) VALUES(?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1,usi.getvID());
            ps.setString(2, usi.getpID());
            ps.setString(3, usi.getName());
            ps.executeUpdate();
            
        } catch (Exception e) {
        }
         
    }
    
    public boolean checkDB(String vID, String pID) {
        String sql  = "SELECT COUNT(name) FROM usb_info WHERE v_id ='" + vID + "' AND p_id ='"
                + pID +"'";
        int total = 0;
        try {
            ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) total = rs.getInt(1);
            else return false;
        } catch (SQLException ex) {
            Logger.getLogger(InfoDAO.class.getName()).log(Level.SEVERE, null, ex);
            System.err.print(ex.getMessage());
        }
        
        if (total == 0) return false;
        else return true;
           
    } 
    //test all method
    
    public static void main(String[] args) {
        InfoDAO usbDao = new InfoDAO();
    }
    
}
