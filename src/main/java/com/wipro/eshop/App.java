package com.wipro.eshop;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import com.wipro.eshop.dao.CartDaoSqlImpl;
import com.wipro.eshop.dao.MenuItemDaoSqlImpl;
import com.wipro.eshop.exception.CartEmptyException;
import com.wipro.eshop.model.MenuItem;
import com.wipro.eshop.service.CartService;
import com.wipro.eshop.service.CartServiceImpl;
import com.wipro.eshop.service.MenuItemService;
import com.wipro.eshop.service.MenuItemServiceImpl;
import com.wipro.eshop.util.DateUtil;

public class App {
	private static Scanner scanner = new Scanner(System.in);
    private static MenuItemService menuItemService;
    private static CartService cartService;

    static {
        menuItemService = new MenuItemServiceImpl(new MenuItemDaoSqlImpl());
        cartService = new CartServiceImpl(new CartDaoSqlImpl());
    }

    public static void main(String[] args) {
        System.out.println("Welcome to eShop");
        System.out.println("Select User Type:");
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.print("Enter your choice: ");
        int userType = scanner.nextInt();
        scanner.nextLine(); 
        
        if (userType == 1) {
            adminMenu();
        } 
        else if (userType == 2) {
            customerMenu();
        } 
        else {
            System.out.println("Invalid choice. Exiting...");
        }
    }

    private static void adminMenu() {
    	System.out.println("\nWelcome Admin!");
        while (true) {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. View Menu Items");
            System.out.println("2. Add Menu Item");
            System.out.println("3. Modify Menu Item");
            System.out.println("4. Delete Menu Item");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: 
                	viewMenuItemsAdmin(); 
                	break;
                case 2: 
                	addMenuItem(); 
                	break;
                case 3: 
                	modifyMenuItem(); 
                	break;
                case 4:
                    deleteMenuItem(); 
                    break;
                case 5:
                    System.out.println("Exiting..");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    
    private static void customerMenu() {
        while (true) {
            System.out.println("\nWelcome to eShop");
            System.out.println("1. Login with User ID");
            System.out.println("2. Register New User");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            long userId;
            String name;

            switch (choice) {
                case 1:
                    System.out.print("Enter your User ID: ");
                    userId = scanner.nextLong();
                    scanner.nextLine(); 

                    if (!cartService.doesUserExist(userId)) {
                        System.out.println("User ID not found. Please register first.");
                        continue;
                    }

                    name = cartService.getUserName(userId);
                    System.out.println("Welcome back, " + name + "!");
                    openCustomerOperations(userId);
                    return;

                case 2:
                    System.out.print("Enter your name: ");
                    name = scanner.nextLine();

                    do {
                        userId = 1000 + (long)(Math.random() * 9000);
                    } while (cartService.doesUserExist(userId)); 

                    cartService.registerUser(userId, name);
                    System.out.println("User registered successfully!");
                    System.out.println("Your Name: " + name);
                    System.out.println("Your User ID: " + userId);
                    System.out.println("Welcome, " + name + "!");
                    openCustomerOperations(userId);
                    return;

                case 3:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void openCustomerOperations(long userId) {
        while (true) {
            System.out.println("\nCustomer Menu:");
            System.out.println("1. View Menu Items");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Remove from Cart");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    viewMenuItemsCustomer();
                    break;
                case 2:
                    addToCart(userId);
                    break;
                case 3:
                    viewCart(userId);
                    break;
                case 4:
                    removeFromCart(userId);
                    break;
                case 5:
                    System.out.println("Exiting Customer Menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void viewMenuItemsAdmin() {
        System.out.println("\nAdmin View - All Menu Items:");
        List<MenuItem> menuItems = menuItemService.getMenuItemListAdmin();
        for (MenuItem item : menuItems) {
            System.out.println(item);
        }
    }
    
    private static void addMenuItem() {
        System.out.println("\nAdd New Menu Item:");
        
        try {
            MenuItem item = new MenuItem();

            System.out.print("Enter Item ID: ");
            long id = Long.parseLong(scanner.nextLine());
            item.setId(id);

            System.out.print("Enter Item Name: ");
            String name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                System.out.println("Item name cannot be empty.");
                return;
            }
            item.setName(name);

            System.out.print("Enter Price: ");
            String priceStr = scanner.nextLine();
            float price = Float.parseFloat(priceStr);
            if (price <= 0) {
                System.out.println("Price must be greater than zero.");
                return;
            }
            item.setPrice(price);

            System.out.print("Is Active (true/false): ");
            String activeStr = scanner.nextLine();
            boolean active = Boolean.parseBoolean(activeStr);
            item.setActive(active);

            System.out.print("Enter Launch Date (dd/MM/yyyy): ");
            String dateStr = scanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
                LocalDate localDate = LocalDate.parse(dateStr, formatter);
                Date utilDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                item.setDateOfLaunch(utilDate);

                if (localDate.isAfter(LocalDate.now())) {
                    System.out.println("Launch date is in the future. Setting 'active' to false automatically.");
                    item.setActive(false);
                }

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            }

            System.out.print("Enter Category: ");
            String category = scanner.nextLine();
            if (category.trim().isEmpty()) {
                System.out.println("Category cannot be empty.");
                return;
            }
            item.setCategory(category);

            System.out.print("Has Free Delivery (true/false): ");
            String freeDeliveryStr = scanner.nextLine();
            boolean freeDelivery = Boolean.parseBoolean(freeDeliveryStr);
            item.setFreeDelivery(freeDelivery);
            menuItemService.addMenuItem(item); 
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number entered.");
        } catch (Exception e) {
            System.out.println("Error adding menu item: " + e.getMessage());
        }
    }
    
    private static void modifyMenuItem() {
        System.out.println("\nModify Menu Item:");
        System.out.print("Enter Menu Item ID: ");
        long id = scanner.nextLong();
        scanner.nextLine();
        
        MenuItem menuItem = menuItemService.getMenuItem(id);
        if (menuItem == null) {
            System.out.println("Menu Item not found!");
            return;
        }
    
        System.out.println("\nCurrent Details:");
        System.out.println(menuItem);
        
        System.out.print("\nEnter new name (leave blank to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            menuItem.setName(name);
        }
        
        System.out.print("Enter new price (0 to keep current): ");
        float price = scanner.nextFloat();
        if (price > 0) {
            menuItem.setPrice(price);
        }
        
        System.out.println("Is active (true/false, leave blank to keep current):");
        String activeStr = scanner.nextLine().trim();
        if (!activeStr.isEmpty()) {
            if (activeStr.equalsIgnoreCase("true") || activeStr.equalsIgnoreCase("false")) {
                menuItem.setActive(Boolean.parseBoolean(activeStr));
            } else {
                System.out.println("Invalid input for 'active'. Keeping current value.");
            }
        }

        System.out.println("Enter new launch date (dd/MM/yyyy, leave blank to keep current):");
        String dateStr = scanner.nextLine().trim();
        if (!dateStr.isEmpty()) {
            try {
                Date launchDate = DateUtil.convertToDate(dateStr);
                Date currentDate = new Date(); 
                if (launchDate.after(currentDate)) {
                    System.out.println("Launch date is in the future. Setting 'active' to false.");
                    menuItem.setActive(false);
                }
                menuItem.setDateOfLaunch(launchDate); 

            } catch (Exception e) {
                System.out.println("Invalid date format. Keeping current date.");
            }
        }

        System.out.println("Enter new category (leave blank to keep current):");
        String category = scanner.nextLine().trim();
        if (!category.isEmpty()) {
            menuItem.setCategory(category);
        }

        System.out.println("Has free delivery (true/false, leave blank to keep current):");
        String freeDeliveryStr = scanner.nextLine().trim();
        if (!freeDeliveryStr.isEmpty()) {
            if (freeDeliveryStr.equalsIgnoreCase("true") || freeDeliveryStr.equalsIgnoreCase("false")) {
                menuItem.setFreeDelivery(Boolean.parseBoolean(freeDeliveryStr));
            } else {
                System.out.println("Invalid input for 'free delivery'. Keeping current value.");
            }
        }

        menuItemService.modifyMenuItem(menuItem);
        System.out.println("\nMenu Item updated successfully!");
    }
    
    private static void deleteMenuItem() {
        System.out.print("Enter Menu Item ID to delete: ");
        long id = scanner.nextLong();
        scanner.nextLine();

        MenuItem menuItem = menuItemService.getMenuItem(id);
        if (menuItem == null) {
            System.out.println("Menu Item not found.");
            return;
        }

        menuItemService.deleteMenuItem(id);
        System.out.println("Menu Item deleted successfully!");
    }

    private static void viewMenuItemsCustomer() {
        System.out.println("\nCustomer View - Available Menu Items:");
        List<MenuItem> menuItems = menuItemService.getMenuItemListCustomer();
        for (MenuItem item : menuItems) {
            System.out.println(item);
        }
    }

    private static void addToCart(long userId) {
        System.out.println("\nAdd to Cart:");
        System.out.print("Enter Menu Item ID(s) to add (space-separated): ");
        String input = scanner.nextLine().trim(); 
        String[] idStrings = input.split("\\s+"); 
        for (String idStr : idStrings) {
            try {
                long menuItemId = Long.parseLong(idStr);
                MenuItem menuItem = menuItemService.getMenuItem(menuItemId);
                
                if (menuItem == null) {
                    System.out.println("Menu Item ID " + menuItemId + " not found!");
                    continue;
                }
                cartService.addCartItem(userId, menuItemId);
                
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID entered: " + idStr);
            }
        }
    }

    private static void viewCart(long userId) {
        System.out.println("\nYour Cart:");
        try {
            List<MenuItem> cartItems = cartService.getAllCartItems(userId);
            double total = 0.0;
            for (MenuItem item : cartItems) {
                System.out.println(item);
                total += item.getPrice();
            }
            System.out.println("Total: $" + String.format("%.2f", total));
        } catch (CartEmptyException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeFromCart(long userId) {
        System.out.println("\nRemove from Cart:");
        System.out.print("Enter Menu Item ID to remove: ");
        long menuItemId = scanner.nextLong();
        scanner.nextLine();
        boolean itemFound = false;

        try {
            List<MenuItem> cartItems = cartService.getAllCartItems(userId);
            for (MenuItem item : cartItems) {
                if (item.getId() == menuItemId) {
                    itemFound = true;
                    break;
                }
            }
            if (itemFound) {
                cartService.removeCartItem(userId, menuItemId);
                System.out.println("Item removed from cart successfully!");
            } else {
                System.out.println("Item ID not found in cart.");
            }

        } catch (CartEmptyException e) {
            System.out.println("Your cart is currently empty. Nothing to remove.");
        }
    }
}