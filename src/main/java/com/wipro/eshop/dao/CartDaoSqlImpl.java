package com.wipro.eshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.wipro.eshop.exception.CartEmptyException;
import com.wipro.eshop.model.MenuItem;

public class CartDaoSqlImpl implements CartDao {
    @Override
    public void addCartItem(long userId, long menuItemId) {
        String checkQuery = "SELECT * FROM cart WHERE ct_us_id = ? AND ct_pr_id = ?";
        String insertQuery = "INSERT INTO cart (ct_us_id, ct_pr_id) VALUES (?, ?)";

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            checkStmt.setLong(1, userId);
            checkStmt.setLong(2, menuItemId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Item already exists in the cart.");
                return;
            }

            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setLong(1, userId);
                insertStmt.setLong(2, menuItemId);
                insertStmt.executeUpdate();
                System.out.println("Item added to cart successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<MenuItem> getAllCartItems(long userId) throws CartEmptyException {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT m.* FROM menu_item m JOIN cart c ON m.me_id = c.ct_pr_id "
                      + "WHERE c.ct_us_id = ?";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    MenuItem item = new MenuItem();
                    item.setId(resultSet.getLong("me_id"));
                    item.setName(resultSet.getString("me_name"));
                    item.setPrice(resultSet.getFloat("me_price"));
                    item.setActive("Yes".equals(resultSet.getString("me_active")));
                    item.setDateOfLaunch(resultSet.getDate("me_date_of_launch"));
                    item.setCategory(resultSet.getString("me_category"));
                    item.setFreeDelivery("Yes".equals(resultSet.getString("me_free_delivery")));
                    menuItems.add(item);
                }
            }
            
            if (menuItems.isEmpty()) {
                throw new CartEmptyException("Cart is empty" );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    @Override
    public void removeCartItem(long userId, long menuItemId) {
        String query = "DELETE FROM cart WHERE ct_us_id = ? AND ct_pr_id = ?";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setLong(1, userId);
            statement.setLong(2, menuItemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean doesUserExist(long userId) {
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT 1 FROM user WHERE us_id = ?")) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void registerUser(long userId, String userName) {
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO user (us_id, us_name) VALUES (?, ?)")) {
            ps.setLong(1, userId);
            ps.setString(2, userName);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUserName(long userId) {
        String query = "SELECT us_name FROM user WHERE us_id = ?";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return rs.getString("us_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "User"; 
    }
}