import java.util.ArrayList;

import javax.swing.JOptionPane;

public class ShopperApp {

    private String branchID;
    private String custID;
    private boolean isLoyal;

    public ShopperApp(String id) {
        // Constructor to set branchID, custID, and loyalty status based on input
        branchID = JOptionPane.showInputDialog(null, "Enter Branch Code (1 north, 2 east, 3 south, 4 west or 5 central:");
        custID = id;
        isLoyal = dbQueries.isLoyal(id);
    }

    public ArrayList<Inventory> getInventory() {
        // Method to retrieve inventory based on branchID
        return dbQueries.getInventory(branchID);
    }

    public void updateStock(int invID, int stockAmt) {
        // Method to update stock quantity for a specific inventory item
        dbUpdate.UpdateStockCust(invID, stockAmt);
    }
    
    public void insertSale(int saleID, int bID, int cID, double total, double discount, double profit) {
        // Method to insert a new sale into the database
        dbInsert.insertSale(saleID, bID, cID, total, discount, profit);
    }

    public boolean isLoyal() {
        // Method to check if the customer is loyal
        return isLoyal;
    }

    public String getBranchID() {
        // Method to retrieve the branchID associated with the shopper
        return branchID;
    }

    public String getCustID() {
        // Method to retrieve the customer ID
        return custID;
    }
}