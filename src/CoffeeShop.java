import javax.swing.*;
import java.awt.*;

public class CoffeeShop {
    public static void main(String[] args) {
        System.out.println("Welcome to our coffee shop");

        // 初始化菜单
        Menu menu = new Menu();
        menu.loadMenuFromFile("menu.txt");

        // 初始化订单列表
        OrderList orderList = new OrderList(menu);
        orderList.loadOrderListFromFile("orders.txt");

        // 订单处理器
        OrderProcessor orderProcessor = new OrderProcessor(orderList);

        // 启动 UI
        SwingUtilities.invokeLater(() -> new CoffeeShopUI(menu, orderProcessor));
    }
}
