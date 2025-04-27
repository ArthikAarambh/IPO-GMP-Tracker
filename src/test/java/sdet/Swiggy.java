package sdet;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.*;
import java.util.HashMap;

public class Swiggy {
    private static String END_POINT;
    private static Map<String,String>headers = new HashMap<>();


    private static RequestSpecification prepareRequestSpec(String endpoint,String body){
        String baseUrl = "https://reqres.in/";
        RequestSpecification requestSpec = RestAssured.given().headers(headers).baseUri(baseUrl).basePath(endpoint);
        if(body!=null && !body.isEmpty()){
            requestSpec.body(body);
        }

        return requestSpec;
    }
    private static Response sendRequest(String endpoint, String body){
        RequestSpecification request = prepareRequestSpec(endpoint,body);
        Response response=request.get();
        Response response1 = request.post();
        return response;
    }

    private static void printResponse(Response response){
        if(response.getBody()!=null){
            System.out.println(response.getBody().asPrettyString());
        }else{
            System.out.println("No body returned for the request");
        }
        System.out.println("status: "+response.getStatusCode());
    }




    public static void main(String Args[]){
        END_POINT="/api/users?page=2";
        headers.put("","");
        String body="";
        Response response = sendRequest(END_POINT,body);
        printResponse(response);

    }

}