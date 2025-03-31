import java.util.List;

public class Order {
    private String orderId;
    private String customerId;
    private String timeStamp;
    private List<Item> itemList;

    public Order(String orderId, String customerId, String timeStamp, List<Item> itemList) throws InvalidOrderException {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new InvalidOrderIdException("Order ID cannot be null or empty.");
        }
        this.orderId = orderId.trim();

        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }
        this.customerId = customerId.trim();

        if (timeStamp == null || timeStamp.trim().isEmpty()) {
            throw new InvalidOrderTimeStampException("Timestamp cannot be null or empty.");
        }
        this.timeStamp = timeStamp.trim();

        if (itemList == null || itemList.isEmpty()) {
            throw new IllegalitemListException("Order must contain at least one item.");
        }
        this.itemList = itemList;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getTimeStamp() { return timeStamp; }
    public List<Item> getItemList() { return itemList; }

    
    public double calculateTotalCost() {
        double total = 0;
        for (Item item : itemList) {
            total += item.getCost();
            
        }
     // 1. If the order contains at least 1 "Dessert" and 1 "Coffee" or "Tea", apply a 10% discount
        boolean hasDessert = false;
        boolean hasCoffeeOrTea = false;

        for (Item item : itemList) {
            if (item.getCategory().equalsIgnoreCase("Dessert")) {
                hasDessert = true; // Check if the order contains a dessert
            }
            if (item.getCategory().equalsIgnoreCase("Coffee") || item.getCategory().equalsIgnoreCase("Tea")) {
                hasCoffeeOrTea = true; // Check if the order contains coffee or tea
            }
        }
        double totalDiscount = total; // Initialize the discounted total
        if (hasDessert && hasCoffeeOrTea) {
            totalDiscount = Math.min(total * 0.9, totalDiscount); // Apply a 10% discount
        }

        // 2. If the order contains at least 5 items, apply a 25% discount
        if (itemList.size() >= 5) {
            totalDiscount = Math.min(total * 0.75, totalDiscount); // Apply a 25% discount
        }

        // 3. If the total cost of the order is >= $100, apply a 20% discount
        if (total >= 100) {
            totalDiscount = Math.min(total * 0.8, totalDiscount); // Apply a 20% discount
        }

        // Return the final total cost after applying all applicable discounts
     
        return totalDiscount;
    }
    
    // add time calculate for each order
    public int calculateTotalTime() {
    	int time = 0;
    	for (Item item : itemList) {
            time += item.getPreparationTime();
        }
        return time;
    }


}
