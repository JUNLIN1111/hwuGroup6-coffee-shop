import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {
	private List<Item> items;

	public Menu() {
		items = new ArrayList<>();
	}

	public void loadMenuFromFile(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			System.out.println("错误：菜单文件不存在 - " + filePath);
			return;
		}

		items.clear(); // 清空现有菜单项
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// 跳过空行
				if (line.trim().isEmpty()) {
					continue;
				}
				
				// 解析每行数据，格式假定为：id,description,category,cost
				String[] parts = line.split(",");
				if (parts.length == 4) {
					try {
						String id = parts[0];
						String description = parts[1];
						String category = parts[2];
						double cost = Double.parseDouble(parts[3]);
						
						Item item = new Item(id, description, category, cost);
						items.add(item);
					} catch (NumberFormatException e) {
						System.out.println("错误：价格格式不正确 - " + line);
					}
				} else {
					System.out.println("错误：行格式不正确 - " + line);
				}
			}
		} catch (IOException e) {
			System.out.println("读取菜单文件时发生错误：" + e.getMessage());
		}
	}

	public List<Item> getItemList() {
		return Collections.unmodifiableList(items);
	}

	public Item findItemById(String id) {
		return items.stream()
				   .filter(item -> item.getId().equals(id))
				   .findFirst()
				   .orElse(null);
	}

	public List<Item> findItemsByCategory(String category) {
		return items.stream()
				   .filter(item -> item.getcategory().equals(category))
				   .collect(Collectors.toList());
	}

	public void addItem(Item item) {
		if (item == null) {
			throw new IllegalArgumentException("商品不能为空");
		}
		// 检查ID是否已存在
		if (findItemById(item.getId()) != null) {
			throw new IllegalArgumentException("商品ID已存在：" + item.getId());
		}
		items.add(item);
	}

	public void removeItemById(String id) {
		Item item = findItemById(id);
		if (item != null) {
			items.remove(item);
		}
	}

	// 获取所有可用的类别
	public List<String> getAllCategories() {
		return items.stream()
				   .map(Item::getcategory)
				   .distinct()
				   .collect(Collectors.toList());
	}

	// 获取菜单项数量
	public int getItemCount() {
		return items.size();
	}
}
