package vn.edu.likelion.dao;

import vn.edu.likelion.config.ConnectionDB;
import vn.edu.likelion.entity.Product;
import vn.edu.likelion.entity.ProductAttribute;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private ConnectionDB connectionDB = new ConnectionDB();
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    private Product product= null;
    private ProductAttribute productAttribute = null;

    public boolean insertInfoProduct(List<Product> listProducts, Integer idBranch, Integer idUser){
        connectionDB.connect();
        String queryProduct = "INSERT INTO PRODUCT(NAME,AMOUNT,PRICE,BRANCH_ID,USER_ID) VALUES(?,?,?,?,?)";

        try {
            String[] returnId = { "ID" };
            preparedStatement = connectionDB.getConnection().prepareStatement(queryProduct,returnId);
            for (Product p : listProducts){
                preparedStatement.setString(1,p.getName());
                preparedStatement.setInt(2,p.getAmount());
                preparedStatement.setLong(3,p.getPrice());
                preparedStatement.setInt(4,idBranch);
                preparedStatement.setInt(5,idUser);
                preparedStatement.executeUpdate();

                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()){
                    int productId = resultSet.getInt(1);
                    insertInfoAttribute(p.getListProductAttributes(),resultSet.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public void insertInfoAttribute(List<ProductAttribute> listAttribute, Integer productId){
        ConnectionDB c = new ConnectionDB();
        c.connect();
        String queryAttribute = "INSERT INTO ATTRIBUTE_PRODUCT(CONTENT,PRODUCT_ID) VALUES(?,?)";

        try {
            PreparedStatement ps = connectionDB.getConnection().prepareStatement(queryAttribute);
            for(ProductAttribute pa : listAttribute){
                ps.setString(1,pa.getContent());
                ps.setInt(2,productId);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            c.disconnect();
        }
    }

    public void moveProductToAnotherWarehouse(int fromBranchId, int toBranchId, int userId){
        connectionDB.connect();
        String query = "UPDATE PRODUCT SET BRANCH_ID = ?,USER_ID = ? WHERE BRANCH_ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, toBranchId);
            preparedStatement.setInt(2, userId);
            preparedStatement.setInt(3,fromBranchId);
            preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
    }

    public boolean checkProductInWarehouse(int idBranch){
        connectionDB.connect();
        String query = "SELECT * FROM PRODUCT WHERE BRANCH_ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, idBranch);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return false;
    }

    public List<Product> listAll(Integer branchId){
        connectionDB.connect();
        List<Product> listProducts = new ArrayList<>();
        String query = "SELECT * FROM PRODUCT WHERE BRANCH_ID = ?";

        try {
            preparedStatement = connectionDB.getConnection().prepareStatement(query);
            preparedStatement.setInt(1,branchId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                product = new Product();
                product.setId(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setAmount(resultSet.getInt(3));
                product.setPrice(resultSet.getInt(4));
                product.setListProductAttributes(listAllAttributeByProduct(resultSet.getInt(1)));
                listProducts.add(product);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connectionDB.disconnect();
        }
        return listProducts;
    }

    public List<ProductAttribute> listAllAttributeByProduct(Integer productId){
        ConnectionDB c = new ConnectionDB();
        c.connect();
        List<ProductAttribute> listProductAttributes = new ArrayList<>();
        String query = "SELECT * FROM ATTRIBUTE_PRODUCT WHERE PRODUCT_ID = ?";

        PreparedStatement p = null;
        try {
            p = c.getConnection().prepareStatement(query);
            p.setInt(1,productId);
            ResultSet r = p.executeQuery();
            while (r.next()){
                productAttribute = new ProductAttribute();
                productAttribute.setContent(r.getString(2));
                listProductAttributes.add(productAttribute);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            c.disconnect();
        }
        return listProductAttributes;
    }
}
