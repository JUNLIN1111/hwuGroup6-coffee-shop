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
			System.out.println("Error: Order file does not exist - " + filePath);
			return;
		}

		items.clear(); // 娓呯┖鐜版湁鑿滃崟椤�
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// 璺宠繃绌鸿
				if (line.trim().isEmpty()) {
					continue;
				}
				
				// 瑙ｆ瀽姣忚鏁版嵁锛屾牸寮忓亣瀹氫负锛歩d,description,category,cost
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
						System.out.println("Error: Incorrect price format - " + line);
					}
				} else {
					System.out.println("Error: Incorrect order format- " + line);
				}
			}
		} catch (IOException e) {
			System.out.println("An error occurred while reading the order file:" + e.getMessage());
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
				   .filter(item -> item.getCategory().equals(category))
				   .collect(Collectors.toList());
	}

	public void addItem(Item item) {
		if (item == null) {
			throw new IllegalArgumentException("Order cannot be empty");
		}
		// 妫�鏌D鏄惁宸插瓨鍦�
		if (findItemById(item.getId()) != null) {
			throw new IllegalArgumentException("Order ID already exists:" + item.getId());
		}
		items.add(item);
	}

	public void removeItemById(String id) {
		Item item = findItemById(id);
		if (item != null) {
			items.remove(item);
		}
	}

	// 鑾峰彇鎵�鏈夊彲鐢ㄧ殑绫诲埆
	public List<String> getAllCategories() {
		return items.stream()
				   .map(Item::getCategory)
				   .distinct()
				   .collect(Collectors.toList());
	}

	// 鑾峰彇鑿滃崟椤规暟閲�
	public int getItemCount() {
		return items.size();
	}
	
	// TODO an exception is needed
	public int indexOf(Item _item) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getId().equals(_item.getId())) {
				return i;
			}
		}
		return -1;
	}
}
