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
import javax.faces.bean.ManagedBean;

/**
 *
 * @author harshshah2303
 */
@Named(value = "trade")
@ManagedBean
@SessionScoped
public class Trade implements Serializable {
    private String bname;
    private double bquantity;
    private double bprice;
    private String sname;
    private double squantity;
    private double sprice;

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public double getBquantity() {
        return bquantity;
    }

    public void setBquantity(double bquantity) {
        this.bquantity = bquantity;
    }

    public double getBprice() {
        return bprice;
    }

    public void setBprice(double bprice) {
        this.bprice = bprice;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public double getSquantity() {
        return squantity;
    }

    public void setSquantity(double squantity) {
        this.squantity = squantity;
    }

    public double getSprice() {
        return sprice;
    }

    public void setSprice(double sprice) {
        this.sprice = sprice;
    }
    
    public String buy()
    {
        String LoginID = Login.loginID;
        double bal = Login.balance1;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e)
        {
            return "error";
        }
        String type = "Buy";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat2 = null;
        ResultSet rs2  = null;
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from sellingorders where stockname = '" + bname + "' and totalshares >= '" + bquantity + "'");
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
                    if(bquantity < rs.getDouble(3))
                    {
                        //check balance
                        if(bal >= bprice)
                        {
                            //check price
                            if(bprice >= rs.getDouble(4))
                            {
                                double diff = rs.getDouble(3) - bquantity;
                                double bdiff = bal - bprice;
                                
                                //update selling order quantity
                                int p = stat.executeUpdate("update sellingorders set totalshares = '" + diff + "' "
                                + "where price = '" + rs.getDouble(4)+ "' and LoginId = '" + SellerId + "' and totalshares = '" + rs.getDouble(3) + "' "
                                        + " and stockname = '" + rs.getString(2) + "'");

                                //add the trade to  user orders
                                
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + LoginID +"','" + bname +"','" + bquantity +"','" + bprice + "','" + type +"')");
                                
                                 //update buyers balance in user details table
                                int a = stat.executeUpdate("update userdetails set Balance = '" + bdiff + "' where LoginID = '" + LoginID + "'");
                                
                                //update seller balance in user details table
                                stat.executeUpdate("update userdetails set Balance = '" + fbal + "' where LoginID = '" + SellerId + "'");
                                
                                //update stocks most recent traded price
                                stat.executeUpdate("update stocks set recentPrice = '" + bprice + "' where name = '" + bname + "'");
                                
                                return "tradecomplete";

                             }
                            else
                            {
                                
                                //pending orders
                                int r = stat.executeUpdate("insert into buyingorders values('" + LoginID +"','" + bname +"','" + bquantity +"',"
                                        + "'" + bprice +"')");

                                //add the trade to  user orders
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + LoginID +"','" + bname +"','" + bquantity +"','" + bprice + "','" + type +"')");
                                
                                return "tradenotcomplete";

                            }
                        }
                        else
                        {
                            //not enough balance
                            return "tradenotcomplete";
                        }
                    }
                    //if same quantity remove the entry from selling orders
                    else if(bquantity == rs.getDouble(3))
                    {
                        //check balance
                        if(bal >= bprice)
                        {
                            //check the price
                            if(bprice >= rs.getDouble(4))
                            {
                                 //delete the entry from selling order table
                                int p = stat.executeUpdate("delete from sellingorders where price = '" + rs.getDouble(4) + "'"
                                        + "and LoginId = '" + SellerId + "' and totalshares = '" + rs.getDouble(3) + "' "
                                        + " and stockname = '" + rs.getString(2) + "' ");

                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + LoginID +"','" + bname +"','" + bquantity +"','" + bprice + "','" + type +"')");

                                double bdiff = bal - bprice;
                                
                                //update buyers balance in user details
                                int a = stat.executeUpdate("update userdetails set Balance = '" + bdiff + "' where LoginID = '" + LoginID + "'");
                                
                                //uodate seller balance in user details table
                                
                                int b = stat.executeUpdate("update userdetails set Balance = '" + fbal + "' where LoginID = '" + SellerId + "'");
                                
                                //update stocks most recent traded price
                                stat.executeUpdate("update stocks set recentPrice = '" + bprice + "' where name = '" + bname + "'");
                                
                                return "tradecomplete";

                            }
                            else
                            {
                                //pending orders
                                int r = stat.executeUpdate("insert into buyingorders values('" + LoginID +"','" + bname +"','" + bquantity +"',"
                                        + "'" + bprice +"')");

                                //add the trade to  user orders
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + LoginID +"','" + bname +"','" + bquantity +"','" + bprice + "','" + type +"')");
                                
                                return "tradenotcomplete";
                            }
                        }
                        else
                        {
                            //not enough balance
                            return "tradenotcomplete";
                        }
                    }
                    else
                    {
                        //quantity of buying more than selling so trade not completed and add it to buying orders
                        
                        //pending orders
                        int r = stat.executeUpdate("insert into buyingorders values('" + LoginID +"','" + bname +"','" + bquantity +"',"
                                    + "'" + bprice +"')");

                        //add the trade to  user orders
                            stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + LoginID +"','" + bname +"','" + bquantity +"','" + bprice + "','" + type +"')");
                            
                            return "tradenotcomplete";
                    }
                }
                else
                {
                    return "tradenotcomplete";
                }
            }
            else
            {
                //pending orders
                int r = stat.executeUpdate("insert into buyingorders values('" + LoginID +"','" + bname +"','" + bquantity +"',"
                                + "'" + bprice +"')");
                
                //add the trade to  user orders
                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + LoginID +"','" + bname +"','" + bquantity +"','" + bprice + "','" + type +"')");
                
                return "tradenotcomplete";
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return "error";
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
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public String sell()
    {
        String LoginID = Login.loginID;
        double bal = Login.balance1;
       try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e)
        {
            return "error";
        }
        String type = "Sell";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        Statement stat2 = null;
        ResultSet rs2 = null;
        Statement stat3 = null;
        ResultSet rs3 = null;
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("select * from myorders where LoginID = '" + LoginID + "' and name = '" + sname +"' and type = 'Buy' "); 
            stat2 = conn.createStatement();
            //check the share is available in buying orders and the price should be more
            rs2 = stat2.executeQuery("select * from buyingorders where stockname = '" + sname + "' and price >= '" + sprice + "' ");
            
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
                        if(squantity > rs2.getDouble(3))
                        {
                            //check buyers balance
                            if(BuyerBal >= rs2.getDouble(4))
                            {    
                                //selling stocks greater than buying stocks
                                squantity = squantity - rs2.getDouble(3);
                                
                                //delete record from buyingorders beacuse all stocks are buyed
                                stat.executeUpdate("delete from buyingorders where price = '" + rs2.getDouble(4) + "' and LoginId = '" + BuyerID + "' "
                                     + "and totalshares = '" + rs2.getDouble(3) + "' and stockname = '" + rs2.getString(2) + "'");
                                
                                //insert into user orders
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + LoginID +"','" + sname +"','" + rs2.getDouble(3) +"','" + sprice + "','" + type +"')");
                                //update sellers balance
                                double f_sellerbal = bal + sprice;
                                stat.executeUpdate("update userdetails set Balance = '" + f_sellerbal + "' where LoginID = '" + LoginID + "'");
                                //update buyers balance
                                double f_buyerbal = BuyerBal - rs2.getDouble(4);
                                stat.executeUpdate("update userdetails set Balance = '" + f_buyerbal + "' where LoginID = '" + BuyerID + "'");
                                
                                return "tradepending";
                                
                            }
                            else
                            {
                                //not enough balance to buy
                                return "tradenotcomplete";
                            }
                        }
                        else if (squantity < rs2.getDouble(3))
                        {
                            //buying stocks greater than selling stocks
                            if(BuyerBal >= rs2.getDouble(4))
                            {    
                                //update buying orders quantity to the stocks that were left to be purchased
                                double fquant = rs2.getDouble(3) - squantity;
                                stat.executeUpdate("update buyingorders set totalshares = '" + fquant + "' where price = '" + rs2.getDouble(4) + "'"
                             + "and LoginId = '" + BuyerID + "' and totalshares = '" + rs2.getDouble(3) + "' and stockname = '" + rs2.getString(2) + "'");
                                //insert into user orders
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + LoginID +"','" + sname +"','" + squantity +"','" + sprice + "','" + type +"')");
                                //update sellers balance
                                double f_sellerbal = bal + sprice;
                                stat.executeUpdate("update userdetails set Balance = '" + f_sellerbal + "' where LoginID = '" + LoginID + "'");
                                //update buyers balance
                                double f_buyerbal = BuyerBal - rs2.getDouble(4);
                                stat.executeUpdate("update userdetails set Balance = '" + f_buyerbal + "' where LoginID = '" + BuyerID + "'");
                                
                                //update stocks most recent traded price
                                stat.executeUpdate("update stocks set recentPrice = '" + sprice + "' where name = '" + sname + "'");
                                squantity = 0;
                                return "tradecomplete";
                                //break;
                            }
                            else
                            {
                                //not enough balance to buy
                                return "tradenotcomplete";
                            }
                        }
                        else
                        {
                            //selling stocks equal to buying stocks
                            if(BuyerBal >= rs2.getDouble(4))
                            {    
                                //delete buying entry from buying table
                                stat.executeUpdate("delete from buyingorders where price = '" + rs2.getDouble(4) + "' and LoginId = '" + BuyerID + "' "
                                     + "and totalshares = '" + rs2.getDouble(3) + "' and stockname = '" + rs2.getString(2) + "'");
                                //insert into user orders
                                stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + LoginID +"','" + sname +"','" + squantity +"','" + sprice + "','" + type +"')");
                                //update sellers balance
                                double f_sellerbal = bal + sprice;
                                stat.executeUpdate("update userdetails set Balance = '" + f_sellerbal + "' where LoginID = '" + LoginID + "'");
                                //update buyers balance
                                double f_buyerbal = BuyerBal - rs2.getDouble(4);
                                stat.executeUpdate("update userdetails set Balance = '" + f_buyerbal + "' where LoginID = '" + BuyerID + "'");
                                
                                //update stocks most recent traded price
                                stat.executeUpdate("update stocks set recentPrice = '" + sprice + "' where name = '" + sname + "'");
                                squantity = 0;
                                return "tradecomplete";
                                //break;
                            }
                            else
                            {
                                //not enough balance to buy
                                return "tradenotcomplete";
                            }
                        }
                    
                    }
                    else
                    {
                        //Buyer not present in user details
                        return "tradenotcomplete";
                    }
                }
                if(squantity > 0)
                {
                    //share not available in buying orders
                    //insert into user orders
                    stat.executeUpdate("INSERT INTO myorders (`LoginID`, `name`, totalshares, price, `type`) " +
                                "VALUES ('" + LoginID +"','" + sname +"','" + squantity +"','" + sprice + "','" + type +"')");
                    
                    stat.executeUpdate("insert into sellingorders values('" + LoginID + "','" + sname + "','" + squantity + "','" + sprice + "')");
                    
                    return "tradenotcomplete";
                }
                return "";
            }
            else
            {
                //share not available
                return "tradenotcomplete";
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return "error";
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
