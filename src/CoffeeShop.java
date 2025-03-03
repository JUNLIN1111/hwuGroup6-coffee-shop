import javafx.application.Application;
import javafx.stage.Stage;

public class CoffeeShop extends Application {
	public static void main(String[] args) {
		System.out.println("Welcome to our coffee shop");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// 初始化菜单
		Menu menu = new Menu();
		menu.loadMenuFromFile("menu.txt");

		// 初始化订单列表
		OrderList orderList = new OrderList(menu);
		orderList.loadOrderListFromFile("orders.txt");

		// 订单处理器
		OrderProcessor orderProcessor = new OrderProcessor(orderList);

		// 启动 UI
		CoffeeShopUI ui = new CoffeeShopUI(menu, orderProcessor);
		primaryStage.setScene(ui.getScene());
		primaryStage.setTitle("Coffee Shop Simulator");
		primaryStage.show();

		// 退出时触发 onExit（如果 onExit() 需要执行一些操作）
		primaryStage.setOnCloseRequest(event -> ui.onExit());
	}
}
