import java.util.*;

public class ThreadedOrderProcessor {
    private final Queue<Order> orderQueue = new LinkedList<>();
    private final Map<String, Order> activeOrders = new HashMap<>();

    public synchronized void addOrder(Order order) {
        orderQueue.offer(order);
        notifyAll();
    }

    public synchronized Order takeOrder(String serverName) throws InterruptedException {
        while (orderQueue.isEmpty()) {
            wait();
        }
        Order order = orderQueue.poll();
        activeOrders.put(serverName, order);
        return order;
    }

    public synchronized void finishOrder(String serverName) {
        activeOrders.remove(serverName);
    }

    public synchronized List<Order> getPendingOrders() {
        return new ArrayList<>(orderQueue);
    }

    public synchronized Map<String, Order> getActiveOrders() {
        return new HashMap<>(activeOrders);
    }
}
