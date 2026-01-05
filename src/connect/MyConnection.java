/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 * SingleTon
 */
public class MyConnection {
    private String userName = "sa";
    private String password = "123456";
    private String url ="jdbc:sqlserver://localhost:1433;" +"databaseName=QL_DatBan_Cafe;"
                +"encrypt=false;" +"trustServerCertificate=true;";

    private Connection conn ;

    public Connection getInstance(){
        if(conn == null){
            conn = openConnection();
        }
        return conn;
    }
    
    private Connection openConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, userName, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    public void closeConnection() {
        if(conn != null){
            try {
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        MyConnection myConnection = new MyConnection();
        if(myConnection.getInstance() != null){
            System.out.println("Connection successfull");
        }else{
            System.out.println("Connection error");
        }
    }
}