/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinebrokeragesystem2016;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author harshshah2303
 */
public class mystocks {
    
    static final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
    
    public static void myorders()
    {
        String LoginID = OnlineSystem.getId();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from myorders where LoginID = '" + LoginID + "' order by TransID desc limit 5 ");
            if(rs.next())
            {
                rs.beforeFirst();
                System.out.println(LoginID + " Your stocks in myorder are ");
                System.out.println();
                System.out.printf("%10s%20s%20s%20s","StockName","TotalShare","Price","Type");
                System.out.println("");
                while(rs.next())
                {
                    System.out.printf("%10s%20s%20s%20s",rs.getString(3),rs.getDouble(4),rs.getDouble(5),rs.getString(6));
                    System.out.println();
                }
            }
            else
            {
                System.out.println("***You Don't have any stocks in myorder");
            }
                    
        }
        catch(SQLException e)
        {
            e.printStackTrace();
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
