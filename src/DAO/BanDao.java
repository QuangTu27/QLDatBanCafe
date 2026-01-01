/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import connect.MyConnection;
import entity.Ban;
import java.util.List;
import java.util.ArrayList;

public class BanDao {

    public List<Ban> selectAll() {
        List<Ban> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_Ban";
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Ban(rs.getString("MaBan"), rs.getString("TenBan"),
                                rs.getInt("SoGhe"), rs.getString("TrangThai")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public boolean insert(Ban b) {
        String sql = "INSERT INTO tbl_Ban (MaBan, TenBan, SoGhe, TrangThai) VALUES (?, ?, ?, ?)";
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, b.getMaBan());
                ps.setString(2, b.getTenBan());
                ps.setInt(3, b.getSoGhe());
                ps.setString(4, b.getTrangThai());
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean update(Ban b) {
        String sql = "UPDATE tbl_Ban SET TenBan=?, SoGhe=?, TrangThai=? WHERE MaBan=?";
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, b.getTenBan());
                ps.setInt(2, b.getSoGhe());
                ps.setString(3, b.getTrangThai());
                ps.setString(4, b.getMaBan());
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean delete(String maBan) {
        String sql = "DELETE FROM tbl_Ban WHERE MaBan=?";
        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, maBan);
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
