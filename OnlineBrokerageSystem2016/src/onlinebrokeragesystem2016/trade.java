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
public class trade {
    
   public static void dotrade()
   {
       System.out.println("Welcome to the TRADE Section");
       System.out.println();
       Scanner sc = new Scanner(System.in);
       String choice = "";
       
       while(!choice.equals("x"))
       {
           System.out.println("Please make a selection ");
           System.out.println("1. Limit Order");
           System.out.println("2. Market Order");
           System.out.println("x. Exit Trade");
           choice = sc.next();
           
           if(choice.equals("1"))
           {
               //limitorder
               limitorder();
           }
           else if(choice.equals("2"))
           {
               //marketorder
               marketorder();
           }
           else if(choice.equals("x"))
           {
               //exit
               ;
           }
       }
       
   }
   
   public static void limitorder()
   {
       System.out.println("Welcome to the LIMIT ORDER Section");
       System.out.println();
       Scanner sc = new Scanner(System.in);
       String choice = "";
       
       while(!choice.equals("x"))
       {
           System.out.println("Please make a selection ");
           System.out.println("1. Buy Stocks");
           System.out.println("2. Sell Stocks");
           System.out.println("x. Exit Limit Order");
           choice = sc.next();
           
           if(choice.equals("1"))
           {
               //buystocks in limit order
               limitorder.buy();
           }
           else if(choice.equals("2"))
           {
               //sellstocks in limit order
               limitorder.sell();
           }
           else if(choice.equals("x"))
           {
               //exit
               ;
           }
       }
   }
   
   public static void marketorder()
   {
       System.out.println("Welcome to the MARKET ORDER Section");
       System.out.println();
       Scanner sc = new Scanner(System.in);
       String choice = "";
       
       while(!choice.equals("x"))
       {
           System.out.println("Please make a selection ");
           System.out.println("1. Buy Stocks");
           System.out.println("2. Sell Stocks");
           System.out.println("x. Exit Market Order");
           choice = sc.next();
           
           if(choice.equals("1"))
           {
               //buystocks in market order
               marketorder.buy();
           }
           else if(choice.equals("2"))
           {
               //sellstocks in market order
               marketorder.sell();
           }
           else if(choice.equals("x"))
           {
               //exit
               ;
           }
       }
   }
    
}
