/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinebrokeragesystem2016;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author harshshah2303
 */
public class NewAccNo {
    
    static final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
    
    public static void createNewAccount()
    {
        String accnum = "";
        int ssn = 0;
        String name = "";
        String userID = "";
        String pass = "";
        double balance = 0.0;
        String ans  = "";
        String ans1 = "";
        String ans2 = "";
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Please enter your userID");
        userID = sc.next();
        
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            
            String sql = "Select * from userdetails where LoginID = '" + userID + "'";
            
            rs = stat.executeQuery(sql);
            if(rs.next())       //To check if user id already exists
            {
                System.out.println("Please enter some other userID.This ID already exists!!!");
            }
            else
            {
                System.out.println("Please enter your SSN");
                ssn = sc.nextInt();
                sc.nextLine();
                System.out.println("Please enter your Full Name");
                name =  sc.nextLine();
                System.out.println("Do you want to add some initial balance.Enter Y for yes and N for no");
                ans = sc.next();
                if("Y".equalsIgnoreCase(ans))
                {
                    System.out.println("Enter balance");
                    balance = sc.nextDouble();
                }
                
                System.out.println("Please enter your password of not more than 15 characters");
                pass = sc.next();
                System.out.println("Security Question 1: What is your favorite color");
                ans1 = sc.next();
                sc.nextLine();
                System.out.println("Security Question 2: What is your favorite team name");
                ans2 = sc.nextLine();
                
                Connection conn2 = null;
                Statement stat2 = null;
                ResultSet rs2 = null;
                
                try
                {
                    conn2 = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
                    stat2 = conn2.createStatement();
                    rs2 = stat.executeQuery("Select * from accountnumber");
                    int nxtnum = 0;
                    while(rs2.next())
                    {
                        accnum = "" + rs2.getInt(1);        //get current acc no
                        nxtnum = rs2.getInt(1) + 1;         //add 1 to that i.e the next acc no
                    }
                    
                    int t = stat2.executeUpdate("Update accountnumber set AccNo = '" + nxtnum + "'" );          //update acc no to next acc no
                    
                    //insert all the values in the user details table
                    int r = stat2.executeUpdate("insert into userdetails values ('" + accnum + "', '" + ssn 
                            + "','" + name + "','" + userID + "','" + pass + "','" + balance + "','" + ans1 + "','" + ans2 + "' )");
                    
                    System.out.println("Congrats!!! Your new bank account is created!");
                    System.out.println("Your account number is " + accnum + "!");
                    System.out.println("Your initial balance is " +  balance);
                }
                catch(SQLException e)
                {
                    System.out.println("Account creation failed");
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {
                        conn2.close();
                        stat2.close();
                        rs2.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
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
