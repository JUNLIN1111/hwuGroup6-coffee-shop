import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class ThreadedOrderProcessor {
    private final Queue<Order> orderQueue;
    private final Map<String, Order> activeOrders = new HashMap<>();
    private Order order;
    private volatile double speedFactor = 1.0; // Default speed (1.0 = normal)
    private static final int MAX_QUEUE_SIZE = 10; // Example limit: 10 orders
   
    public ThreadedOrderProcessor() {
        this.orderQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
    }
    
    public synchronized boolean addOrder(Order order) throws InterruptedException {
        if (orderQueue.size() >= MAX_QUEUE_SIZE) {
            return false; // Queue is full, return false to signal rejection
        }
        orderQueue.offer(order);
        notifyAll(); // Notify waiting consumers (servers)
        return true;
    }

    public synchronized Order takeOrder(String serverName) throws InterruptedException {
        while (orderQueue.isEmpty()) {
            wait();
        }
        order = orderQueue.poll();
        activeOrders.put(serverName, order);
        return order;
    }
    
    // New methods for speed control
    public synchronized void setSpeedFactor(double factor) {
        this.speedFactor = Math.max(0.1, Math.min(5.0, factor)); // Limit range: 0.1 (very fast) to 5.0 (very slow)
    }
    
    public double getSpeedFactor() {
        return speedFactor;
    }
    
    public synchronized int getQueueSize() {
        return orderQueue.size();
    }
    
    public int getMaxQueueSize() {
        return MAX_QUEUE_SIZE;
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
    public synchronized Order getOrder() {
        return order;
    }

}
