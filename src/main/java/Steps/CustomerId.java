package Steps;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.awaitility.Awaitility;

import java.util.concurrent.TimeUnit;

public class CustomerId {

    private static String status;
    private static Long phone;

    public static Long getCustomer(String customerId, String token){
        Awaitility.await()
                .atMost(3, TimeUnit.MINUTES)
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
                        //json = response.jsonPath().getString("return");
                        phone = response.jsonPath().getLong("return.phone");
                        return "ACTIVE".equals(status);
                    }
                    else {System.out.println(status);
                    return false;}
                });
        return phone;
    }
}