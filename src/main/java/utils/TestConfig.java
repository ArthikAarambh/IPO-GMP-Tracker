package utils;
import exceptions.PropFileReadException;
import io.cucumber.java.Scenario;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


public class TestConfig {
    public static final String USER_HOME_PATH = System.getProperty("user.home");
    public static final String PROJECT_BASE_PATH = System.getProperty("user.dir");

    // Config files and child paths
    public static final String CONFIG_FILES_PATH = PROJECT_BASE_PATH + "/config";
    private static final String SYSTEM_PROPERTIES = CONFIG_FILES_PATH + "/system.properties";

}
