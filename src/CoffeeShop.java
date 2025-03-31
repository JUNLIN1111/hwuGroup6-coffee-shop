import javax.swing.*;
import java.awt.*;

public class CoffeeShop {
    public static void main(String[] args) {
        System.out.println("Welcome to our coffee shop");

        // new Menu
        Menu menu = new Menu();
        menu.loadMenuFromFile("menu.txt");

        // new Orderlist
        OrderList orderList = new OrderList(menu);
        try {
			orderList.loadOrderListFromFile("orders.txt");
            System.out.println(orderList.getOrderList().size());
            for (Order order : orderList.getOrderList()) {
                System.out.println(order.getOrderId());
            }
		} catch (InvalidOrderException e) {

			e.printStackTrace();
		}

        //orderProcessor
        OrderProcessor orderProcessor = new OrderProcessor(orderList);

        ThreadedOrderProcessor threadedProcessor = new ThreadedOrderProcessor();

        OrderThreadManager threadManager = new OrderThreadManager(threadedProcessor);

        threadManager.startServers(2);

        threadManager.addOrdersWithTiming(orderList.getOrderList());

        SwingUtilities.invokeLater(() -> {
            new CoffeeShopUI(menu, orderProcessor, threadedProcessor);
            ThreadMonitorUI.launch(threadedProcessor);
        });


        Runtime.getRuntime().addShutdownHook(new Thread(threadManager::stopServers));
    }
}
