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
public class OnlineSystem {
    
    private static String id = ""; 
    private static double balance = 0.0;
    
    public static void OnlineSystem(String loginID,double bal)
    {
        id = loginID;
        balance = bal;
    }
    
    public static void Login()
    {
        Scanner sc = new Scanner(System.in);
        String pass = "";
        System.out.println("Login to your Brokerage System!!!");
        System.out.println();
        System.out.println("Please enter your user ID");
        id = sc.next();
        System.out.println("Please enter your password");
        pass = sc.next();
        int countpass = 0;
        
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
        
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from userdetails where LoginID = '" + id + "'");
            if(rs.next())
            {
                balance = rs.getDouble(6);
                if(pass.equals(rs.getString(5)))
                {
                    welcome();
                }
                else
                {
                    System.out.println("Your Password is incorrect***");
                    countpass++;
                    System.out.println("Please enter correct password");
                    String pass2 = sc.next();
                    if(pass2.equals(rs.getString(5)))
                    {
                        welcome();
                    }
                    else
                    {
                        countpass++;
                        //password wroong consecutively 2 times go to forgot password
                        if(countpass == 2)
                        {
                            System.out.println("You entered the incorrect password twice!!!Your Account is LOCKED ***");
                            PassRecovery.ForgotPassword();
                        }
                    }
                }
            }
            else
            {
                System.out.println("Your user ID does not exist***");
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
    
    public static void welcome()
    {
        Scanner sc = new Scanner(System.in);
        String select = "";
        
        System.out.println("***Welcome to your Brokerage System!!!Please make a selection***");
        System.out.println();
        
        while(!select.equals("x"))
        {
            System.out.println("1: Trade");
            System.out.println("2: Display favourite List");
            System.out.println("3: Search");
            System.out.println("4: Display My orders");
            System.out.println("x: Sign Out");

            select = sc.next();
            
            if(select.equals("1"))
            {
                //trade
                trade.dotrade();
            }
            else if(select.equals("2"))
            {
                //display watched list
                watchedList.updatestocks();
                watchedList.favstocks();
                
            }
            else if(select.equals("3"))
            {
                //search
                watchedList.updatestocks();
                search.searchsym();
                
            }
            else if(select.equals("4"))
            {
                //Display orders
                mystocks.myorders();
            }
            else if(select.equals("x"))
            {
                //End
                ;
            }
            
       }
        
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        OnlineSystem.id = id;
    }

    public static double getBalance() {
        return balance;
    }

    public static void setBalance(double balance) {
        OnlineSystem.balance = balance;
    }
    
    
}
