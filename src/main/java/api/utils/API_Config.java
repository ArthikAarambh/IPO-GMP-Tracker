package api.utils;

import io.restassured.response.Response;
import org.json.JSONObject;
import utils.JsonReader;
import utils.ProjectFileUtilities;
import utils.TestConfig;

import java.io.File;

public class API_Config {

    public static String BEARER_TOKEN;
    private static JSONObject ENVI_DATA;
    public static JSONObject API_TEMPLATE;
    public static String API_TEMPLATE_PATH;
    public static JSONObject REQUEST_BODY = new JSONObject();
    public static String REQUEST_BODY_STRING = null;
    public static JSONObject QUERY_PARAMS = new JSONObject();
    public static String API_CONFIG_FILES_PATH;
    public static Response RESPONSE;
    public static final String ENVI_VARIABLES_JSON_FILE_NAME = "environment_variables.json";



    public static Object getEnviData(String key) {

        if (getEnviDataObject().getJSONObject("constants").has(key)) {
            return getEnviDataObject().getJSONObject("constants").get(key);
        }

        return getEnviDataObject().opt(key + "_" + TestConfig.ENVI + "_" + getCurrentApp());
    }

    public static String getCurrentApp() {
        return TestConfig.APP.split("\\/")[0];
    }

    public static void setEnviData(String key, Object value) {
        key = key + "_" + TestConfig.ENVI + "_" + getCurrentApp();
        getEnviDataObject();
        ENVI_DATA.put(key, value);
        saveNewDataInEvniFile();
//        TestLogger.save_n_log_info("Test data saved in Environment Variables", "key: " + key + "\nValue: " + value);
    }

    public static JSONObject getEnviDataObject() {
        if (!new File(API_CONFIG_FILES_PATH + ENVI_VARIABLES_JSON_FILE_NAME).exists()) {
            ProjectFileUtilities.copyFileToTargetLocation(API_CONFIG_FILES_PATH + "backup/" + ENVI_VARIABLES_JSON_FILE_NAME, API_CONFIG_FILES_PATH + ENVI_VARIABLES_JSON_FILE_NAME);
        }
        return (ENVI_DATA = JsonReader.getJsonData(API_CONFIG_FILES_PATH + ENVI_VARIABLES_JSON_FILE_NAME));
    }

    public static void saveNewDataInEvniFile() {
        JsonReader.saveDataToJsonFile(API_CONFIG_FILES_PATH + ENVI_VARIABLES_JSON_FILE_NAME, ENVI_DATA);
        ENVI_DATA = getEnviDataObject();
    }
}
