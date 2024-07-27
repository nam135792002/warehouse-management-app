package vn.edu.likelion.dao;

import vn.edu.likelion.config.ConnectionDB;
import vn.edu.likelion.entity.Branch;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BranchDAO {
    private ConnectionDB connectionDB = new ConnectionDB();
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private Branch branch = null;

    public boolean insertOneBranch(Branch branch){
        connectionDB.connect();
        String query = "INSERT INTO BRANCH(NAME,ADDRESS,USER_ID) VALUES(?,?,?)";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1, branch.getName());
            preparedStatement.setString(2, branch.getAddress());
            preparedStatement.setInt(3,branch.getUser().getId());

            int row = preparedStatement.executeUpdate();
            if(row > 0) return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return false;
    }
}
