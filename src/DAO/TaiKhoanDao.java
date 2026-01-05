/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import connect.MyConnection;
import entity.TaiKhoan;
import java.util.List;
import java.util.ArrayList;


public class TaiKhoanDao {
    public List<TaiKhoan> selectAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_TaiKhoan";
        
        MyConnection myConn = new MyConnection(); 
        Connection conn = myConn.getInstance();
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaTK(rs.getString("MaTK"));
                    tk.setTenDangNhap(rs.getString("TenDangNhap"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setTenHienThi(rs.getString("TenHienThi"));
                    tk.setPhanQuyen(rs.getInt("PhanQuyen"));
                    list.add(tk);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return list;
    }

    public boolean insert(TaiKhoan tk) {
        String sql = "INSERT INTO tbl_TaiKhoan (MaTK, TenDangNhap, MatKhau, TenHienThi, PhanQuyen) VALUES (?, ?, ?, ?, ?)";
        
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tk.getMaTK());
                ps.setString(2, tk.getTenDangNhap());
                ps.setString(3, tk.getMatKhau());
                ps.setString(4, tk.getTenHienThi());
                ps.setInt(5, tk.getPhanQuyen());
                
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return false;
    }

    // Cập nhật thông tin
    public boolean update(TaiKhoan tk) {
        String sql = "UPDATE tbl_TaiKhoan SET MatKhau=?, TenHienThi=?, PhanQuyen=? WHERE MaTK=?";
        
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, tk.getMatKhau());
                ps.setString(2, tk.getTenHienThi());
                ps.setInt(3, tk.getPhanQuyen());
                ps.setString(4, tk.getMaTK());
                
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return false;
    }

    // Xóa tài khoản
    public boolean delete(String maTK) {
        String sql = "DELETE FROM tbl_TaiKhoan WHERE MaTK=?";
        
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maTK);
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return false;
    }
    
    // Tìm kiếm theo Username 
    public TaiKhoan selectByUsername(String username) {
        String sql = "SELECT * FROM tbl_TaiKhoan WHERE TenDangNhap=?";
        
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    TaiKhoan tk = new TaiKhoan();
                    tk.setMaTK(rs.getString("MaTK"));
                    tk.setTenDangNhap(rs.getString("TenDangNhap"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setTenHienThi(rs.getString("TenHienThi"));
                    tk.setPhanQuyen(rs.getInt("PhanQuyen"));
                    return tk;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return null;
    }
}
