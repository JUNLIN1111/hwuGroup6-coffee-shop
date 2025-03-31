public class ServerThread implements Runnable {
    private final String serverName;
    private final ThreadedOrderProcessor processor;

    public ServerThread(String name, ThreadedOrderProcessor processor) {
        this.serverName = name;
        this.processor = processor;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Order order = OrderList.getInstance().getNextOrder();
                processOrder(order);
                processor.finishOrder(serverName);
            } catch (InterruptedException e) {
                System.out.println(serverName + " shutting down.");
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }

    private void processOrder(Order order) throws InterruptedException {
        System.out.println(serverName + " processing: " + order.getOrderId());

        int totalTime = 0;
        for (Item item : order.getItemList()) {
            totalTime += (int) (item.getPreparationTime() * 10); // ms
        }
        Thread.sleep(totalTime);

        System.out.printf("%s completed: %s ($%.2f)%n", serverName, order.getOrderId(), order.calculateTotalCost());
    }
}
