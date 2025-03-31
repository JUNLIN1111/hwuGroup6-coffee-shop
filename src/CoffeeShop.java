import javax.swing.*;
import java.awt.*;

public class CoffeeShop {
    public static void main(String[] args) throws InvalidOrderException {
        System.out.println("Welcome to our coffee shop");

        final OrderProcessor orderProcessor;
        try {
            orderProcessor = new OrderProcessor(OrderList.getInstance());
        } catch (InvalidOrderException e) {
            throw new RuntimeException(e);
        }

        final ThreadedOrderProcessor threadedProcessor = new ThreadedOrderProcessor();
        final OrderThreadManager threadManager = new OrderThreadManager();

        threadManager.startServers(4);

        SwingUtilities.invokeLater(() -> {
            new CoffeeShopUI(orderProcessor, threadedProcessor);
            ThreadMonitorUI.launch(threadedProcessor);
        });

        Runtime.getRuntime().addShutdownHook(new Thread(threadManager::stopServers));
    }
}

