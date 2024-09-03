

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class loginGUI extends JFrame{
    private JButton managerButton;
    private JButton shopperButton;
	
    public loginGUI() {
    	// Set up the JFrame
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Create the manager login button
        managerButton = new JButton("Manager");
        managerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	  String id = JOptionPane.showInputDialog(null, "Enter ID:");
                  if (id != null && !id.isEmpty()) {
                      // Pass the ID to dbQueries class
                      boolean isManager = dbQueries.mandID(id);
                      if(isManager == true){
                          JOptionPane.showMessageDialog(null, "Welcome Manager");
                          setVisible(false);
                          new ManagerUI(id);
                      	}else {
                      JOptionPane.showMessageDialog(null, "ID not found! Try again");
                      	}
                  } else {
                      JOptionPane.showMessageDialog(null, "Invalid ID! Try again");
                  }}
        });
        // Create the shopper login button
        shopperButton = new JButton("Shopper");
        shopperButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
          	  String id = JOptionPane.showInputDialog(null, "Enter shopper ID:");
              if (id != null && !id.isEmpty()) {
                  // Pass the ID to dbQueries class
                  boolean isShopper = dbQueries.shopID(id);
                  if(isShopper == true){
                      JOptionPane.showMessageDialog(null, "Welcome shopper");
                      setVisible(false);
                     new ShopperUI(id);
                  	}else {
                        JOptionPane.showMessageDialog(null, "ID not found! Try again");
                  	}
              } else {
                  JOptionPane.showMessageDialog(null, "Invalid ID! Try again");
              }}
    });
        // Add the buttons to the JFrame
        add(managerButton);
        add(shopperButton);

        // Display the JFrame
        setVisible(true);
    }
}
