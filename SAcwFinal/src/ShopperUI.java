import javax.swing.JButton;
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
import java.util.Random;

@SuppressWarnings("serial")
public class ShopperUI extends JFrame implements ActionListener{

    private ShopperApp app;
    
    private DefaultTableModel tableModel;
    private JTable inventoryTable;
    private JPanel inventoryPanel;
    private JPanel editPanel;
    private JTextField nameField;
    private JTextField invIDField;
    private JTextField stockButton;
    private JTextField priceField;
    private JTextField discountComboBox;
    private JTextField loyaltyComboBox;
    private JTextField totalCost;
    private JButton purchaseWithCash;
    private JButton purchaseWithForm;
   
    private JButton logOut;
    
    public ShopperUI(String id) {
        app = new ShopperApp(id);
        
        setTitle("Shopper UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        purchaseWithCash = new JButton("Buy with cash");
        purchaseWithForm = new JButton("Buy with finance form");
        // New Logout, purchaseWithCash and purchaseWithForm button
        logOut = new JButton("Logout"); 
        purchaseWithCash.setVisible(false);
        purchaseWithForm.setVisible(false);
        // Add action listener for the three buttons
        purchaseWithCash.addActionListener(this);
        purchaseWithForm.addActionListener(this);
        logOut.addActionListener(this);

        
        JPanel buttonPanel = new JPanel();
        // Increase grid layout to accommodate new button
        buttonPanel.setLayout(new GridLayout(3, 1)); 
        // Add buttons to button panel
        buttonPanel.add(purchaseWithCash);
        buttonPanel.add(purchaseWithForm);
        buttonPanel.add(logOut); 
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.WEST);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.7),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.7));
        // Position the window at the centre of the screen again 
        setLocationRelativeTo(null);
        // Make the window visible to the user
        setVisible(true);
        // Display the inventory content in the application
        displayInventory();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Check if the event is triggered by the "purchaseWithCash" button
        if (e.getSource() == purchaseWithCash) {
            // Extract stock amount and price from text fields
            int stockAmt = Integer.parseInt(stockButton.getText());
            double priceOf1 = Double.parseDouble(priceField.getText());

            // Generate a random sale ID, retrieve branch and customer IDs
            Random rand = new Random();
            int saleID = rand.nextInt(1000);
            int bID = Integer.parseInt(app.getBranchID());
            int cID = Integer.parseInt(app.getCustID());
            double total = priceOf1 * stockAmt;
            double discount = 0;
            
            // Apply discounts based on selected options in the discountComboBox
            if (discountComboBox.getText().equals("No discount for this product at the moment")) {
                discount = 0;
            } else if (discountComboBox.getText().equals("Buy One Get One Free") && stockAmt >= 2) {
                discount = priceOf1;
            } else if (discountComboBox.getText().equals("Free Delivery!")) {
                discount = 4.99;
            } else if (discountComboBox.getText().equals("Three for the price of two") && stockAmt >= 3) {
                discount = priceOf1;
            }
            
            // Calculate profit
            double profit = total - discount;

            // Retrieve selected inventory item and perform sale and stock update
            int row = inventoryTable.getSelectedRow();
            int invID = (int) tableModel.getValueAt(row, 0);
            app.insertSale(saleID, bID, cID, total, discount, profit);
            app.updateStock(invID, stockAmt);
            JOptionPane.showMessageDialog(null, "Purchase Successful: ");

        } 
        // Check if the event is triggered by the "purchaseWithForm" button
        else if (e.getSource() == purchaseWithForm) {
            // Extract stock amount and selected inventory details
            int stockAmt = Integer.parseInt(stockButton.getText());
            int row = inventoryTable.getSelectedRow();
            int invID = (int) tableModel.getValueAt(row, 0);
            String itemName = (String) tableModel.getValueAt(row, 1);
            double price = (double) tableModel.getValueAt(row, 3);
            
            // Generate a random ID for the form and create a text file
            Random rand = new Random();
            int ID = rand.nextInt(1000);
            String formID = Integer.toString(ID);
            try {
                String filename = formID + "REQUEST.txt";
                BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
                
                // Write details to the text file
                writer.write("Item Name: " + itemName);
                writer.newLine();
                writer.write("Amount: " + stockAmt);
                writer.newLine();
                writer.write("Price: " + price);
                writer.newLine();
                writer.write("Name: ");
                writer.newLine();
                writer.write("Bank Details: ");
                writer.newLine();
                writer.write("Address: ");
                writer.newLine();
                writer.close();
                // Open the text file using notepad
                ProcessBuilder pb = new ProcessBuilder("notepad.exe", filename);
                pb.start();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            
            // Update stock after purchase with form
            app.updateStock(invID, stockAmt);
            JOptionPane.showMessageDialog(null, "Fill in details for purchase to process... ");

        } 
        // Check if the event is triggered by the "logOut" button
        else if (e.getSource() == logOut) {
            // Hide the current window and display the login GUI
            setVisible(false);
            new loginGUI();
        }
    }

    private void displayInventory() {
        // Retrieve inventory data from the application
        ArrayList<Inventory> inventory = app.getInventory();

        // Check if inventory data is available and not empty
        if (inventory != null && !inventory.isEmpty()) {
            // Define column names for the table
            String[] columnNames = {"Product ID", "Name", "Stock", "Price", "Discount", "Loyalty"};

            // Prepare data array to populate the table
            Object[][] data = new Object[inventory.size()][6];

            // Fill the data array with inventory details
            for (int i = 0; i < inventory.size(); i++) {
                Inventory item = inventory.get(i);
                data[i][0] = item.getProductId();
                data[i][1] = item.getProductName();
                data[i][2] = item.getQuantity();
                data[i][3] = item.getPrice();
                data[i][4] = item.getDiscount();
                data[i][5] = item.getLoyalty();
            }

            // Create a table model using the data and column names
            tableModel = new DefaultTableModel(data, columnNames);

            // Create a JTable with the table model
            inventoryTable = new JTable(tableModel) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Disable cell editing
                    return false;
                }
            };

            // Set font size for the table
            inventoryTable.setFont(inventoryTable.getFont().deriveFont(16.0f));

            // Add a mouse click listener to handle row selection
            inventoryTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int row = inventoryTable.getSelectedRow();
                    if (row != -1) {
                        // Retrieve selected item details
                        int invID = (int) tableModel.getValueAt(row, 0);
                        String name = (String) tableModel.getValueAt(row, 1);
                        int stock = (int) tableModel.getValueAt(row, 2);
                        double price = (double) tableModel.getValueAt(row, 3);
                        int discount = (int) tableModel.getValueAt(row, 4);
                        int loyalty = (int) tableModel.getValueAt(row, 5);

                        // Update the edit panel with selected item details
                        updateEditPanel(invID, name, stock, price, discount, loyalty);
                    }
                }
            });

            // Update the inventory panel with the created table
            updateInventoryPanel();
        } else {
            // Display a message if inventory is empty
            JOptionPane.showMessageDialog(null, "Inventory is empty");
        }
    }


    private void updateInventoryPanel() {
        if (inventoryPanel != null) {
            // Remove previous inventoryPanel if it exists
            remove(inventoryPanel); 
        }

        // Create a new inventoryPanel and set its layout
        inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BorderLayout());
        
        // Add the inventoryTable within a scroll pane to the centre of the inventoryPanel
        inventoryPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        // Create an editPanel to display details of the selected inventory item
        editPanel = new JPanel();
        editPanel.setLayout(new GridLayout(7, 2));

        // Initialise text fields for inventory details
        invIDField = new JTextField();
        nameField = new JTextField();
        stockButton = new JTextField();
        priceField = new JTextField();
        discountComboBox = new JTextField();
        loyaltyComboBox = new JTextField();
        totalCost = new JTextField();

        // Set text fields as non-editable
        invIDField.setEditable(false);
        nameField.setEditable(false);
        stockButton.setEditable(false);
        priceField.setEditable(false);
        discountComboBox.setEditable(false);
        loyaltyComboBox.setEditable(false);
        totalCost.setEditable(false);

        // Add labels and corresponding text fields to the editPanel
        editPanel.add(new JLabel("Ordering For the following:"));
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
        editPanel.add(new JLabel("Total Cost:"));
        editPanel.add(totalCost);

        // Add the editPanel to the bottom of the inventoryPanel
        inventoryPanel.add(editPanel, BorderLayout.SOUTH);

        // Add the updated inventoryPanel to the main container
        add(inventoryPanel, BorderLayout.CENTER);

        // Revalidates and repaint the container to reflect changes
        revalidate();
        repaint();
    }


    private void updateEditPanel(int invID, String name, int stock, double price, int discount, int loyalty) {
        // Retrieving the selected row index and the maximum available stock
        int row = inventoryTable.getSelectedRow();
        int maxAmt = (int) tableModel.getValueAt(row, 2);
        
        // Asking for the desired stock amount
        String input = JOptionPane.showInputDialog(null, "Amount:");
        int stockWanted;
        while (input == null || input.isEmpty() || !input.matches("\\d+") || Integer.parseInt(input) > maxAmt) {
            input = JOptionPane.showInputDialog(null, "Please enter a valid amount:");
        }
        stockWanted = Integer.parseInt(input);
        
        // Setting discount and loyalty messages based on their values
        if (discount == 0) {   
            discountComboBox.setText("No discount for this product at the moment");
        } else if (discount == 1) {
            discountComboBox.setText("Buy One Get One Free");
        } else if (discount == 2) {
            discountComboBox.setText("Three for the price of two");
        } else {
            discountComboBox.setText("Free Delivery!");
        }
        
        if (loyalty == 0) {    
            loyaltyComboBox.setText("Loyalty discount not applicable!");
        } else if (loyalty == 1) {
            loyaltyComboBox.setText("Loyalty discount of 5% applicable!");
        } else {
            loyaltyComboBox.setText("Loyalty discount of free delivery applicable!");
        }
        String fivePercent = "Loyalty discount of 5% applicable!";
        String freeDeliv = "Loyalty discount of free delivery applicable!";
        double delivCost = 4.99;
        
        double currPrice = price;
        
        // Applying discounts based on loyalty status
        if (app.isLoyal() == true) {
            if (loyaltyComboBox.getText().equals(fivePercent)) {
                double fiveP = 5;
                double discountCost = currPrice * (fiveP / 100);
                currPrice = (double) (currPrice - discountCost);
            }
        }
        
        currPrice = currPrice * stockWanted + delivCost;
        
        // Adjusting price based on selected discounts and stock amounts
        if (discount == 1 && stockWanted >= 2) {
            currPrice = currPrice - (double) tableModel.getValueAt(row, 3);
        } else if (discount == 2 && stockWanted >= 3) {
            currPrice = currPrice - (double) tableModel.getValueAt(row, 3);
        } else if (discount == 3) {
            currPrice = currPrice - delivCost;
        }   
        
        // Applying additional loyalty-based discount if applicable
        if (app.isLoyal() == true) {
            if (loyaltyComboBox.getText().equals(freeDeliv)) {
                currPrice = currPrice - delivCost;
                JOptionPane.showMessageDialog(null, "done");
            }
        }
        totalCost.setText(String.valueOf(currPrice));
        
        // Displaying selected inventory item details
        invIDField.setText("MAKE SURE YOU DON'T TRY TO BUY MORE THAN IS IN STOCK:");
        nameField.setText(name);
        stockButton.setText(String.valueOf(stockWanted));
        priceField.setText(String.valueOf(price));
        
        // Making purchase buttons visible
        purchaseWithCash.setVisible(true);
        purchaseWithForm.setVisible(true);
    }

    }
