// Source code is decompiled from a .class file using FernFlower decompiler.
package utils;

import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.*;


public class TestLogger {
    private final String message;
    static Logger logger;
    public static boolean USE_CUCUMBER_REPORTING = false;

    public static Logger logger(String className) {
        if (logger == null) {
            logger = Logger.getLogger(className);
            configureLogger();
        }
        return logger;
    }

    public static void info(String msg) {
//        logger.info(msg);
        System.out.println(msg);
    }

    public TestLogger(String message) {
        this.message = message;
    }

    @Step("Message: {0}")
    public static void add_text_msg(String msg) {
        TestConfig.getSCENARIO().log(msg);
    }

    public static void scenario_text_info(String msgTitle, String msg) {
        if (USE_CUCUMBER_REPORTING) {
            TestConfig.getSCENARIO().attach(msg, "text/plain", msgTitle);
        } else {
            Allure.addAttachment(msgTitle, "text/plain", msg);
        }
    }

    public static void scenario_json_info(String msgTitle, String jsonData) {
        if (USE_CUCUMBER_REPORTING) {
            TestConfig.getSCENARIO().attach(jsonData, "application/json", msgTitle);
        } else {
            Allure.addAttachment(msgTitle, "application/json", jsonData);
        }
    }

    public static void scenario_image_info(String msgTitle, byte[] imageBase64Data) {
        if (USE_CUCUMBER_REPORTING) {
            TestConfig.getSCENARIO().attach(imageBase64Data, "image/png", msgTitle);
        }
    }

    public static void save_n_log_info(String msgTitle, String msg) {
        info(msgTitle + "\n" + msg);
        scenario_text_info(msgTitle, msg);
    }

    public static void print_scenario_info(String LOG_FILE_PATH, Scenario scenario) {
        String[] parts = scenario.getUri().getPath().split("/");
        String FEATURE = parts[parts.length - 1].replace(".feature", "");
        String SCENARIO = scenario.getName();

        print_text_to_log_file(LOG_FILE_PATH, FEATURE, SCENARIO);
    }

    public static void print_scenario_info(String LOG_FILE_PATH, String SCENARIO) {
        print_text_to_log_file(LOG_FILE_PATH, "--", SCENARIO);
    }

    private static void print_text_to_log_file(String LOG_FILE_PATH, String FEATURE, String SCENARIO) {
        String TIMESTAMP = getTimestamp();

        TestConfig.TEST_LOG_FILE = LOG_FILE_PATH + "/" + TIMESTAMP + ".txt";
        save_to_test_log(TestConfig.TEST_LOG_FILE, ">>>>> " + TIMESTAMP + " <<<<<");
        save_to_test_log(TestConfig.TEST_LOG_FILE, "\nFeature: " + FEATURE);
        save_to_test_log(TestConfig.TEST_LOG_FILE, "Scenario: " + SCENARIO);
    }

    private static String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd' 'HH'h_'mm'm_'ss's'_SSS");
        Date now = new Date();
        return dateFormat.format(now);
    }

    public static void save_to_test_log(String logFile, String logMsg) {
        ProjectFileUtilities.writeLogsToFile(logFile, logMsg);
    }

    public String getFormattedMessage() {
        return "Custom Format: " + this.message;
    }

    public String getFormat() {
        return this.message;
    }

    public Object[] getParameters() {
        return null;
    }

    public Throwable getThrowable() {
        return null;
    }


    private static void configureLogger() {
        // Create console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL); // Set log level to output all messages

        // Set formatter to the console handler
        consoleHandler.setFormatter(new CustomFormatter());

        // Add console handler to the logger
        logger.addHandler(consoleHandler);

        // Set log level for the logger
        logger.setLevel(Level.ALL); // Set log level to output all messages

//        logger.severe("severe Hello world !");
//        logger.warning("warning Hello world !");
//        logger.info("info Hello world !");
//        logger.config("config Hello world !");
//        logger.fine("fine Hello world !");
//        logger.finer("finer Hello world !");
//        logger.finest("finest Hello world !");

//        System.out.println();
    }

}

// Custom formatter class
class CustomFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord record) {
        // Get current date and time
        LocalDateTime dateTime = LocalDateTime.now();

        // Format date and time
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));

        // Format log message
//        String formattedMessage = String.format("%s %s [%s] %s",
//                formattedDateTime,
//                record.getLoggerName(),
//                record.getLevel().getName(),
//                record.getMessage());

        String formattedMessage = String.format("%s ",
//                formattedDateTime,
                record.getLoggerName()
//                record.getLevel().getName(),
//                record.getMessage()
        );

        return formattedMessage;// + System.getProperty("line.separator");
    }
}
