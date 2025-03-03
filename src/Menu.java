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
			System.out.println("Error: Menu file not found - " + filePath);
			return;
		}

		items.clear(); // Clear existing menu items
		
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Skip empty lines
				if (line.trim().isEmpty()) {
					continue;
				}
				
				// Parse each line, format: id,description,category,cost
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
						System.out.println("Error: Invalid price format - " + line);
					}
				} else {
					System.out.println("Error: Invalid line format - " + line);
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading menu file: " + e.getMessage());
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
			throw new IllegalArgumentException("Item cannot be null");
		}
		// Check if ID already exists
		if (findItemById(item.getId()) != null) {
			throw new IllegalArgumentException("Item ID already exists: " + item.getId());
		}
		items.add(item);
	}

	public void removeItemById(String id) {
		Item item = findItemById(id);
		if (item != null) {
			items.remove(item);
		}
	}

	// Get all available categories
	public List<String> getAllCategories() {
		return items.stream()
				   .map(Item::getCategory)
				   .distinct()
				   .collect(Collectors.toList());
	}

	// Get total number of menu items
	public int getItemCount() {
		return items.size();
	}
}
