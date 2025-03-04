import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class OrderList {
	private List<Order> orders;
	private Menu menu;

	public OrderList(Menu menu) {
		this.orders = new ArrayList<>();
		this.menu = menu;
	}

	public List<Order> getOrderList() {
		return orders;
	}

	public void addOrder(Order order) {
		if (order == null) {
			throw new IllegalArgumentException("订单不能为空");
		}
		if (findOrderByOrderId(order.getOrderId()) != null) {
			throw new IllegalArgumentException("订单ID已存在：" + order.getOrderId());
		}
		orders.add(order);
	}

	public Order findOrderByOrderId(String orderId) {
		return orders.stream()
				.filter(order -> order.getOrderId().equals(orderId))
				.findFirst()
				.orElse(null);
	}

	public void removeOrderByOrderId(String orderId) {
		Order order = findOrderByOrderId(orderId);
		if (order != null) {
			orders.remove(order);
		}
	}

	/**
	 * 从文件加载订单
	 */
	public void loadOrderListFromFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("订单文件不存在: " + filePath);
			return;
		}

		Map<String, List<Item>> orderItemsMap = new HashMap<>();  // 用于存储订单的商品列表
		Map<String, String[]> orderInfoMap = new HashMap<>();  // 存储 orderId 对应的 (customerId, timeStamp)

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) {
					continue;
				}

				// 解析格式: orderId,customerId,timeStamp,itemId
				String[] parts = line.split(",");
				if (parts.length != 4) {
					System.out.println("错误: 订单格式不正确 - " + line);
					continue;
				}

				String orderId = parts[0];
				String customerId = parts[1];
				String timeStamp = parts[2];
				String itemId = parts[3];

				// 查找商品
				Item item = menu.findItemById(itemId);
				if (item == null) {
					System.out.println("警告: 找不到商品 ID - " + itemId + "，跳过该商品");
					continue;
				}

				// 记录订单的基本信息（仅在第一次遇到该订单时记录）
				orderInfoMap.putIfAbsent(orderId, new String[]{customerId, timeStamp});

				// 添加商品到订单的商品列表
				orderItemsMap.computeIfAbsent(orderId, k -> new ArrayList<>()).add(item);
			}
		} catch (IOException e) {
			System.out.println("读取订单文件出错: " + e.getMessage());
		}

		// 生成订单对象
		orders.clear();
		for (String orderId : orderItemsMap.keySet()) {
			List<Item> itemList = orderItemsMap.get(orderId);
			if (itemList.isEmpty()) {
				System.out.println("警告: 订单 " + orderId + " 没有商品，跳过该订单");
				continue;  // **确保不会创建空订单**
			}

			String[] info = orderInfoMap.get(orderId);
			orders.add(new Order(orderId, info[0], info[1], itemList));
		}
	}
}
