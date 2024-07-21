package ipo;

import api.MasterAPI_Steps;
import api.utils.API_Config;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class API_Automation {
    private static Response DELIVERY_API_REPONSE;
    public static void verifyResponse(){
        MasterAPI_Steps.an_api_template_for_request("config/testData/rawapi/upstox/HistoricalData.json");
        MasterAPI_Steps.publish_the_final_api_call_without_body_validation();
        Response response = API_Config.RESPONSE;
        DELIVERY_API_REPONSE = response;
        System.out.println(DELIVERY_API_REPONSE.getBody().asString());
    }
    public static void main(String[] args) {

        try {
            verifyResponse();
        } catch (Exception e) {
            e.printStackTrace(); // Print any exception that occurs in main method
        }
            // Base URI
//        RestAssured.baseURI = "https://api.restful-api.dev";
//
//        try {
//            // Construct the request
//            Response response = RestAssured
//                    .given()
//                    .header("Content-Type", "application/json")
//                    .header("Cookie", "__cf_bm=FhFnoUghF8YAxyeKaPqu74fV_lRaXSNqXCxABiugMYs-1721487669-1.0.1.1-yFyljemzxcAVLDb2TX0oQBikeUUy2n_nuQpR8CsxG9TtObDIBGzfcXolUG_E2gsK; _cfuvid=4_FdiKqQ6I5yKWySm.xh_HGAFa8dpjmsYdn3xs5rLHs-1721487669798-0.0.1.1-604800000")
//                    .when()
//                    .get("objects");
//
//            // Print response status code
//            System.out.println("Response Code : " + response.getStatusCode());
//
//            // Print response body
//            System.out.println("Response Body : " + response.getBody().asString());
//
//        } catch (Exception e) {
//            System.out.println("An error occurred: " + e.getMessage());
//            e.printStackTrace();
//        }
        }
}
