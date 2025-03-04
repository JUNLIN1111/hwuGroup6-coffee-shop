import javax.swing.*;
import java.awt.*;

public class CoffeeShop {
	public static void main(String[] args) {
		System.out.println("Welcome to our coffee shop");

		// 初始化菜单
		Menu menu = new Menu();
		menu.loadMenuFromFile("menu.txt");

		// 初始化订单列表
		// TODO.. 导入orders.txt
		// TODO.. Order里不要加Menu，物品计算订单数量和总价任务交给OrderProcessor或者UI完成
		// TODO.. 计算折扣没有生效，不要写在order里，order仅用于储存订单信息，方法同上，减少耦合
		OrderList orderList = new OrderList(menu); // ✅ 传入 Menu

		// 订单处理器（✅ 传入 orderList 和 menu）
		OrderProcessor orderProcessor = new OrderProcessor(orderList, menu);

		// 启动 UI
		SwingUtilities.invokeLater(() -> new CoffeeShopUI(menu, orderProcessor));
	}
}
