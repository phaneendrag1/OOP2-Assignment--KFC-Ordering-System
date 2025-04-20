package com.kfc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerGUI extends JFrame {

    private final List<MenuItem> menu = new ArrayList<>();
    private final List<OrderLine> cart = new ArrayList<>();
    private final DefaultTableModel cartModel;
    private final JTextField quantityField = new JTextField();
    private final DefaultListModel<MenuItem> menuModel = new DefaultListModel<>();
    private final JList<MenuItem> itemList = new JList<>(menuModel);
    private final JTextField searchField = new JTextField();

    public CustomerGUI(Customer customer) {
        setTitle("Welcome, " + customer.getUsername());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sample menu
        menu.add(new MenuItem("Zinger Burger", "Burger", 5.99));
        menu.add(new MenuItem("Hot Wings", "Chicken", 3.49));
        menu.add(new MenuItem("Pepsi", "Drink", 1.99));
        menu.add(new MenuItem("Fries", "Side", 2.49));
        updateMenuList("");

        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Panel for item selection
        JPanel menuPanel = new JPanel(new BorderLayout());
        searchField.setColumns(20);
        menuPanel.add(searchField, BorderLayout.NORTH);
        menuPanel.add(new JScrollPane(itemList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Quantity:"));
        quantityField.setColumns(5);
        bottomPanel.add(quantityField);
        JButton addButton = new JButton("Add to Cart");
        bottomPanel.add(addButton);
        menuPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Custom item panel
        JPanel customPanel = new JPanel();
        customPanel.setLayout(new GridLayout(4, 2));
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JButton addCustomButton = new JButton("Add Custom Item");

        customPanel.setBorder(BorderFactory.createTitledBorder("Add Custom Item"));
        customPanel.add(new JLabel("Name:")); customPanel.add(nameField);
        customPanel.add(new JLabel("Category:")); customPanel.add(categoryField);
        customPanel.add(new JLabel("Price:")); customPanel.add(priceField);
        customPanel.add(addCustomButton);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(menuPanel, BorderLayout.CENTER);
        leftPanel.add(customPanel, BorderLayout.SOUTH);

        // Cart table
        cartModel = new DefaultTableModel(new String[]{"Item", "Qty", "Price", "Subtotal"}, 0);
        JTable cartTable = new JTable(cartModel);
        JScrollPane cartScroll = new JScrollPane(cartTable);

        JButton placeOrderButton = new JButton("Place Order");

        add(leftPanel, BorderLayout.WEST);
        add(cartScroll, BorderLayout.CENTER);
        add(placeOrderButton, BorderLayout.SOUTH);

        // Actions
        searchField.getDocument().addDocumentListener((SimpleDocumentListener) e -> updateMenuList(searchField.getText()));

        addButton.addActionListener(e -> {
            MenuItem selected = itemList.getSelectedValue();
            if (selected != null) {
                try {
                    int qty = Integer.parseInt(quantityField.getText());
                    if (qty <= 0) throw new NumberFormatException();
                    OrderLine order = new OrderLine(selected, qty);
                    cart.add(order);
                    cartModel.addRow(new Object[]{selected.name(), qty, selected.price(), order.getTotal()});
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter a valid quantity.");
                }
            }
        });

        addCustomButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String category = categoryField.getText();
                double price = Double.parseDouble(priceField.getText());
                MenuItem customItem = new MenuItem(name, category, price);
                menu.add(customItem);
                updateMenuList(searchField.getText());
                JOptionPane.showMessageDialog(this, "Item added to menu!");
                nameField.setText(""); categoryField.setText(""); priceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price.");
            }
        });

        placeOrderButton.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cart is empty.");
                return;
            }

            StringBuilder receipt = new StringBuilder("==== KFC Receipt ====\n");
            receipt.append("Date: ").append(DateTimeUtil.getCurrentTimestamp()).append("\n\n");

            double total = 0;
            for (OrderLine line : cart) {
                receipt.append(line.getItem().name())
                        .append(" x").append(line.getQuantity())
                        .append(" = $").append(line.getTotal()).append("\n");
                total += line.getTotal();
            }
            receipt.append("----------------------\nTotal: $").append(total);

            try {
                String fileName = "receipt_" + System.currentTimeMillis() + ".txt";
                java.nio.file.Files.writeString(java.nio.file.Path.of(fileName), receipt.toString());
                JOptionPane.showMessageDialog(this, "Order placed!\n\nReceipt saved to:\n" + fileName);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to save receipt: " + ex.getMessage());
            }

            cart.clear();
            cartModel.setRowCount(0);
        });

        setVisible(true);
    }

    private void updateMenuList(String filter) {
        menuModel.clear();
        List<MenuItem> filtered = menu.stream()
                .filter(item -> item.name().toLowerCase().contains(filter.toLowerCase()) ||
                        item.category().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());
        filtered.forEach(menuModel::addElement);
    }

    @FunctionalInterface
    interface SimpleDocumentListener extends javax.swing.event.DocumentListener {
        void update(javax.swing.event.DocumentEvent e);
        @Override default void insertUpdate(javax.swing.event.DocumentEvent e) { update(e); }
        @Override default void removeUpdate(javax.swing.event.DocumentEvent e) { update(e); }
        @Override default void changedUpdate(javax.swing.event.DocumentEvent e) { update(e); }
    }
}
