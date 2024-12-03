package Steps;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.awaitility.Awaitility;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class Customer {

    static String customerId;

    public static String postCustomer(String token, List<Long> phones) {

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
                                        "        \"string\":\"string\" " +
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
}