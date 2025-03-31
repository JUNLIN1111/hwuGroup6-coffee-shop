import java.util.ArrayList;
import java.util.List;

public class OrderThreadManager {
    private final List<Thread> serverThreads = new ArrayList<>();
    private final ThreadedOrderProcessor processor;

    public OrderThreadManager(ThreadedOrderProcessor processor) {
        this.processor = processor;
    }

    public void startServers(int count) {
        for (int i = 1; i <= count; i++) {
            ServerThread task = new ServerThread("Server-" + i, processor);
            Thread thread = new Thread(task);
            thread.start();
            serverThreads.add(thread);
        }
    }

    public void stopServers() {
        for (Thread thread : serverThreads) {
            thread.interrupt();
        }
    }

    public void addOrdersWithTiming(List<Order> sortedOrders) {
        new Thread(() -> {
            if (sortedOrders.isEmpty()) return;
            try {
                Order baseOrder = sortedOrders.get(0);
                long baseTime = parseTimestamp(baseOrder.getTimeStamp());

                for (Order order : sortedOrders) {
                    long currentTime = parseTimestamp(order.getTimeStamp());
                    long delay = currentTime - baseTime;
                    if (delay > 0) Thread.sleep(delay);
                    processor.addOrder(order);
                    System.out.println("[Scheduled] Order added: " + order.getOrderId());
                    baseTime = currentTime;
                }
            } catch (InterruptedException e) {
                System.out.println("[Scheduler] Interrupted");
            }
        }).start();
    }

    private long parseTimestamp(String ts) {
        return java.time.LocalDateTime.parse(ts, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                .atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
