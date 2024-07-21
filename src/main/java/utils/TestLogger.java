// Source code is decompiled from a .class file using FernFlower decompiler.
package utils;


public class TestLogger {
    private final String message;
    public static void info(String msg) {
        System.out.println(msg);
    }

    public TestLogger(String message) {
        this.message = message;
    }
    public static void save_to_test_log(String logMsg) {
        ProjectFileUtilities.writeLogsToFile(logMsg);
    }
}
