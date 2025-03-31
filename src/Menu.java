import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {

	private static volatile Menu instance;
	private static final String FILE_PATH = "menu.txt";

	private List<Item> items;

	private Menu(){
		items = new ArrayList<>();
		loadMenuFromFile(FILE_PATH);
	}

	public static Menu getInstance(){
		if(instance == null){
			synchronized (Menu.class){
				if(instance == null){
					instance = new Menu();
				}
			}
		}
		return instance;
	}


	private void loadMenuFromFile(String filePath) {
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
						
						Item item;
						try {
							item = new Item(id, description, category, cost);
							items.add(item);
						} catch (Itemexception e) {
							e.printStackTrace();
						}
						
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

	private List<Item> getItemList() {
		return Collections.unmodifiableList(items);
	}

	private Item findItemById(String id) {
		return items.stream()
				   .filter(item -> item.getId().equals(id))
				   .findFirst()
				   .orElse(null);
	}

	private List<Item> findItemsByCategory(String category) {
		return items.stream()
				   .filter(item -> item.getCategory().equals(category))
				   .collect(Collectors.toList());
	}

	private void addItem(Item item) {
		if (item == null) {
			throw new IllegalArgumentException("Item cannot be null");
		}
		// Check if ID already exists
		if (findItemById(item.getId()) != null) {
			throw new IllegalArgumentException("Item ID already exists: " + item.getId());
		}
		items.add(item);
	}
}
