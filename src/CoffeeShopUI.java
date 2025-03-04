import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        reportButton.addActionListener(e -> generateFinalReport());

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

        // ✅ 生成新的订单 ID
        String newOrderId = "O" + (orderProcessor.getList().getOrderList().size() + 1);
        Order newOrder = new Order(newOrderId, "Guest", "Now", convertToItems());

        // ✅ 仅添加到内存中的 OrderList，不写入文件
        orderProcessor.getList().addOrder(newOrder);

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

    private void generateFinalReport() {
        StringBuilder finalReport = new StringBuilder();
        System.out.println("Generating final report...");

        finalReport.append("======= Final Report =======\n\n");

        // ✅ 先加载 `orders.txt`，获取历史订单
        OrderList tempOrderList = new OrderList(menu);
        tempOrderList.loadOrderListFromFile("orders.txt");

        // ✅ 添加当前会话中的订单（但不写入文件）
        tempOrderList.getOrderList().addAll(orderProcessor.getList().getOrderList());

        // 获取菜单和所有订单
        List<Item> itemList = menu.getItemList();
        List<Order> orderList = tempOrderList.getOrderList(); // ✅ 包含所有订单（历史订单 + 当前订单）

        // 统计每个商品的订单数量
        Map<String, Integer> itemOrderCount = new HashMap<>();
        double totalIncome = 0;

        // 初始化计数器（所有商品初始销量为 0）
        for (Item item : itemList) {
            itemOrderCount.put(item.getId(), 0);
        }

        // 遍历所有订单，统计每个商品的销售数量
        for (Order order : orderList) {
            for (Item item : order.getItemList()) {
                itemOrderCount.put(item.getId(), itemOrderCount.get(item.getId()) + 1);
                totalIncome += item.getCost();
            }
        }

        // 生成表头
        finalReport.append(String.format("%-6s %-20s %-15s %11s %23s%n",
                "Code", "Name", "Category", "Price", "Number of Orders"));
        finalReport.append("--------------------------------------------------------------------------------\n");

        // 遍历菜单，打印每个商品的统计信息
        for (Item item : itemList) {
            int count = itemOrderCount.get(item.getId()); // 获取订单数量
            finalReport.append(String.format("%-6s %-20s %-15s %10.2f %15d%n",
                    item.getId(), item.getDescription(), item.getCategory(),
                    item.getCost(), count));
        }

        finalReport.append("--------------------------------------------------------------------------------\n\n");
        finalReport.append(String.format("Total income: %.2f%n", totalIncome));

        // ✅ 仅写入 `finalReport.txt`，不影响 `orders.txt`
        try (FileWriter writer = new FileWriter("finalReport.txt")) {
            writer.write(finalReport.toString());
        } catch (IOException e) {
            System.out.println("Error occurred while writing file: " + e.getMessage());
        }

        System.out.println("Final report successfully generated.");
    }


}
