package com.wipro.eshop.service;

import java.util.List;
import com.wipro.eshop.exception.CartEmptyException;
import com.wipro.eshop.model.MenuItem;

public interface CartService {
    void addCartItem(long userId, long menuItemId);
    List<MenuItem> getAllCartItems(long userId) throws CartEmptyException;
    void removeCartItem(long userId, long menuItemId);
    boolean doesUserExist(long userId);
    void registerUser(long userId, String userName);
	String getUserName(long userId);
}