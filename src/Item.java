public class Item {
    private String id;
    private String description;
    private String category;
    private double cost;
    private static final int MAX_DESCRIPTION_LENGTH = 50; // 最大描述长度

    public Item(String id, String description, String category, double cost) throws IllegalArgumentException {
        // Validate id
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty.");
        }
        this.id = id.trim();

        // Validate description
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Description is too long (max " + MAX_DESCRIPTION_LENGTH + " characters).");
        }
        this.description = description.trim();

        // Validate category
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty.");
        }
        this.category = category.trim();

        // Validate cost
        if (cost <= 0) {
            throw new IllegalArgumentException("Cost cannot be negative or 0.");
        }
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public double getCost() {
        return cost;
    }

    
}
