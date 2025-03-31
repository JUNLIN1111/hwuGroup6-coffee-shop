public class ServerThread extends Thread {
    private final String serverName;
    private final ThreadedOrderProcessor processor;

    public ServerThread(String name, ThreadedOrderProcessor processor) {
        this.serverName = name;
        this.processor = processor;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Order order = processor.takeOrder(serverName);
                processOrder(order);
                processor.finishOrder(serverName);
            } catch (InterruptedException e) {
                System.out.println(serverName + " shutting down.");
                break;
            }
        }
    }

    private void processOrder(Order order) throws InterruptedException {
        System.out.println(serverName + " processing: " + order.getOrderId());

        int totalTime = 0;
        for (Item item : order.getItemList()) {
            totalTime += item.getPreparationTime()*10; // 已是毫秒单位
        }
        Thread.sleep(totalTime);

        System.out.printf("%s completed: %s ($%.2f)%n", serverName, order.getOrderId(), order.calculateTotalCost());
    }
}