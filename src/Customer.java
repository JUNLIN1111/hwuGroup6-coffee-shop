import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Customer {
    private String customerId;
    private String name;
    private Order order;

    public Customer(String customerId, String name, Order order) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty.");
        }
        this.customerId = customerId.trim();

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name.trim();

        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }
        this.order = order;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public Order getOrder() {
        return order;
    }

    public double calculateOrderCost() {
        return order.calculateTotalCost();
    }

    public int calculateOrderTime() {
        return order.calculateTotalTime();
    }
}

class CustomerQueue {
    private BlockingQueue<Customer> customerQueue = new LinkedBlockingQueue<>();

    public void addCustomer(Customer customer) {
        try {
            customerQueue.put(customer);
            System.out.println("Customer " + customer.getName() + " added to the queue.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to add customer to the queue.");
        }
    }

    public Customer getNextCustomer() {
        try {
            return customerQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to retrieve customer from the queue.");
            return null;
        }
    }

    public void processCustomers(Server server) {
        while (!customerQueue.isEmpty()) {
            Customer customer = getNextCustomer();
            if (customer != null) {
                server.processOrder(customer.getOrder());
            }
        }
    }
}
