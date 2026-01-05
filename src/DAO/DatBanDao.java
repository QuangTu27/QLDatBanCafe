package DAO;

import connect.MyConnection;
import entity.DatBan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Admin
 */
public class DatBanDao {

    // 1. Thêm mới Đặt Bàn
    public boolean insertDatBan(String tenKH, String sdt, String gioHen, String maBan) {
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        if (conn == null) {
            return false;
        }

        try {
            conn.setAutoCommit(false);
            String suffix = String.valueOf(System.currentTimeMillis()).substring(5);
            String maKH = "KH" + suffix;
            String maDB = "DB" + suffix;

            String sqlKH = "INSERT INTO tbl_KhachHang (MaKhachHang, TenKhachHang, SoDienThoai) VALUES (?, ?, ?)";
            try (PreparedStatement psKH = conn.prepareStatement(sqlKH)) {
                psKH.setString(1, maKH);
                psKH.setNString(2, tenKH);
                psKH.setString(3, sdt);
                psKH.executeUpdate();
            }

            String sqlDB = "INSERT INTO tbl_DatBan (MaDatBan, MaKhachHang, MaBan, ThoiGianHen, TrangThai) VALUES (?, ?, ?, ?, N'Đã đặt')";
            try (PreparedStatement psDB = conn.prepareStatement(sqlDB)) {
                psDB.setString(1, maDB);
                psDB.setString(2, maKH);
                psDB.setString(3, maBan);
                psDB.setString(4, gioHen);
                psDB.executeUpdate();
            }

            String sqlBan = "UPDATE tbl_Ban SET TrangThai = N'Đã đặt' WHERE MaBan = ?";
            try (PreparedStatement psBan = conn.prepareStatement(sqlBan)) {
                psBan.setString(1, maBan);
                psBan.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            myConn.closeConnection();
        }
    }

    // 2. Lấy toàn bộ danh sách (Đã bỏ hàm phụ, viết trực tiếp)
    public List<DatBan> selectAll() {
        List<DatBan> list = new ArrayList<>();
        String sql = "SELECT db.MaDatBan, kh.TenKhachHang, kh.SoDienThoai, db.MaBan, "
                    + "db.ThoiGianHen, db.ThoiGianBatDau, db.TrangThai "
                    + "FROM tbl_DatBan db "
                    + "INNER JOIN tbl_KhachHang kh ON db.MaKhachHang = kh.MaKhachHang";

        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();

        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return list;
    }

    // 3. Tìm kiếm (Đã bỏ hàm phụ, viết trực tiếp)
    public List<DatBan> searchByCustomerName(String keyword) {
        List<DatBan> list = new ArrayList<>();
        String sql = "SELECT db.MaDatBan, kh.TenKhachHang, kh.SoDienThoai, db.MaBan, "
                    + "db.ThoiGianHen, db.ThoiGianBatDau, db.TrangThai "
                    + "FROM tbl_DatBan db "
                    + "JOIN tbl_KhachHang kh ON db.MaKhachHang = kh.MaKhachHang "
                    + "WHERE kh.TenKhachHang LIKE ? OR kh.SoDienThoai LIKE ?";

        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();

        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
            } finally {
                myConn.closeConnection();
            }
        }
        return list;
    }

    // 4. Xóa đặt bàn
    public boolean delete(String maDB, String maBan) {
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        if (conn == null) {
            return false;
        }

        // 1. Xóa các món ăn đã gọi (Chi tiết) để dọn sạch bàn
        String sqlDeleteFood = "DELETE FROM tbl_ChiTietDatBan WHERE MaDatBan = ?";
        // 2. Xóa đơn đặt bàn
        String sqlDeleteBooking = "DELETE FROM tbl_DatBan WHERE MaDatBan = ?";
        // 3. Đưa bàn về trạng thái trống hoàn toàn
        String sqlUpdateBan = "UPDATE tbl_Ban SET TrangThai = N'Trống' WHERE MaBan = ?";

        try {
            conn.setAutoCommit(false); // Bắt đầu giao dịch an toàn

            // Thực hiện xóa món ăn trước (Xóa con trước)
            try (PreparedStatement ps1 = conn.prepareStatement(sqlDeleteFood)) {
                ps1.setString(1, maDB);
                ps1.executeUpdate();
            }

            // Sau đó mới xóa đơn đặt bàn (Xóa cha)
            try (PreparedStatement ps2 = conn.prepareStatement(sqlDeleteBooking)) {
                ps2.setString(1, maDB);
                ps2.executeUpdate();
            }

            // Cuối cùng giải phóng bàn
            try (PreparedStatement ps3 = conn.prepareStatement(sqlUpdateBan)) {
                ps3.setString(1, maBan);
                ps3.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            myConn.closeConnection();
        }
    }

    // 5. Kết thúc đặt bàn
    public boolean ketThucDatBan(String maDatBan, String maBan) {
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        if (conn == null) {
            return false;
        }

        String sqlDB = "UPDATE tbl_DatBan SET ThoiGianKetThuc = GETDATE(), TrangThai = N'Hoàn tất' WHERE MaDatBan = ?";
        String sqlBan = "UPDATE tbl_Ban SET TrangThai = N'Trống' WHERE MaBan = ?";

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlDB)) {
                ps1.setString(1, maDatBan);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlBan)) {
                ps2.setString(1, maBan);
                ps2.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            myConn.closeConnection();
        }
    }

    // 6. Bắt đầu phục vụ
    public boolean batDauPhucVu(String maDatBan, String maBan) {
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        if (conn == null) {
            return false;
        }

        String sqlDB = "UPDATE tbl_DatBan SET ThoiGianBatDau = GETDATE(), TrangThai = N'Đang phục vụ' WHERE MaDatBan = ?";
        String sqlBan = "UPDATE tbl_Ban SET TrangThai = N'Có khách' WHERE MaBan = ?";

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlDB)) {
                ps1.setString(1, maDatBan);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement(sqlBan)) {
                ps2.setString(1, maBan);
                ps2.executeUpdate();
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            myConn.closeConnection();
        }
    }
}
