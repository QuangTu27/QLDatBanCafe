package DAO;

import connect.MyConnection; // Import file kết nối của bạn
import entity.Menu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuDao {
    
    // Hàm lấy danh sách Menu từ SQL Server
    public List<Menu> getListMenu() {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_Menu"; // Tên bảng trong SQL của bạn

        // 1. Khởi tạo đối tượng kết nối theo đúng class của bạn
        MyConnection myConn = new MyConnection(); 
        Connection conn = myConn.getInstance(); // Gọi hàm getInstance()

        if (conn == null) {
            System.out.println("Kết nối thất bại!");
            return list;
        }

        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Đọc dữ liệu từ cột SQL
                String ma = rs.getString("MaMenu");
                String ten = rs.getString("TenMon");
                double gia = rs.getDouble("DonGia");
                // Cột LoaiMon có thể null, nên cần xử lý cẩn thận hoặc để mặc định
                String loai = rs.getString("LoaiMon"); 

                // Tạo đối tượng Menu
                Menu m = new Menu(ma, ten, gia, loai);
                list.add(m);
            }
            
            // Đóng kết nối sau khi dùng xong (Optional, tùy vào logic quản lý kết nối của bạn)
            // myConn.closeConnection(); 

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return list;
    }
}