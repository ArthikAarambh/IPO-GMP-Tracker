package api.utils;

import io.restassured.response.Response;
import org.json.JSONObject;
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

}
