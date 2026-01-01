package DAO;

import entity.ChiTietDatBan;
import connect.MyConnection; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietDatBanDAO {

    // 1. LẤY MENU
    public List<ChiTietDatBan> getDanhSachMenu() {
        List<ChiTietDatBan> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            MyConnection myCon = new MyConnection();
            conn = myCon.getInstance();
            
            String sql = "SELECT * FROM tbl_Menu"; 
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String maMenu = rs.getString("MaMenu");
                String tenMon = rs.getString("TenMon");
                double donGia = rs.getDouble("DonGia");
                list.add(new ChiTietDatBan(maMenu, tenMon, donGia));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return list;
    }

    // 2. LẤY CHI TIẾT THEO MÃ BÀN (Đã sửa thành LEFT JOIN)
    public List<ChiTietDatBan> getChiTietByMaDatBan(String maDatBan) {
        List<ChiTietDatBan> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            MyConnection myCon = new MyConnection();
            conn = myCon.getInstance();
            
            // --- SỬA Ở ĐÂY: Đổi JOIN thành LEFT JOIN ---
            String sql = "SELECT ct.MaCTDatBan, ct.MaDatBan, ct.MaMenu, ct.SoLuong, ct.DonGia, m.TenMon " +
                         "FROM tbl_ChiTietDatBan ct " +
                         "LEFT JOIN tbl_Menu m ON ct.MaMenu = m.MaMenu " + // <--- LEFT JOIN lấy tất cả chi tiết
                         "WHERE ct.MaDatBan = ?";
                         
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, maDatBan);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String maCT = rs.getString("MaCTDatBan");
                String maDB = rs.getString("MaDatBan");
                String maMenu = rs.getString("MaMenu");
                int soLuong = rs.getInt("SoLuong");
                double donGia = rs.getDouble("DonGia");
                
                // Kiểm tra null nếu món ăn đã bị xóa khỏi menu
                String tenMon = rs.getString("TenMon");
                if (tenMon == null) {
                    tenMon = "Món không xác định (" + maMenu + ")";
                }

                list.add(new ChiTietDatBan(maCT, maDB, maMenu, tenMon, soLuong, donGia));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return list;
    }

    // 3. THÊM MÓN
    public boolean insertChiTiet(ChiTietDatBan ct) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int n = 0;
        try {
            MyConnection myCon = new MyConnection();
            conn = myCon.getInstance();
            
            String sql = "INSERT INTO tbl_ChiTietDatBan (MaCTDatBan, MaDatBan, MaMenu, SoLuong, DonGia) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, ct.getMaCTDatBan());
            stmt.setString(2, ct.getMaDatBan());
            stmt.setString(3, ct.getMaMenu());
            stmt.setInt(4, ct.getSoLuong());
            stmt.setDouble(5, ct.getDonGia());
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, null);
        }
        return n > 0;
    }
    
    // 4. XÓA MÓN
    public boolean deleteChiTiet(String maCTDatBan) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int n = 0;
        try {
            MyConnection myCon = new MyConnection();
            conn = myCon.getInstance();
            
            String sql = "DELETE FROM tbl_ChiTietDatBan WHERE MaCTDatBan = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, maCTDatBan);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, null);
        }
        return n > 0;
    }

    // 5. CẬP NHẬT SỐ LƯỢNG
    public boolean updateSoLuong(String maCT, int soLuongMoi) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int n = 0;
        try {
            MyConnection myCon = new MyConnection();
            conn = myCon.getInstance();
            
            String sql = "UPDATE tbl_ChiTietDatBan SET SoLuong = ? WHERE MaCTDatBan = ?";
            stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, soLuongMoi);
            stmt.setString(2, maCT);
            
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, null);
        }
        return n > 0;
    }

    private void closeResources(PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String taoMaCTMoi() {
        String maMoi = "CT001"; // Mặc định nếu bảng trống
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            MyConnection myCon = new MyConnection();
            conn = myCon.getInstance();
            
            // Lấy mã lớn nhất hiện có trong toàn bộ bảng tbl_ChiTietDatBan
            String sql = "SELECT MAX(MaCTDatBan) FROM tbl_ChiTietDatBan";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String maxMa = rs.getString(1);
                if (maxMa != null) {
                    // maxMa dạng "CT005" -> lấy phần số là 5
                    // Dùng try-catch để tránh lỗi nếu dữ liệu cũ không đúng định dạng
                    try {
                        int so = Integer.parseInt(maxMa.substring(2));
                        // Tăng lên 1 -> 6 -> format thành "CT006"
                        maMoi = String.format("CT%03d", so + 1);
                    } catch (NumberFormatException e) {
                        // Nếu mã cũ ko đúng chuẩn CTxxx thì tạo ngẫu nhiên để tránh lỗi
                        maMoi = "CT" + System.currentTimeMillis() % 10000;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return maMoi;
    }
    // 6. LẤY DANH SÁCH CÁC MÃ ĐẶT BÀN ĐANG CÓ MÓN (Để tô màu bàn)
    public List<String> getDanhSachMaDatBanCoMon() {
        List<String> listMa = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            MyConnection myCon = new MyConnection();
            conn = myCon.getInstance();
            
            // Lấy duy nhất (DISTINCT) các mã đặt bàn đang tồn tại trong bảng chi tiết
            String sql = "SELECT DISTINCT MaDatBan FROM tbl_ChiTietDatBan";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                listMa.add(rs.getString("MaDatBan"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, rs);
        }
        return listMa;
    }
}