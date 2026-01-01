/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Admin
 */
public class Ban {
    private String maBan;    
    private String tenBan;    
    private int soGhe;     
    private String trangThai;

    public Ban() {
    }

    public Ban(String maBan, String tenBan, int soGhe, String trangThai) {
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.soGhe = soGhe;
        this.trangThai = trangThai;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public int getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(int soGhe) {
        this.soGhe = soGhe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "Ban{" + "maBan=" + maBan + ", tenBan=" + tenBan + ", soGhe=" + soGhe + ", trangThai=" + trangThai + '}';
    }
}
