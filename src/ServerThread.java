import java.util.HashMap;
import java.util.Map;

public class ServerThread implements Runnable {
    private final String serverName;
    private final ThreadedOrderProcessor processor; // Add reference to processor
    private Order order;
    private final Map<String, Order> activeOrders = new HashMap<>();
    private boolean isFree;

    public ServerThread(String name,ThreadedOrderProcessor processor) {
    	this.processor = processor;
        this.serverName = name;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Order order = OrderList.getInstance().getNextOrder(serverName);
                if (order != null) {
                	isFree=false;
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
        activeOrders.put(serverName, order);
        System.out.println(serverName + " processing: " + order.getOrderId());
        CafeLogger.getInstance().log(serverName + " processing: " + order.getOrderId() + "\n");
        int totalTime = 0;
        for (Item item : order.getItemList()) {
            totalTime += (int) (item.getPreparationTime() * 10); // ms
        }
        
     // Apply speed factor: smaller factor = faster, larger factor = slower
        long adjustedTime = (long) (totalTime * processor.getSpeedFactor());
//        System.out.printf("dealing time :"+adjustedTime);
        Thread.sleep(adjustedTime);
//        Thread.sleep(totalTime);

        System.out.printf("%s completed: %s ($%.2f)%n", serverName, order.getOrderId(), order.calculateTotalCost());
        CafeLogger.getInstance().log(serverName + " completed : " + order.getOrderId()+", original price is "+order.calculateOriginalCost()+", discount price is "+order.calculateTotalCost()+"\n");
        activeOrders.remove(serverName);
        isFree=true;
    }
    
    public boolean isFree() {
    	return isFree;
    }
}
