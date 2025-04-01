import java.util.HashMap;
import java.util.Map;

public class ServerThread implements Runnable {
    private final String serverName;
    private Order order;
    private final Map<String, Order> activeOrders = new HashMap<>();

    public ServerThread(String name) {
        this.serverName = name;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Order order = OrderList.getInstance().getNextOrder(serverName);
                if (order != null) {
                    processOrder(order);
                }
                OrderList.getInstance().finishOrder(serverName);
            } catch (InterruptedException e) {
                System.out.println(serverName + " shutting down.");
                break;
            } catch (Exception e) {
                System.out.println(serverName + " error: " + e.getMessage());
                break;
            }
        }
    }
    
    

    private void processOrder(Order order) throws InterruptedException {
        System.out.println(serverName + " processing: " + order.getOrderId());
        CafeLogger.getInstance().log(serverName + " processing: " + order.getOrderId()+"\n");
        activeOrders.put(serverName, order);
        int totalTime = 0;
        for (Item item : order.getItemList()) {
            totalTime += (int) (item.getPreparationTime() * 10); // ms
        }
        Thread.sleep(totalTime);

        System.out.printf("%s completed: %s ($%.2f)%n", serverName, order.getOrderId(), order.calculateTotalCost());
        CafeLogger.getInstance().log(serverName + " completed : " + order.getOrderId()+", original price is "+order.calculateOriginalCost()+", discount price is "+order.calculateTotalCost()+"\n");
        activeOrders.remove(serverName);
    }
    

}
