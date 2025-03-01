

import java.util.List;

public class Order {
    private String orderId;
    private String customerId;
    private String timeStamp;
    private List<Item> itemList;

    public Order(String orderId, String customerId, String timeStamp, List<Item> itemList) {
        // Validate orderId
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID cannot be null or empty.");
        }
        this.orderId = orderId.trim();

        // Validate customerId
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }
        this.customerId = customerId.trim();

        // Validate timeStamp
        if (timeStamp == null || timeStamp.trim().isEmpty()) {
            throw new IllegalArgumentException("Timestamp cannot be null or empty.");
        }
        this.timeStamp = timeStamp.trim();

        // Validate itemList
        if (itemList == null || itemList.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
        this.itemList = itemList;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getTimeStamp() { return timeStamp; }
    public List<Item> getItemList() { return itemList; }

    
    public double calculateTotalCost() {
        double totalCost = 0;
        for (Item item : itemList) {
            totalCost += item.getCost();
        }
        return totalCost;
    }

    
}
