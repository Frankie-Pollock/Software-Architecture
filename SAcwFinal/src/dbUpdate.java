/**
 * 
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * @author Frank
 *
 */
public class dbUpdate {
	
	public static void UpdateStock(int invID) {
		Connection connection = null;
	    try {
	    	System.out.println("DOING IT");
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/central_inv_sys", "root", "");
	    
	        String query = "UPDATE cinventory SET Stock = Stock - 2 WHERE ProductID = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, invID);
	        // Execute the query
            statement.executeUpdate();
	        // Close the database connection
	        connection.close();
            return;
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	public static void UpdateInv(int invID) {
		Connection connection = null;
	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/set10101cw", "root", "");
	    
	        String query = "UPDATE inventory SET stockQuantity = stockQuantity + 2 WHERE inventoryID = ?";
	        
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, invID);
	        // Execute the query
            statement.executeUpdate();
	        // Close the database connection
	        connection.close();
            return;
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	public static void UpdateStockCust(int invID, int stockAmt) {
		Connection connection = null;
	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/set10101cw", "root", "");
	    
	        String query = "UPDATE inventory SET stockQuantity = stockQuantity - ? WHERE inventoryID = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, stockAmt);
            statement.setInt(2, invID);
	        // Execute the query
            statement.executeUpdate();
	        // Close the database connection
	        connection.close();
            return;
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	public static void updatedInfo(Inventory inventory, int invID) {
		Connection connection = null;
	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/set10101cw", "root", "");
	    
	        String query = "UPDATE inventory SET invName = ?, price = ?, discount = ?, loyal = ?  WHERE inventoryID = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, inventory.getProductName());
	        preparedStatement.setDouble(2, inventory.getPrice());
	        preparedStatement.setDouble(3, inventory.getDiscount());
	        preparedStatement.setInt(4, inventory.getLoyalty());
	        preparedStatement.setDouble(5, invID);

	        // Execute the query
	        preparedStatement.executeUpdate();

	        // Close the PreparedStatement
	        preparedStatement.close();
	        // Close the database connection
	        connection.close();
            return;
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
}
