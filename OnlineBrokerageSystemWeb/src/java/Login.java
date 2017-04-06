/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.*;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

/**
 *
 * @author harshshah2303
 */
@Named(value = "login")
@ManagedBean
@SessionScoped
public class Login implements Serializable {
    
    public static String loginID;
    public static double balance1;
    private String id;
    private String password;
    private double balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public String login()
    {
        loginID = id;
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
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
        
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from userdetails where LoginID = '" + id + "'");
            if(rs.next())
            {
                balance = rs.getDouble(6);
                balance1 = balance;
                if(password.equals(rs.getString(5)))
                {
                    return "welcome";
                }
                else
                {
                   // System.out.println("Your Password is incorrect!!!");
                    password = "";
                    return "passAgain";
                }
            }
            else
            {
                id = "";
                password = "";
                return "loginNotOk";
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
    
    public String loginAgain()
    {
        loginID = id;
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
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/shahh6626";
        
        try
        {
            conn = DriverManager.getConnection(DB_URL,"shahh6626","1453730");
            stat = conn.createStatement();
            rs = stat.executeQuery("Select * from userdetails where LoginID = '" + id + "'");
            if(rs.next())
            {
                balance = rs.getDouble(6);
                balance1 = balance;
                if(password.equals(rs.getString(5)))
                {
                    return "welcome";
                }
                else
                {
                   //second time password incorrect
                    password = "";
                    return "loginNotOk";
                }
            }
            else
            {
                id = "";
                password = "";
                return "loginNotOk";
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
    
    public String logout()
    {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
    
    
    
    
    
}
