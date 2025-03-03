import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class CoffeeShopUI extends JFrame {
    private JList<String> menuList;
    private DefaultListModel<String> menuListModel;
    private JList<String> orderList;
    private DefaultListModel<String> orderListModel;
    private JLabel totalPriceLabel;
    private Menu menu;
    private OrderProcessor orderProcessor;

    public CoffeeShopUI(Menu menu, OrderProcessor orderProcessor) {
        this.menu = menu;
        this.orderProcessor = orderProcessor;

        setTitle("Coffee Shop Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 左侧菜单列表
        menuListModel = new DefaultListModel<>();
        menuList = new JList<>(menuListModel);
        loadMenu();

        // 右侧订单列表
        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);

        // 按钮
        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(e -> addItemToOrder());

        JButton submitButton = new JButton("Submit Order");
        submitButton.addActionListener(e -> submitOrder());

        JButton reportButton = new JButton("Generate Report");
        reportButton.addActionListener(e -> generateReport());

        totalPriceLabel = new JLabel("Total Price: $0.00");

        // 布局
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Menu"), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(menuList), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Current Order"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        rightPanel.add(totalPriceLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(reportButton);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
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
        }
    }

    private void submitOrder() {
        if (orderListModel.isEmpty()) {
            showMessage("No items selected!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = calculateTotalPrice();
        totalPriceLabel.setText(String.format("Total Price: $%.2f", total));

        orderProcessor.getList().addOrder(new Order("O" + System.currentTimeMillis(), "Guest", "Now", convertToItems()));

        orderListModel.clear();
    }

    private double calculateTotalPrice() {
        double total = 0;
        for (int i = 0; i < orderListModel.getSize(); i++) {
            String itemText = orderListModel.get(i);
            String[] parts = itemText.split(" - \\$");
            double price = Double.parseDouble(parts[1]);
            total += price;
        }
        return total;
    }

    private List<Item> convertToItems() {
        return menu.getItemList().stream()
                .filter(item -> {
                    for (int i = 0; i < orderListModel.getSize(); i++) {
                        if (orderListModel.get(i).contains(item.getDescription())) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    private void generateReport() {
        String reportFilePath = "finalReport.txt";
        orderProcessor.generateFinalReport(reportFilePath);
        showMessage("Order report saved to '" + reportFilePath + "'.", JOptionPane.INFORMATION_MESSAGE);
    }


    private void showMessage(String message, int messageType) {
        JOptionPane.showMessageDialog(this, message, "Message", messageType);
    }
}
