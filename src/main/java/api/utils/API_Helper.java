package api.utils;

import com.google.gson.Gson;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.MethodNotSupportedException;
import org.json.JSONException;
import org.json.JSONObject;
import utils.TestLogger;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.Function;

import static api.utils.API_Config.*;
import static utils.TestLogger.*;

public class API_Helper {

    private final String REQUEST_METHOD;
    private String END_POINT;

    public TreeMap<String, String> pathParams;
    public TreeMap<String, String> queryParams;
    public TreeMap<String, String> headers;
    public TreeMap<String, String> formData; // --form | --data-urlencode
    public TreeMap<String, String> body; // --data-raw => JSON
    public boolean readBodyFromString;
    public String bodyString; // --data-raw => JSON
    public Object stringBody; // --data-raw => text (assuming)
    public JSONObject testData;
    private static RequestSpecification REQUEST_SPEC;
    private static Map<String, Function<String, Response>> REQUEST_METHODS;
    private static String LOG_FILE;


    public API_Helper(JSONObject jsonTemplate, String logFile) {
        LOG_FILE = logFile;

        prepareRequestSpecification();

        END_POINT = jsonTemplate.getString("end-point");
        REQUEST_METHOD = jsonTemplate.getString("request-method");

        this.testData = jsonTemplate;
        this.headers = setValue(testData.getJSONObject("headers"));
//        this.pathParams = setValue(testData.optJSONObject("pathParams"));
//        this.queryParams = setValue(testData.optJSONObject("queryParams"));
//        this.formData = setValue(testData.optJSONObject("formData"));
        this.body = setValue(testData.optJSONObject("body"));
        if (this.body == null) {
            this.body = setValue(testData.optJSONObject("data"));
        }
//        this.stringBody = testData.optString("payloadString");
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

//        if (!testData.optBoolean("secure-data")) {
//            scenario_json_info("Final Request Body:", REQUEST_BODY.toString(4));
//        }

        if (readBodyFromString) {
            REQUEST_SPEC = REQUEST_SPEC.body(bodyString);
        } else if (body != null && !body.isEmpty()) {
            REQUEST_SPEC = REQUEST_SPEC.body(body);
        }

//        try {
////            save_n_log_info("Request curl: \n", logCurl());
////            TestLogger.save_to_test_log(LOG_FILE, "\n >>>> Request curl: \n\n" + logCurl());
//
//            FileOutputStream fos = new FileOutputStream(LOG_FILE, true);
//            // Create a PrintStream to write the log to the file
//            PrintStream logPrintStream = new PrintStream(fos);
//
////            REQUEST_SPEC = REQUEST_SPEC.filter(new RequestLoggingFilter(LogDetail.ALL, logPrintStream));
//
//            if (testData.optBoolean("secure-data")) {
//                REQUEST_SPEC = REQUEST_SPEC.when()
//                        .log().method()
//                        .log().headers()
//                        .log().cookies()
//                        .log().uri()
//                        .log().parameters();
//
//            } else {
//                REQUEST_SPEC = REQUEST_SPEC.when()
//                        .log()
//                        .all();
//            }
//
            response = sendRequest(REQUEST_METHOD);
//        } catch (MalformedURLException e) {
//            System.err.println("Error in Response: \n");
//            response.then().log().all();
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (response != null) {
//                info("======== Response Output ========");
//                response.then().log().all();
//                saveResponseToFile(response);
//                scenario_json_info("Response body:", response.asPrettyString());
//            }
//        }
        return response;
    }

    private void saveResponseToFile(Response response) {
        TestLogger.save_to_test_log(LOG_FILE, "\n >>>> Response: \n");
        TestLogger.save_to_test_log(LOG_FILE, response.asPrettyString());
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

//    public String logCurl() throws MalformedURLException {
//        StringJoiner curl = new StringJoiner(" \\\n");
//
//        String url = adjustFwdSlashesInURL();
//        url = replacePathParams(url, pathParams);
//        curl.add("curl --request " + REQUEST_METHOD + " '" + url + attachQueryParams(queryParams) + "'");
//
//        curl = constructCurlBodyFor("--header", ": ", headers, curl);
//
//        if (formData != null && formData.size() > 0) {
//            String option = curl.toString().contains("application/x-www-form-urlencoded") ? "--data-urlencode" : "--form";
//            return constructCurlBodyFor(option, "=", formData, curl).toString();
//        }
//
//        if (!testData.optBoolean("secure-data")) {
//            if (readBodyFromString) {
//                curl.add("--data-raw '" + (bodyString) + "'");
//            } else if (body != null && body.size() > 0) {
//                curl.add("--data-raw '" + (new JSONObject(body).toString()) + "'");
//            } else if (stringBody != null) {
//                curl.add("--data-raw '" + stringBody + "'");
//            }
//        }
//
//        return curl.toString();
//    }

    private String adjustFwdSlashesInURL() throws MalformedURLException {
        if (!RestAssured.baseURI.endsWith("/") && !END_POINT.startsWith("/")) {
            return (RestAssured.baseURI + "/" + END_POINT);
        } else if (RestAssured.baseURI.endsWith("/") && END_POINT.startsWith("/")) {
            throw new MalformedURLException("RestAssured.baseURI and Endpoint has '/' unable to resolve");
        }
        return RestAssured.baseURI + END_POINT;
    }

    private String attachQueryParams(TreeMap<String, String> queryParams) {
        String queryPath = "";
        if (queryParams != null && queryParams.size() > 0) {
            StringJoiner combinedParams = new StringJoiner("&");
            for (Object key : queryParams.keySet()) {
                combinedParams.add(key + "=" + String.valueOf(queryParams.get(key)));
                REQUEST_SPEC = REQUEST_SPEC.queryParam(String.valueOf(key), queryParams.get(key));
            }
            queryPath = "?" + combinedParams;
        }
        return queryPath;
    }

    private String replacePathParams(String url, TreeMap<String, String> pathParams) {
        if (pathParams != null && pathParams.size() > 0) {
            for (Object key : pathParams.keySet()) {
                url = url.replace("{" + key + "}", String.valueOf(pathParams.get(key)));
                REQUEST_SPEC = REQUEST_SPEC.pathParam(key.toString(), pathParams.get(key));
            }
        }
        return url;
    }

    private StringJoiner constructCurlBodyFor(String option, String delimiter, TreeMap dataSet, StringJoiner curl) {
        for (Object key : dataSet.keySet()) {
            curl.add(option + " '" + key + delimiter + dataSet.get(key) + "'");
        }
        return curl;
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


    public void readBodyFromAsString(boolean readBodyFromString, String bodyString) {
        this.readBodyFromString = readBodyFromString;
        this.bodyString = bodyString;

    }
}
