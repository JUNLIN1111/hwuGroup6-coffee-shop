public class OrderProcessor {
    private OrderList orderList; // Reference to the OrderList object to manage orders

    public OrderProcessor(OrderList orderList) {
        this.orderList = orderList; // Constructor to initialize the OrderList
    }

    public OrderList getList() {
        return orderList; // Getter method to retrieve the OrderList
    }

    /**
     * Calculate the total cost of an order with applicable discounts.
     *
     * @param order The order to calculate the total cost for.
     * @return The total cost after applying discounts.
     */
    public double calculateOrderTotal(Order order) {
        double total = order.calculateTotalCost(); // Calculate the initial total cost of the order

        // 1. If the order contains at least 1 "Dessert" and 1 "Coffee" or "Tea", apply a 10% discount
        boolean hasDessert = false;
        boolean hasCoffeeOrTea = false;

        for (Item item : order.getItemList()) {
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
        if (order.getItemList().size() >= 5) {
            totalDiscount = Math.min(total * 0.75, totalDiscount); // Apply a 25% discount
        }

        // 3. If the total cost of the order is >= $100, apply a 20% discount
        if (total >= 100) {
            totalDiscount = Math.min(total * 0.8, totalDiscount); // Apply a 20% discount
        }

        // Return the final total cost after applying all applicable discounts
        return totalDiscount;
    }
}
