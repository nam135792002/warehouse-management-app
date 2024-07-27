package vn.edu.likelion.dao;

import vn.edu.likelion.config.ConnectionDB;
import vn.edu.likelion.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

public class UserDAO {
    private ConnectionDB connectionDB = new ConnectionDB();
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private User user = null;

    public boolean insertOneUser(User user){
        connectionDB.connect();
        boolean flag = false;

        String query = "INSERT INTO USERS(USERNAME,PASSWORD,ROLE_ID) VALUES(?,?,?)";
        String passwordEncode = Base64.getEncoder().encodeToString(user.getPassword().getBytes());

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,passwordEncode);
            preparedStatement.setInt(3,2);
            preparedStatement.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }

        return flag;
    }

    public boolean updateUser(User user){
        connectionDB.connect();

        String query = "UPDATE USERS SET USERNAME = ?, PASSWORD = ? WHERE ID = ?";
        String passwordEncode = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
        boolean flag = false;

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, passwordEncode);
            preparedStatement.setInt(3, user.getId());
            preparedStatement.executeUpdate();
            flag = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }

        return flag;
    }
}
