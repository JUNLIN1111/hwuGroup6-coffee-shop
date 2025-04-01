import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.sql.Time;
import java.util.List;
import java.util.Map;

public class ThreadMonitorUI extends JFrame {
	private final ThreadedOrderProcessor processor;
	private final JTextArea wait_queue;
	JTextArea[] info_sever = new JTextArea[4];
//private final JTextArea pendingArea;
//private final JTextArea activeArea;
	private final JLabel lastUpdated;

	public ThreadMonitorUI(ThreadedOrderProcessor processor) {
		this.processor = processor;
		JFrame frame = new JFrame("Information");
		frame.setSize(800, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridBagLayout());

//pendingArea = new JTextArea();
//锟斤拷锟斤拷锟斤拷一锟叫等达拷锟斤拷锟斤拷
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH; // 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷元锟斤拷
		gbc.insets = new Insets(2, 2, 2, 2); // 锟斤拷蛹锟斤拷
		wait_queue = new JTextArea("Wating queue");
		wait_queue.setText("None");
		wait_queue.setName("Wating queue");
		gbc.gridx = 0; // 锟斤拷锟斤拷锟斤拷
		gbc.gridy = 0; // 锟斤拷锟斤拷锟斤拷
		gbc.gridwidth = 4; // 锟斤拷 4 锟斤拷
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;
		frame.add(new JScrollPane(wait_queue), gbc);

//activeArea = new JTextArea();
		info_sever = new JTextArea[4];
		gbc.gridy = 1; // 锟节讹拷锟斤拷
		gbc.gridwidth = 1; // 每锟斤拷占 1 锟斤拷
		gbc.weighty = 0.5;
		for (int i = 0; i < 4; i++) {
			info_sever[i] = new JTextArea();
			info_sever[i].setName("server" + (i + 1)); // 锟斤拷锟斤拷锟斤拷锟斤拷
			info_sever[i].setText("free"); // 锟斤拷锟矫筹拷始锟侥憋拷
			gbc.gridx = i; // 每锟斤拷锟侥憋拷锟斤拷锟斤拷诓锟酵拷锟斤拷锟�
			frame.add(new JScrollPane(info_sever[i]), gbc);
		}
		lastUpdated = new JLabel();

		startRefreshing();
		frame.setVisible(true);
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
	        
	        info_sever[i-1].setText(activeText.toString());
	    }

	    lastUpdated.setText("Last updated: " + java.time.LocalTime.now().withNano(0));
	}

	public static void launch(ThreadedOrderProcessor processor) {
		SwingUtilities.invokeLater(() -> new ThreadMonitorUI(processor).setVisible(true));
	}
	
}