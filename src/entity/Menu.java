package entity;

public class Menu {
    private String maMenu;
    private String tenMon;
    private double donGia;
    private String loaiMon;
    private String hinhAnh;

    public Menu() {
    }

    public Menu(String maMenu, String tenMon, double donGia, String loaiMon, String hinhAnh) {
        this.maMenu = maMenu;
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.loaiMon = loaiMon;
        this.hinhAnh = hinhAnh;
    }

    public String getMaMenu() {
        return maMenu;
    }

    public void setMaMenu(String maMenu) {
        this.maMenu = maMenu;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public String getLoaiMon() {
        return loaiMon;
    }

    public void setLoaiMon(String loaiMon) {
        this.loaiMon = loaiMon;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return tenMon;
    }
}
