/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author harshshah2303
 */
public class Orders {
    
    private String Stock_name;
    private double total_shares;
    private double price;
    private String type;
    
    public Orders(String name, double total, double prce, String typ)
    {
        Stock_name = name;
        total_shares = total;
        price = prce;
        type = typ;
    }

    public String getStock_name() {
        return Stock_name;
    }

    public void setStock_name(String Stock_name) {
        this.Stock_name = Stock_name;
    }

    public double getTotal_shares() {
        return total_shares;
    }

    public void setTotal_shares(double total_shares) {
        this.total_shares = total_shares;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
  
    
}
