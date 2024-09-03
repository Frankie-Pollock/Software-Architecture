

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class dbQueries {
	// This method checks if a manager ID exists in the database
	public static boolean mandID(String id) 
	{
	    Connection connection = null;
	    try {
	        // Load the MySQL JDBC driver and establish a connection
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/set10101cw", "root", "");
	        // Prepare SQL query to select manager based on ID
	        String query = "SELECT * FROM manager WHERE id = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, id);
	        // Execute the query and get the results
	        ResultSet resultSet = preparedStatement.executeQuery();
	        // Check if the ID exists in the result set
	        if (resultSet.next()) {
	            return true; 
	        }
	        // Close the ResultSet and database connection
	        resultSet.close();
	        connection.close();
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
	    // ID does not exist in the database
	    return false; 
	}
	// This method checks if a shop ID exists in the database
	public static boolean shopID(String id) 
	{
	    Connection connection = null;
	    try {
	        // Establish a database connection
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/set10101cw", "root", "");
	    
	        // Prepare SQL query to select a customer based on ID
	        String query = "SELECT * FROM customer WHERE id = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, id);

	        // Execute the query and obtain the results
	        ResultSet resultSet = preparedStatement.executeQuery();

	        // Check if the ID exists in the result set
	        if (resultSet.next()) {
	            return true; 
	        }
	        // Close the database connection
	        connection.close();
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
	    // ID does not exist in the database
	    return false; 
	}
	// This method retrieves the branch ID for a manager in the database
	public static String bID(String id) {
		Connection connection = null;
			try {
				// Establish a database connection
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/set10101cw", "root", "");

				// Prepare SQL query to select branch ID based on ManagerID
				String query = "SELECT branchID FROM BRANCH WHERE ManagerID = ?";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setString(1, id);

				// Execute the query and get the results
				ResultSet resultSet = preparedStatement.executeQuery();

				// Check if the ID exists
				if (resultSet.next()) {
					return resultSet.getString("branchID");
				}
				// Close the database connection
				connection.close();
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
    return "";
}
	// This method retrieves the inventory for a branch in the database
	public static ArrayList<Inventory> getInventory(String bID) {
		ArrayList<Inventory> inventoryList = new ArrayList<>();
		Connection connection = null;
		try {
        // Establish a database connection
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/set10101cw", "root", "");

        // Prepare SQL query to select inventory based on branchID
        String query = "SELECT * FROM inventory WHERE branchID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, bID);

        // Execute the query and get the results
        ResultSet resultSet = preparedStatement.executeQuery();

        // Check if the ID exists
        	while (resultSet.next()) {
            // Retrieve inventory details and create Inventory objects
            int inventoryID = resultSet.getInt("inventoryID");
            String invName = resultSet.getString("invName");
            int stockQuantity = resultSet.getInt("stockQuantity");
            double price = resultSet.getDouble("price");
            int discount = resultSet.getInt("discount");
            int loyalty = resultSet.getInt("Loyal");
            Inventory inventory = new Inventory(inventoryID, invName, stockQuantity, price, discount, loyalty);
            inventoryList.add(inventory);
        	}
        // Close the database connection
        connection.close();
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
    return inventoryList;
}
	// This method retrieves the row of data matching invID in the database
	public static ArrayList<Inventory> getInventoryById(int invID) {
	    ArrayList<Inventory> inventory = new ArrayList<>();
	    Connection connection = null;
	    try {
	        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/set10101cw", "root", "");

	        // Prepare SQL query to retrieve inventory based on inventoryID
	        String query = "SELECT * FROM inventory WHERE inventoryID = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setInt(1, invID);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        // Retrieve inventory details from the result set
	        while (resultSet.next()) {
	            int inventoryID = resultSet.getInt("inventoryID");
	            String invName = resultSet.getString("invName");
	            double price = resultSet.getDouble("price");
	            int discount = resultSet.getInt("discount");
	            int loyalty = resultSet.getInt("Loyal");
	            Inventory inventoryList = new Inventory(inventoryID, invName, price, discount, loyalty);
	            inventory.add(inventoryList);
	        }
	    } catch (SQLException e) {
	        // Print error message in case of SQL exception
	        System.out.println("Error executing query: " + e.getMessage());
	    } finally {
	        // Close the database connection in the finally block
	        if (connection != null) {
	            try {
	                connection.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return inventory;
	}

	public static ArrayList<Inventory> getLowStock (String bID)
	{
	ArrayList<Inventory> inventoryList = new ArrayList<>();


	Connection connection = null;
    try {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/set10101cw", "root", "");
    
        String query = "SELECT * FROM inventory WHERE branchID = ? AND stockQuantity <= 3";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, bID);
        // Execute the query and get the results
        ResultSet resultSet = preparedStatement.executeQuery();

        // Check if the ID exists
        while  (resultSet.next()) {
            int inventoryID = resultSet.getInt("inventoryID");
            String invName = resultSet.getString("invName");
            int stockQuantity = resultSet.getInt("stockQuantity");
            double price = resultSet.getDouble("price");
            int discount = resultSet.getInt("discount");
            int loyalty = resultSet.getInt("Loyal");
            Inventory inventory = new Inventory(inventoryID, invName, stockQuantity, price, discount,loyalty );
            inventoryList.add(inventory);

        }
        
        // Close the database connection
        connection.close();
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
    }return inventoryList;
   }

	public static boolean checkStock (int invID) {	
		Connection connection = null;
	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/central_inv_sys", "root", "");
	    
	        String query = "SELECT * FROM cinventory WHERE ProductID = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setInt(1, invID);
	        // Execute the query and get the results
	        ResultSet resultSet = preparedStatement.executeQuery();

	        // Check if the ID exists
	        if (resultSet.next()) {
	            if(resultSet.getInt("Stock") >= 2) {
	            	return true;
	            }
	            else {
	            	return false;
	            }
	        }
	        // Close the database connection
	        connection.close();
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
	    
		return false;
		}

	public static boolean isLoyal (String id)
	{
	Connection connection = null;
    try {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/set10101cw", "root", "");
    
        String query = "SELECT * FROM customer WHERE id = ? AND loyal = 1";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, id);
        // Execute the query and get the results
        ResultSet resultSet = preparedStatement.executeQuery();

        // Check if the ID exists
        if (resultSet.next()) {
            return true;
        }
        
        // Close the database connection
        connection.close();
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
    
	return false;
	}
	
	// This method retrieves sale of a branch using branchID in the database
	public static ArrayList<Sale> getBranchPerf(int branchID) {
		ArrayList<Sale> sale = new ArrayList<>();
		Connection connection = null;

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/set10101cw", "root", "");

			// Prepare SQL query to retrieve sales data based on branchID
			String query = "SELECT * FROM sale WHERE branchID = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, branchID);
			ResultSet resultSet = preparedStatement.executeQuery();
        
			// Retrieve sales data from the result set
			while (resultSet.next()) {
				int saleID = resultSet.getInt("saleID");
				int bID = resultSet.getInt("branchID");
				int cID = resultSet.getInt("customerID");
				double total = resultSet.getDouble("totalAmount");
				double discount = resultSet.getDouble("discountAmount");
				double profit = resultSet.getDouble("profitAmount");
				Sale saleList = new Sale(saleID, bID, cID, total, discount, profit);
				sale.add(saleList);
			}
		} catch (SQLException e) {
			// Print error message in case of SQL exception
			System.out.println("Error executing query: " + e.getMessage());
		} finally {
			// Close the database connection in the finally block
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
            e.printStackTrace();
            }
        }
    }
  return sale;
}

}
