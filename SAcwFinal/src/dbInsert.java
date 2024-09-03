

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class dbInsert {
	public static void insertSale(int saleID, int bID, int cID, double total, double discount, double profit) {
		Connection connection = null;
	    try {
	        Class.forName("com.mysql.jdbc.Driver");
	        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/set10101cw", "root", "");
	        // query to insert a sale row into the Sale table
	        String query = "INSERT INTO Sale (saleID, branchID, customerID, totalAmount, discountAmount, profitAmount) VALUES (?, ?, ?, ?, ?, ?)";
	        // prepared statement using the query
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        // Set the values for in prepared statement
	        preparedStatement.setInt(1, saleID);
	        preparedStatement.setInt(2, bID);
	        preparedStatement.setInt(3, cID);
	        preparedStatement.setDouble(4, total);
	        preparedStatement.setDouble(5, discount);
	        preparedStatement.setDouble(6, profit);

	        // Execute the query to insert the sale into the database
	        preparedStatement.executeUpdate();

	        // Close the PreparedStatement
	        preparedStatement.close();
	        // Close the database connection
	        connection.close();
	        
	        // Return after successful insertion
	        return;
	    } catch (ClassNotFoundException | SQLException e) {
	        // Print any exceptions that occur
	        e.printStackTrace();
	    } finally {
	        // Close the connection in the event of an exception or successful insertion
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
