
package com.kfc;

import javax.swing.*;
import java.awt.*;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("KFC Ordering System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setLayout(new BorderLayout());

            JLabel label = new JLabel("Welcome to KFC Ordering System", JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 16));
            frame.add(label, BorderLayout.NORTH);

            JPanel buttonPanel = new JPanel();
            JButton customerButton = new JButton("Customer");
            JButton adminButton = new JButton("Admin");

            customerButton.addActionListener(e -> new CustomerGUI(new Customer("guest")));
            adminButton.addActionListener(e -> new AdminGUI(new Admin("admin")));

            buttonPanel.add(customerButton);
            buttonPanel.add(adminButton);
            frame.add(buttonPanel, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
