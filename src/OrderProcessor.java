

public class OrderProcessor {
    private OrderList orderList;

    public OrderProcessor(OrderList orderList) {
        this.orderList = orderList;
    }

    public OrderList getList() {
        return orderList;
    }

    public double calculateOrderTotal(Order order) {
        return order.getItemList().stream().mapToDouble(Item::getCost).sum();
    }
}
