package Steps;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class CustomerStatus {

    public static String ChangeCustomerStatus(String customerId, String token, String status){
    Response response = RestAssured.given()
            .contentType("application/json")
            .header("authToken", token)
            .pathParam("customerId", customerId)
            .body("{\"status\": \"" + status + "\"}")
            .post("http://localhost:8090/customer/{customerId}/changeCustomerStatus")
            .then().extract().response();
    if (response.statusCode() == 200){

    }
        return customerId;
    }
}
