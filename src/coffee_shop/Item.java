package coffee_shop;

public class Item {
	private String id;
	private String description;
	private String category;
	private double cost;
	
	public Item(String id,String description,String category,double cost) {
		this.id = id.trim();
		this.description = description.trim();
		this.category = category.trim();
		this.cost = cost;
	}
	
	public String getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getcategory() {
		return category;
	}
	
	public double getCost() {
		return cost;
	}
}


