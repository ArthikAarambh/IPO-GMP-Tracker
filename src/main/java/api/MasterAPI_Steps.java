package api;

import api.utils.API_Helper;
import io.cucumber.java.en.Given;
import io.restassured.response.Response;
import org.apache.http.MethodNotSupportedException;
import org.json.JSONObject;
import utils.JsonReader;
import static api.utils.API_Config.*;
public class MasterAPI_Steps  {
    private static boolean IS_DATA_CRC_ENCRYPTED=false;
    public static void an_api_template_for_request(String apiFileName) {
        API_TEMPLATE_PATH = (apiFileName).replaceAll("\\.json", "") + ".json";
        API_TEMPLATE = JsonReader.getJsonData(API_TEMPLATE_PATH);
       API_TEMPLATE.getJSONObject("headers").put("Authorization", BEARER_TOKEN);
        updateAPIBodyVariables();
    }

    public static void updateAPIBodyVariables() {
//       REQUEST_BODY = API_TEMPLATE.getJSONObject("body");
        QUERY_PARAMS = getJsonObjectIfPresent(API_TEMPLATE, "queryParams");
    }
    private static JSONObject getJsonObjectIfPresent(JSONObject jsonObject, String keyParam) {
        if (jsonObject.has(keyParam)) {
            return jsonObject.getJSONObject(keyParam);
        }
        return new JSONObject();
    }
    public static Response publish_the_final_api_call_validation() {

        System.out.println(API_TEMPLATE+" api_template");
        try {
            API_Helper apiHelper = new API_Helper(API_TEMPLATE);
            apiHelper.readBodyFromAsString(IS_DATA_CRC_ENCRYPTED, REQUEST_BODY_STRING);

            RESPONSE = apiHelper.publish();

        } catch (MethodNotSupportedException e) {
            throw new RuntimeException(e);
        } finally {
            IS_DATA_CRC_ENCRYPTED = false;
        }
        return RESPONSE;
    }
}
