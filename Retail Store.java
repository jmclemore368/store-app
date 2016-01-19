
package RetailStore;

import java.sql.*;
import java.util.*;
import java.text.*;

public class RetailStore {
    static Scanner input = new Scanner(System.in);
    static Connection connection  = null;
    static Statement statement  = null;
    static ResultSet resultSet  = null;
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("Connecting database...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/javabase", "java", "password");
            System.out.println("Database connected");
            while(true){
                statement = connection.createStatement();
                System.out.println("\nPlease Make Selection:");
                System.out.println("1. Customer menu");
                System.out.println("2. Staff menu");
                System.out.println("3. Exit program");
                int userSelection = Integer.parseInt(input.nextLine());
                
                if(userSelection == 3){break;}
                else if(userSelection == 1){customerMenu();}
                else if(userSelection == 2){staffMenu();}
            }
            statement.close();
            connection.close();
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
        finally { statement.close();connection.close();}
    }
    
    public static int userSelection() throws SQLException {
        System.out.println("\nPlease Make Selection:");
        System.out.println("1. Customer menu");
        System.out.println("2. Staff menu");
        System.out.println("3. Exit program");
        int choice = Integer.parseInt(input.nextLine());
        return choice;
    }
    
    public static void customerMenu() throws SQLException {
        System.out.println("\nPlease Make Selection:");
        System.out.println("1. Search Products");
        System.out.println("2. Create/View Account");
        System.out.println("3. Sign In");
        int choice = Integer.parseInt(input.nextLine());
        
        if (choice == 1){searchProducts();}
        if (choice == 2){
            System.out.println("\nPlease Make Selection:");
            System.out.println("1. Create Account");
            System.out.println("2. Update Account");
            System.out.println("3. Delete Account");
            int accountSelection = Integer.parseInt(input.nextLine());
            if(accountSelection == 1){newUser();}
            else if(accountSelection == 2){updateUser();}
            else{deleteUser();}
        }
        if (choice == 3){userOrder();}
    }
    
    public static void searchProducts() throws SQLException {
        try {
            System.out.println("\nPlease Enter the Following:");
            System.out.print("Search Words: ");
            String search = input.nextLine();
            System.out.print("Asc(1) or Dsc(2): ");
            String sortOrder = input.nextLine().equals("1") ? "asc" : "desc";
            String query = "select * from Product where name like \'%" + search + "%\' order by price " + sortOrder;
            resultSet = statement.executeQuery(query);
            int count = 0;
            while(resultSet.next()) {
                count++;
                System.out.println("\n-------------------------------------------------------");
                System.out.println("ID:            \t\t\t" + resultSet.getObject(1).toString());
                System.out.println("Name:          \t\t\t" + resultSet.getObject(2).toString());
                System.out.println("Description:   \t\t\t" + resultSet.getObject(3).toString());
                System.out.println("Stock Quantity:\t\t\t" + resultSet.getObject(4).toString());
                System.out.println("Price:         \t\t\t" + resultSet.getObject(5).toString());
                System.out.println("Active:        \t\t\t" + resultSet.getObject(6).toString());
            }
            if (count == 0) {System.out.println("\nNo results.");}
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    
    public static void newUser() throws SQLException {
        try {
            System.out.println("\nPlease Enter the Following:");
            System.out.print("User ID: ");
            String userId = input.nextLine();
            if(checkDuplicate(userId, "User")) { System.out.println("\nError: ID already exists"); return;}
            System.out.print("Name: ");
            String username = input.nextLine();
            System.out.print("Password: ");
            String password = input.nextLine();
            System.out.print("Address: ");
            String address = input.nextLine();
            System.out.print("Email: ");
            String email = input.nextLine();
            String update = "insert into User values (" + userId + ", \'" + username + "\', \'" + address + "\', \'" + email + "\', \'" + password + "\', \'n\')";
            statement.executeUpdate(update);
            System.out.println("\nCongrats, user created!\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static boolean checkDuplicate(String id, String table) throws SQLException {
        String query = "select id from " + table + " where id = " + id;
        resultSet = statement.executeQuery(query);
        while(resultSet.next()) {return true;}
        return false;
    }
    
    public static void updateUser() throws SQLException {
        try {
            System.out.print("\nEnter your ID: ");
            String userId = input.nextLine();
            if(!checkDuplicate(userId, "User")) {System.out.println("\nThat ID is not in the database"); return;}
            
            System.out.print("Enter your password: ");
            String password = input.nextLine();
            if(!checkPassword(userId, password)) {System.out.println("\nPassword incorrect"); return;}
            
            System.out.println("\nPlease Enter the Following:");
            System.out.print("New name: ");
            String username = input.nextLine();
            System.out.print("New password: ");
            password = input.nextLine();
            System.out.print("New address: ");
            String address = input.nextLine();
            System.out.print("New email: ");
            String email = input.nextLine();
            String update = "update User set name = \'" + username + "\', address = \'" + address + "\', email = \'" + email + "\', password = \'" + password + "\' where id = \'" + userId + "\'";
            statement.executeUpdate(update);
            System.out.println("\nCongrats, account updated!\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static boolean checkPassword(String userId, String password) throws SQLException {
        String query = "select password from User where id = " + userId;
        resultSet = statement.executeQuery(query);
        while(resultSet.next()){return resultSet.getObject(1).toString().equals(password);}
        return false;
    }
    
    public static void deleteUser() throws SQLException {
        try {
            System.out.print("\nEnter your ID: ");
            String userId = input.nextLine();
            if(!checkDuplicate(userId, "User")) {System.out.println("\nThat ID is not in the database"); return;}
            
            System.out.print("Enter your password: ");
            String password = input.nextLine();
            if(!checkPassword(userId, password)) {System.out.println("\nPassword incorrect"); return;}
            
            String update = "delete from User where id = \'" + userId + "\'";
            statement.executeUpdate(update);
            System.out.println("\nYour account has been deleted.\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void userOrder() throws SQLException {
        try {
            System.out.print("\nEnter your ID: ");
            String userId = input.nextLine();
            if(!checkDuplicate(userId, "User")) {System.out.println("\nThat ID is not in the database"); return;}
            
            System.out.print("Enter your password: ");
            String password = input.nextLine();
            if(!checkPassword(userId, password)) {System.out.println("\nPassword incorrect"); return;}
            
            System.out.println("\n1. Add to order\n2. Pay order");
            boolean userSelection = input.nextLine().equals("1");
            
            if(userSelection) {
                
                DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
                java.util.Date date = new java.util.Date();
                
                System.out.print("\nDate: " + dateFormat.format(date));
                String currentDate = dateFormat.format(date);
                String orderId;
                System.out.print("\nEnter a personal 3-digit order ID: ");
                
                while(true) {
                    orderId = input.nextLine();
                    if(!checkDuplicate(orderId, "userOrder")){break;}
                    System.out.print("\norder ID already exists. Try again: ");
                }
                newOrderId(orderId, currentDate);
                
                System.out.println("\nEnter the Product ID you want. Enter '~' when done.");
                double total = 0.0;
                while(true) {
                    String userInput = input.nextLine();
                    if(userInput.equals("~")){break;}
                    if(!checkProduct(userInput)) {System.out.println("Product doesn't exist or is out of stock"); continue;}
                    total += add(orderId, userInput);
                    System.out.println("Item has been added to shopping cart.");
                }
                setOrder(userId, orderId, total);
            }
            payOrder(userId);
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void newOrderId(String orderId, String currentDate) throws SQLException {
        try {
            String update = "insert into userOrder values (" + orderId + ", 0.0, \'" + currentDate + "\', \'n\')";
            statement.executeUpdate(update);
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static boolean checkProduct(String productId) throws SQLException {
        String query = "select id, stockQuantity from Product where id = " + productId;
        resultSet = statement.executeQuery(query);
        while(resultSet.next()) {
            if(resultSet.getObject(2).toString().equals("0")){return false;}
            return true;
        }
        return false;
    }
    
    public static double add(String orderId, String productId) throws SQLException {
        String update = "update Product set stockQuantity = stockQuantity-1 where id = " + productId;
        statement.executeUpdate(update);
        
        update = "insert into itContains values (" + orderId + ", " + productId + ")";
        statement.executeUpdate(update);
        
        String query = "select price from Product where id = " + productId;
        resultSet = statement.executeQuery(query);
        while(resultSet.next()) {return Double.parseDouble(resultSet.getObject(1).toString());}
        return 0.0;
    }
    
    public static void setOrder(String userId, String orderId, double total) throws SQLException {
        try {
            String update = "update userOrder set totalPrice = " + total + " where id = " + orderId;
            statement.executeUpdate(update);
            update = "insert into Orders values (\'" + userId + "\', \'" + orderId + "\')";
            statement.executeUpdate(update);
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void payOrder(String userId) throws SQLException {
        try {
            String query = "select orderId from orders where userId = \'" + userId + "\'";
            resultSet = statement.executeQuery(query);
            List<String> idList = new ArrayList<String>();
            boolean isPaying = true;
            while(resultSet.next()) {idList.add(resultSet.getObject(1).toString());}
            
            
            for(int i = 0; i < idList.size(); i++) {
                String s = idList.get(i);
                query = "select totalPrice from userOrder where id = " + s + " and paid = \'n\'";
                
                resultSet = statement.executeQuery(query);
                while(resultSet.next()) {
                    if(isPaying) {
                        System.out.println("\nLets pay your order(s)");
                        System.out.println("Which order would you like to pay?\nSelect the ID or \'0\' if you don't want to pay right now\n");
                        isPaying = false;
                    }
                    System.out.println(s + ":\t$" + Double.parseDouble(resultSet.getObject(1).toString()));
                }
            }
            if(isPaying) {System.out.println("\nShopping Cart Empty"); return;}
            String userSelection = input.nextLine();
            
            if(userSelection.equals("0")){return;}
            
            System.out.println("\nChecking for discounts\n");
            
            
            query = "select productId from itContains where orderId = \'" + userSelection + "\'";
            resultSet = statement.executeQuery(query);
            idList.clear();
            while(resultSet.next()){idList.add(resultSet.getObject(1).toString());}
            System.out.println(idList.size());
            findDiscounts(idList, userSelection);
            
            
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void findDiscounts(List<String> productIds, String userSelection) throws SQLException {
        try {
            
            //Get discountID for all of the users products in the order
            double totalDiscount = 0.0;
            List<String> secondArray = new ArrayList<String>();
            for(int i = 0; i < productIds.size(); i++) {
                secondArray.add(null);
                String productId = productIds.get(i);
                String query = "select discountId from discountAvailable where productId = " + productId;
                resultSet = statement.executeQuery(query);
                while(resultSet.next())
                    secondArray.set(i, resultSet.getObject(1).toString());
            }
            
            //Get discount for each item
            System.out.println("Available discounts:");
            for(int i = 0; i < secondArray.size(); i++) {
                String discount = secondArray.get(i);
                double currentDiscount = 0.0;
                
                if(discount != null) {
                    String query = "select value from Discount where id = " + discount;
                    resultSet = statement.executeQuery(query);
                    while(resultSet.next()){currentDiscount = Double.parseDouble(resultSet.getObject(1).toString());}
                }
                
                currentDiscount += getDiscountValue(productIds.get(i));
                totalDiscount += currentDiscount;
                System.out.println(getItemName(productIds.get(i)) + ":$" + currentDiscount);
            }
            System.out.print("\n");
            System.out.println("Total Discount:\t$" + totalDiscount);
            System.out.print("Final total:$");
            String query = "select totalPrice from userOrder where id = " + userSelection;
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){System.out.println(Double.parseDouble(resultSet.getObject(1).toString()) - totalDiscount);}
            
            query = "update userOrder set paid = \'y\' where id = " + userSelection;
            statement.executeUpdate(query);
            System.out.println("Payed. Thank you.");
            
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static double getDiscountValue(String productId) throws SQLException {
        
        String query = "select categoryId from PinC where productId = " + productId;
        resultSet = statement.executeQuery(query);
        String categoryId = null;
        while(resultSet.next()){categoryId = resultSet.getObject(1).toString();}
        if(categoryId == null){return 0;}
        
        query = "select discountId from categoryAvailable where categoryId = " + categoryId;
        resultSet = statement.executeQuery(query);
        String discountId = null;
        while(resultSet.next()){discountId = resultSet.getObject(1).toString();}
        if(discountId == null){return 0;}
        
        query = "select value from Discount where id = " + discountId;
        resultSet = statement.executeQuery(query);
        while(resultSet.next()){return Double.parseDouble(resultSet.getObject(1).toString());}
        
        return 0.0;
    }
    
    public static String getItemName(String productId) throws SQLException {
        String query = "select name from Product where id = " + productId;
        resultSet = statement.executeQuery(query);
        while(resultSet.next()){return resultSet.getObject(1).toString();}
        return null;
    }
    
    public static void staffMenu() throws SQLException {
        System.out.print("\nEnter ID: ");
        String userId = input.nextLine();
        String password = null;
        String staffQuery = "select password from User where isStaff = \'y\' and id = " + userId;
        resultSet = statement.executeQuery(staffQuery);
        while(resultSet.next()){
            password = resultSet.getObject(1).toString();
        }
        
        
        if(password == null) {System.out.println("\nStaff ID not in database"); return;}
        
        System.out.print("Enter password: ");
        if(!input.nextLine().equals(password)) {System.out.println("\nIncorrect password"); return;}
        
        System.out.println("\nPlease make your selection:");
        System.out.println("1.  Edit User Acct.");
        System.out.println("2.  Edit Product");
        System.out.println("3.  Edit Order");
        System.out.println("4.  Edit Categories");
        System.out.println("5.  Edit Discount");
        System.out.println("6.  Edit Supplier Menu");
        System.out.println("7.  Edit Shelf Menu");
        System.out.println("8.  View Total Sales");
        System.out.println("9.  View Shelf Locations");
        System.out.println("10. Alerts");
        
        int userSelection = Integer.parseInt(input.nextLine());
        
        switch (userSelection){
            case 1: editUser(); break;
            case 2: editProduct(); break;
            case 3: editOrder(); break;
            case 4: editCategories(); break;
            case 5: editDiscount(); break;
            case 6: editSupplier(); break;
            case 7: editShelf(); break;
            case 8: Sales(); break;
            case 9: ShelfLocations(); break;
            case 10: Alerts(); break;
        }
    }
    
    
    public static void editUser() throws SQLException {
        System.out.println("\n1. Create New User");
        System.out.println("2. Edit Existing User");
        System.out.println("3. Delete Existing User\n");
        int userSelection = Integer.parseInt(input.nextLine());
        if(userSelection == 1) {newUser();}
        else if(userSelection == 2) {updateUserAdmin();}
        else if(userSelection == 3) {deleteUserAdmin();}
        else {return;}
    }
    
    public static void updateUserAdmin() throws SQLException {
        try {
            System.out.print("\nEnter user ID: ");
            String userId = input.nextLine();
            if(!checkDuplicate(userId, "User")) {System.out.println("\nUser Does Not Exist.");return;}
            System.out.print("\nNew name: ");
            String username = input.nextLine();
            System.out.print("\nNew password: ");
            String password = input.nextLine();
            System.out.print("\nNew address: ");
            String address = input.nextLine();
            System.out.print("\nNew email: ");
            String email = input.nextLine();
            String update = "update User set name = \'" + username + "\', address = \'" + address + "\', email = \'" + email + "\', password = \'" + password + "\' where id = \'" + userId + "\'";
            statement.executeUpdate(update);
            System.out.println("\nUpdate Complete\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void deleteUserAdmin() throws SQLException {
        try {
            System.out.print("\nEnter ID: ");
            String userId = input.nextLine();
            if(!checkDuplicate(userId, "User")) {System.out.println("\nuser ID does not exist.");return;}
            String update = "delete from User where id = \'" + userId + "\'";
            statement.executeUpdate(update);
            System.out.println("\nAccount deleted.\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void editProduct() throws SQLException {
        System.out.println("\n1. Create Product");
        System.out.println("2. Edit Product");
        System.out.println("3. Delete Product");
        int userSelection = Integer.parseInt(input.nextLine());
        if(userSelection == 1) {createNew();}
        else if(userSelection == 2) {updateItem();}
        else if(userSelection == 3) {deleteItem();}
        else {System.out.println("Invalid choice");return;}
    }
    
    public static void createNew() throws SQLException {
        try {
            System.out.print("\nEnter product ID: ");
            String productId = input.nextLine();
            if(checkDuplicate(productId, "Product")) {System.out.println("\nProduct ID already exists");return;}
            System.out.print("\nName: ");
            String name = input.nextLine();
            System.out.print("\nDescription: ");
            String description = input.nextLine();
            System.out.print("\nQuantity: ");
            String quantity = input.nextLine();
            System.out.print("\nPrice: ");
            String price = input.nextLine();
            String update = "insert into Product values (" + productId + ", \'" + name + "\', \'" + description + "\', " + quantity + ", " + price + ", \'y\')";
            statement.executeUpdate(update);
            System.out.println("\nProduct Created\n");
            
            
            System.out.print("Place product in category? \'y\' or \'n\' ");
            boolean category = input.nextLine().equals("y");
            if(!category) {return;}
            System.out.print("\nCategory ID: ");
            String categoryId = input.nextLine();
            if(!checkDuplicate(categoryId, "Category")) {System.out.println("\nCategory ID does not exist.");return;}
            String query = "insert into PinC values (" + categoryId + ", " + productId + ")";
            statement.executeUpdate(query);
            System.out.println("Product now has a category.");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void updateItem() throws SQLException {
        try {
            System.out.print("\nEnter ID: ");
            String productId = input.nextLine();
            if(!checkDuplicate(productId, "Product")) {System.out.println("\nProduct ID does not exist");return;}
            System.out.print("\nNew name: ");
            String name = input.nextLine();
            System.out.print("\nNew description: ");
            String description = input.nextLine();
            System.out.print("\nNew quantity: ");
            String quantity = input.nextLine();
            System.out.print("\nNew price: ");
            String price = input.nextLine();
            System.out.print("\nActive? (y) or (n): ");
            String active = input.nextLine().equals("n") ? "n" : "y";
            String update = "update Product set name = \'" + name + "\', description = \'" + description + "\', stockQuantity = " + quantity + ", price = " + price + ", active = \'" + active + "\' where id = " + productId;
            statement.executeUpdate(update);
            
            System.out.print("Place product in category? (\'y\' or \'n\') ");
            boolean category = input.nextLine().equals("y");
            if(!category) {System.out.println("\nProduct updated.\n");return;}
            System.out.print("\nCategory ID: ");
            String categoryId = input.nextLine();
            if(!checkDuplicate(categoryId, "Category")) {System.out.println("\nCategory ID does not exist.\n"); return;}
            update = "insert into PinC values (" + categoryId + ", " + productId + ")";
            statement.executeUpdate(update);
            System.out.println("Product now has a category.");
            System.out.println("\nProduct updated.\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void deleteItem() throws SQLException {
        try {
            System.out.print("\nEnter ID: ");
            String productId = input.nextLine();
            if(!checkDuplicate(productId, "Product")) {System.out.println("\nProduct ID already exists.");return;}
            String update = "delete from Product where id = " + productId;
            statement.executeUpdate(update);
            System.out.println("\nProduct deleted.\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    
    public static void editOrder() throws SQLException {
        System.out.println("\n1. Create Order");
        System.out.println("2. Edit Order");
        System.out.println("3. Delete Order");
        int userSelection = Integer.parseInt(input.nextLine());
        if(userSelection == 1) {createOrder();}
        else if(userSelection == 2) {updateOrder();}
        else if(userSelection == 3) {deleteOrder();}
        else {System.out.println("Invalid choice"); return;}
    }
    
    public static void createOrder() throws SQLException {
        try {
            System.out.print("\nEnter orderId: ");
            String orderId = input.nextLine();
            if(checkDuplicate(orderId, "userOrder")) {System.out.println("\nOrder ID already exists."); return;}
            System.out.print("\nName: ");
            String name = input.nextLine();
            System.out.print("\nPrice: ");
            String price = input.nextLine();
            System.out.print("\nEnter date as DD-MMM-YY: ");
            String date = input.nextLine();
            System.out.print("\nEnter customer ID: ");
            String customerId = input.nextLine();
            if(!checkDuplicate(customerId, "User")) {System.out.println("Customer ID does not exist.");return;}
            
            String update = "insert into userOrder values (" + orderId + ", " + price + ", \'" + date + "\', \'n\')";
            statement.executeUpdate(update);
            update = "insert into Orders values (" + customerId + ", " + orderId + ")";
            statement.executeUpdate(update);
            
            System.out.println("\nOrder Created.\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    
    public static void updateOrder() throws SQLException {
        try {
            System.out.print("\nEnter order ID: ");
            String orderId = input.nextLine();
            if(!checkDuplicate(orderId, "userOrder")) {System.out.println("\nOrder ID does not exist."); return;}
            System.out.print("\nNew price: ");
            String price = input.nextLine();
            System.out.print("\nNew date as DD-MMM-YY format: ");
            String date = input.nextLine();
            String update = "update userOrder set totalPrice = " + price + ", orderDate = \'" + date + "\' where id = " + orderId;
            statement.executeUpdate(update);
            System.out.println("\nOrder Updated\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void deleteOrder() throws SQLException {
        try {
            System.out.print("\nEnter order ID: ");
            String orderId = input.nextLine();
            if(!checkDuplicate(orderId, "userOrder")) {System.out.println("\norder ID does not exist.");return;}
            String update = "delete from userOrder where id = \'" + orderId + "\'";
            statement.executeUpdate(update);
            System.out.println("\nOrder Deleted\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void editCategories() throws SQLException {
        System.out.println("\n1. Create Category");
        System.out.println("2. Edit Category");
        System.out.println("3. Delete Category");
        int userSelection = Integer.parseInt(input.nextLine());
        if(userSelection == 1) {createCategory();}
        else if(userSelection == 2) {updateCategory();}
        else if(userSelection == 3) {deleteCategory();}
        else {System.out.println("Invalid choice"); return;}
    }
    
    public static void createCategory() throws SQLException {
        try {
            System.out.print("\nEnter category ID: ");
            String categoryId = input.nextLine();
            if(checkDuplicate(categoryId, "Category")) {System.out.println("\nThat ID is already taken");return;}
            System.out.print("\nName: ");
            String name = input.nextLine();
            System.out.print("\nDescription: ");
            String description = input.nextLine();
            String update = "insert into Category values (" + categoryId + ", \'" + name + "\', \'" + description + "\')";
            statement.executeUpdate(update);
            System.out.println("\nCategory inserted.\n");
            
            
            System.out.print("Does this category have a parent? (\'y\' or \'n\') ");
            boolean parent = input.nextLine().equals("y");
            if(parent) {
                System.out.print("\nEnter the parent category's ID: ");
                String parentId = input.nextLine();
                if(categoryId.equals(parentId)) { System.out.println("\nSelf cannot be parent."); return;}
                if(!checkDuplicate(categoryId, "Category")) {System.out.println("\nID does not exist"); return;}
                update = "insert into Parent values (" + parentId + ", " + categoryId + ")";
                statement.executeUpdate(update);
                System.out.println("Category parent created.");
            }
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void updateCategory() throws SQLException {
        try {
            System.out.print("\nEnter category ID: ");
            String categoryId = input.nextLine();
            
            if(!checkDuplicate(categoryId, "Category")) {System.out.println("\nID does not exist"); return;}
            System.out.print("\nNew name: ");
            String name = input.nextLine();
            System.out.print("\nNew description: ");
            String description = input.nextLine();
            String update = "update Category set name = \'" + name + "\', description = \'" + description + "\' where id = " + categoryId;
            statement.executeUpdate(update);
            System.out.println("\nUpdate Complete.\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void deleteCategory() throws SQLException {
        try {
            System.out.print("\nEnter category ID: ");
            String categoryId = input.nextLine();
            if(!checkDuplicate(categoryId, "Category")) {System.out.println("\nID does not exist");return;}
            String update  = "delete from Category where id = " + categoryId;
            statement.executeUpdate(update);
            System.out.println("\nDeletion Complete\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void editDiscount() throws SQLException {
        System.out.println("\n1. Create Discount");
        System.out.println("2. Edit Discount");
        System.out.println("3. Delete Discount");
        int userSelection = Integer.parseInt(input.nextLine());
        if(userSelection== 1) {createDiscount();}
        else if(userSelection == 2) {updateDiscount();}
        else if(userSelection == 3) {deleteDiscount();}
        else {System.out.println("Invalid choice"); return;}
    }
    
    public static void createDiscount() throws SQLException {
        try {
            System.out.print("\nEnter discount ID: ");
            String discountId = input.nextLine();
            if(checkDuplicate(discountId, "Discount")) {System.out.println("\ndiscountId already exists."); return;}
            System.out.print("\nName: ");
            String name = input.nextLine();
            System.out.print("\nValue: $");
            double value = Double.parseDouble(input.nextLine());
            String update = "insert into Discount values (" + discountId + ", \'" + name + "\', " + value + ")";
            statement.executeUpdate(update);
            System.out.println("\nDiscount Created\n");
            
            
            System.out.println("Discount: Product - 1 or Category -2 ");
            String temp = input.nextLine().equals("1") ? "Product" : "Category";
            String table = temp.equals("Product") ? "discountAvailable" : "categoryAvailable";
            System.out.print("Enter ID: ");
            String id = input.nextLine();
            if(!checkDuplicate(id, temp)) {System.out.println("ID does not exist");return;}
            update = "insert into " + table + " values (" + discountId + ", " + id + ")";
            statement.executeUpdate(update);
            System.out.println("Discount created.");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void updateDiscount() throws SQLException {
        try {
            System.out.print("\nEnter discout ID: ");
            String discountId = input.nextLine();
            if(!checkDuplicate(discountId, "Discount")) {System.out.println("\ndiscountId does not exist.");return;}
            System.out.print("\nNew name: ");
            String name = input.nextLine();
            System.out.print("\nNew value: ");
            double price = Double.parseDouble(input.nextLine());
            String update = "update Discount set name = \'" + name + "\', value = " + price + " where id = " + discountId;
            statement.executeUpdate(update);
            System.out.println("\nDiscount Updated\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void deleteDiscount() throws SQLException {
        try {
            System.out.print("\nEnter discount ID: ");
            String discountId = input.nextLine();
            if(!checkDuplicate(discountId, "Discount")) {System.out.println("\nDiscount ID does not exist"); return;}
            String update = "delete from Discount where id = " + discountId;
            statement.executeUpdate(update);
            System.out.println("\nDiscount Deleted\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void editSupplier() throws SQLException {
        System.out.println("\n1. Create Supplier");
        System.out.println("2. Assign product");
        int userSelection = Integer.parseInt(input.nextLine());
        if(userSelection == 1) {createSupplier();}
        else if(userSelection== 2) {giveProduct();}
        else {System.out.println("Invalid choice"); return;}
    }
    
    public static void createSupplier() throws SQLException {
        try {
            System.out.print("\nEnter supplier ID: ");
            String supplierId = input.nextLine();
            if(checkDuplicate(supplierId, "Supplier")) {System.out.println("\nSupplier ID already exists"); return;}
            System.out.print("\nName: ");
            String name = input.nextLine();
            String update = "insert into Supplier values (" + supplierId + ", \'" + name + "\')";
            statement.executeUpdate(update);
            System.out.println("\nSupplier created.\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void giveProduct() throws SQLException {
        try {
            System.out.print("\nEnter Supplier ID: ");
            String supplierId = input.nextLine();
            System.out.print("\nEnter Product ID: ");
            String productId = input.nextLine();
            if(!checkDuplicate(supplierId, "Supplier") || !checkDuplicate(productId, "Product")) {
                System.out.println("\nID does not exist."); return;
            }
            
            String update = "insert into Supplys values (" + supplierId + ", " + productId + ")";
            statement.executeUpdate(update);
            System.out.println("\nSupplier now has given product.\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void editShelf() throws SQLException {
        System.out.println("\n1. Create Shelf");
        System.out.println("2. Assign Shelf");
        int userSelection = Integer.parseInt(input.nextLine());
        if(userSelection == 1) {createShelf();}
        else if(userSelection == 2) {attachShelf();}
        else { System.out.println("Invalid choice"); return;}
    }
    
    public static void createShelf() throws SQLException {
        try {
            System.out.print("\nEnter shelf ID: ");
            String shelfId = input.nextLine();
            if(checkDuplicate(shelfId, "Shelf")) {System.out.println("\nShelf ID already exists"); return;}
            System.out.print("\nName: ");
            String name = input.nextLine();
            System.out.print("\nAvail. quantity: ");
            String quantity = input.nextLine();
            String update = "insert into Shelf values (" + shelfId + ", \'" + name + "\', " + quantity + ")";
            statement.executeUpdate(update);
            System.out.println("\nShelf created\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void attachShelf() throws SQLException {
        try {
            System.out.print("\nProduct ID: ");
            String productId = input.nextLine();
            System.out.print("\nShelf ID: ");
            String shelfId = input.nextLine();
            
            if(!checkDuplicate(productId, "Product") || !checkDuplicate(shelfId, "Shelf")) {
                System.out.println("\nID does not exist");return;
            }
            
            String update = "insert into Location values (" + productId + ", " + shelfId + ")";
            statement.executeUpdate(update);
            System.out.println("\nProduct Assigned\n");
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void Sales() throws SQLException {
        try {
            String query = "select Supplier.name, sum(Product.price) from itContains join Supplys on itContains.productId = Supplys.productId join Product on itContains.productId = Product.id join Supplier on Supplys.supplierId = Supplier.id group by Supplier.name";
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                System.out.println(resultSet.getObject(1).toString() + "\t$" + resultSet.getObject(2).toString());
            }
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    
    public static void ShelfLocations() throws SQLException {
        try {
            System.out.print("\nEnter Order ID: ");
            String orderId = input.nextLine();
            
            if(!checkDuplicate(orderId, "userOrder")) {System.out.println("\nOrder ID does not exist");return;}
            String query = "select productId from itContains where orderId = " + orderId;
            resultSet = statement.executeQuery(query);
            
            List<String> list = new ArrayList<String>();
            while(resultSet.next()){list.add(resultSet.getObject(1).toString());}
            
            for(String element: list) {
                query = "select Product.name, Shelf.name from Location join Product on Location.productId = Product.id join Shelf on Location.shelfId = Shelf.id where Location.productId = " + element;
                resultSet = statement.executeQuery(query);
                while(resultSet.next())
                    System.out.println(resultSet.getObject(1).toString() + "\t" + resultSet.getObject(2).toString());
            }
        }
        catch(SQLException e) {System.out.println("SQLException: " + e.getMessage());}
        catch(Exception e) {System.out.println("Exception: " + e.getMessage());}
    }
    public static void Alerts() throws SQLException {
        try {
            String stockCheck = "select id, stockQuantity from Product";
            resultSet = statement.executeQuery(stockCheck);
            List<String> idList = new ArrayList<String>();
            List<String> stockList = new ArrayList<String>();
            while(resultSet.next()){
                idList.add(resultSet.getObject(1).toString());
                stockList.add(resultSet.getObject(2).toString());
            }
            String x;
            int stock;
            System.out.println("The following Products have less than 5 in stock:");
            for(int i = 0; i < idList.size(); i++) {
                
                x = stockList.get(i);
                stock = Integer.parseInt(x);
                
                if(stock < 5) {
                    System.out.println("Product ID: " + idList.get(i));
                    System.out.println("Stock: " + stockList.get(i));
                }
            }
            
        } catch(SQLException e) {
            System.out.println("SQLException:" + e.getMessage() + " <BR>");
        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage() + " <BR>");
        }
    }
    
}