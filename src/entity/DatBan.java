package entity;

public class DatBan {
    private String maDatBan;
    private String tenKhachHang;
    private String soDienThoai;
    private String maBan;
    private String thoiGianHen;
    private String thoiGianBatDau; // Cột giờ vào thực tế
    private String trangThai;

    // Hàm tạo không đối số
    public DatBan() {
    }

    // Các hàm Getter và Setter (Phải viết chính xác từng chữ cái)
    public String getMaDatBan() { return maDatBan; }
    public void setMaDatBan(String maDatBan) { this.maDatBan = maDatBan; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getMaBan() { return maBan; }
    public void setMaBan(String maBan) { this.maBan = maBan; }

    public String getThoiGianHen() { return thoiGianHen; }
    public void setThoiGianHen(String thoiGianHen) { this.thoiGianHen = thoiGianHen; }

    // ĐÂY LÀ HÀM ĐANG BỊ THIẾU CỦA BẠN
    public String getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(String thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}