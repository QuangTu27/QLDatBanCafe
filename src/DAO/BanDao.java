package DAO;

import java.sql.*;
import connect.MyConnection;
import entity.Ban;
import java.util.List;
import java.util.ArrayList;

public class BanDao {
private connect.MyConnection myConn = new connect.MyConnection();
    // Hàm lấy danh sách: Đã thêm .trim() để xử lý CHAR(10)
    public List<Ban> selectAll() {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_Ban";
        Connection conn = new MyConnection().getInstance();
        
        if (conn == null) {
            System.out.println("LỖI: Không thể kết nối Database!");
            return list;
        }
        
        try (PreparedStatement ps = conn.prepareStatement(sql); 
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Ban(
                    rs.getString("MaBan").trim(), 
                    rs.getString("TenBan"),
                    rs.getInt("SoGhe"),
                    rs.getString("TrangThai")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // MỚI: Hàm kiểm tra trùng mã trước khi Insert để tránh lỗi
    public boolean checkDuplicate(String maBan) {
        String sql = "SELECT COUNT(*) FROM tbl_Ban WHERE MaBan = ?";
        try (Connection conn = new MyConnection().getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insert(Ban b) {
        // Kiểm tra trùng trước khi thực hiện lệnh SQL
        if (checkDuplicate(b.getMaBan())) {
            System.out.println("Lỗi: Mã bàn " + b.getMaBan() + " đã tồn tại!");
            return false;
        }

        String sql = "INSERT INTO tbl_Ban (MaBan, TenBan, SoGhe, TrangThai) VALUES (?, ?, ?, ?)";
        try (Connection conn = new MyConnection().getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getMaBan());
            ps.setString(2, b.getTenBan());
            ps.setInt(3, b.getSoGhe());
            ps.setString(4, b.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Ban b) {
        String sql = "UPDATE tbl_Ban SET TenBan=?, SoGhe=?, TrangThai=? WHERE MaBan=?";
        try (Connection conn = new MyConnection().getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, b.getTenBan());
            ps.setInt(2, b.getSoGhe());
            ps.setString(3, b.getTrangThai());
            ps.setString(4, b.getMaBan());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String maBan) {
        String sql = "DELETE FROM tbl_Ban WHERE MaBan=?";
        try (Connection conn = new MyConnection().getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maBan);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
   public List<Ban> getDanhSachBanTrong() {
    List<Ban> list = new ArrayList<>();
    // Dùng N'Trống' vì cột TrangThai là NVARCHAR
    String sql = "SELECT * FROM tbl_Ban WHERE TrangThai = N'Trống'"; 
    try (Connection conn = myConn.getInstance();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ban b = new Ban();
            b.setMaBan(rs.getString("MaBan"));
            b.setTenBan(rs.getString("TenBan"));
            list.add(b);
        }
    } catch (Exception e) { e.printStackTrace(); }
    return list;
}
   
}