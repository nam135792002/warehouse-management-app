package vn.edu.likelion.dao;

import vn.edu.likelion.config.ConnectionDB;
import vn.edu.likelion.entity.Branch;
import vn.edu.likelion.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

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
            if(branch.getUser() == null) preparedStatement.setNull(3, Types.INTEGER);
            else preparedStatement.setInt(3,branch.getUser().getId());

            int row = preparedStatement.executeUpdate();
            if(row > 0) return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public boolean checkDuplicateNameBranch(String name, int id){
        connectionDB.connect();
        String query = "SELECT * FROM BRANCH WHERE NAME = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1,name);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                if(id == 0 || id != resultSet.getInt(1)) return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public boolean assignManageWarehouse(Integer userId, Integer branchId){
        connectionDB.connect();
        String query = "UPDATE BRANCH SET USER_ID = ? WHERE ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,userId);
            preparedStatement.setInt(2,branchId);
            int row = preparedStatement.executeUpdate();
            if(row > 0) return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public List<Branch> getAllWithoutUser(){
        connectionDB.connect();
        String query = "SELECT ID, NAME, ADDRESS FROM BRANCH WHERE USER_ID IS NULL";
        List<Branch> listBranches = new ArrayList<>();

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                branch = new Branch();
                branch.setId(resultSet.getInt(1));
                branch.setName(resultSet.getString(2));
                branch.setAddress(resultSet.getString(3));
                listBranches.add(branch);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return listBranches;
    }

    public List<Branch> getAll(){
        connectionDB.connect();
        StringBuilder query = new StringBuilder("SELECT A.ID, A.NAME, A.ADDRESS, B.USERNAME ");
        query.append("FROM BRANCH A LEFT JOIN USERS B ON A.USER_ID = B.ID");
        List<Branch> listBranches = new ArrayList<>();

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(String.valueOf(query));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                branch = new Branch();
                branch.setId(resultSet.getInt(1));
                branch.setName(resultSet.getString(2));
                branch.setAddress(resultSet.getString(3));
                branch.setUser(new User(resultSet.getString(4)));
                listBranches.add(branch);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return listBranches;
    }

    public Branch checkExistedBranchById(int warehouseId){
        connectionDB.connect();
        branch = new Branch();
        String query = "SELECT * FROM BRANCH WHERE ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, warehouseId);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                branch.setId(resultSet.getInt(1));
                branch.setName(resultSet.getString(2));
                branch.setAddress(resultSet.getString(3));
                branch.setUser(new User(resultSet.getInt(4)));
                return branch;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return null;
    }

    public boolean deleteBranchById(int idWarehouse){
        connectionDB.connect();
        String query = "DELETE FROM BRANCH WHERE ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, idWarehouse);
            int row = preparedStatement.executeUpdate();
            if(row > 0) return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public boolean deleteBranchByUserId(int userId){
        connectionDB.connect();
        String query = "DELETE FROM BRANCH WHERE USER_ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, userId);
            int row = preparedStatement.executeUpdate();
            if(row > 0) return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public boolean updateBranch(Branch branch){
        connectionDB.connect();
        String query = "UPDATE BRANCH SET NAME = ?,ADDRESS = ?,USER_ID = ? WHERE ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setString(1, branch.getName());
            preparedStatement.setString(2, branch.getAddress());
            if(branch.getUser().getId() == 0) preparedStatement.setNull(3,Types.INTEGER);
            else preparedStatement.setInt(3,branch.getUser().getId());
            preparedStatement.setInt(4, branch.getId());

            int row = preparedStatement.executeUpdate();
            if (row > 0) return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public boolean checkManagementOfWarehouse(Integer branchId){
        connectionDB.connect();
        String query = "SELECT * FROM BRANCH WHERE ID = ? AND USER_ID IS NULL";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, branchId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public List<Branch> listBranchWithoutBranchChoice(Integer branchId){
        connectionDB.connect();
        StringBuilder query = new StringBuilder("SELECT B.ID, B.NAME, B.ADDRESS ");
        query.append("FROM USERS A RIGHT JOIN BRANCH B ON A.ID = B.USER_ID ");
        query.append("WHERE B.USER_ID IS NOT NULL AND B.ID != ?");

        List<Branch> listBranches = new ArrayList<>();
        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(String.valueOf(query));
            preparedStatement.setInt(1, branchId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                branch = new Branch();
                branch.setId(resultSet.getInt(1));
                branch.setName(resultSet.getString(2));
                branch.setAddress(resultSet.getString(3));
                listBranches.add(branch);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return listBranches;
    }
}
