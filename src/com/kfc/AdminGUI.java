
package com.kfc;

import javax.swing.*;
import java.awt.*;

public class AdminGUI extends JFrame {
    public AdminGUI(Admin admin) {
        setTitle("Admin Panel - " + admin.getUsername());
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel label = new JLabel("Admin Menu Management Coming Soon!", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        add(label);
        setVisible(true);
    }
}
