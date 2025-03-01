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
	
	public OrderList() {
		orders = new ArrayList<>();
	}
	
	public List<Order> getOrderList() {
		return Collections.unmodifiableList(orders);
	}
	
	public void loadOrderListFromFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("错误：订单文件不存在 - " + filePath);
			return;
		}
		
		orders.clear(); // 清空现有订单
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) {
					continue;
				}
				
				// 解析订单数据，格式：orderId,customerId,timestamp,itemId,description,category,cost
				String[] parts = line.split(",");
				if (parts.length >= 7) {
					try {
						String orderId = parts[0];
						String customerId = parts[1];
						String timestamp = parts[2];
						
						// 创建商品
						String itemId = parts[3];
						String description = parts[4];
						String category = parts[5];
						double cost = Double.parseDouble(parts[6]);
						Item item = new Item(itemId, description, category, cost);
						
						// 创建商品列表
						List<Item> items = new ArrayList<>();
						items.add(item);
						
						// 创建订单
						Order order = new Order(orderId, customerId, timestamp, items);
						orders.add(order);
					} catch (NumberFormatException e) {
						System.out.println("错误：价格格式不正确 - " + line);
					}
				} else {
					System.out.println("错误：订单格式不正确 - " + line);
				}
			}
		} catch (IOException e) {
			System.out.println("读取订单文件时发生错误：" + e.getMessage());
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
}

 
