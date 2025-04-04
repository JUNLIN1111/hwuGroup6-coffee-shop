import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

public class OrderThreadManager {
    private final List<Thread> serverThreads = new ArrayList<>();
    private final ThreadedOrderProcessor processor;
    private final List<ServerThread> serverTasks = new ArrayList<>(); 

    public OrderThreadManager(ThreadedOrderProcessor processor) {
        this.processor = processor;
    }

    public void startServers(int count) {
        for (int i = 1; i <= count; i++) {
            ServerThread task = new ServerThread("Server-" + i, processor);
            Thread thread = new Thread(task);
            thread.start();
            serverThreads.add(thread);
            serverTasks.add(task);
        }
    }

    public void stopServers() {
        for (Thread thread : serverThreads) {
            thread.interrupt();
        }
    }
    
    public boolean areAllThreadsIdle() {
        for (ServerThread task : serverTasks) {
            if (!task.isFree()) {
                return false;
            }
        }
        return true;
    }

    public void checkAndExitIfIdle() throws InterruptedException {
        while (true) {
            if (areAllThreadsIdle()) {
            	stopServers();
                System.out.println("All threads are idle. Exiting program.");
                CafeLogger.getInstance().log("All threads are idle. Exiting program.");
                System.exit(0);
            }
            Thread.sleep(1000); 
        }
    }
}
