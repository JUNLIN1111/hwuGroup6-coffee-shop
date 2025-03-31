import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class OrderList {
    private List<Order> orders; // List to store all orders
    private Menu menu; // Reference to the Menu object for item lookup

    public OrderList(Menu menu) {
        this.orders = new ArrayList<>();
        this.menu = menu;
    }

    public List<Order> getOrderList() {
        return orders; // Getter to retrieve the list of orders
    }

    public void addOrder(Order order) throws InvalidOrderException {
        // Validate the order before adding it to the list
        if (order == null) {
            throw new InvalidOrderException("Order cannot be null.");
        }
        if (findOrderByOrderId(order.getOrderId()) != null) {
            throw new InvalidOrderIdException("Order Id already exists: " + order.getOrderId());
        }
        orders.add(order); // Add the order to the list
    }

    public Order findOrderByOrderId(String orderId) {
        // Find an order by its order ID
        return orders.stream()
                .filter(order -> order.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    public void removeOrderByOrderId(String orderId) {
        // Remove an order by its order ID
        Order order = findOrderByOrderId(orderId);
        if (order != null) {
            orders.remove(order);
        }
    }

    /**
     * Load orders from a file.
     *
     * @param filePath The path to the file containing order data.
     * @throws InvalidOrderException If there is an issue with the order data.
     */
    public void loadOrderListFromFile(String filePath) throws InvalidOrderException {
        File file;
        try {
            file = new File(filePath);
        } catch (NullPointerException e) {
            System.out.println("The file is invalid.");
            return;
        }

        // Maps to temporarily store order information and items
        Map<String, List<Item>> orderItemsMap = new LinkedHashMap<>();
        Map<String, String[]> orderInfoMap = new LinkedHashMap<>();



        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                // Expected format: orderId,customerId,timeStamp,itemId
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.out.println("Error: Invalid line format - " + line);
                    continue;
                }

                String orderId = parts[0];
                String customerId = parts[1];
                String timeStamp = parts[2];
                String itemId = parts[3];

                // Retrieve the item from the menu
                Item item = menu.findItemById(itemId);
                if (item == null) {
                    System.out.println("Warning: Item not found for ID - " + itemId + " in the menu.");
                    continue;
                }

                // Store order information and items
                orderInfoMap.putIfAbsent(orderId, new String[]{customerId, timeStamp});
                orderItemsMap.computeIfAbsent(orderId, k -> new ArrayList<>()).add(item);
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        // Clear existing orders and add new ones from the file
        orders.clear();
        for (String orderId : orderItemsMap.keySet()) {
            List<Item> itemList = orderItemsMap.get(orderId);
            if (itemList.isEmpty()) {
                System.out.println("Warning: Order " + orderId + " has no items.");
                continue;  // **Skip if the order has no items**
            }

            String[] info = orderInfoMap.get(orderId);
            orders.add(new Order(orderId, info[0], info[1], itemList));
        }
    }
}
