package com.wipro.eshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.wipro.eshop.model.MenuItem;

public class MenuItemDaoSqlImpl implements MenuItemDao {
    @Override
    public List<MenuItem> getMenuItemListAdmin() {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT * FROM menu_item";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    @Override
    public List<MenuItem> getMenuItemListCustomer() {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT * FROM menu_item";

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                MenuItem item = new MenuItem();
                item.setId(resultSet.getLong("me_id"));
                item.setName(resultSet.getString("me_name"));
                item.setPrice(resultSet.getFloat("me_price"));
                item.setActive(resultSet.getBoolean("me_active"));  
                item.setDateOfLaunch(resultSet.getDate("me_date_of_launch"));
                item.setCategory(resultSet.getString("me_category"));
                item.setFreeDelivery("Yes".equalsIgnoreCase(resultSet.getString("me_free_delivery")));
                menuItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    @Override
    public void modifyMenuItem(MenuItem menuItem) {
        String query = "UPDATE menu_item SET me_name = ?, me_price = ?, me_active = ?, "
                     + "me_date_of_launch = ?, me_category = ?, me_free_delivery = ? "
                     + "WHERE me_id = ?";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, menuItem.getName());
            statement.setFloat(2, menuItem.getPrice());
            statement.setString(3, menuItem.isActive() ? "Yes" : "No");
            statement.setDate(4, new java.sql.Date(menuItem.getDateOfLaunch().getTime()));
            statement.setString(5, menuItem.getCategory());
            statement.setString(6, menuItem.isFreeDelivery() ? "Yes" : "No");
            statement.setLong(7, menuItem.getId());
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MenuItem getMenuItem(long menuItemId) {
        String query = "SELECT * FROM menu_item WHERE me_id = ?";
        MenuItem item = null;
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setLong(1, menuItemId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    item = new MenuItem();
                    item.setId(resultSet.getLong("me_id"));
                    item.setName(resultSet.getString("me_name"));
                    item.setPrice(resultSet.getFloat("me_price"));
                    item.setActive("Yes".equals(resultSet.getString("me_active")));
                    item.setDateOfLaunch(resultSet.getDate("me_date_of_launch"));
                    item.setCategory(resultSet.getString("me_category"));
                    item.setFreeDelivery("Yes".equals(resultSet.getString("me_free_delivery")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }
    
    @Override
    public void addMenuItem(MenuItem menuItem) {
        String query = "INSERT INTO menu_item (me_id, me_name, me_price, me_active, " +
                     "me_date_of_launch, me_category, me_free_delivery) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setLong(1, menuItem.getId());
            statement.setString(2, menuItem.getName());
            statement.setFloat(3, menuItem.getPrice());
            statement.setString(4, menuItem.isActive() ? "Yes" : "No");
            statement.setDate(5, new java.sql.Date(menuItem.getDateOfLaunch().getTime()));
            statement.setString(6, menuItem.getCategory());
            statement.setString(7, menuItem.isFreeDelivery() ? "Yes" : "No");

            statement.executeUpdate(); 
            System.out.println("Menu item added successfully!");

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Error: Menu item ID already exists. Please use a unique ID.");
            } else {
                System.err.println("Error adding menu item: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void deleteMenuItem(long menuItemId) {
        String query = "DELETE FROM menu_item WHERE id = ?";

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, menuItemId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted == 0) {
                System.out.println("No menu item found with ID " + menuItemId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}