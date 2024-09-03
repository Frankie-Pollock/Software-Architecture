import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class ManagerUI extends JFrame implements ActionListener {

    private JButton branchPerformanceButton;
    private JButton inventoryViewButton;
    private String branchID;
    private DefaultTableModel tableModel;
    private JTable inventoryTable;
    private JPanel inventoryPanel;
    private JPanel editPanel;
    private JTextField nameField;
    private JTextField invIDField;
    private JButton stockButton;
    private JTextField priceField;
    private JComboBox<String> discountComboBox;
    private JComboBox<String> loyaltyComboBox;
    private JButton updateButton;

    public ManagerUI(String id) {
        branchID = id;

        // Setting up the JFrame
        setTitle("Manager UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creating buttons and setting their actions
        branchPerformanceButton = new JButton("Branch Performance");
        inventoryViewButton = new JButton("Inventory View");
        updateButton = new JButton("Click to Update Inventory"); 
        // Hiding the update button initially
        updateButton.setVisible(false); 

        branchPerformanceButton.addActionListener(this);
        inventoryViewButton.addActionListener(this);
        updateButton.addActionListener(this); 

        // Creating a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        // Increase grid layout to accommodate new button
        buttonPanel.setLayout(new GridLayout(3, 1)); 
        // Add buttons to button panel
        buttonPanel.add(branchPerformanceButton);
        buttonPanel.add(inventoryViewButton);
        buttonPanel.add(updateButton); 

        // Creating the main panel for the UI
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.WEST);

        // Configuring the JFrame
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.7),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7));
        setLocationRelativeTo(null);
        setVisible(true);

        // Retrieving low inventory items
        ArrayList<Inventory> lowInventory = dbQueries.getLowStock(branchID);

        // Generating a message for low inventory items
        StringBuilder message = new StringBuilder();
        for (Inventory item : lowInventory) {
            message.append(item.toString()).append("\n");
        }

        // Displaying a message dialog if low inventory items exist
        if (!lowInventory.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The following Items are in need of a stock request: ");

            JOptionPane.showMessageDialog(null, message.toString());

        }
    }


    @SuppressWarnings("unused")
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == branchPerformanceButton) {
            // Retrieve branch performance data and write it to a file
            ArrayList<Sale> sale = dbQueries.getBranchPerf(Integer.parseInt(branchID));
            String fileName = "sales.txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (Sale sales : sale) {
                    // Write each sale item to the file
                    writer.write(sale.toString());
                    writer.newLine();
                }
                // Open the file using note pad
                ProcessBuilder pb = new ProcessBuilder("notepad.exe", fileName);
                pb.start();
            } catch (IOException e2) {
                e2.printStackTrace();
            }

        } else if (e.getSource() == inventoryViewButton) {
            // Display inventory and update the button text
            displayInventory();
            inventoryViewButton.setText("Refresh Inventory");
            updateButton.setVisible(true);
        } else if (e.getSource() == stockButton) {
            // Check stock availability for a selected inventory item
            int selectedR = inventoryTable.getSelectedRow();
            if (selectedR != -1) {
                int invID = (int) tableModel.getValueAt(selectedR, 0);
                boolean hasStock = dbQueries.checkStock(invID);
                if (hasStock) {
                    JOptionPane.showMessageDialog(null, "Stock Found, Processing Request...");
                    dbUpdate.UpdateStock(invID);
                    dbUpdate.UpdateInv(invID);
                    displayInventory();
                }
            }
        } else if (e.getSource() == updateButton) {
            // Handle update button action
            int selectedRow = inventoryTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get original data from the table
                int invID = (int) tableModel.getValueAt(selectedRow, 0);
                String OGproductName = (String) tableModel.getValueAt(selectedRow, 1);
                double OGprice = (double) tableModel.getValueAt(selectedRow, 3);
                int OGdiscount = (int) tableModel.getValueAt(selectedRow, 4);
                int OGloyalty = (int) tableModel.getValueAt(selectedRow, 5);

                // Get updated data
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int discount = discountComboBox.getSelectedIndex();
                int loyalty = loyaltyComboBox.getSelectedIndex();

                // Create inventory objects for original and updated data
                Inventory inventory = new Inventory(invID, name, price, discount, loyalty);
                Inventory ogInventory = new Inventory(invID, OGproductName, OGprice, OGdiscount, OGloyalty);

                // Check for changes and update if necessary
                if (ogInventory != null && inventory.getPrice() == ogInventory.getPrice()
                        && inventory.getDiscount() == ogInventory.getDiscount()
                        && inventory.getLoyalty() == ogInventory.getLoyalty()
                        && inventory.getProductName() == ogInventory.getProductName()) {
                    JOptionPane.showMessageDialog(null, "Nothing to update");
                } else {
                    dbUpdate.updatedInfo(inventory, invID);
                    JOptionPane.showMessageDialog(null, "Inventory updated");
                    // Refresh displayed inventory
                    displayInventory(); 
                }
            }
        }
    }


    private void displayInventory() {
        // Retrieve inventory data based on the branch ID
        ArrayList<Inventory> inventory = dbQueries.getInventory(branchID);
        if (inventory != null && !inventory.isEmpty()) {
            // Prepare data for the table
            String[] columnNames = {"Product ID", "Name", "Stock", "Price", "Discount", "Loyalty"};
            Object[][] data = new Object[inventory.size()][6];

            // Populate data array with inventory information
            for (int i = 0; i < inventory.size(); i++) {
                Inventory item = inventory.get(i);
                data[i][0] = item.getProductId();
                data[i][1] = item.getProductName();
                data[i][2] = item.getQuantity();
                data[i][3] = item.getPrice();
                data[i][4] = item.getDiscount();
                data[i][5] = item.getLoyalty();
            }

            // Create a table model with the retrieved data
            tableModel = new DefaultTableModel(data, columnNames);

            // Create a JTable to display inventory data
            inventoryTable = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                	// Disable cell editing
                    return false; 
                }
            };
            // Set font size
            inventoryTable.setFont(inventoryTable.getFont().deriveFont(16.0f)); 

            // Add a mouse listener to handle clicks on the table rows
            inventoryTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int row = inventoryTable.getSelectedRow();
                    if (row != -1) {
                        int invID = (int) tableModel.getValueAt(row, 0);
                        String name = (String) tableModel.getValueAt(row, 1);
                        int stock = (int) tableModel.getValueAt(row, 2);
                        double price = (double) tableModel.getValueAt(row, 3);
                        int discount = (int) tableModel.getValueAt(row, 4);
                        int loyalty = (int) tableModel.getValueAt(row, 5);
                        updateEditPanel(invID, name, stock, price, discount, loyalty);
                    }
                }
            });

            // Update the inventory display panel
            updateInventoryPanel();
        } else {
            JOptionPane.showMessageDialog(null, "Inventory is empty");
        }
    }

    private void updateInventoryPanel() {
        if (inventoryPanel != null) {
        	// Remove previous inventoryPanel if it exists
            remove(inventoryPanel); 
        }

        // Create inventoryPanel to display inventory data in a scrolling table
        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BorderLayout());
        inventoryPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        // Create an editPanel to display editable fields for inventory items
        editPanel = new JPanel();
        editPanel.setLayout(new GridLayout(6, 2));

        // Initialise text fields and buttons for inventory editing
        invIDField = new JTextField();
        nameField = new JTextField();
        stockButton = new JButton("Click this button for a restock request");
        priceField = new JTextField();
        discountComboBox = new JComboBox<>(new String[]{"", "BOGOF", "Free Delivery", "3 for 2"});
        loyaltyComboBox = new JComboBox<>(new String[]{"---PLEASE SELECT AN OPTION BELOW---", "No", "Yes"});

        // Add labels and corresponding fields to the editPanel
        editPanel.add(new JLabel("Product ID:"));
        editPanel.add(invIDField);
        editPanel.add(new JLabel("Name:"));
        editPanel.add(nameField);
        editPanel.add(new JLabel("Stock:"));
        editPanel.add(stockButton);
        editPanel.add(new JLabel("Price:"));
        editPanel.add(priceField);
        editPanel.add(new JLabel("Discount:"));
        editPanel.add(discountComboBox);
        editPanel.add(new JLabel("Loyalty:"));
        editPanel.add(loyaltyComboBox);

        // Add action listener to the stockButton for handling stock requests
        stockButton.addActionListener(this);

        // Add editPanel to inventoryPanel and update the UI
        inventoryPanel.add(editPanel, BorderLayout.SOUTH);
        add(inventoryPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }


    private void updateEditPanel(int invID, String name, int stock, double price, int discount, int loyalty) {
        // Set the text of the fields based on the received data
        invIDField.setText(String.valueOf(invID));
        nameField.setText(name);
        stockButton.setText("Click this button for a restock request");
        priceField.setText(String.valueOf(price));
        
        // Set the selected items in the combo boxes based on the received discount and loyalty values
        discountComboBox.setSelectedItem(getDiscountOption(discount));
        loyaltyComboBox.setSelectedItem(getLoyaltyOption(loyalty));
    }

    // Returns the appropriate discount option string based on the received discount value
    private String getDiscountOption(int discount) {
        switch (discount) {
            case 1:
                return "BOGOF";
            case 2:
                return "Free Delivery";
            case 3:
                return "3 for 2";
            default:
                return "";
        }
    }

    // Returns the loyalty option string based on the received loyalty value (1 or 0)
    private String getLoyaltyOption(int loyalty) {
        return loyalty == 1 ? "1" : "0";
    }

}