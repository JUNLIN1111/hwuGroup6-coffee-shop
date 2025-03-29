import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ThreadMonitorUI extends JFrame {
    private final ThreadedOrderProcessor processor;
    private final JTextArea pendingArea;
    private final JTextArea activeArea;
    private final JLabel lastUpdated;

    public ThreadMonitorUI(ThreadedOrderProcessor processor) {
        this.processor = processor;

        setTitle("Order Processing Monitor");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        pendingArea = new JTextArea();
        activeArea = new JTextArea();
        lastUpdated = new JLabel();

        pendingArea.setEditable(false);
        activeArea.setEditable(false);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel center = new JPanel(new GridLayout(1, 2));

        center.add(new JScrollPane(pendingArea));
        center.add(new JScrollPane(activeArea));

        panel.add(center, BorderLayout.CENTER);
        panel.add(lastUpdated, BorderLayout.SOUTH);

        add(panel);

        startRefreshing();
    }

    private void startRefreshing() {
        Timer timer = new Timer(1000, e -> refreshDisplay());
        timer.start();
    }

    private void refreshDisplay() {
        List<Order> pending = processor.getPendingOrders();
        Map<String, Order> active = processor.getActiveOrders();

        StringBuilder pendingText = new StringBuilder("Pending Orders:\n");
        for (Order order : pending) {
            pendingText.append(order.getOrderId()).append(" - ")
                    .append(order.getCustomerId()).append("\n");
        }

        StringBuilder activeText = new StringBuilder("Active Orders:\n");
        for (Map.Entry<String, Order> entry : active.entrySet()) {
            activeText.append(entry.getKey()).append(" -> ")
                    .append(entry.getValue().getOrderId()).append(" (")
                    .append(entry.getValue().getCustomerId()).append(")\n");
        }

        pendingArea.setText(pendingText.toString());
        activeArea.setText(activeText.toString());
        lastUpdated.setText("Last updated: " + java.time.LocalTime.now().withNano(0));
    }

    public static void launch(ThreadedOrderProcessor processor) {
        SwingUtilities.invokeLater(() -> new ThreadMonitorUI(processor).setVisible(true));
    }
}
