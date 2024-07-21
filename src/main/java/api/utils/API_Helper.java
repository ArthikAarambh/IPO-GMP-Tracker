package api.utils;

import com.google.gson.Gson;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.MethodNotSupportedException;
import org.json.JSONException;
import org.json.JSONObject;
import utils.TestLogger;
import java.io.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class API_Helper {

    private final String REQUEST_METHOD;
    private String END_POINT;

    public TreeMap<String, String> pathParams;
    public TreeMap<String, String> queryParams;
    public TreeMap<String, String> headers;
    public TreeMap<String, String> formData; // --form | --data-urlencode
   public TreeMap<String, String> body; // --data-raw => JSON
//   public JSONObject body;
    public Object stringBody; // --data-raw => text (assuming)
    public JSONObject testData;
    private static RequestSpecification REQUEST_SPEC;
    private static Map<String, Function<String, Response>> REQUEST_METHODS;


    public API_Helper(JSONObject jsonTemplate) {
        prepareRequestSpecification();

        END_POINT = jsonTemplate.getString("end-point");
        REQUEST_METHOD = jsonTemplate.getString("request-method");
        this.testData = jsonTemplate;
        this.headers = setValue(testData.getJSONObject("headers"));
        this.pathParams = setValue(testData.optJSONObject("pathParams"));
        this.queryParams = setValue(testData.optJSONObject("queryParams"));
        this.formData = setValue(testData.optJSONObject("formData"));
        this.body = setValue(testData.optJSONObject("body"));
//        this.body=testData.optJSONObject("body");
        if (this.body == null) {
            this.body = setValue(testData.optJSONObject("data"));
        }
        System.out.println(this.body);
        this.stringBody = testData.optString("payloadString");
    }

    private void prepareRequestSpecification() {
        REQUEST_SPEC = RestAssured.given();
        REQUEST_METHODS = new TreeMap<>();
        REQUEST_METHODS.put("GET", REQUEST_SPEC::get);
        REQUEST_METHODS.put("POST", REQUEST_SPEC::post);
        REQUEST_METHODS.put("PUT", REQUEST_SPEC::put);
        REQUEST_METHODS.put("PATCH", REQUEST_SPEC::patch);
        REQUEST_METHODS.put("DELETE", REQUEST_SPEC::delete);
    }

    public Response publish() throws MethodNotSupportedException {
        Response response = null;
        RestAssured.baseURI="https://api.restful-api.dev";
        REQUEST_SPEC = REQUEST_SPEC.headers(headers)
                .baseUri(RestAssured.baseURI);
            response = sendRequest(REQUEST_METHOD);
        saveResponseToFile(response);
        return response;
    }

    private void saveResponseToFile(Response response) {
        TestLogger.save_to_test_log("\n >>>> Response: \n");
        TestLogger.save_to_test_log(response.asPrettyString());
    }

    private Response sendRequest(String requestMethod) throws MethodNotSupportedException {
        Function<String, Response> requestFunction = REQUEST_METHODS.get(requestMethod);
        if (requestFunction != null) {
            return requestFunction.apply(END_POINT);
        } else {
            throw new MethodNotSupportedException("Given method: '" + requestMethod + "' is not supported");
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public TreeMap<String, String> setValue(JSONObject values) {
        return convertJSONObjectToSortedHash(values);
    }

    public static TreeMap<String, String> convertJSONObjectToSortedHash(JSONObject values) {
        TreeMap<String, String> returnHash = null;
        try {
            returnHash = new ObjectMapper().readValue(values.toString(), TreeMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException | NullPointerException e) {
        }
        return returnHash;
    }
}
