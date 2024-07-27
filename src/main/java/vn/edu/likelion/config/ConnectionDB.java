package vn.edu.likelion.config;

import java.sql.*;

public class ConnectionDB {
    private String url;
    private String user;
    private String password;
    Connection connection;

    public ConnectionDB() {
        this.url = "jdbc:oracle:thin:@localhost:1523";
        this.user = "NAM";
        this.password = "123";
    }

    public void connect(){
        try {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void disconnect(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
