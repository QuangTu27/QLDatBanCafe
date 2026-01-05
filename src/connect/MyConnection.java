package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    // Sử dụng chuỗi kết nối Windows Authentication cho SQLEXPRESS
    private final String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;" 
                             + "databaseName=QL_DatBan_Cafe;"
                             + "integratedSecurity=true;" 
                             + "encrypt=true;trustServerCertificate=true;";

    private Connection conn; // Thêm lại biến này để tương thích với các hàm cũ

    public Connection getInstance() {
        // Nếu kết nối cũ đã đóng hoặc chưa có, thì tạo mới
        try {
            if (conn == null || conn.isClosed()) {
                conn = openConnection();
            }
        } catch (SQLException e) {
            conn = openConnection();
        }
        return conn;
    }

    public Connection openConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            System.err.println("Lỗi kết nối: " + e.getMessage());
            return null;
        }
    }

    // THÊM LẠI HÀM NÀY ĐỂ HẾT LỖI NoSuchMethodError
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MyConnection myConn = new MyConnection();
        if (myConn.getInstance() != null) {
            System.out.println("KẾT NỐI WINDOWS THÀNH CÔNG!");
            myConn.closeConnection();
        }
    }
}