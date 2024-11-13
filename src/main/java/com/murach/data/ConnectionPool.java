package com.murach.data;

import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionPool {

    private static ConnectionPool pool = null;
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    // Private constructor to prevent instantiation
    private ConnectionPool() {
        try {
            // Lấy thông tin kết nối từ biến môi trường
            dbUrl = System.getenv("DB_URL");          // Ví dụ: "azure-server-murach-123.mysql.database.azure.com"
            dbUser = System.getenv("DB_USER");        // Ví dụ: "ductho"
            dbPassword = System.getenv("DB_PASSWORD"); // Ví dụ: "Tho0411@"
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Singleton pattern để lấy thể hiện của ConnectionPool
    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }

    // Phương thức lấy kết nối từ DataSource
    public Connection getConnection() {
        try {
            // Sử dụng JNDI để lấy DataSource từ Context.xml
            Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/murach");

            return ds.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Phương thức giải phóng kết nối
    public void freeConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Optional: Kiểm tra xem kết nối có hợp lệ không
    public boolean validateConnection(Connection c) {
        try {
            if (c != null && !c.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
