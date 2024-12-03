package Steps;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.awaitility.Awaitility;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EmptyPhone {

    static List<Long> result;
    static String errorMessage;

    public static List<Long> getEmptyPhone(String token){


        Awaitility.await()
                .atMost(1,TimeUnit.MINUTES)
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
}
