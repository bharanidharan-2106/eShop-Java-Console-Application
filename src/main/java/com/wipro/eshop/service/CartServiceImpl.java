package com.wipro.eshop.service;

import java.util.List;
import com.wipro.eshop.dao.CartDao;
import com.wipro.eshop.exception.CartEmptyException;
import com.wipro.eshop.model.MenuItem;

public class CartServiceImpl implements CartService {
    private final CartDao cartDao;

    public CartServiceImpl(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    @Override
    public void addCartItem(long userId, long menuItemId) {
        cartDao.addCartItem(userId, menuItemId);
    }

    @Override
    public List<MenuItem> getAllCartItems(long userId) throws CartEmptyException {
        return cartDao.getAllCartItems(userId);
    }

    @Override
    public void removeCartItem(long userId, long menuItemId) {
        cartDao.removeCartItem(userId, menuItemId);
    }
    
    @Override
    public boolean doesUserExist(long userId) {
        return cartDao.doesUserExist(userId);
    }

    @Override
    public void registerUser(long userId, String userName) {
        cartDao.registerUser(userId, userName);
    }

	@Override
	public String getUserName(long userId) {
		return cartDao.getUserName(userId);
	}
}