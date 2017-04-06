/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author harshshah2303
 */
@Named(value = "myorder")
@ManagedBean
@SessionScoped
public class myorder implements Serializable {

    
    public ArrayList<Orders> myorder() 
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e)
        {
            return null;
        }
        
        ArrayList<Orders> aray = new ArrayList<>();
        String LoginID = Login.loginID;
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from myorders where LoginID = '" + LoginID + "' order by TransID desc limit 5 ");
           
            while(rs.next())
            {
                aray.add(new Orders(rs.getString(3),rs.getDouble(4),rs.getDouble(5),rs.getString(6)));
            }
            
            return aray;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            try
            {
               if(conn!= null)
                   conn.close();
               if(stat!= null)
                   stat.close();
               if(rs!=null)
                   rs.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
