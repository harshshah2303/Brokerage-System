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
public class watchedList {

    static final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";

    public static void favstocks() {
        Scanner sc = new Scanner(System.in);
        String choice = "";
        String LoginID = OnlineSystem.getId();
        String option = "";

        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(DB_URL, "shahh6626", "1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from favstocks where LoginID = '" + LoginID + "'");

            if (rs.next()) {
                System.out.println(LoginID + " Your Favourite stocks are \n");
                System.out.printf("%10s%20s%20s%20s%20s%20s%20s", "Symbol", "Name", "RecentPrice", "HighestBuyPrice", "TotalBuyShares",
                        "LowestSellPrice", "TotalSellShares");
                System.out.println();
                rs.beforeFirst();
                while (rs.next()) {

                    System.out.printf("%10s%20s%20f%20f%20f%20f%20f", rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6),
                            rs.getDouble(7), rs.getDouble(8));
                    System.out.println();

                }
                System.out.println("Do you want to remove any of the stocks from your favourite stock list?y or n");
                option = sc.next();
                if ("Y".equalsIgnoreCase(option)) {
                    System.out.println("Enter symbol of the stock you would like to remove");
                    String select = sc.next();
                    stat.executeUpdate("delete from favstocks where LoginID = '" + LoginID + "' and symbol = '" + select + "'");
                    System.out.println("***The stock has been removed from your favourite stock list");
                }
            } else {
                System.out.println("***You don't have any stocks in your watched list!!!");
            }

            //stock not found
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                stat.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void updatestocks() {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat2 = null;
        ResultSet rs2 = null;
        
        try 
        {
            conn = DriverManager.getConnection(DB_URL, "shahh6626", "1453730");
            stat = conn.createStatement();
            stat2 = conn.createStatement();
            rs = stat.executeQuery("select * from stocks");

            while (rs.next()) 
            {
                // rs.beforeFirst();
                String stockname = rs.getString(2);
                double buy_quantity = 0;
                double buy_price = 0;
                double sell_quantity = 0;
                double sell_price = 0;
                
                rs2 = stat2.executeQuery("select * from buyingorders where stockname = '" + stockname + "' order by price desc");
                
                if(rs2.next())
                {
                    buy_price = rs2.getDouble(4);
                    rs2.beforeFirst();
                    while(rs2.next())
                    {
                       if(rs2.getDouble(4) == buy_price)
                       {
                           buy_quantity += rs2.getDouble(3);
                       }
                    }
                }
                rs2.beforeFirst();
                
                rs2 = stat2.executeQuery("select * from sellingorders where stockname = '" + stockname + "' order by price ");
                
                if(rs2.next())
                {
                    sell_price = rs2.getDouble(4);
                    rs2.beforeFirst();
                    while(rs2.next())
                    {
                       if(rs2.getDouble(4) == sell_price)
                       {
                           sell_quantity += rs2.getDouble(3);
                       }
                    }
                }
                
                marketorder.insertUpdate("update stocks set highestBuyPrice = '" + buy_price + "', totalBuyShares = '" + buy_quantity + "',"
                        + " lowestSellPrice = '" + sell_price + "', totalSellShares = '" + sell_quantity + "' where name = '" + stockname + "'");
            }
        }
         catch (SQLException e) 
         {
            e.printStackTrace();
        } 
        finally
        {
            try 
            {
                if (conn != null) 
                    conn.close();
                
                if (stat != null) 
                    stat.close();
                
                if (rs != null) 
                    rs.close();
                
                if (stat2 != null) 
                    stat2.close();
                
                if (rs2 != null) 
                    rs2.close();
                
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }

}
