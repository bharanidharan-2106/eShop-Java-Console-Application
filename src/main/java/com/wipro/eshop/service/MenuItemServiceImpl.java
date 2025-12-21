package com.wipro.eshop.service;

import java.util.List;
import com.wipro.eshop.dao.MenuItemDao;
import com.wipro.eshop.model.MenuItem;

public class MenuItemServiceImpl implements MenuItemService {
    private final MenuItemDao menuItemDao;

    public MenuItemServiceImpl(MenuItemDao menuItemDao) {
        this.menuItemDao = menuItemDao;
    }

    @Override
    public List<MenuItem> getMenuItemListAdmin() {
        return menuItemDao.getMenuItemListAdmin();
    }

    @Override
    public List<MenuItem> getMenuItemListCustomer() {
        return menuItemDao.getMenuItemListCustomer();
    }

    @Override
    public void modifyMenuItem(MenuItem menuItem) {
        menuItemDao.modifyMenuItem(menuItem);
    }

    @Override
    public MenuItem getMenuItem(long menuItemId) {
        return menuItemDao.getMenuItem(menuItemId);
    }
    
    @Override
    public void addMenuItem(MenuItem menuItem) {
        menuItemDao.addMenuItem(menuItem);
    }
    
    @Override
    public void deleteMenuItem(long menuItemId) {
        menuItemDao.deleteMenuItem(menuItemId);
    }
}