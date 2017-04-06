/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.sql.*;

/**
 *
 * @author harshshah2303
 */

@ManagedBean
@RequestScoped
public class registration {

    final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
    private String id;
    private String ssn;
    private String password;
    private String name;
    private double balance;
    private String security_Q_1;
    private String security_Q_2;
    private String answer1;
    private String answer2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getSecurity_Q_1() {
        return security_Q_1;
    }

    public void setSecurity_Q_1(String security_Q_1) {
        this.security_Q_1 = security_Q_1;
    }

    public String getSecurity_Q_2() {
        return security_Q_2;
    }

    public void setSecurity_Q_2(String security_Q_2) {
        this.security_Q_2 = security_Q_2;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }
    
    
    public String register()
    {
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e)
        {
            return "error";
        }
        
        Connection conn = null;
        Statement stat = null;
        ResultSet rs = null;
        
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            
            String sql = "Select * from userdetails where LoginID = '" + id + "' or SSN = '" + ssn + "'";
            
            rs = stat.executeQuery(sql);
            if(rs.next())       //To check if user id already exists
            {
                return("Please enter some other userID.This ID already exists!!!Go back to Login to your Account");
            }
            else
            {
                Connection conn2 = null;
                Statement stat2 = null;
                ResultSet rs2 = null;
                
                try
                {
                    String accnum = "";
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
                            + "','" + name + "','" + id + "','" + password + "','" + balance + "','" + answer1 + "','" + answer2 + "' )");
                    
                    return("Congrats!!! Your new bank account is created!!!"
                            + "Go back to Login to your Account");
                    
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
                       if(conn2!=null) 
                        conn2.close();
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
   
    
}
