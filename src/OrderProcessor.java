public class OrderProcessor {
    private OrderList orderList;

    public OrderProcessor(OrderList orderList) {
        this.orderList = orderList;
    }

    public OrderList getList() {
        return orderList;
    }

    public double calculateOrderTotal(Order order) {
        double total = order.calculateTotalCost();

        // 1. 如果订单中至少有 1 个 "Dessert" 和 1 个 "Coffee" 或 "Tea"，打 10% 折扣
        boolean hasDessert = false;
        boolean hasCoffeeOrTea = false;

        for (Item item : order.getItemList()) {
            if (item.getCategory().equalsIgnoreCase("Dessert")) {
                hasDessert = true;
            }
            if (item.getCategory().equalsIgnoreCase("Coffee") || item.getCategory().equalsIgnoreCase("Tea")) {
                hasCoffeeOrTea = true;
            }
        }

        if (hasDessert && hasCoffeeOrTea) {
            return total * 0.9;  // 10% 折扣
        }

        // 2. 如果订单中有 5 件及以上商品，打 25% 折扣
        if (order.getItemList().size() >= 5) {
            return total * 0.75;
        }

        // 3. 如果订单总价 >= 100，打 20% 折扣
        if (total >= 100) {
            return total * 0.8;
        }

        // 没有符合的折扣
        return total;
    }
}
