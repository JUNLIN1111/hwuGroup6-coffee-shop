//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import java.util.Arrays;
//import java.util.List;
//
//public class OrderTest {
//
//    private final OrderProcessor orderProcessor = new OrderProcessor(new OrderList(\new Menu()));
//
//    @Test
//    public void testOrderCreation() throws Itemexception, InvalidOrderException {
//        Item item1 = new Item("C001", "Americano", "Coffee", 25.0);
//        Item item2 = new Item("C002", "Latte", "Coffee", 30.0);
//        List<Item> items = Arrays.asList(item1, item2);
//
//        Order order = new Order("ORD123", "CUST001", "2025-03-03 12:00", items);
//
//        assertEquals("ORD123", order.getOrderId());
//        assertEquals("CUST001", order.getCustomerId());
//        assertEquals("2025-03-03 12:00", order.getTimeStamp());
//        assertEquals(2, order.getItemList().size());
//    }
//
//    @Test
//    public void testCalculateTotalCostWithDessertAndCoffeeOrTea() throws Itemexception, InvalidOrderException {
//        Item item1 = new Item("D001", "Chocolate Cake", "Dessert", 35.0);
//        Item item2 = new Item("C002", "Latte", "Coffee", 30.0);
//        List<Item> items = Arrays.asList(item1, item2);
//
//        Order order = new Order("ORD123", "CUST001", "2025-03-03 12:00", items);
//
//        double expectedTotal = (35.0 + 30.0) * 0.9;
//        assertEquals(expectedTotal, orderProcessor.calculateOrderTotal(order), 0.01);
//    }
//
//    @Test
//    public void testCalculateTotalCostWithMultipleItems() throws Itemexception, InvalidOrderException {
//        Item item1 = new Item("C001", "Americano", "Coffee", 25.0);
//        Item item2 = new Item("C002", "Latte", "Coffee", 30.0);
//        Item item3 = new Item("C003", "Cappuccino", "Coffee", 30.0);
//        Item item4 = new Item("T001", "Black Tea", "Tea", 20.0);
//        Item item5 = new Item("T002", "Green Tea", "Tea", 20.0);
//        List<Item> items = Arrays.asList(item1, item2, item3, item4, item5);
//
//        Order order = new Order("ORD124", "CUST002", "2025-03-03 12:00", items);
//
//        double expectedTotal = (25.0 + 30.0 + 30.0 + 20.0 + 20.0) * 0.75;
//        assertEquals(expectedTotal, orderProcessor.calculateOrderTotal(order), 0.01);
//    }
//
//    @Test
//    public void testCalculateTotalCostWithHighAmount() throws Itemexception, InvalidOrderException {
//        Item item1 = new Item("D001", "Chocolate Cake", "Dessert", 35.0);
//        Item item2 = new Item("C003", "Cappuccino", "Coffee", 30.0);
//        Item item3 = new Item("D002", "Tiramisu", "Dessert", 35.0);
//        List<Item> items = Arrays.asList(item1, item2, item3);
//
//        Order order = new Order("ORD125", "CUST003", "2025-03-03 12:00", items);
//
//        double expectedTotal = (35.0 + 30.0 + 35.0) * 0.8;
//        assertEquals(expectedTotal, orderProcessor.calculateOrderTotal(order), 0.01);
//    }
//
//    @Test
//    public void testCalculateTotalCostWithExactly100() throws Itemexception, InvalidOrderException {
//        Item item1 = new Item("C001", "Americano", "Coffee", 50.0);
//        Item item2 = new Item("C002", "Latte", "Coffee", 50.0);
//        List<Item> items = Arrays.asList(item1, item2);
//
//        Order order = new Order("ORD126", "CUST004", "2025-03-03 12:00", items);
//
//        double expectedTotal = (50.0 + 50.0) * 0.8; // 100 元打 20% 折扣
//        assertEquals(expectedTotal, orderProcessor.calculateOrderTotal(order), 0.01);
//    }
//
//    @Test
//    public void testCalculateTotalCostWithMultipleDiscounts() throws Itemexception, InvalidOrderException {
//        Item item1 = new Item("D002", "Tiramisu", "Dessert", 40.0);
//        Item item2 = new Item("C002", "Latte", "Coffee", 30.0);
//        Item item3 = new Item("T001", "Black Tea", "Tea", 20.0);
//        Item item4 = new Item("T002", "Green Tea", "Tea", 20.0);
//        List<Item> items = Arrays.asList(item1, item2, item3, item4);
//
//        Order order = new Order("ORD127", "CUST005", "2025-03-03 12:00", items);
//
//        double expectedTotal = (40.0 + 30.0 + 20.0 + 20.0) * 0.8; // 100 元以上，应用 20% 折扣
//        assertEquals(expectedTotal, orderProcessor.calculateOrderTotal(order), 0.01);
//    }
//}
