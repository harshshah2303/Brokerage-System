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
import java.util.Scanner;

/**
 *
 * @author harshshah2303
 */
public class search {
    
    static final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
    
    public static void searchsym()
    {
        Scanner sc = new Scanner(System.in);
        String choice = "";
        System.out.println("Enter the symbol of the stock you want to search");
        choice = sc.next();
        String option = "";
        String LoginID = OnlineSystem.getId();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from stocks where symbol = '" + choice + "'");
            
            if(rs.next())
            {
                System.out.printf("%s%15s%25s%20s%20s%20s%20s","Symbol","Name","RecentPrice","HighestBuyPrice","TotalBuyShares",
                        "LowestSellPrice","TotalSellShares");
                System.out.println();
                System.out.printf("%s%20s%20f%20f%20f%20f%20f",rs.getString(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4),rs.getDouble(5),
                                   rs.getDouble(6),rs.getDouble(7));
                System.out.println();
                
                System.out.println("Do you want to add this stock to your favourite stocks?y or n");
                option = sc.next();
                if("Y".equalsIgnoreCase(option))
                {
                    stat.executeUpdate("insert into favstocks values ('" + LoginID + "','" + rs.getString(1) + "','" + rs.getString(2) + "',"
                                    + "'" + rs.getDouble(3) + "','" + rs.getDouble(4) + "',"
                                    + "'" + rs.getDouble(5) + "','" + rs.getDouble(6) + "','" + rs.getDouble(7) + "')");
                    System.out.println("***The stock has been added to your favourite stock list!!!");
                }
                else
                {
                    System.out.println("***The stock has not been added to your favourite stock list!!!");
                }
            }
            else
            {
                //symbol not found
                System.out.println("***Symbol Not Found!!!");
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
                conn.close();
                stat.close();
                rs.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
