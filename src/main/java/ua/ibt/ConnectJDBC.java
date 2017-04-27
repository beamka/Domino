package ua.ibt;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Created by IRINA on 23.04.2017.
 */
public class ConnectJDBC {
    private Connection conn = null;

    public ConnectJDBC() {
        super();
        String path = "db.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties properties = new Properties();
        try (InputStream resourceStream = loader.getResourceAsStream(path)) {
            properties.load(resourceStream);

            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

            Class.forName(properties.getProperty("driver"));

            this.conn = DriverManager.getConnection(url, user, password);
            System.out.println("Successful connection.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed connection:" + e.getMessage());
        }
    }

    public Connection getConnect(){
        return conn;
    }

    @Override
    protected void finalize() throws Throwable {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Close connection.");
            } catch (SQLException e) {
                System.out.println("Not close connection.");
                e.printStackTrace();
            }
        }
        super.finalize();
    }

}
