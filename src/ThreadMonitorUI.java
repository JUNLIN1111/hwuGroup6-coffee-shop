import javax.swing.*;
import java.awt.*;
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
		frame.setSize(600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridBagLayout());

//pendingArea = new JTextArea();
//������һ�еȴ�����
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH; // ������������Ԫ��
		gbc.insets = new Insets(5, 5, 5, 5); // ��Ӽ��
		wait_queue = new JTextArea("Wating queue");
		wait_queue.setText("None");
		wait_queue.setName("Wating queue");
		gbc.gridx = 0; // ������
		gbc.gridy = 0; // ������
		gbc.gridwidth = 4; // �� 4 ��
		gbc.weightx = 1.0;
		gbc.weighty = 0.6;
		frame.add(new JScrollPane(wait_queue), gbc);

//activeArea = new JTextArea();
		info_sever = new JTextArea[4];
		gbc.gridy = 1; // �ڶ���
		gbc.gridwidth = 1; // ÿ��ռ 1 ��
		gbc.weighty = 0.4;
		for (int i = 0; i < 4; i++) {
			info_sever[i] = new JTextArea();
			info_sever[i].setName("server" + (i + 1)); // ��������
			info_sever[i].setText("free"); // ���ó�ʼ�ı�
			gbc.gridx = i; // ÿ���ı�����ڲ�ͬ����
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
		List<Order> waitqueue;

		try {
			waitqueue = OrderList.getInstance().getOrderList();
		} catch (InvalidOrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Order> active = OrderList.getInstance().getActiveOrders();


		StringBuilder pendingText = new StringBuilder("Waiting Queue:\n");
		try {
			for (Order order : OrderList.getInstance().getOrderList()) {
				pendingText.append(order.getOrderId()).append(" - ").append(order.getCustomerId()).append("\n");
			}
		} catch (InvalidOrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		wait_queue.setText(pendingText.toString());
		for (int i = 1; i <= 4; i++) {
	        StringBuilder activeText = new StringBuilder("Server" + i + ":\n");
	        
	        if(active == null) {
		        System.out.println("active is null");
	        }
	        
	        Order activeOrder = active.get("Server-" + i);
	        
	        System.out.println(activeOrder);
	        if (activeOrder != null) {
	            activeText.append("Processing: ").append(activeOrder.getOrderId()).append(" (").append(activeOrder.getCustomerId()).append(")\n");
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