package ipos;

import api.MasterAPI_Steps;
import api.utils.API_Config;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static htmlutils.TableExtractor.ext;

public class ipoGMPs {
    private static Response DELIVERY_API_REPONSE;
    @Test
    public static void getGMPInvestorgain(){
        MasterAPI_Steps.an_api_template_for_request("config/testData/rawapi/ipos-api/ipoInvestorgain.json");
        MasterAPI_Steps.publish_the_final_api_call_validation();
        Response response = API_Config.RESPONSE;
        DELIVERY_API_REPONSE = response;
        ext(DELIVERY_API_REPONSE,"InvestorGain");
    }

    @Test
    public static void getGMPipoPremium(){
        MasterAPI_Steps.an_api_template_for_request("config/testData/rawapi/ipos-api/ipoPremium.json");
        MasterAPI_Steps.publish_the_final_api_call_validation();
        Response response = API_Config.RESPONSE;
        DELIVERY_API_REPONSE = response;
        ext(DELIVERY_API_REPONSE,"IpoPremium");
    }
    @Test
    public static void getGMPipoWatch(){
        MasterAPI_Steps.an_api_template_for_request("config/testData/rawapi/ipos-api/ipoWatch.json");
        MasterAPI_Steps.publish_the_final_api_call_validation();
        Response response = API_Config.RESPONSE;
        DELIVERY_API_REPONSE = response;
        ext(DELIVERY_API_REPONSE,"IpoWatch");
    }
    public static void main(String[] args) {

        try {
            getGMPInvestorgain();
            getGMPipoPremium();
            getGMPipoWatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
