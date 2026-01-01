package entity; 

public class Menu {
    // Các thuộc tính cơ bản của một món ăn
    private String maMenu;      // Mã món (ví dụ: MN001) - Khóa chính
    private String tenMon;      // Tên món (ví dụ: Cà phê đá)
    private double donGia;      // Đơn giá niêm yết
    private String donViTinh;   // Đơn vị tính (Ly, Chai, Dĩa...) - Tùy chọn

    // 1. Constructor không tham số (Bắt buộc để tránh lỗi khi dùng Framework sau này)
    public Menu() {
    }

    // 2. Constructor đầy đủ tham số
    public Menu(String maMenu, String tenMon, double donGia, String donViTinh) {
        this.maMenu = maMenu;
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.donViTinh = donViTinh;
    }

    // Constructor rút gọn (nếu không cần Đơn vị tính)
    public Menu(String maMenu, String tenMon, double donGia) {
        this.maMenu = maMenu;
        this.tenMon = tenMon;
        this.donGia = donGia;
        this.donViTinh = "Ly"; // Mặc định
    }

    // 3. Getter và Setter
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

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    // 4. Ghi đè toString()
    // Rất quan trọng: Giúp JList hoặc ComboBox hiển thị tên món thay vì mã hash
    @Override
    public String toString() {
        return tenMon; 
    }
    
    // Nếu bạn muốn hiển thị kiểu "MN001 - Cà phê", hãy dùng code dưới:
    /*
    @Override
    public String toString() {
        return maMenu + " - " + tenMon;
    }
    */
}