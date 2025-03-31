import javax.swing.*;
import java.awt.*;

public class CoffeeShop {
    public static void main(String[] args) {
        System.out.println("Welcome to our coffee shop");

        final Menu menu = Menu.getInstance();
        final OrderList orderList;
        final OrderProcessor orderProcessor;
        try {
            orderList = OrderList.getInstance();
            orderProcessor = new OrderProcessor(orderList);
        } catch (InvalidOrderException e) {
            throw new RuntimeException(e);
        }

        final ThreadedOrderProcessor threadedProcessor = new ThreadedOrderProcessor();
        final OrderThreadManager threadManager = new OrderThreadManager(threadedProcessor);

        threadManager.startServers(2);

        threadManager.addOrdersWithTiming(orderList.getOrderList());

        SwingUtilities.invokeLater(() -> {
            new CoffeeShopUI(orderProcessor, threadedProcessor);
            ThreadMonitorUI.launch(threadedProcessor);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(threadManager::stopServers));
    }
}

