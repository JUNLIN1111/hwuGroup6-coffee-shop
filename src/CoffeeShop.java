import javax.swing.*;
import java.awt.*;

public class CoffeeShop {
    public static void main(String[] args) {
        System.out.println("Welcome to our coffee shop");

        // new Orderlist
        OrderList orderList = new OrderList();

        //orderProcessor
        OrderProcessor orderProcessor = new OrderProcessor(orderList);

        ThreadedOrderProcessor threadedProcessor = new ThreadedOrderProcessor();

        OrderThreadManager threadManager = new OrderThreadManager(threadedProcessor);

        threadManager.startServers(2);


        SwingUtilities.invokeLater(() -> {
            new CoffeeShopUI(menu, orderProcessor, threadedProcessor);
            ThreadMonitorUI.launch(threadedProcessor);
        });


        Runtime.getRuntime().addShutdownHook(new Thread(threadManager::stopServers));
    }
}
