/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entityModel;

/**
 *
 * @author MINH DANG
 */
public class Menu {
    private String foodname;
    private int count;
    private float price, totalPrice;

    public Menu() {
    }

    public Menu(String foodname, int count, float price, float totalPrice) {
        this.foodname = foodname;
        this.count = count;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return this.foodname+" --- "+this.count+" --- "+this.price+" --- "+this.totalPrice+"\n";
    }    
}
