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
public class marketorder {

    static final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
    
    public static void insertUpdate(String query)
    {
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        try 
        {
            conn = DriverManager.getConnection(DB_URL, "shahh6626", "1453730");
            stat = conn.createStatement();
            stat.executeUpdate(query);
        } 
        catch (SQLException e)
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
            } 
            catch (Exception e)
            {
                e.printStackTrace();
            }
        } 
    }

    public static void buy() 
    {
        Scanner sc = new Scanner(System.in);
        String name = "";
        double quantity = 0;
        String loginID = OnlineSystem.getId();
        double balance = OnlineSystem.getBalance();
        String type = "Buy";

        System.out.println("Enter the stocks you want to buy");
        name = sc.next();
        System.out.println("Enter how many stocks you want to buy");
        quantity = sc.nextDouble();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat2 = null;
        ResultSet rs2 = null;
        try 
        {
            conn = DriverManager.getConnection(DB_URL, "shahh6626", "1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from sellingorders where stockname = '" + name + "' order by price ");
            while (rs.next() || quantity == 0  ) 
            {
                double cost = rs.getDouble(4);
                String SellerId = rs.getString(1);
                stat2 = conn.createStatement();
                //get details of buyer from user details tables
                rs2 = stat2.executeQuery("select * from userdetails where LoginID = '" + SellerId + "'");
                if (rs2.next()) 
                {
                    double SellerBal = rs2.getDouble(6);
                    double fbal = SellerBal + cost;
                    //check users balance
                    if (balance >= rs.getDouble(4)) 
                    {
                        //check quantity
                        if(quantity > rs.getDouble(3) )
                        {
                            //user buying more stocks than available in selling orders
                            double bdiff = balance - cost;
                            OnlineSystem.setBalance(bdiff);
                            
                            System.out.println("You bought " + rs.getDouble(3) + " out of " + quantity + " stocks");
                            
                            //update quantity of stocks
                            quantity = quantity - rs.getDouble(3);
                            
                            insertUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + rs.getDouble(3) +"','" + cost + "','" + type +"')");
                            
                            //delete the stocks from selling orders
                            insertUpdate("delete from sellingorders where price = '" + rs.getDouble(4) + "' and LoginId = '" + SellerId + "' "
                                    + " and totalshares = '" + rs.getDouble(3) + "' and stockname = '" + rs.getString(2) + "' ");
                            
                            //update buyers balance in user details table
                            insertUpdate("update userdetails set Balance = '" + bdiff + "' where LoginID = '" + loginID + "'");
                            balance = bdiff;
                                
                            //update seller balance in user details table
                            insertUpdate("update userdetails set Balance = '" + fbal + "' where LoginID = '" + SellerId + "'");
                        
                        }
                        else if(quantity < rs.getDouble(3))
                        {
                            //user buying less stocks than available in selling orders
                            System.out.println("***Congratulations!!!Your trade is completed!!!");
                            
                            double diff = rs.getDouble(3) - quantity;
                            double bdiff = balance - cost;
                            OnlineSystem.setBalance(bdiff);

                            //update selling order quantity
                            insertUpdate("update sellingorders set totalshares = '" + diff + "' where price = '" + rs.getDouble(4)+ "'"
                            + " and LoginId = '" + SellerId + "' and totalshares = '" + rs.getDouble(3) + "' and stockname = '" + rs.getString(2) + "'");
                            
                            //add the trade to  user orders
                            insertUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                           
                            //update buyers balance in user details table
                            insertUpdate("update userdetails set Balance = '" + bdiff + "' where LoginID = '" + loginID + "'");
                            
                            //update seller balance in user details table
                            insertUpdate("update userdetails set Balance = '" + fbal + "' where LoginID = '" + SellerId + "'");
                            
                            //update stocks most recent traded price
                            insertUpdate("update stocks set recentPrice = '" + cost + "' where name = '" + name + "'");
                            quantity = 0;
                            break;
                        }
                        else
                        {
                            //user buying equal stocks than available in selling orders
                            System.out.println("***Congratulations!!!Your trade is completed!!!");
                            
                            //delete the stocks from selling orders
                            insertUpdate("delete from sellingorders where price = '" + rs.getDouble(4) + "' and LoginId = '" + SellerId + "' "
                                    + "and totalshares = '" + rs.getDouble(3) + "' and stockname = '" + rs.getString(2) + "'   ");
                            
                            double bdiff = balance - cost;
                            OnlineSystem.setBalance(bdiff);
                            
                            //add the trade to  user orders
                            insertUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                           
                            //update buyers balance in user details table
                            insertUpdate("update userdetails set Balance = '" + bdiff + "' where LoginID = '" + loginID + "'");
                            
                            //update seller balance in user details table
                            insertUpdate("update userdetails set Balance = '" + fbal + "' where LoginID = '" + SellerId + "'");
                            
                            //update stocks most recent traded price
                            insertUpdate("update stocks set recentPrice = '" + cost + "' where name = '" + name + "'");
                            quantity = 0;
                            break;
                        }
                    } 
                    else 
                    {
                        //not enough balance
                        System.out.println("***TRADE NOT COMPLETED!!!You don't have enough balance ");
                        String choice = "";
                        System.out.println("Would you like to add some balance? y or n");
                        choice = sc.next();
                        if ("Y".equalsIgnoreCase(choice)) 
                        {
                            double new_bal = 0.0;
                            System.out.println("Enter the balance you would like to add");
                            new_bal = sc.nextDouble();
                            double bal_add = balance + new_bal;
                            OnlineSystem.setBalance(bal_add);
                            insertUpdate("update userdetails set Balance = '" + bal_add + "' where LoginID = '" + loginID + "'");
                        }
                    }
                }
                
                else 
               {
                    System.out.println("***Error!!!Seller not found!");
               }
              //  quantity = quantity - rs.getDouble(3);
            }
            
            double cost = 300;
            
            if(quantity > 0)
            {
            //stock not available so trade not completed and add it to buying orders
            System.out.println("***" + quantity + " stocks you want to buy are not available ");
            //pending orders
            insertUpdate("insert into buyingorders values('" + loginID +"','" + name +"','" + quantity +"',"
                                + "'" + cost +"')");
                
            //add the trade to  user orders
            insertUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
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
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }

    }
    
    
    public static void sell() 
    {
        Scanner sc = new Scanner(System.in);
        String name = "";
        double quantity = 0;
        String loginID = OnlineSystem.getId();
        double balance = OnlineSystem.getBalance();
        String type = "Sell";

        System.out.println("Enter the stocks you want to sell");
        name = sc.next();
        System.out.println("Enter how many stocks you want to sell");
        quantity = sc.nextDouble();
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat2 = null;
        ResultSet rs2 = null;
        Statement stat3 = null;
        ResultSet rs3 = null;
        try 
        {
            conn = DriverManager.getConnection(DB_URL, "shahh6626", "1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from myorders where LoginID = '" + loginID + "' and name = '" + name +"' and type = 'Buy' "); 
            stat2 = conn.createStatement();
            //check the share is available in buying orders and the price should be more
            rs2 = stat2.executeQuery("select * from buyingorders where stockname = '" + name + "' order by price desc");
            
            if(rs.next())
            {
                rs.beforeFirst();
                while(rs2.next())
                {
                    double cost = rs2.getDouble(4);
                    String BuyerID = rs2.getString(1);
                    stat3 = conn.createStatement();
                    //get details of buyer from user details tables
                    rs3 = stat3.executeQuery("select * from userdetails where LoginID = '" + BuyerID +"'");
                    if(rs3.next())
                    {
                       double BuyerBal = rs3.getDouble(6);
                       if(BuyerBal >= rs2.getDouble(4))
                       {
                         //enough balance
                         if(quantity > rs2.getDouble(3))
                         {
                             System.out.println("You sold " + rs2.getDouble(3) + " out of " + quantity + " stocks");

                             //update quantity of stocks
                             quantity = quantity - rs2.getDouble(3);

                             //delete the buying order
                             insertUpdate("delete from buyingorders where price = '" + cost + "' and LoginId = '" + BuyerID + "' "
                                     + "and totalshares = '" + rs2.getDouble(3) + "' and stockname = '" + rs2.getString(2) + "'");

                             //insert into user orders
                             insertUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + rs2.getDouble(3) +"','" + cost + "','" + type +"')");

                             //update sellers balance
                             double f_sellerbal = balance + cost;
                             insertUpdate("update userdetails set Balance = '" + f_sellerbal + "' where LoginID = '" + loginID + "'");
                             balance = f_sellerbal;

                             //update buyers balance
                             double f_buyerbal = BuyerBal - rs2.getDouble(4);
                             insertUpdate("update userdetails set Balance = '" + f_buyerbal + "' where LoginID = '" + BuyerID + "'");
                            
                         }
                         else if (quantity < rs2.getDouble(3)) 
                         {
                             //selling stocks less than buying stocks
                             System.out.println("***Congratulations!!!Your trade is completed");
                             //update buying orders quantity to the stocks that were left to be purchased
                             double fquant = rs2.getDouble(3) - quantity;
                             insertUpdate("update buyingorders set totalshares = '" + fquant + "' where price = '" + rs2.getDouble(4) + "' "
                             + "and LoginId = '" + BuyerID + "' and totalshares = '" + rs2.getDouble(3) + "' and stockname = '" + rs2.getString(2) + "'");
                             //insert into user orders
                             insertUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                             //update sellers balance
                             double f_sellerbal = balance + cost;
                             insertUpdate("update userdetails set Balance = '" + f_sellerbal + "' where LoginID = '" + loginID + "'");
                             //update buyers balance
                             double f_buyerbal = BuyerBal - rs2.getDouble(4);
                             insertUpdate("update userdetails set Balance = '" + f_buyerbal + "' where LoginID = '" + BuyerID + "'");
                             
                             //update stocks most recent traded price
                             insertUpdate("update stocks set recentPrice = '" + cost + "' where name = '" + name + "'");
                             quantity = 0;
                             break;
                         }
                         else
                         {
                             //equal quantity
                             System.out.println("***Congratulations!!!Your trade is completed");
                             //delete buying entry from buying table
                             insertUpdate("delete from buyingorders where price = '" + rs2.getDouble(4) + "' and LoginId = '" + BuyerID + "' "
                                     + "and totalshares = '" + rs2.getDouble(3) + "' and stockname = '" + rs2.getString(2) + "'");
                             //insert into user orders
                             insertUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                             //update sellers balance
                             double f_sellerbal = balance + cost;
                             insertUpdate("update userdetails set Balance = '" + f_sellerbal + "' where LoginID = '" + loginID + "'");
                             //update buyers balance
                             double f_buyerbal = BuyerBal - rs2.getDouble(4);
                             insertUpdate("update userdetails set Balance = '" + f_buyerbal + "' where LoginID = '" + BuyerID + "'");
                             
                             //update stocks most recent traded price
                             insertUpdate("update stocks set recentPrice = '" + cost + "' where name = '" + name + "'");
                             quantity = 0;
                             break;
                         }
                       }
                       else
                        {
                            //not enough balance to buy
                            System.out.println("***TRADE NOT COMPLETED!!!The buyer doesn't have enough balance to buy the stocks");
                        }
                    }
                    else
                    {
                        //Buyer not present in user details
                        System.out.println("***Error!!!Buyer not found");
                    }
                }
                double cost = 300;
                if(quantity > 0)
                {
                    //share not available in buying orders
                    System.out.println("***" + quantity + " stocks you want to sell are not available ");
                    //insert into user orders
                    insertUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + loginID +"','" + name +"','" + quantity +"','" + cost + "','" + type +"')");
                    
                    stat.executeUpdate("insert into sellingorders values('" + loginID + "','" + name + "','" + quantity + "','" + cost + "')");
                } 

            }
            else
            {
                //share not available
                System.out.println("***TRADE NOT COMPLETED!!!!The share you want to sell is not available in your orders");
            }
            
            
        } catch (SQLException e)
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
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

}
