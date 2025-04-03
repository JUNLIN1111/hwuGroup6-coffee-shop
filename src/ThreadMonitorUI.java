import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * UI for monitoring thread activity and order processing status
 */
public class ThreadMonitorUI extends JFrame {
    // Reference to the order processor
    private final ThreadedOrderProcessor processor;
    
    // UI components
    private final JTextArea wait_queue;       // Displays waiting orders queue
    private JTextArea[] info_sever = new JTextArea[4];  // Server status panels
    private final JLabel lastUpdated;         // Last update time display
    private final JLabel speedLabel;          // Simulation speed display
    private final JLabel queueStatusLabel;    // Queue capacity status display

    /**
     * Constructs the monitoring UI
     * @param processor Reference to the order processor for data access
     */
    public ThreadMonitorUI(ThreadedOrderProcessor processor) {
        this.processor = processor;

        // Configure main window
        setTitle("Information");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        // Waiting queue panel setup
        wait_queue = new JTextArea("Waiting queue");
        wait_queue.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        add(new JScrollPane(wait_queue), gbc);

        // Server status panels setup
        info_sever = new JTextArea[4];
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.5;
        for (int i = 0; i < 4; i++) {
            info_sever[i] = new JTextArea();
            info_sever[i].setEditable(false);
            info_sever[i].setText("Free");
            gbc.gridx = i;
            add(new JScrollPane(info_sever[i]), gbc);
        }

        // Status bar setup
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;

        // Last updated time label
        lastUpdated = new JLabel("Last updated: ");
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        add(lastUpdated, gbc);

        // Queue status label (current/max capacity)
        queueStatusLabel = new JLabel("Queue: 0/" + processor.getMaxQueueSize());
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        add(queueStatusLabel, gbc);

        // Simulation speed display
        speedLabel = new JLabel("Simulation Speed: 1.0x");
        gbc.gridx = 3;
        add(speedLabel, gbc);

        // Start UI refresh timer
        startRefreshing();
        setVisible(true);
    }

    /**
     * Starts the automatic UI refresh timer
     */
    private void startRefreshing() {
        Timer timer = new Timer(1000, e -> {
            try {
                refreshDisplay();
            } catch (InvalidOrderException ex) {
                ex.printStackTrace();
            }
        });
        timer.start();
    }

    /**
     * Refreshes all UI components with latest data
     * @throws InvalidOrderException If invalid order data is encountered
     */
    private void refreshDisplay() throws InvalidOrderException {
        // Update waiting queue display
        StringBuilder pendingText = new StringBuilder("Waiting Queue:\n");
        try {
            List<Order> waitqueue = OrderList.getInstance().getOrderList();
            for (Order order : waitqueue) {
                pendingText.append(order.getOrderId())
                          .append(" - ")
                          .append(order.getCustomerId())
                          .append("\n");
            }
            // Update queue capacity status
            queueStatusLabel.setText("Queue: " + waitqueue.size() + "/" + processor.getMaxQueueSize());
            if (waitqueue.size() >= processor.getMaxQueueSize()) {
            	queueStatusLabel.setForeground(Color.RED); // Highlight when full
            } else {
            	queueStatusLabel.setForeground(Color.BLACK);
            }
        } catch (InvalidOrderException e) {
            e.printStackTrace();
        }
        wait_queue.setText(pendingText.toString());
        

        // Update server status displays
        Map<String, Order> active = OrderList.getInstance().getActiveOrders();
        if (active == null) {
            System.out.println("Active orders map is null");
            return;
        }

        for (int i = 1; i <= 4; i++) {
            StringBuilder activeText = new StringBuilder("Server " + i + ":\n");
            String serverKey = "Server-" + i;
            Order activeOrder = active.get(serverKey);

            if (activeOrder != null) {
                // Show order details if server is busy
                activeText.append("Processing ")
                          .append(activeOrder.getCustomerId())
                          .append("'s order\n");
                
                // List order items
                for (Item item : activeOrder.getItemList()) {
                    activeText.append(item.getDescription()).append("\n");
                }
                
                // Show pricing information
                activeText.append("\nOriginal total cost: ")
                          .append(activeOrder.calculateOriginalCost())
                          .append("\nTotal cost: ")
                          .append(activeOrder.calculateTotalCost());
            } else {
                activeText.append("Free");
            }
            info_sever[i - 1].setText(activeText.toString());
        }

        // Update status bar information
        lastUpdated.setText("Last updated: " + java.time.LocalTime.now().withNano(0));
        speedLabel.setText(String.format("Simulation time: %.1fx", processor.getSpeedFactor()));
    }

    /**
     * Launches the monitoring UI
     * @param processor Order processor to monitor
     */
    public static void launch(ThreadedOrderProcessor processor) {
        SwingUtilities.invokeLater(() -> new ThreadMonitorUI(processor));
    }
}
