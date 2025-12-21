package com.wipro.eshop.dao;

import java.util.List;
import com.wipro.eshop.model.MenuItem;

public interface MenuItemDao {
    List<MenuItem> getMenuItemListAdmin();
    List<MenuItem> getMenuItemListCustomer();
    void modifyMenuItem(MenuItem menuItem);
    MenuItem getMenuItem(long menuItemId);
    void addMenuItem(MenuItem menuItem);
    void deleteMenuItem(long menuItemId);
}