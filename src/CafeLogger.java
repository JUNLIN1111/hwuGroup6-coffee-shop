import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//用了单例模式
public class CafeLogger {

    // Use volatile to ensure visibility in a multithreaded environment
    private static volatile CafeLogger instance;
    private static final String LOG_FILE = "cafe.log";

    // Private constructor to prevent external instantiation
    private CafeLogger() {}

    // Double-checked locking for singleton implementation
    public static CafeLogger getInstance() {
        if (instance == null) {
            synchronized (CafeLogger.class) {
                if (instance == null) {
                    instance = new CafeLogger();
                }
            }
        }
        return instance;
    }

    // Method to log cafe activities
    public void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logMessage = String.format("[%s] %s%n", timestamp, message);
        
        // Write log to file
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(logMessage);
        } catch (IOException e) {
            System.err.println("Failed to write log: " + e.getMessage());
        }
    }
}

