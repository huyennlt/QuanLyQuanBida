/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entityModel;

import java.util.Date;

/**
 *
 * @author MINH DANG
 */
public class Bill {
    private int id;
    private Date datecheckin, datecheckout;
    private int idtable, status;
    private float totalPrice;

    public Bill() {
    }

    public Bill(int id, Date datecheckin, Date datecheckout, int idtable, int status, float totalPrice) {
        this.id = id;
        this.datecheckin = datecheckin;
        this.datecheckout = datecheckout;
        this.idtable = idtable;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDatecheckin() {
        return datecheckin;
    }

    public void setDatecheckin(Date datecheckin) {
        this.datecheckin = datecheckin;
    }

    public Date getDatecheckout() {
        return datecheckout;
    }

    public void setDatecheckout(Date datecheckout) {
        this.datecheckout = datecheckout;
    }

    public int getIdtable() {
        return idtable;
    }

    public void setIdtable(int idtable) {
        this.idtable = idtable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return this.status + "(" +this.datecheckin + " " +this.datecheckout+")";
    }
}
