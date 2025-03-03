

import org.junit.jupiter.api.Test;  // Import JUnit 5 Test annotation
import static org.junit.jupiter.api.Assertions.*;  // Import assertion methods

import java.util.Arrays;
import java.util.List;

public class OrderTest {

    @Test
    public void testOrderCreation() {
        // create item list
        Item item1 = new Item("C001", "Americano", "Coffee", 25.0);
        Item item2 = new Item("C002", "Latte", "Coffee", 30.0);
        List<Item> items = Arrays.asList(item1, item2);

        // create order
        Order order = new Order("ORD123", "CUST001", "2025-03-03 12:00", items);

        // Validate order attributes
        assertEquals("ORD123", order.getOrderId());
        assertEquals("CUST001", order.getCustomerId());
        assertEquals("2025-03-03 12:00", order.getTimeStamp());
        assertEquals(2, order.getItemList().size());
    }


 // Test case 1: Verify if the 10% discount is applied correctly when there is at least 1 dessert and 1 coffee or tea
    @Test
    public void testCalculateTotalCostWithDessertAndCoffeeOrTea() {
        Item item1 = new Item("D001", "Chocolate Cake", "Dessert", 35.0);  // Dessert item
        Item item2 = new Item("C002", "Latte", "Coffee", 30.0);           // Coffee item
        List<Item> items = Arrays.asList(item1, item2);
        
        Order order = new Order("ORD123", "CUST001", "2025-03-03 12:00", items);
        
        // The expected total is 58.5 (after applying the 10% discount)
        double expectedTotal = (35.0 + 30.0) * 0.9;
        assertEquals(expectedTotal, order.calculateTotalCost(), 0.01);
    }

    // Test case 2: Verify if the 25% discount is applied correctly when there are 5 or more items
    @Test
    public void testCalculateTotalCostWithMultipleItems() {
        Item item1 = new Item("C001", "Americano", "Coffee", 25.0);
        Item item2 = new Item("C002", "Latte", "Coffee", 30.0);
        Item item3 = new Item("C003", "Cappuccino", "Coffee", 30.0);
        Item item4 = new Item("T001", "Black Tea", "Tea", 20.0);
        Item item5 = new Item("T002", "Green Tea", "Tea", 20.0);
        List<Item> items = Arrays.asList(item1, item2, item3, item4, item5);
        
        Order order = new Order("ORD124", "CUST002", "2025-03-03 12:00", items);
        
        // The expected total is 125.0 (after applying the 25% discount)
        double expectedTotal = (25.0 + 30.0 + 30.0 + 20.0 + 20.0) * 0.75;
        assertEquals(expectedTotal, order.calculateTotalCost(), 0.01);
    }

    // Test case 3: Verify if the 20% discount is applied correctly when the total cost is exactly 100
    @Test
    public void testCalculateTotalCostWithHighAmount() {
        Item item1 = new Item("D001", "Chocolate Cake", "Dessert", 35.0);  // Dessert item
        Item item2 = new Item("C003", "Cappuccino", "Coffee", 30.0);
        Item item3 = new Item("D001", "Chocolate Cake", "Dessert", 35.0); // Coffee item
        List<Item> items = Arrays.asList(item1, item2,item3);
        
        Order order = new Order("ORD125", "CUST003", "2025-03-03 12:00", items);
        
        // The expected total is 52.0 (after applying the 20% discount)
        double expectedTotal = (35.0 + 30.0 + 35.0) * 0.8;
        assertEquals(expectedTotal, order.calculateTotalCost(), 0.01);
    }

    // Test case 4: Verify if no discount is applied when the total cost is less than 100
    @Test
    public void testCalculateTotalCostWithExactly100() {
        Item item1 = new Item("C001", "Americano", "Coffee", 25.0);  // Coffee item
        Item item2 = new Item("C001", "Americano", "Coffee", 25.0);  
        List<Item> items = Arrays.asList(item1, item2);
        
        Order order = new Order("ORD126", "CUST004", "2025-03-03 12:00", items);
        
        // The total is 60.0, no discount is applied
        double expectedTotal = 50.0;
        assertEquals(expectedTotal, order.calculateTotalCost(), 0.01);
    }

    // Test case 5: Verify that discounts are not stacked, and the best discount is applied
    @Test
    public void testCalculateTotalCostWithMultipleDiscounts() {
        Item item1 = new Item("D002", "Tiramisu", "Dessert", 40.0);  // Dessert item
        Item item2 = new Item("C002", "Latte", "Coffee", 30.0);  // Coffee item
        Item item3 = new Item("T001", "Black Tea", "Tea", 20.0);    // Tea item
        Item item4 = new Item("T001", "Black Tea", "Tea", 20.0);
        List<Item> items = Arrays.asList(item1, item2, item3,item4);
        
        Order order = new Order("ORD127", "CUST005", "2025-03-03 12:00", items);
        
      
        double expectedTotal = (40.0 + 30.0 + 20.0+20.0) * 0.8;
        assertEquals(expectedTotal, order.calculateTotalCost(), 0.01);
    }

}
