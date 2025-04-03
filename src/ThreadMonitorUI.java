import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Map;

public class ThreadMonitorUI extends JFrame {
    private final ThreadedOrderProcessor processor;
    private final JTextArea wait_queue;
    private JTextArea[] info_sever = new JTextArea[4];
    private final JLabel lastUpdated;
    private final JLabel speedLabel; // New label for speed factor

    public ThreadMonitorUI(ThreadedOrderProcessor processor) {
        this.processor = processor;

        // Set properties for 'this' JFrame (instead of creating 'frame')
        setTitle("Information");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);

        // Initialize waiting queue
        wait_queue = new JTextArea("Waiting queue");
        wait_queue.setText("None");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        add(new JScrollPane(wait_queue), gbc);  // Add to 'this' JFrame

        // Initialize servers
        info_sever = new JTextArea[4];
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.5;
        for (int i = 0; i < 4; i++) {
            info_sever[i] = new JTextArea();
            info_sever[i].setText("Free");
            gbc.gridx = i;
            add(new JScrollPane(info_sever[i]), gbc);  // Add to 'this' JFrame
        }

        // Add last updated label
        lastUpdated = new JLabel();
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(lastUpdated, gbc);

        // Speed factor label
        speedLabel = new JLabel("Simulation Speed: 1.0x");
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        add(speedLabel, gbc);  // Add to 'this' JFrame

        // Start refreshing UI
        startRefreshing();
        setVisible(true);
    }

    private void startRefreshing() {
        Timer timer = new Timer(1000, e -> {
            try {
                refreshDisplay();
            } catch (InvalidOrderException ex) {
                throw new RuntimeException(ex);
            }
        });
        timer.start();
    }

    private void refreshDisplay() throws InvalidOrderException {
        // Update waiting queue display
        StringBuilder pendingText = new StringBuilder("Waiting Queue:\n");
        try {
            List<Order> waitqueue = OrderList.getInstance().getOrderList();
            for (Order order : waitqueue) {
                pendingText.append(order.getOrderId()).append(" - ").append(order.getCustomerId()).append("\n");
            }
        } catch (InvalidOrderException e) {
            e.printStackTrace();
        }
        wait_queue.setText(pendingText.toString());

        // Update active orders display
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
                activeText.append("Processing ").append(activeOrder.getCustomerId()).append("'s order\n");
                List<Item> orderItems = activeOrder.getItemList();
                for (Item item : orderItems) {
                    activeText.append(item.getDescription()).append(" \n");
                }

                activeText.append("\n Original total cost: ").append(activeOrder.calculateOriginalCost()).append(" \n");
                activeText.append("Total cost: ").append(activeOrder.calculateTotalCost()).append(" \n");

            } else {
                activeText.append("Free\n");
            }

            info_sever[i - 1].setText(activeText.toString());
        }

        lastUpdated.setText("Last updated: " + java.time.LocalTime.now().withNano(0));
        speedLabel.setText(String.format("Simulation Speed: %.1fx", processor.getSpeedFactor()));
    }

    public static void launch(ThreadedOrderProcessor processor) {
        SwingUtilities.invokeLater(() -> new ThreadMonitorUI(processor).setVisible(true));
    }
}
