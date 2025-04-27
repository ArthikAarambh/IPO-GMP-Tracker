package learning;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.asserts.SoftAssert;
import utils.JsonReader;

public class jsonLearning {

    private static void getJson(){
        String path="src/test/java/learning/complex.json";
        SoftAssert softAssert = new SoftAssert();
        String jsonBody ="{\"name\": \"morpheus\",\"job\": \"leader\"}";
        JSONObject stringJson = new JSONObject(jsonBody);
        softAssert.assertEquals(stringJson.getString("name"),"morph","Data mismatch");
        JSONObject jsonObject = JsonReader.getJsonData(path);
        String id = jsonObject.getJSONObject("user").getString("id");
        String firstName = jsonObject.getJSONObject("user").getJSONObject("profile").getJSONObject("name").getString("first");
        JSONArray jsonArray = jsonObject.getJSONObject("user").getJSONObject("profile").getJSONArray("contacts");
        String email = null;
        String phone = null;
        for(int i=0;i<jsonArray.length();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            String type = jsonObject1.getString("type");
            String value = jsonObject1.getString("value");
            if(type.equalsIgnoreCase("email")){
                email=value;
            } else if (type.equals("phone")) {
                phone=value;
            }

        }
        JSONArray tags = jsonObject.getJSONObject("metadata").getJSONArray("tags");
        for(int i=0;i< tags.length();i++){
            System.out.print(tags.getString(i)+" ");
        }
        //to access directly
        System.out.println("\n"+tags.getString(0));

        System.out.println(id+" "+firstName+" "+email+" "+phone);
        softAssert.assertEquals(id,"12","data mismatch");
//        System.out.println(jsonObject.toString(4));
        softAssert.assertAll();
    }


    public static void main(String[] args) {
        getJson();
    }
}
