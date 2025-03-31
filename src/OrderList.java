
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class OrderList {

    private final List<Order> orders = new ArrayList<Order>(); // List to store all orders
    private static final String FILE_PATH = "orders.txt";
    private static OrderList instance;
    
    private OrderList() throws InvalidOrderException {
    	loadOrderListFromFile(FILE_PATH);
    }
    
    public static synchronized OrderList getInstance() throws InvalidOrderException {
    	if (instance==null) {
    		instance = new OrderList();
    	}
    	return instance;
    }

    public List<Order> getOrderList() {
        return orders; // Getter to retrieve the list of orders
    }

    // producer method
    public synchronized void addOrder(Order order) throws InterruptedException {
        orders.add(order);
        notifyAll();
    }
    
    // consumer method
    public synchronized Order getNextOrder() throws InterruptedException {
        if(orders.size()>0) {
        	Order order = orders.get(0);
            notifyAll();
            return order;
        }
        return null;
    }


    /**
     * Load orders from a file.
     *
     * @param filePath The path to the file containing order data.
     * @throws InvalidOrderException If there is an issue with the order data.
     */
    private void loadOrderListFromFile(String filePath) throws InvalidOrderException {
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
                Item item = Menu.getInstance().findItemById(itemId);
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
