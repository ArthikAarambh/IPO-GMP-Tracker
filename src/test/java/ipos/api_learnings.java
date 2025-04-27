package ipos;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class api_learnings {

    private static RequestSpecification REQUEST_SPEC;
    private static Map<String, Function<String, Response>> REQUEST_METHODS;
    public static HashMap<String, String> headers=new HashMap<>();
//    public HashMap<String, String> body;
    private static String END_POINT;
//    private static String REQUEST_METHOD;h

    private static RequestSpecification prepareRequestSpecification(String endpoint,String body) {
        RestAssured.baseURI="https://reqres.in/";
        System.out.println(headers.values());
//        REQUEST_SPEC=RestAssured.given().body();
        REQUEST_SPEC = RestAssured.given().headers(headers).baseUri(RestAssured.baseURI).basePath(endpoint);
//        REQUEST_SPEC = RestAssured.given().baseUri("").basePath("").auth().oauth2("").headers("m","n").pathParams("s","k").queryParam("s").body("xyz");
        if (body != null && !body.isEmpty()) {
            REQUEST_SPEC.body(body);
        }

        return REQUEST_SPEC;

    }
    private static Response sendRequest(String httpMethod,String endpoint,String body) {
        RequestSpecification request = prepareRequestSpecification(endpoint,body);
        Response response = null;

        switch (httpMethod.toUpperCase()) {
            case "GET":
                response = request.get();
                break;
            case "POST":
                response = request.post();
                break;
            case "PUT":
                response = request.put();
                break;
            case "DELETE":
                response = request.delete();
                break;
            case "PATCH":
                response = request.patch();
                break;
            case "HEAD":
                response = request.head();
                break;
            default:
                throw new UnsupportedOperationException("HTTP method " + httpMethod + " is not supported");
        }

        return response;
    }
    // Method to print the response
    private static void printResponse(String method, Response response) {
        System.out.println(method + " Response: ");
        if (response.getBody() != null) {
            System.out.println(response.getBody().asPrettyString());
        } else {
            System.out.println("No body returned for " + method + " request.");
        }
        System.out.println("Status Code: " + response.getStatusCode());
    }
//    public HashMap<String, Object> setValue(JSONObject jsonObject) {
//        HashMap<String, Object> map = new HashMap<>();
//        Iterator<String> keys = jsonObject.keys();
//
//        while (keys.hasNext()) {
//            String key = keys.next();
//            Object value = jsonObject.get(key);
//            map.put(key, value);
//        }
//        return map;
//    }

    public static void main(String[] args) {
        END_POINT = "/api/users?page=2";
        headers.put("Content-Type", "application/json");
        headers.put("x-api-key","reqres-free-v1");
        String body="{\"name\": \"chotu\",\"job\": \"gaming\"}";
        Response response2 = sendRequest("post",END_POINT , body);
        printResponse("Post", response2);
        Response response = sendRequest("GET",END_POINT , null);
        printResponse("GET", response);
    }
}
