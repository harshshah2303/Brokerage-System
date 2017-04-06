/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlinebrokeragesystem2016;

import java.util.Scanner;

/**
 *
 * @author harshshah2303
 */
public class OnlineBrokerageSystem2016 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Scanner sc = new Scanner(System.in);
        String choice = "";
        
        while(!choice.equals("x"))
        {
            System.out.println("");
            System.out.println("Please make your selection");
            System.out.println("1: Sign Up if you are a new user");
            System.out.println("2: Login to your account");
            System.out.println("3: Forgot Password");
            System.out.println("x: Exit the brokerage system");
            
            choice = sc.next();
            if(choice.equals("1"))
            {
                //Sign Up
                NewAccNo.createNewAccount();
            }
            else if(choice .equals("2"))
            {
                //Login
                OnlineSystem.Login();
            }
            else if(choice.equals("3"))
            {
                //password retrieval
                PassRecovery.ForgotPassword();
            }
            else if(choice.equals("x"))
            {
                //End
                ;
            }
                
        }
    }
    
}
