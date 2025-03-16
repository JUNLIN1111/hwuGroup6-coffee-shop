
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class server extends Thread {
    private static final Lock lock = new ReentrantLock(); // 共享锁，确保线程安全
   
    private String serverName;
    private boolean running = true;

    public server(String name) {
        this.serverName = name;
    }
    

    @Override
    public void run() {
        while (running) {
            try {
                Order order = orderQueue.take(); // 这里要orderqueue实现一个弹出第一个order的函数
                processOrder(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(serverName + " interrupted and shutting down.");
                break;
            }
        }
    }

    private void processOrder(Order order) {
        lock.lock(); // 锁
        try {
            System.out.println(serverName + " is processing order for " + order.getCustomerId());
            Thread.sleep(order.calculateTotalTime()); // 模拟订单处理时间
            System.out.println(serverName + " has completed " + order.getCustomerId() + "'s order.");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock(); 
        }
    }
}
