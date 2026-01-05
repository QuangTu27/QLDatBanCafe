package DAO;

import connect.MyConnection;
import entity.Menu;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuDao {
    public List<Menu> selectAll() {
        List<Menu> list = new ArrayList<>();
        String sql = "SELECT * FROM tbl_Menu";

        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();

        if (conn != null) {
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    Menu m = new Menu();
                    m.setMaMenu(rs.getString("MaMenu"));
                    m.setTenMon(rs.getString("TenMon"));
                    m.setDonGia(rs.getDouble("DonGia"));
                    m.setLoaiMon(rs.getString("LoaiMon"));
                    m.setHinhAnh(rs.getString("HinhAnh"));
                    list.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return list;
    }

    public boolean insert(Menu m) {
        String sql = "INSERT INTO tbl_Menu VALUES ('"
                + m.getMaMenu() + "', N'"
                + m.getTenMon() + "', "
                + m.getDonGia() + ", N'"
                + m.getLoaiMon() + "', N'"
                + m.getHinhAnh() + "')";

        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();

        if (conn != null) {
            try {
                Statement st = conn.createStatement();
                return st.executeUpdate(sql) > 0;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return false;
    }

    public boolean update(Menu m) {
        String sql = "UPDATE tbl_Menu SET "
                + "TenMon = N'" + m.getTenMon() + "', "
                + "DonGia = " + m.getDonGia() + ", "
                + "LoaiMon = N'" + m.getLoaiMon() + "', "
                + "HinhAnh = N'" + m.getHinhAnh() + "' "
                + "WHERE MaMenu = '" + m.getMaMenu() + "'";

        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();

        if (conn != null) {
            try {
                Statement st = conn.createStatement();
                return st.executeUpdate(sql) > 0;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return false;
    }

    public boolean delete(String maMenu) {
        String sql = "DELETE FROM tbl_Menu WHERE MaMenu = '" + maMenu + "'";

        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();

        if (conn != null) {
            try {
                Statement st = conn.createStatement();
                return st.executeUpdate(sql) > 0;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                myConn.closeConnection();
            }
        }
        return false;
    }

    public Menu selectByName(String tenMon) {
        String sql = "SELECT * FROM tbl_Menu WHERE TenMon = N'" + tenMon + "'";

        MyConnection myConn = new MyConnection();
        Connection conn = myConn.getInstance();

        if (conn != null) {
            try {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql);

                if (rs.next()) {
                    Menu m = new Menu();
                    m.setMaMenu(rs.getString("MaMenu"));
                    m.setTenMon(rs.getString("TenMon"));
                    m.setDonGia(rs.getDouble("DonGia"));
                    m.setLoaiMon(rs.getString("LoaiMon"));
                    m.setHinhAnh(rs.getString("HinhAnh"));
                    return m;
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
