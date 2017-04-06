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
public class limitorder {
    
    static final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
    
    public static void buy()
    {
        Scanner sc = new Scanner(System.in);
        String loginID = OnlineSystem.getId();
        double balance = OnlineSystem.getBalance();
        String name = "";
        double quantity = 0.0;
        double cost = 0.0;
        String type = "Buy";
        
        System.out.println("Enter the stock you want to Buy");
        name = sc.next();
        
        System.out.println("Enter how many stocks you want to Buy");
        quantity = sc.nextDouble();
        
        System.out.println("Enter the price for which you want to buy the stock");
        cost = sc.nextDouble();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet rs  = null;
        Statement stat2 = null;
        ResultSet rs2  = null;
        
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from sellingorders where stockname = '" + name + "' and totalshares >= '" + quantity + "'");
            if(rs.next())
            {
                String SellerId = rs.getString(1);
                stat2 = conn.createStatement();
                    //get details of buyer from user details tables
                rs2 = stat2.executeQuery("select * from userdetails where LoginID = '" + SellerId +"'");
                if(rs2.next())
                {
                    double SellerBal = rs2.getDouble(6);
                    double fbal = SellerBal + rs.getDouble(4);
                    //check quantity if its less than update the quantity in  selling orders
                    if(quantity < rs.getDouble(3))
                    {
                        //check balance
                        if(balance >= cost)
                        {
                            //check price
                            if(cost >= rs.getDouble(4))
                            {
                                System.out.println("***Congratulations!!!Your trade is completed");

                                double diff = rs.getDouble(3) - quantity;
                                double bdiff = balance - cost;
                                OnlineSystem.setBalance(bdiff);

                                //update selling order quantity
                                int p = stat.executeUpdate("update sellingorders set totalshares = '" + diff + "' "
                                + "where price = '" + rs.getDouble(4)+ "' and LoginId = '" + SellerId + "' and totalshares = '" + rs.getDouble(3) + "' "
                                        + " and stockname = '" + rs.getString(2) + "'");

                                //add the trade to  user orders
                                
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                                
                                
                                

                                //update buyers balance in user details table
                                int a = stat.executeUpdate("update userdetails set Balance = '" + bdiff + "' where LoginID = '" + loginID + "'");
                                
                                //update seller balance in user details table
                                stat.executeUpdate("update userdetails set Balance = '" + fbal + "' where LoginID = '" + SellerId + "'");
                                
                                //update stocks most recent traded price
                                stat.executeUpdate("update stocks set recentPrice = '" + cost + "' where name = '" + name + "'");

                             }
                            else
                            {
                                //price is less than selling price
                                System.out.println("***TRADE NOT COMPLETED!!!The price you quoted is too less");
                                //pending orders
                                int r = stat.executeUpdate("insert into buyingorders values('" + loginID +"','" + name +"','" + quantity +"',"
                                        + "'" + cost +"')");

                                //add the trade to  user orders
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");

                            }
                        }
                        else
                        {
                            //not enough balance
                            System.out.println("***TRADE NOT COMPLETED!!!You don't have enough balance ");

                            String choice = "";
                            System.out.println("Would you like to add some balance? y or n");
                            choice = sc.next();
                            if("Y".equalsIgnoreCase(choice))
                            {
                              double new_bal = 0.0;
                              System.out.println("Enter the balance you would like to add");
                              new_bal = sc.nextDouble();
                              double bal_add = balance + new_bal;
                              OnlineSystem.setBalance(bal_add);
                              int a = stat.executeUpdate("update userdetails set Balance = '" + bal_add + "' where LoginID = '" + loginID + "'");
                            }

                        }
                    }
                    //if same quantity remove the entry from selling orders
                    else if(quantity == rs.getDouble(3))
                    {
                        //check balance
                        if(balance >= cost)
                        {
                            //check the price
                            if(cost >= rs.getDouble(4))
                            {
                                System.out.println("***Congratulations!!!Your trade is completed");

                                //delete the entry from selling order table
                                int p = stat.executeUpdate("delete from sellingorders where price = '" + rs.getDouble(4) + "'"
                                        + "and LoginId = '" + SellerId + "' and totalshares = '" + rs.getDouble(3) + "' "
                                        + " and stockname = '" + rs.getString(2) + "' ");

                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");

                                double bdiff = balance - cost;
                                OnlineSystem.setBalance(bdiff);

                                //update buyers balance in user details
                                int a = stat.executeUpdate("update userdetails set Balance = '" + bdiff + "' where LoginID = '" + loginID + "'");
                                
                                //uodate seller balance in user details table
                                
                                int b = stat.executeUpdate("update userdetails set Balance = '" + fbal + "' where LoginID = '" + SellerId + "'");
                                
                                //update stocks most recent traded price
                                stat.executeUpdate("update stocks set recentPrice = '" + cost + "' where name = '" + name + "'");

                            }
                            else
                            {
                                //price is less than selling price
                                System.out.println("***TRADE NOT COMPLETED!!!The price you quoted is too less");
                                //pending orders
                                int r = stat.executeUpdate("insert into buyingorders values('" + loginID +"','" + name +"','" + quantity +"',"
                                        + "'" + cost +"')");

                                //add the trade to  user orders
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                            }
                        }
                        else
                        {
                            //not enough balance
                            System.out.println("***TRADE NOT COMPLETED!!!You don't have enough balance ");
                            
                            String choice = "";
                            System.out.println("Would you like to add some balance? y or n");
                            choice = sc.next();
                            if("Y".equalsIgnoreCase(choice))
                            {
                              double new_bal = 0.0;
                              System.out.println("Enter the balance you would like to add");
                              new_bal = sc.nextDouble();
                              double bal_add = balance + new_bal;
                              OnlineSystem.setBalance(bal_add);
                              int a = stat.executeUpdate("update userdetails set Balance = '" + bal_add + "' where LoginID = '" + loginID + "'");
                            }

                        }
                    }
                    else
                    {
                        //quantity of buying more than selling so trade not completed and add it to buying orders
                        System.out.println("***TRADE NOT COMPLETED!!!The amount of stocks you want to buy are not available");
                        //pending orders
                        int r = stat.executeUpdate("insert into buyingorders values('" + loginID +"','" + name +"','" + quantity +"',"
                                    + "'" + cost +"')");

                        //add the trade to  user orders
                            stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                    }
                }
                else
                {
                    System.out.println("***Error!!!Seller not found!");
                }
            }
            else
            {
                //stock not available so trade not completed and add it to buying orders
                System.out.println("***TRADE NOT COMPLETED!!!The stock you want to buy is not available ");
                //pending orders
                int r = stat.executeUpdate("insert into buyingorders values('" + loginID +"','" + name +"','" + quantity +"',"
                                + "'" + cost +"')");
                
                //add the trade to  user orders
                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
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
               if(conn!=null)
                    conn.close();
                if(stat!=null)
                    stat.close();
                if(rs!=null)
                    rs.close();
                if(stat2!=null)
                    stat2.close();
                if(rs2!=null)
                    rs2.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    
    public static void sell()
    {
        Scanner sc = new Scanner(System.in);
        String loginID = OnlineSystem.getId();
        double balance = OnlineSystem.getBalance();
        String name = "";
        double quantity = 0.0;
        double cost = 0.0;
        String type = "Sell";
        
        System.out.println("Enter the stock you want to Sell");
        name = sc.next();
        
        System.out.println("Enter how many stocks you want to Sell");
        quantity = sc.nextDouble();
        
        System.out.println("Enter the price for which you want to Sell the stock");
        cost = sc.nextDouble();
        
        Connection conn = null;
        Statement stat = null;
        Statement stat2 = null;
        ResultSet rs2 = null;
        ResultSet rs  = null; 
        Statement stat3 = null;
        ResultSet rs3 = null;
        
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
             //checks the shares the user wants to sell is there in his orders
            rs = stat.executeQuery("select * from myorders where LoginID = '" + loginID + "' and name = '" + name +"' and type = 'Buy' "); 
            stat2 = conn.createStatement();
            //check the share is available in buying orders and the price should be more
            rs2 = stat2.executeQuery("select * from buyingorders where stockname = '" + name + "' and price >= '" + cost + "' ");
            
            if(rs.next())
            {
                rs.beforeFirst();
                //shares are available
                while(rs2.next())
                {
                    String BuyerID = rs2.getString(1);
                    stat3 = conn.createStatement();
                    //get details of buyer from user details tables
                    rs3 = stat3.executeQuery("select * from userdetails where LoginID = '" + BuyerID +"'");
                    if(rs3.next())
                    {
                        double BuyerBal = rs3.getDouble(6);
                        //price and shares valid in buying orders.Now check quantity
                        if(quantity > rs2.getDouble(3))
                        {
                            //check buyers balance
                            if(BuyerBal >= rs2.getDouble(4))
                            {    
                                //selling stocks greater than buying stocks
                                System.out.println("You sold " + rs2.getDouble(3) + " out of " + quantity + " stocks");
                                
                                quantity = quantity - rs2.getDouble(3);
                                
                                //delete record from buyingorders beacuse all stocks are buyed
                                stat.executeUpdate("delete from buyingorders where price = '" + rs2.getDouble(4) + "' and LoginId = '" + BuyerID + "' "
                                     + "and totalshares = '" + rs2.getDouble(3) + "' and stockname = '" + rs2.getString(2) + "'");
                                
                                //insert into user orders
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + rs2.getDouble(3) +"','" + cost + "','" + type +"')");
                                //update sellers balance
                                double f_sellerbal = balance + cost;
                                stat.executeUpdate("update userdetails set Balance = '" + f_sellerbal + "' where LoginID = '" + loginID + "'");
                                //update buyers balance
                                double f_buyerbal = BuyerBal - rs2.getDouble(4);
                                stat.executeUpdate("update userdetails set Balance = '" + f_buyerbal + "' where LoginID = '" + BuyerID + "'");
                                
                            }
                            else
                            {
                                //not enough balance to buy
                                System.out.println("***TRADE NOT COMPLETED!!!The buyer doesn't have enough balance to buy the stocks");
                            }
                        }
                        else if (quantity < rs2.getDouble(3))
                        {
                            //buying stocks greater than selling stocks
                            if(BuyerBal >= rs2.getDouble(4))
                            {    
                                //selling stocks less than buying stocks
                                System.out.println("***Congratulations!!!Your trade is completed");
                                //update buying orders quantity to the stocks that were left to be purchased
                                double fquant = rs2.getDouble(3) - quantity;
                                stat.executeUpdate("update buyingorders set totalshares = '" + fquant + "' where price = '" + rs2.getDouble(4) + "'"
                             + "and LoginId = '" + BuyerID + "' and totalshares = '" + rs2.getDouble(3) + "' and stockname = '" + rs2.getString(2) + "'");
                                //insert into user orders
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                                //update sellers balance
                                double f_sellerbal = balance + cost;
                                stat.executeUpdate("update userdetails set Balance = '" + f_sellerbal + "' where LoginID = '" + loginID + "'");
                                //update buyers balance
                                double f_buyerbal = BuyerBal - rs2.getDouble(4);
                                stat.executeUpdate("update userdetails set Balance = '" + f_buyerbal + "' where LoginID = '" + BuyerID + "'");
                                
                                //update stocks most recent traded price
                                stat.executeUpdate("update stocks set recentPrice = '" + cost + "' where name = '" + name + "'");
                                quantity = 0;
                                break;
                            }
                            else
                            {
                                //not enough balance to buy
                                System.out.println("***TRADE NOT COMPLETED!!!The buyer doesn't have enough balance to buy the stocks");
                            }
                        }
                        else
                        {
                            //selling stocks equal to buying stocks
                            if(BuyerBal >= rs2.getDouble(4))
                            {    
                                System.out.println("***Congratulations!!!Your trade is completed");
                                //delete buying entry from buying table
                                stat.executeUpdate("delete from buyingorders where price = '" + rs2.getDouble(4) + "' and LoginId = '" + BuyerID + "' "
                                     + "and totalshares = '" + rs2.getDouble(3) + "' and stockname = '" + rs2.getString(2) + "'");
                                //insert into user orders
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                                //update sellers balance
                                double f_sellerbal = balance + cost;
                                stat.executeUpdate("update userdetails set Balance = '" + f_sellerbal + "' where LoginID = '" + loginID + "'");
                                //update buyers balance
                                double f_buyerbal = BuyerBal - rs2.getDouble(4);
                                stat.executeUpdate("update userdetails set Balance = '" + f_buyerbal + "' where LoginID = '" + BuyerID + "'");
                                
                                //update stocks most recent traded price
                                stat.executeUpdate("update stocks set recentPrice = '" + cost + "' where name = '" + name + "'");
                                quantity = 0;
                                break;
                            }
                            else
                            {
                                //not enough balance to buy
                                System.out.println("***TRADE NOT COMPLETED!!!The buyer doesn't have enough balance to buy the stocks");
                            }
                        }
                    
                    }
                    else
                    {
                        //Buyer not present in user details
                        System.out.println("***Error!!!Buyer not found");
                    }
                }
                if(quantity > 0)
                {
                    //share not available in buying orders
                    System.out.println("***" + quantity + " stocks you want to sell are not available ");
                    //insert into user orders
                    stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                    
                    stat.executeUpdate("insert into sellingorders values('" + loginID + "','" + name + "','" + quantity + "','" + cost + "')");
                }
            }
            else
            {
                //share not available
                System.out.println("***TRADE NOT COMPLETED!!!!The share you want to sell is not available in your orders");
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
                if(conn!=null)
                    conn.close();
                if(stat!=null)
                    stat.close();
                if(rs!=null)
                    rs.close();
                if(stat2!=null)
                    stat2.close();
                if(rs2!=null)
                    rs2.close();
                if(stat3!=null)
                    stat3.close();
                if(rs3!=null)
                    rs3.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
}
