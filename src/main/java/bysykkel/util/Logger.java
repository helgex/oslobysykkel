package bysykkel.util;

public class Logger {
    public static void LOG_INFO(String message) {
        System.out.println("[INFO]" + message);
    }

    public static void LOG_WARNING(String message) {
        System.out.println("[WARNING]" + message);
    }

    public static void LOG_ERROR(String message, Exception e) {
        System.out.println("[ERROR]" + message);
        e.printStackTrace();
    }
}
