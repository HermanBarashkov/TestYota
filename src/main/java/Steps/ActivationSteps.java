package Steps;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.awaitility.Awaitility;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ActivationSteps {

    static List<Long> result;
    static String errorMessage;

    static String customerId;

    static String status, json;
    static Long phone;

    public static String getLogin(String login, String password){

        return  RestAssured.given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .post("http://localhost:8090/login")
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
                            .get("http://localhost:8090/simcards/getEmptyPhone")
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
                                .post("http://localhost:8090/customer/postCustomer")
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

    public static String getCustomerById(String customerId, String token){
        Awaitility.await()
                .atMost(125, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .until(() -> {
                    Response response = RestAssured.given()
                            .header("authToken", token)
                            .queryParam("customerId", customerId)
                            .contentType("application/json")
                            .get("http://localhost:8090/customer/getCustomerById")
                            .then()
                            .extract().response();
                    if (response.statusCode() == 200){
                        status = response.jsonPath().getString("return.status");
                        json = response.jsonPath().getString("return");
                        return "ACTIVE".equals(status);
                    }
                    else {System.out.println(status);
                        return false;}
                });
        return json;
    }



}
