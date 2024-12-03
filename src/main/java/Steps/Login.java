package Steps;

import io.restassured.RestAssured;

public class Login {

    public static String login(String login, String password){
        return  RestAssured.given()
                .contentType("application/json")
                .body("{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}")
                .post("http://localhost:8090/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");
    }
}
