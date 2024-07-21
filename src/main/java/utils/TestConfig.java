package utils;
import exceptions.PropFileReadException;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.StringJoiner;

public class TestConfig extends TestLogger {
    public static final String USER_HOME_PATH = System.getProperty("user.home");
    public static final String PROJECT_BASE_PATH = System.getProperty("user.dir");

    // Config files and child paths
    public static final String CONFIG_FILES_PATH = PROJECT_BASE_PATH + "/config";
    public static final String TEST_DATA_PATH = CONFIG_FILES_PATH + "/testData";
    public static final String TEMP_TEST_DATA_PATH = TEST_DATA_PATH + "/temp";
    private static final String SYSTEM_PROPERTIES = CONFIG_FILES_PATH + "/system.properties";

    public static final String APP = getSystemPropertyValue("app");
    public static final int MAX_DAYS_TO_STORE_TEMP_TEST_DATA = Integer.parseInt(getSystemPropertyValue("MAX_DAYS_TO_STORE_TEMP_TEST_DATA"));
    public static String PLATFORM = getSystemPropertyValue("platform");
    public static final String DEVICE = getSystemPropertyValue("device");
    public static final String ENVI = getSystemPropertyValue("envi");


    // Test Reports and child paths
    public static final String ALLURE_RESULTS_PATH = PROJECT_BASE_PATH + "/allure-results";
    public static final String TEST_REPORTS_PATH = PROJECT_BASE_PATH + "/test-reports";
    public static String CSV_REPORTS_PATH = TEST_REPORTS_PATH + "/csv-reports";
    public static String RESULT_CSV_FILE = CSV_REPORTS_PATH + "/results.csv";
    public static final String ALLURE_REPORTS_PATH = TEST_REPORTS_PATH + "/allure";
    public static final String TEST_SCREENSHOTS_PATH = TestConfig.ALLURE_REPORTS_PATH + "/screenshots";
    public static final String LOG_FILES_PATH = TEST_REPORTS_PATH + "/logFiles/";
    public static String MASTER_LOG_FILE = LOG_FILES_PATH + "/" + "master-logs_1.txt";
    public static String TEST_LOG_FILE = MASTER_LOG_FILE;
    public static final String CUCUMBER_REPORTS = TEST_REPORTS_PATH + "/cucumber/";
    public static final String CUCUMBER_TIMELINE_REPORTS = CUCUMBER_REPORTS + "/timeline-report.html/";
    public static final String DEFAULT_LOG_FILE = LOG_FILES_PATH + ".log";
    private static Scenario SCENARIO;
    private static final LocalDate CURRENT_DATE = LocalDate.now();
    private static final LocalTime CURRENT_TIME = LocalTime.now();
    private static final String DATE_OF_RUN = getTodayDate();
    private static final String TIME_OF_RUN = getTodayTime();
    public static boolean RESET_LOG_PATH = false;// = resetLogsFolder();
    public static final String BACKUP_FOLDER_PATH = createTestRunBackupFolder();
    public static final String PLATFORM_TOOLS_PATH = getPlatFormToolsPath();
    public static String USER = getSystemPropertyValue("user");

    private static String getPlatFormToolsPath() {
        String platformToolsPath = getSystemPropertyValue("platform-tools-path");
        return (platformToolsPath != null) ? platformToolsPath : USER_HOME_PATH + "/Library/Android/sdk/platform-tools/";
    }

    public static synchronized boolean resetLogsFolder() {
        boolean flag = false;
        if (!RESET_LOG_PATH) {
            try {

                // omit this - ALLURE_RESULTS_PATH from reset folder list to get trend on results
                for (String filePath : new String[]{TEST_REPORTS_PATH, ALLURE_REPORTS_PATH, ALLURE_RESULTS_PATH, TEST_SCREENSHOTS_PATH}) {

                    if (checkFileIgnoreCriteria(filePath)) {
                        continue;
                    }

                    createProjectDirectory(filePath);
                    for (File source : new File(filePath).listFiles()) {
                        boolean isDirectory = source.isDirectory();
                        if ((source.isFile() || isDirectory) && !source.getName().equals("backup")) {
                            if (checkFileIgnoreCriteria(source.getAbsolutePath())) {
                                continue;
                            }
                            FileUtils.forceDelete(source);
                            if (isDirectory) {
                                createProjectDirectory(source.getPath());
                            }
                        }
                    }
                    info("Old report files deletion completed...");
                    flag = true;
                }
                setupAllureEnviProperties("System ENVI OS", System.getProperty("os.name"));
                setupAllureEnviProperties("OS Version", System.getProperty("os.version"));
                setupAllureEnviProperties("APP", APP);
                setupAllureEnviProperties("APP Environment", ENVI);
                setupAllureEnviProperties("Platform", PLATFORM + "");
                setupAllureEnviProperties("Device", DEVICE + "");
                setupAllureEnviProperties("USER Info", USER + "");
                setupAllureEnviProperties("Execution Tags", getSystemPropertyValue("cucumber.filter.tags") + "");
                ProjectFileUtilities.sweepFolderFilesUntochedInLastNDays(MAX_DAYS_TO_STORE_TEMP_TEST_DATA, TEMP_TEST_DATA_PATH);
            } catch (IOException e) {
                // do nothing
                info("Old report files deletion failed. Error: " + e.getMessage());
            }
        }
        return flag;
    }

    private static boolean checkFileIgnoreCriteria(String filePath) {
        for (String pathToIgnore : new String[]{CSV_REPORTS_PATH, TEST_REPORTS_PATH + "/logFiles/", ALLURE_RESULTS_PATH}) {
            if (Paths.get(filePath).equals(Paths.get(pathToIgnore))) {
                return getSystemPropertyValue("ALLURE_SAVE_RESULTS_HISTORY").equalsIgnoreCase("ON");
            }
        }
        return false;
    }

    public static void setupAllureEnviProperties(String key, String value) {
        String filePath = ALLURE_RESULTS_PATH + "/environment.properties";
        createProjectDirectory(ALLURE_RESULTS_PATH);
        ProjectFileUtilities.createFileIfNotExists(filePath);

        try {
            // Append the string to the file
            Files.write(Path.of(filePath), ((key.replaceAll("\\s", "_") + "=" + value) + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public TestConfig(String message) {
        super(message);
    }


    public static String getSystemPropertyValue(String propKey) {
        return System.getProperty(propKey) == null ? readPropertyFile(SYSTEM_PROPERTIES).getProperty(propKey) : System.getProperty(propKey).replace(".json", "").replace(".csv", "");
    }

    private static Properties readPropertyFile(String filePath) {
        Properties props = new Properties();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            props.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new PropFileReadException(e);
        }
        return props;
    }

    public static String getTodayDate() {
        return CURRENT_DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private static String getTodayTime() {
        return CURRENT_TIME.format(DateTimeFormatter.ofPattern("HH'H'")) + "/" + CURRENT_TIME.format(DateTimeFormatter.ofPattern("HH'H'_mm'M'_ss'S'"));
    }

    private static String createTestRunBackupFolder() {

        StringJoiner directoryPath = new StringJoiner("/");
        // Define the directory path you want to create
        directoryPath.add(TEST_REPORTS_PATH);
        directoryPath.add("backup");
        directoryPath.add(DATE_OF_RUN);
        directoryPath.add(APP);
        directoryPath.add(TIME_OF_RUN.toUpperCase());

        createProjectDirectory(directoryPath.toString());
        createProjectDirectory(CUCUMBER_REPORTS);
        createProjectDirectory(CUCUMBER_TIMELINE_REPORTS);

        return directoryPath.toString();
    }

    public static void createProjectDirectory(String directoryPath) {
        try {
            File directory = new File(directoryPath);

            if (directory.exists()) {
                return;
            }

            Files.createDirectories(Paths.get(directoryPath));
            info("Created Folder: " + directoryPath);
        } catch (IOException e) {
            info("Failed to create directory: " + e.getMessage());
        }
    }

    public static Scenario getSCENARIO() {
        return SCENARIO;
    }

    public static void setSCENARIO(Scenario scenario) {
        SCENARIO = scenario;
    }

    public static void sleep(int timeInMilliSeconds) {
        try {
            Thread.sleep(timeInMilliSeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateTestResultReport() {

        String isAllureEnabled = TestConfig.getSystemPropertyValue("ALLURE_REPORTS");
        if (isAllureEnabled.equalsIgnoreCase("OFF")) {
            return;
        }

        try {

            String OS_NAME = System.getProperty("os.name");
            if (OS_NAME.toLowerCase().contains("windows")) { // mac, linux, redhat
                OS_NAME = "allure_reports.bat";
            } else {
                OS_NAME = "./allure_reports.sh";
            }

            ProcessBuilder processBuilder = new ProcessBuilder(OS_NAME);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                info("Allure reports created...");
            } else {
                throw new RuntimeException("Allure report generation failed. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getAppName() {
        return TestConfig.APP.split("\\W")[0];
    }

    public static String getAppPlatformConfig() {
        return TestConfig.APP.split("\\W")[1];
    }
}
