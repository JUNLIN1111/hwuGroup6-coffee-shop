

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
        double total = 0.0;
        int dessertCount = 0;
        int coffeeOrTeaCount = 0;
        int totalItemCount = itemList.size();

        for (Item item : itemList) {
            total += item.getCost();

            if (item.getCategory().equalsIgnoreCase("Dessert")) {
                dessertCount++;
            } else if (item.getCategory().equalsIgnoreCase("Coffee") || item.getCategory().equalsIgnoreCase("Tea")) {
                coffeeOrTeaCount++;
            }
        }

        double discount1 = (dessertCount >= 2 && coffeeOrTeaCount >= 1) ? total * 0.9 : total;
        double discount2 = (totalItemCount >= 5) ? total * 0.75 : total;
        double discount3 = (total > 100) ? total * 0.8 : total;

        // Choose the best discount (minimum total price)
        total = Math.min(discount1, Math.min(discount2, discount3));

        return total;
    }
}
