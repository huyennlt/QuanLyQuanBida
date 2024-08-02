/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entityModel;

/**
 *
 * @author MINH DANG
 */
public class Customer {
    private String idcustomer, name, gender, phonenumber, daycheckin, idcategorycustomer;

    public Customer() {
    }

    public Customer(String idcustomer, String name, String gender, String phonenumber, String daycheckin, String idcategorycustomer) {
        this.idcustomer = idcustomer;
        this.name = name;
        this.gender = gender;
        this.phonenumber = phonenumber;
        this.daycheckin = daycheckin;
        this.idcategorycustomer = idcategorycustomer;
    }

    public String getIdcustomer() {
        return idcustomer;
    }

    public void setIdcustomer(String idcustomer) {
        this.idcustomer = idcustomer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getDaycheckin() {
        return daycheckin;
    }

    public void setDaycheckin(String daycheckin) {
        this.daycheckin = daycheckin;
    }

    public String getIdcategorycustomer() {
        return idcategorycustomer;
    }

    public void setIdcategorycustomer(String idcategorycustomer) {
        this.idcategorycustomer = idcategorycustomer;
    }

    @Override
    public String toString() {
        return this.name;
    }   
}
