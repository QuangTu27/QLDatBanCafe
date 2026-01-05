package DAO;

import connect.MyConnection;
import entity.DatBan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatBanDao {

    private MyConnection myConn = new MyConnection();

    // 1. Thêm mới Đặt Bàn (Bao gồm Tên KH, SĐT, Giờ hẹn)
    public boolean insertDatBan(String tenKH, String sdt, String gioHen, String maBan) {
        Connection conn = myConn.getInstance();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false); // Bắt đầu giao dịch (Transaction)

            // Tự sinh mã KH và mã DB bằng timestamp (đảm bảo CHAR(10))
            String suffix = String.valueOf(System.currentTimeMillis()).substring(5);
            String maKH = "KH" + suffix;
            String maDB = "DB" + suffix;

            // Bước 1: Thêm khách hàng mới
            String sqlKH = "INSERT INTO tbl_KhachHang (MaKhachHang, TenKhachHang, SoDienThoai) VALUES (?, ?, ?)";
            try (PreparedStatement psKH = conn.prepareStatement(sqlKH)) {
                psKH.setString(1, maKH);
                psKH.setNString(2, tenKH);
                psKH.setString(3, sdt);
                psKH.executeUpdate();
            }

            // Bước 2: Thêm đơn đặt bàn với cột ThoiGianHen
            String sqlDB = "INSERT INTO tbl_DatBan (MaDatBan, MaKhachHang, MaBan, ThoiGianHen, TrangThai) VALUES (?, ?, ?, ?, N'Đã đặt')";
            try (PreparedStatement psDB = conn.prepareStatement(sqlDB)) {
                psDB.setString(1, maDB);
                psDB.setString(2, maKH);
                psDB.setString(3, maBan);
                psDB.setString(4, gioHen); // Định dạng: yyyy-MM-dd HH:mm:ss
                psDB.executeUpdate();
            }

            // Bước 3: Cập nhật trạng thái bàn sang 'Đã đặt'
            String sqlBan = "UPDATE tbl_Ban SET TrangThai = N'Đã đặt' WHERE MaBan = ?";
            try (PreparedStatement psBan = conn.prepareStatement(sqlBan)) {
                psBan.setString(1, maBan);
                psBan.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println("Lỗi insertDatBan: " + e.getMessage());
            return false;
        } finally {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    // 2. Lấy toàn bộ danh sách (Sử dụng JOIN để lấy Tên, SĐT và cột ThoiGianHen)
    public List<DatBan> selectAll() {
        List<DatBan> list = new ArrayList<>();
        String sql = "SELECT db.MaDatBan, kh.TenKhachHang, kh.SoDienThoai, db.MaBan, " +
                     "db.ThoiGianHen, db.ThoiGianBatDau, db.TrangThai " +
                     "FROM tbl_DatBan db " +
                     "INNER JOIN tbl_KhachHang kh ON db.MaKhachHang = kh.MaKhachHang";

        try (Connection conn = myConn.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DatBan db = new DatBan();
                db.setMaDatBan(rs.getString("MaDatBan"));
                db.setTenKhachHang(rs.getString("TenKhachHang"));
                db.setSoDienThoai(rs.getString("SoDienThoai"));
                db.setMaBan(rs.getString("MaBan"));
                
                // Xử lý an toàn cho ThoiGianHen (Giờ khách hẹn)
                db.setThoiGianHen(rs.getString("ThoiGianHen"));
                
                // Sửa lỗi setThoiGianBatDau (Giờ khách vào ngồi thực tế)
                // Dùng getString hoặc getTimestamp().toString() để gán vào Entity kiểu String
                Timestamp batDau = rs.getTimestamp("ThoiGianBatDau");
                db.setThoiGianBatDau(batDau != null ? batDau.toString() : "");
                
                db.setTrangThai(rs.getString("TrangThai"));
                list.add(db);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Tìm kiếm theo tên khách hoặc SĐT
    public List<DatBan> searchByCustomerName(String keyword) {
        List<DatBan> list = new ArrayList<>();
        String sql = "SELECT db.MaDatBan, kh.TenKhachHang, kh.SoDienThoai, db.MaBan, " +
                     "db.ThoiGianHen, db.ThoiGianBatDau, db.TrangThai " +
                     "FROM tbl_DatBan db " +
                     "JOIN tbl_KhachHang kh ON db.MaKhachHang = kh.MaKhachHang " +
                     "WHERE kh.TenKhachHang LIKE ? OR kh.SoDienThoai LIKE ?";

        try (Connection conn = myConn.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DatBan db = new DatBan();
                    db.setMaDatBan(rs.getString("MaDatBan"));
                    db.setTenKhachHang(rs.getString("TenKhachHang"));
                    db.setSoDienThoai(rs.getString("SoDienThoai"));
                    db.setMaBan(rs.getString("MaBan"));
                    db.setThoiGianHen(rs.getString("ThoiGianHen"));
                    
                    Timestamp batDau = rs.getTimestamp("ThoiGianBatDau");
                    db.setThoiGianBatDau(batDau != null ? batDau.toString() : "");
                    
                    db.setTrangThai(rs.getString("TrangThai"));
                    list.add(db);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4. Xóa đặt bàn
    public boolean delete(String maDB) {
        String sql = "DELETE FROM tbl_DatBan WHERE MaDatBan = ?";
        try (Connection conn = myConn.getInstance();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDB);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Trong file DatBanDao.java

public boolean ketThucDatBan(String maDatBan, String maBan) {
    Connection conn = myConn.getInstance();
    if (conn == null) return false;

    String sqlUpdateDatBan = "UPDATE tbl_DatBan SET ThoiGianKetThuc = GETDATE(), TrangThai = N'Hoàn tất' WHERE MaDatBan = ?";
    String sqlUpdateBan = "UPDATE tbl_Ban SET TrangThai = N'Trống' WHERE MaBan = ?";

    try {
        conn.setAutoCommit(false); // Bắt đầu Transaction

        // 1. Cập nhật bảng đặt bàn
        try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateDatBan)) {
            ps1.setString(1, maDatBan);
            ps1.executeUpdate();
        }

        // 2. Giải phóng bàn về trạng thái Trống
        try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateBan)) {
            ps2.setString(1, maBan);
            ps2.executeUpdate();
        }

        conn.commit();
        return true;
    } catch (SQLException e) {
        try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        e.printStackTrace();
        return false;
    } finally {
        try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}
public boolean batDauPhucVu(String maDatBan, String maBan) {
    Connection conn = myConn.getInstance();
    if (conn == null) return false;

    // Cập nhật đơn đặt bàn sang 'Đang phục vụ' và ghi nhận giờ vào thực tế
    String sqlUpdateDatBan = "UPDATE tbl_DatBan SET ThoiGianBatDau = GETDATE(), TrangThai = N'Đang phục vụ' WHERE MaDatBan = ?";
    // Cập nhật bàn sang trạng thái 'Có khách'
    String sqlUpdateBan = "UPDATE tbl_Ban SET TrangThai = N'Có khách' WHERE MaBan = ?";

    try {
        conn.setAutoCommit(false); // Bắt đầu Transaction

        try (PreparedStatement ps1 = conn.prepareStatement(sqlUpdateDatBan)) {
            ps1.setString(1, maDatBan);
            ps1.executeUpdate();
        }

        try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateBan)) {
            ps2.setString(1, maBan);
            ps2.executeUpdate();
        }

        conn.commit();
        return true;
    } catch (SQLException e) {
        try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        e.printStackTrace();
        return false;
    } finally {
        try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
}
}