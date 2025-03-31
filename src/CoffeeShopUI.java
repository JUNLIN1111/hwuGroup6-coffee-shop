import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoffeeShopUI extends JFrame {
    private JList<String> menuList; // List to display the menu items
    private DefaultListModel<String> menuListModel; // Model for the menu list
    private JList<String> orderList; // List to display the current order
    private DefaultListModel<String> orderListModel; // Model for the order list
    private JLabel totalPriceLabel; // Label to display the total price
    private Menu menu; // Menu object containing menu items
    private OrderProcessor orderProcessor; // OrderProcessor to handle order processing
    private ThreadedOrderProcessor threadedProcessor; // Processor for threaded execution

    public CoffeeShopUI(Menu menu, OrderProcessor orderProcessor, ThreadedOrderProcessor threadedProcessor) {
        this.menu = menu;
        this.orderProcessor = orderProcessor;
        this.threadedProcessor = threadedProcessor;

        setTitle("Coffee Shop Simulator"); // Set the title of the window
        setSize(800, 600); // Set the size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when the window is closed
        setLayout(new BorderLayout()); // Use BorderLayout for the main layout

        // Initialize the menu list
        menuListModel = new DefaultListModel<>();
        menuList = new JList<>(menuListModel);
        loadMenu(); // Load menu items into the list model

        // Initialize the order list
        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);

        // Add button to add selected item to the order
        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(e -> addItemToOrder());

        // Submit button to place the order
        JButton submitButton = new JButton("Submit Order");
        submitButton.addActionListener(e -> submitOrder());

        // Button to generate a final report
        JButton reportButton = new JButton("Generate Report");
        reportButton.addActionListener(e -> generateFinalReport());

        totalPriceLabel = new JLabel("Total Price: $0.00"); // Initialize total price label

        // Left panel for the menu
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Menu"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(menuList), BorderLayout.CENTER);

        // Right panel for the current order
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Current Order"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        rightPanel.add(totalPriceLabel, BorderLayout.SOUTH);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(reportButton);

        // Add panels to the main frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true); // Make the window visible
    }

    private void loadMenu() {
        for (Item item : menu.getItemList()) {
            menuListModel.addElement(item.getDescription() + " - $" + item.getCost());
        }
    }

    private void addItemToOrder() {
        String selectedItem = menuList.getSelectedValue();
        if (selectedItem != null) {
            orderListModel.addElement(selectedItem);
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        try {
            Order order = new Order("O" + System.currentTimeMillis(), "Guest", "Now", convertToItems());
            double total = orderProcessor.calculateOrderTotal(order);
            totalPriceLabel.setText(String.format("Total Price: $%.2f", total));
        } catch (InvalidOrderException e) {
            System.out.println(e.getMessage());
        }
    }

    private void submitOrder() {
        if (orderListModel.isEmpty()) {
            showMessage("No items selected!", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Order order = new Order("O" + System.currentTimeMillis(), "Guest", "Now", convertToItems());
            orderProcessor.getList().addOrder(order);
            threadedProcessor.addOrder(order); // 添加到线程处理队列

            double total = orderProcessor.calculateOrderTotal(order);
            totalPriceLabel.setText(String.format("Total Price: $%.2f", total));

            orderListModel.clear();
        } catch (InvalidOrderException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Item> convertToItems() {
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < orderListModel.getSize(); i++) {
            String selectedItem = orderListModel.get(i);
            String itemName = selectedItem.split(" - \\$", 2)[0];

            Item menuItem = menu.getItemList().stream()
                    .filter(item -> item.getDescription().equals(itemName))
                    .findFirst()
                    .orElse(null);

            if (menuItem != null) {
                itemList.add(menuItem);
            }
        }
        return itemList;
    }

    private void generateFinalReport() {
        StringBuilder finalReport = new StringBuilder();
        System.out.println("Generating final report...");

        finalReport.append("======= Final Report =======\n\n");

        List<Order> orderList = orderProcessor.getList().getOrderList();
        List<Item> itemList = menu.getItemList();

        Map<String, Integer> itemOrderCount = new HashMap<>();
        double totalIncome = 0;

        for (Item item : itemList) {
            itemOrderCount.put(item.getId(), 0);
        }

        for (Order order : orderList) {
            for (Item item : order.getItemList()) {
                itemOrderCount.put(item.getId(), itemOrderCount.get(item.getId()) + 1);
                totalIncome += item.getCost();
            }
        }

        finalReport.append(String.format("%-6s %-20s %-15s %11s %23s%n",
                "Code", "Name", "Category", "Price", "Number of Orders"));
        finalReport.append("--------------------------------------------------------------------------------\n");

        for (Item item : itemList) {
            int count = itemOrderCount.get(item.getId());
            finalReport.append(String.format("%-6s %-20s %-15s %10.2f %15d%n",
                    item.getId(), item.getDescription(), item.getCategory(),
                    item.getCost(), count));
        }

        finalReport.append("--------------------------------------------------------------------------------\n\n");
        finalReport.append(String.format("Total income: %.2f%n", totalIncome));

        try (FileWriter writer = new FileWriter("finalReport.txt")) {
            writer.write(finalReport.toString());
        } catch (IOException e) {
            System.out.println("Error occurred while writing file: " + e.getMessage());
        }

        System.out.println("Final report successfully generated.");
    }

    private void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, "Message", messageType);
    }
}
