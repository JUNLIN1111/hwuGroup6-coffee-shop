

import java.io.FileWriter;
import java.io.IOException;

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
		
		finalreport.append("Menu Items\n\n");
		
		String report = finalreport.toString();
		
//		System.out.println("Current working directory: " + System.getProperty("user.dir"));
		
		try (FileWriter writer = new FileWriter("src/finalReport.txt")){
			writer.write(report);
		} catch (IOException e) {
			System.out.println("Error occurred while writing file: "+e.getMessage());
		}
	}
	
	
}
