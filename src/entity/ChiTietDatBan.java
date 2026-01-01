package entity;

import java.text.NumberFormat;
import java.util.Locale;

public class ChiTietDatBan {
    private String maCTDatBan; // PK
    private String maDatBan;   // FK
    private String maMenu;     // FK
    
    private int soLuong;       
    private double donGia;     

    // Thuộc tính hiển thị (Không nằm trong bảng ChiTietDatBan nhưng cần lấy từ bảng Menu)
    private String tenMon; 

    public ChiTietDatBan() {
    }

    // Constructor 1: Dùng để load danh sách MENU lên JList (Chưa có mã đặt bàn, chưa có số lượng)
    public ChiTietDatBan(String maMenu, String tenMon, double donGia) {
        this.maMenu = maMenu;
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.soLuong = 0; // Mặc định
    }

    // Constructor 2: Đầy đủ (Dùng khi lưu vào CSDL hoặc đọc chi tiết đơn hàng)
    public ChiTietDatBan(String maCTDatBan, String maDatBan, String maMenu, String tenMon, int soLuong, double donGia) {
        this.maCTDatBan = maCTDatBan;
        this.maDatBan = maDatBan;
        this.maMenu = maMenu;
        this.tenMon = tenMon; 
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // --- Getters và Setters ---
    public String getMaCTDatBan() { return maCTDatBan; }
    public void setMaCTDatBan(String maCTDatBan) { this.maCTDatBan = maCTDatBan; }

    public String getMaDatBan() { return maDatBan; }
    public void setMaDatBan(String maDatBan) { this.maDatBan = maDatBan; }

    public String getMaMenu() { return maMenu; }
    public void setMaMenu(String maMenu) { this.maMenu = maMenu; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGia() { return donGia; }
    public void setDonGia(double donGia) { this.donGia = donGia; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    // Tính thành tiền
    public double getThanhTien() {
        return this.soLuong * this.donGia;
    }

    // --- QUAN TRỌNG: Hiển thị lên JList ---
    @Override
    public String toString() {
        // Định dạng tiền tệ kiểu Việt Nam để hiển thị đẹp trên List
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        // Kết quả hiển thị ví dụ: "Gà Rán (35.000 ₫)"
        return tenMon + " (" + currencyFormat.format(donGia) + ")";
    }
}