package Steps;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.CustomerPOJO;
import org.awaitility.Awaitility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Activation {


    static String errorMessage, customerId, status, addParam, pd;
    static List<Long> result;
    static Long phone;



    public static String getLogin(String login, String password){

        return  RestAssured.given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .post("/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");
    }

    public static List<Long> getEmptyPhone(String token){

        Awaitility.await()
                .atMost(1, TimeUnit.MINUTES)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .until(() -> {
                    Response response = RestAssured.given()
                            .header("authToken", token)
                            .get("/simcards/getEmptyPhone")
                            .then()
                            .extract().response();
                    if (response.getStatusCode() == 200){
                        List<Long> phones = response.jsonPath().getList("phones.phone", Long.class);
                        if (!phones.isEmpty()){
                            result = phones;
                            return true;
                        }
                    } else { errorMessage = response.jsonPath().getString("errorMessage");
                        System.out.println(errorMessage);
                    }
                    return false;
                });
        return result;
    }

    public static String postCustomer(String token, List<Long> phones, String addParam) {

        Awaitility.await()
                .atMost(1, TimeUnit.MINUTES)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .until(() -> {
                    for (Long phone : phones) {
                        Response response = RestAssured.given()
                                .contentType("application/json")
                                .header("authToken", token)
                                .body("{" +
                                        "    \"name\":\"арсений\", " +
                                        "    \"phone\":" + phone + ", " +
                                        "    \"additionalParameters\":{ " +
                                        "        \"string\":\"" + addParam + "\" " +
                                        "    } " +
                                        "}")
                                .post("/customer/postCustomer")
                                .then().extract().response();
                        if (response.getStatusCode() == 200) {
                            customerId = response.jsonPath().getString("id");
                            return true;
                        }
                    }

                    return false;
                });
        return customerId;
    }

    public static CustomerPOJO getCustomerById(String customerId, String token){

        Awaitility.await()
                .atMost(125, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .until(() -> {
                    Response response = RestAssured.given()
                            .contentType("application/json")
                            .header("authToken", token)
                            .queryParam("customerId", customerId)
                            .get("/customer/getCustomerById")
                            .then()
                            .extract().response();
                    if (response.statusCode() == 200){
                        status = response.jsonPath().getString("return.status");
                        phone = response.jsonPath().getLong("return.phone");
                        addParam = response.jsonPath().getString("return.additionalParameters");
                        pd = response.jsonPath().getString("return.pd");

                        return "ACTIVE".equals(status);
                    }
                    return false;
                });
        return new CustomerPOJO(status,phone,addParam,pd);
    }

    public static void findByPhoneNumber(String token, Long phone){
        String xmlBody;
        try {
            xmlBody = new String(Files.readAllBytes(Paths.get("src/test/resources/xmlQuery.xml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String rightXmlBody = xmlBody.replace("{authToken}", token).replace("{phone}", phone.toString());
        Response response = RestAssured.given()
                .header("Content-Type", "application/xml")
                .body(rightXmlBody)
                .post("/customer/findByPhoneNumber")
                .then().extract().response();
        if (response.statusCode() == 200){
            System.out.println(response.jsonPath().getString("Envelope.Body.customerId"));
        }
    }
}
