import java.util.ArrayList;

public class ManagerApp {

    private static String branchID;

    public ManagerApp(String branchID) {
        // Constructor to set the branchID
        ManagerApp.branchID = branchID;
    }

    public static ArrayList<Inventory> getInventory() {
        // Method to retrieve inventory based on branchID
        return dbQueries.getInventory(branchID);
    }

    public static ArrayList<Sale> getBranchPerf() {
        // Method to retrieve branch performance data based on branchID
        return dbQueries.getBranchPerf(Integer.parseInt(branchID));
    }

    public static void updateInventory(int invID) {
        // Method to update inventory
        dbUpdate.UpdateInv(invID);
    }

    public static boolean checkStock(int invID) {
        // Method to check stock availability
        return dbQueries.checkStock(invID);
    }
}
