/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;

/**
 *
 * @author Admin
 */
public class Ban implements Serializable {
   private  String maban,tenban,soghe, trangthai;
   public Ban(){}
   public Ban(String maban, String tenban, String soghe, String trangthai){
       this.maban= maban;
       this.tenban= tenban;
       this.soghe=soghe;
       this.trangthai=trangthai;
   }

    public String getMaban() {
        return maban;
    }

    public String getTenban() {
        return tenban;
    }

    public String getSoghe() {
        return soghe;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setMaban(String maban) {
        this.maban = maban;
    }

    public void setTenban(String tenban) {
        this.tenban = tenban;
    }

    public void setSoghe(String soghe) {
        this.soghe = soghe;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }
   
}
