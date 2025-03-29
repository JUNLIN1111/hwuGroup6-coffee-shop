import java.util.ArrayList;
import java.util.List;

public class OrderThreadManager {
    private final List<ServerThread> serverThreads = new ArrayList<>();
    private final ThreadedOrderProcessor processor;

    public OrderThreadManager(ThreadedOrderProcessor processor) {
        this.processor = processor;
    }

    public void startServers(int count) {
        for (int i = 1; i <= count; i++) {
            ServerThread thread = new ServerThread("Server-" + i, processor);
            serverThreads.add(thread);
            thread.start();
        }
    }

    public void stopServers() {
        for (ServerThread thread : serverThreads) {
            thread.interrupt();
        }
    }
}
