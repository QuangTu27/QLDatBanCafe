/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import connect.MyConnection;
import entity.KhachHang;
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author Admin
 */
public class KhachHangDao {
    public List<KhachHang> selectAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_KhachHang";
        
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKhachHang(rs.getString("MaKhachHang"));
                    kh.setTenKhachHang(rs.getString("TenKhachHang"));
                    kh.setSoDienThoai(rs.getString("SoDienThoai"));
                    kh.setEmail(rs.getString("Email"));
                    list.add(kh);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return list;
    }

    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO tbl_KhachHang (MaKhachHang, TenKhachHang, SoDienThoai, Email) VALUES (?, ?, ?, ?)";
        
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, kh.getMaKhachHang());
                ps.setString(2, kh.getTenKhachHang());
                ps.setString(3, kh.getSoDienThoai());
                ps.setString(4, kh.getEmail());
                
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return false;
    }

    public boolean update(KhachHang kh) {
        String sql = "UPDATE tbl_KhachHang SET TenKhachHang=?, SoDienThoai=?, Email=? WHERE MaKhachHang=?";
        
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, kh.getTenKhachHang());
                ps.setString(2, kh.getSoDienThoai());
                ps.setString(3, kh.getEmail());
                ps.setString(4, kh.getMaKhachHang());
                
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return false;
    }

    public boolean delete(String maKH) {
        String sql = "DELETE FROM tbl_KhachHang WHERE MaKhachHang=?";
        
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maKH);
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return false;
    }
    
    public List<KhachHang> search(String keyword) {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_KhachHang WHERE TenKhachHang LIKE ? OR SoDienThoai LIKE ?";
        
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                String key = "%" + keyword + "%";
                ps.setString(1, key);
                ps.setString(2, key);
                
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKhachHang(rs.getString("MaKhachHang"));
                    kh.setTenKhachHang(rs.getString("TenKhachHang"));
                    kh.setSoDienThoai(rs.getString("SoDienThoai"));
                    kh.setEmail(rs.getString("Email"));
                    list.add(kh);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return list;
    }
}
