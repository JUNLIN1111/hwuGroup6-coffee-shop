public class CoffeeShop {

	
	public static void main(String[] args) {
		
		System.out.println("Welcome to our coffee shop");
		
		// initialize item list
		Menu menu = new Menu();
		menu.loadMenuFromFile("menu.txt");
		
		// initialize order list
		OrderList orderList = new OrderList();
		orderList.loadOrderListFromFile("orders.txt");
		
		OrderProcessor orderProcessor = new OrderProcessor(orderList);
		
		
		// initialize UI
		UI ui = new UI(menu,orderProcessor);
		ui.onExit();
		
	}

}
