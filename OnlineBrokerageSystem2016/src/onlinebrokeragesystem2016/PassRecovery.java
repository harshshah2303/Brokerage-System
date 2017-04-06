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
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author harshshah2303
 */
public class PassRecovery {
    
    public static void ForgotPassword()
    {
        Scanner sc = new Scanner(System.in);
        
        String ans1 = "";
        String ans2 = "";
        String id = "";
        System.out.println("Forgot Password!!!");
        System.out.println();
        System.out.println("Please enter your user ID");
        id = sc.next();
        
        Random rand = new Random();
        int r = rand.nextInt(2) + 0;    // will generate 2 numbers at random i.e 0 and 1 nextInt(how many numbers) + min. start from min to the count of numbers required
         if(r == 0)
        {
            System.out.println("Security Question 1: What is your favorite color");
            ans1 = sc.next();
        }
        if(r == 1)
        {
            System.out.println("Security Question 2: What is your favorite team name");
            ans2 = sc.next();
        }
        
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        try
        {
           conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
           stat = conn.createStatement();
           
           rs = stat.executeQuery("Select * from userdetails where LoginID = '" + id + "'");
           if(rs.next())
           {
               if((ans1.equals(rs.getString(7))) || (ans2.equals(rs.getString(8))))
               {
                   System.out.println("Your password is " + rs.getString(5));
                   System.out.println();
                   OnlineSystem.Login();
               }
               else
               {
                   System.out.println("Your answer is incorrect ***");
               }
           }
           else
           {
               System.out.println("Your user ID does not exist");
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
