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
		} catch (InvalidOrderException e) {
			
			e.printStackTrace();
		}

        //orderProcessor
        OrderProcessor orderProcessor = new OrderProcessor(orderList);

        
        SwingUtilities.invokeLater(() -> new CoffeeShopUI(menu, orderProcessor));
    }
}
