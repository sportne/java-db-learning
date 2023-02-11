package me.database.learning;

import java.sql.*;

public class MySQLTest {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/YOUR_DATABASE_NAME";
    
    static final String USER = "YOUR_USERNAME";
    static final String PASS = "YOUR_PASSWORD";
    
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM YOUR_TABLE_NAME";
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("NAME");
                System.out.println("ID: " + id + ", Name: " + name);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
