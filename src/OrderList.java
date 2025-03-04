import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class OrderList {
	private List<Order> orders;
	private Menu menu;
	
	public OrderList(Menu menu) {
		this.orders = new ArrayList<>();
		this.menu = menu;
	}
	
	public List<Order> getOrderList() {
		return Collections.unmodifiableList(orders);
	}
	
	public void loadOrderListFromFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("Error: Order file not found - " + filePath);
			return;
		}
		
		orders.clear(); // Clear existing orders
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			String currentOrderId = null;
			String currentCustomerId = null;
			String currentTimestamp = null;
			List<Item> currentItems = new ArrayList<>();
			
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) {
					continue;
				}
				
				// Parse order data, format: orderId,customerId,timestamp,itemId
				String[] parts = line.split(",");
				if (parts.length >= 4) {  // 现在只需要4个字段
					try {
						String orderId = parts[0];
						String customerId = parts[1];
						String timestamp = parts[2];
						String itemId = parts[3];
						
						// 从菜单中查找商品
						Item menuItem = menu.findItemById(itemId);
						if (menuItem == null) {
							System.out.println("Error: Item not found in menu - " + itemId);
							continue;
						}
						
						// If this is a new order
						if (currentOrderId == null || !currentOrderId.equals(orderId)) {
							// Save previous order if exists
							if (currentOrderId != null) {
								Order order = new Order(currentOrderId, currentCustomerId, currentTimestamp, 
													  new ArrayList<>(currentItems));
								orders.add(order);
							}
							// Start new order
							currentOrderId = orderId;
							currentCustomerId = customerId;
							currentTimestamp = timestamp;
							currentItems.clear();
						}
						
						// Add item to current order
						currentItems.add(menuItem);
						
					} catch (NumberFormatException e) {
						System.out.println("Error: Invalid number format - " + line);
					}
				} else {
					System.out.println("Error: Invalid line format - " + line);
				}
			}
			
			// Add the last order if exists
			if (currentOrderId != null) {
				Order order = new Order(currentOrderId, currentCustomerId, currentTimestamp, 
									  new ArrayList<>(currentItems));
				orders.add(order);
			}
			
		} catch (IOException e) {
			System.out.println("Error reading order file: " + e.getMessage());
		}
	}
	
	public Order findOrderByOrderId(String orderId) {
		return orders.stream()
					.filter(order -> order.getOrderId().equals(orderId))
					.findFirst()
					.orElse(null);
	}
	
	public List<Order> findOrderByCustomerId(String customerId) {
		return orders.stream()
					.filter(order -> order.getCustomerId().equals(customerId))
					.collect(Collectors.toList());
	}
	
	public void addOrder(Order order) {
		if (order == null) {
			throw new IllegalArgumentException("订单不能为空");
		}
		// 检查订单ID是否已存在
		if (findOrderByOrderId(order.getOrderId()) != null) {
			throw new IllegalArgumentException("订单ID已存在：" + order.getOrderId());
		}
		orders.add(order);
	}
	
	public void removeOrderByOrderId(String orderId) {
		Order order = findOrderByOrderId(orderId);
		if (order != null) {
			orders.remove(order);
		}
	}
	
	public void removeOrderByCustomerId(String customerId) {
		orders.removeIf(order -> order.getCustomerId().equals(customerId));
	}
	
	public void generateLog() {
		try (FileWriter writer = new FileWriter("order_log.txt", true)) {
			writer.write("\n=== Order Log ===\n");
			writer.write("Total Orders: " + orders.size() + "\n");
			
			// Group statistics by customer
			writer.write("\nCustomer Order Statistics:\n");
			orders.stream()
				  .collect(Collectors.groupingBy(Order::getCustomerId))
				  .forEach((customerId, customerOrders) -> {
					  try {
						  double totalCost = customerOrders.stream()
														 .mapToDouble(Order::calculateTotalCost)
														 .sum();
						  writer.write(String.format("Customer ID: %s, Order Count: %d, Total Spent: $%.2f\n",
									 customerId, customerOrders.size(), totalCost));
					  } catch (IOException e) {
						  System.out.println("Error writing to log: " + e.getMessage());
					  }
				  });
			
			writer.write("\nDetailed Order Information:\n");
			for (Order order : orders) {
				writer.write(String.format("Order ID: %s, Customer ID: %s, Time: %s\n",
						   order.getOrderId(), order.getCustomerId(), order.getTimeStamp()));
				for (Item item : order.getItemList()) {
					writer.write(String.format("  - %s: %s ($%.2f)\n",
							   item.getId(), item.getDescription(), item.getCost()));
				}
				writer.write(String.format("  Total: $%.2f\n\n", order.calculateTotalCost()));
			}
		} catch (IOException e) {
			System.out.println("Error generating log: " + e.getMessage());
		}
	}

	public void saveOrdersToFile(String filePath) {
		try (FileWriter writer = new FileWriter(filePath, true)) { // ✅ 使用 `true` 追加订单
			for (Order order : orders) {
				for (Item item : order.getItemList()) {
					writer.write(order.getOrderId() + "," + order.getCustomerId() + "," +
							order.getTimeStamp() + "," + item.getId() + "\n");
				}
			}
		} catch (IOException e) {
			System.out.println("Error writing orders to file: " + e.getMessage());
		}
	}

}

 

