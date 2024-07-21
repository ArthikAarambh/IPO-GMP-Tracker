// Source code is decompiled from a .class file using FernFlower decompiler.
package utils;


public class TestLogger {
    public static void save_to_test_log(String logMsg) {
        ProjectFileUtilities.writeLogsToFile(logMsg);
    }
}
