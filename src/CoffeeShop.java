import javax.swing.*;



import java.awt.*;

public class CoffeeShop {
    public static void main(String[] args) throws InvalidOrderException {
        System.out.println("Welcome to our coffee shop");
        CafeLogger.getInstance().clearLog();

        final OrderProcessor orderProcessor;
        try {
            orderProcessor = new OrderProcessor(OrderList.getInstance());
        } catch (InvalidOrderException e) {
            throw new RuntimeException(e);
        }

        final ThreadedOrderProcessor threadedProcessor = new ThreadedOrderProcessor();
        final OrderThreadManager threadManager = new OrderThreadManager(threadedProcessor);

        // choose number of servers here
        CafeLogger.getInstance().log("Current server number: 4\n");
        threadManager.startServers(4);
        

        SwingUtilities.invokeLater(() -> {
            new CoffeeShopUI(orderProcessor, threadedProcessor);
            ThreadMonitorUI.launch(threadedProcessor);
        });

     // 启动后台线程来检查线程是否空闲
        Thread idleCheckerThread = new Thread(() -> {
            try {
                threadManager.checkAndExitIfIdle();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        idleCheckerThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(threadManager::stopServers));
    }
}

