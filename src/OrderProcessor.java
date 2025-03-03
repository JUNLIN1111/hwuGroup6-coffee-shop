import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderProcessor {
	private OrderList orderList;
	private Menu menu; // ✅ 直接存储 Menu

	public OrderProcessor(OrderList orderList, Menu menu) { // ✅ 传入 Menu
		this.orderList = orderList;
		this.menu = menu;
	}

	public OrderList getList() {
		return orderList;
	}

	// 生成最终订单报告
	public void generateFinalReport(String filePath) {
		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write("======= Final Report =======\n\n");
			writer.write(String.format("%-6s %-20s %-20s %-10s %-10s\n",
					"Code", "Name", "Category", "Price", "Number of Orders"));
			writer.write("--------------------------------------------------------------------------------\n");

			// 统计每个商品的销售数量
			Map<String, Integer> orderCount = new HashMap<>();
			double totalIncome = 0;

			for (Order order : orderList.getOrderList()) {
				for (Item item : order.getItemList()) {
					orderCount.put(item.getId(), orderCount.getOrDefault(item.getId(), 0) + 1);
					totalIncome += item.getCost();
				}
			}

			// ✅ 直接用 menu 遍历所有商品，避免调用 `getMenu()`
			for (Item item : menu.getItemList()) {
				int count = orderCount.getOrDefault(item.getId(), 0);
				writer.write(String.format("%-6s %-20s %-20s %-10.2f %-10d\n",
						item.getId(), item.getDescription(), item.getCategory(), item.getCost(), count));
			}

			writer.write("--------------------------------------------------------------------------------\n");
			writer.write(String.format("\nTotal income: %.2f\n", totalIncome));

			System.out.println("Final report successfully saved to " + filePath);
		} catch (IOException e) {
			System.err.println("Error writing final report: " + e.getMessage());
		}
	}
}
