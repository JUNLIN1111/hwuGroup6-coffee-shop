public class Item {
    private String id;
    private String description;
    private String category;
    private double cost;
    private static final int MAX_DESCRIPTION_LENGTH = 50; //max description
    private double preparationTime; // new attribute to describe preparationTime

    public Item(String id, String description, String category, double cost) throws Itemexception {
        // Validate id
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalItemIdException("ID cannot be null or empty.");
        }
        this.id = id.trim();

        // Validate description
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalDescriptionException("Description cannot be null or empty.");
        }
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalDescriptionException("Description is too long (max " + MAX_DESCRIPTION_LENGTH + " characters).");
        }
        this.description = description.trim();

        // Validate category
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalCategoryException("Category cannot be null or empty.");
        }
        this.category = category.trim();

        // Validate cost
        if (cost < 0) {
            throw new IllegalCostException("Cost cannot be negative 0.");
        }
        this.cost = cost;

        // Set preparation time based on category
        setPreparationTimeByCategory();
    }

    private void setPreparationTimeByCategory() {
        switch (category.toLowerCase()) {
            case "dessert":
                this.preparationTime = 500;
                break;
            case "tea":
                this.preparationTime = 1000;
                break;
            case "coffee":
                this.preparationTime = 1500;
                break;
            default:
                this.preparationTime = 1000;
        }
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

    public double getPreparationTime() {
        return preparationTime;
    }

    public double getCost() {
        return cost;
    }


}
