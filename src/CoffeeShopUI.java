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
    private JLabel queueStatusLabel; // New label to display queue status
    private OrderProcessor orderProcessor; // OrderProcessor to handle order processing
    private ThreadedOrderProcessor threadedProcessor; // Processor for threaded execution
    private JSlider speedSlider; // Slider for speed control

    public CoffeeShopUI(OrderProcessor orderProcessor, ThreadedOrderProcessor threadedProcessor) {
        this.orderProcessor = orderProcessor;
        this.threadedProcessor = threadedProcessor;

        setTitle("Coffee Shop Simulator");
        setSize(1000, 600);
        setMinimumSize(new Dimension(800, 600));
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize the menu list
        menuListModel = new DefaultListModel<>();
        menuList = new JList<>(menuListModel);
        loadMenu();

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

        totalPriceLabel = new JLabel("Total Price: $0.00");
        queueStatusLabel = new JLabel("Queue: 0/" + threadedProcessor.getMaxQueueSize()); // Initialize queue status

        // Speed control slider
        speedSlider = new JSlider(JSlider.HORIZONTAL, 10, 500, 100);
        speedSlider.setMajorTickSpacing(100);
        speedSlider.setMinorTickSpacing(10);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.addChangeListener(e -> {
            double factor = speedSlider.getValue() / 100.0;
            threadedProcessor.setSpeedFactor(factor);
            CafeLogger.getInstance().log("Simulation speed set to: " + factor + "x\n");
        });

        // Left panel for the menu
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Menu"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(menuList), BorderLayout.CENTER);

        // Right panel for the current order
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Current Order"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        JPanel rightBottomPanel = new JPanel(new GridLayout(2, 1)); // Stack total price and queue status
        rightBottomPanel.add(totalPriceLabel);
        rightBottomPanel.add(queueStatusLabel);
        rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);

        // Panel for buttons with GridBagLayout
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(addButton, gbc);

        gbc.gridx = 1;
        buttonPanel.add(submitButton, gbc);

        gbc.gridx = 2;
        buttonPanel.add(reportButton, gbc);

        gbc.gridx = 3;
        buttonPanel.add(new JLabel("Speed:"), gbc);

        gbc.gridx = 4;
        gbc.weightx = 1.0;
        buttonPanel.add(speedSlider, gbc);

        // Add panels to the main frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        add(new JLabel("Coffee Shop Simulator"), BorderLayout.NORTH);

        // Start a timer to update queue status every second
        new Timer(1000, e -> updateQueueStatus()).start();

        setVisible(true);
    }

    private void loadMenu() {
        for (Item item : Menu.getInstance().getItemList()) {
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
            if (threadedProcessor.addOrder(order)) { // Check if order was successfully added
                orderProcessor.getList().addOrder(order); // Add to OrderProcessor only if queued
                double total = orderProcessor.calculateOrderTotal(order);
                totalPriceLabel.setText(String.format("Total Price: $%.2f", total));
                orderListModel.clear();
                showMessage("Order submitted successfully!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showMessage("Order queue is full (" + threadedProcessor.getMaxQueueSize() + " orders max)! Please wait and try again.", JOptionPane.WARNING_MESSAGE);
            }
        } catch (InterruptedException e) {
            showMessage("Error submitting order: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        } catch (InvalidOrderException e) {
            showMessage("Invalid order: " + e.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Item> convertToItems() {
        List<Item> itemList = new ArrayList<>();
        for (int i = 0; i < orderListModel.getSize(); i++) {
            String selectedItem = orderListModel.get(i);
            String itemName = selectedItem.split(" - \\$", 2)[0];

            Item menuItem = Menu.getInstance().getItemList().stream()
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
        List<Item> itemList = Menu.getInstance().getItemList();

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

    private void updateQueueStatus() {
        int currentSize = threadedProcessor.getQueueSize();
        int maxSize = threadedProcessor.getMaxQueueSize();
        queueStatusLabel.setText("Queue: " + currentSize + "/" + maxSize);
        // Optional: Highlight when queue is full
        if (currentSize >= maxSize) {
            queueStatusLabel.setForeground(Color.RED);
        } else {
            queueStatusLabel.setForeground(Color.BLACK);
        }
    }
}
