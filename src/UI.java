

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UI {
	
	private Menu menu;
	private Order myOrder;
	
	// put new order in orderProcessor 
	private OrderProcessor orderProcessor;
	
	
	public UI(Menu menu, OrderProcessor orderProcessor) {
		this.menu = menu;
		this.orderProcessor = orderProcessor;
	}

	public void displayBill() {
		// TODO...
	}
	
	public Order takeOrder() {
		// TODO...
		myOrder = new Order(null, null, null, null);
		return myOrder;
	}
	
	
	
	// called when program exited.
	public void onExit() {
		StringBuilder finalreport = new StringBuilder();
		
		System.out.println("Generating final report...");
		
		// generate all items in menu.
		finalreport.append("======= Final Report =======\n\n");
		// list of all items in menu.
		List<Item> itemList = menu.getItemList();
		// list of all orders.
		List<Order> orderList = orderProcessor.getList().getOrderList();
		
		// record how many times each items of ordered.
		int[] numberOfOrders = new int[itemList.size()];
		
		Arrays.fill(numberOfOrders,0);
		
		double finalCost = 0;
		for (Order order: orderList) {
			for (Item item: order.getItemList()) {
				numberOfOrders[menu.indexOf(item)]++;
			}
			finalCost += order.calculateTotalCost();
		}
		
		
		finalreport.append(String.format("%-6s %-20s %-15s %11s %23s%n",
		        "Code", "Name", "Category", "Price", "Number of Orders") );
		finalreport.append("------------------------------"
				+ "--------------------------------------------------\n");
		int i = 0;
		for (Item item: itemList){
			finalreport.append(String.format("%-6s %-20s %-15s %10s %15s%n",
					item.getId(), item.getDescription(), item.getDescription(),
					item.getCost(), numberOfOrders[i++]) );
		}
		finalreport.append("------------------------------"
				+ "--------------------------------------------------\n\n");
		
		finalreport.append("Total income: " + finalCost);
		
		
		String report = finalreport.toString();
		
//		System.out.println("Current working directory: " + System.getProperty("user.dir"));
		
		try (FileWriter writer = new FileWriter("finalReport.txt")){
			writer.write(report);
		} catch (IOException e) {
			System.out.println("Error occurred while writing file: "+e.getMessage());
		}
	}
	
	
}
