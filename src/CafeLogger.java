import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//鐢ㄤ簡鍗曚緥妯″紡
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
    public boolean clearLog() {
        try (FileWriter writer = new FileWriter(LOG_FILE)) {
            // 打开文件并立即关闭，会清空文件内容
            return true;
        } catch (IOException e) {
            System.err.println("Failed to clear log: " + e.getMessage());
            return false;
        }
    }
}

