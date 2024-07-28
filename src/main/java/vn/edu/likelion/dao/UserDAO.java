package vn.edu.likelion.dao;

import vn.edu.likelion.config.ConnectionDB;
import vn.edu.likelion.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserDAO {
    private ConnectionDB connectionDB = new ConnectionDB();
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private User user = null;
    private BranchDAO branchDAO = new BranchDAO();

    public boolean insertOneUser(User user){
        connectionDB.connect();

        String query = "INSERT INTO USERS(USERNAME,PASSWORD,ROLE_ID) VALUES(?,?,?)";
        String passwordEncode = Base64.getEncoder().encodeToString(user.getPassword().getBytes());

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1,user.getUsername());
            preparedStatement.setString(2,passwordEncode);
            preparedStatement.setInt(3,2);
            int row = preparedStatement.executeUpdate();
            if(row > 0) return true;
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }

        return false;
    }

    public boolean checkDuplicateUsername(String username, int id){
        connectionDB.connect();
        String query = "SELECT * FROM USERS WHERE USERNAME = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                if((id == 0) || (id != resultSet.getInt(1))) return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public User checkExistOfManager(int id){
        connectionDB.connect();
        String query = "SELECT * FROM USERS WHERE ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                return user;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            connectionDB.disconnect();
        }
        return null;
    }

    public boolean updateUser(User user){
        connectionDB.connect();
        String query = "UPDATE USERS SET USERNAME = ?, PASSWORD = ? WHERE ID = ?";
        String passwordEncode = Base64.getEncoder().encodeToString(user.getPassword().getBytes());

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, passwordEncode);
            preparedStatement.setInt(3, user.getId());
            int row = preparedStatement.executeUpdate();
            if(row > 0) return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }

        return false;
    }

    public List<User> getAll(){
        connectionDB.connect();
        List<User> listUser = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT A.ID, A.USERNAME, B.NAME ");
        query.append("FROM USERS A INNER JOIN ROLE B ON A.ROLE_ID = B.ID");

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(String.valueOf(query));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setRole(resultSet.getString(3));
                listUser.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            connectionDB.disconnect();
        }
        return listUser;
    }

    public boolean delete(int id){
        connectionDB.connect();
        String query = "DELETE FROM USERS WHERE ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            int row = preparedStatement.executeUpdate();
            if(row > 0) return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public List<User> getAllUserWithoutAssign(){
        connectionDB.connect();
        StringBuilder query = new StringBuilder("SELECT A.ID, A.USERNAME ");
        query.append("FROM USERS A LEFT JOIN BRANCH B ON A.ID = B.USER_ID ");
        query.append("WHERE A.ROLE_ID = 2 AND B.ID IS NULL");
        List<User> listUsers = new ArrayList<>();
        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(String.valueOf(query));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                listUsers.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return listUsers;
    }

    public int getIdBranch(Integer userId){
        connectionDB.connect();
        String query = "SELECT B.ID FROM USERS A INNER JOIN BRANCH B ON A.ID = b.user_id WHERE A.ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return 0;
    }

    public int checkUserManageWarehouse(Integer userId){
        connectionDB.connect();
        String query = "SELECT B.ID FROM USERS A INNER JOIN BRANCH B ON A.ID = B.USER_ID WHERE A.ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return 0;
    }

    public User checkExistUsernameAndPasswordInDB(String username, String password){
        connectionDB.connect();
        String query = "SELECT A.ID,A.USERNAME,B.NAME FROM USERS A INNER JOIN ROLE B ON A.ROLE_ID = B.ID WHERE USERNAME = ? AND PASSWORD = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            String passwordEncode = Base64.getEncoder().encodeToString(password.getBytes());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2,passwordEncode);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user = new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionDB.disconnect();
        }
        return null;
    }
}
