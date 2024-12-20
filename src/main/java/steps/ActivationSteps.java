package steps;

import service.config.RequestSpecApi;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import service.pojo.AddParam;
import service.pojo.AuthRequestPOJO;
import service.pojo.ChangeCustomerStatusPOJO;
import service.pojo.CreateCustomerPOJO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;


public class ActivationSteps {


    AuthRequestPOJO authRequestPOJO = new AuthRequestPOJO();
    CreateCustomerPOJO createCustomerPOJO = new CreateCustomerPOJO();
    ChangeCustomerStatusPOJO changeCustomerStatusPOJO = new ChangeCustomerStatusPOJO();

    public String getAuthToken(String login, String password){

        return given()
                .spec(RequestSpecApi.REQUEST_SPECIFICATION_JSON)
                .body(authRequestPOJO.withLogin(login).withPassword(password))
                .when()
                .post("/login")
                .then().statusCode(200)
                .extract().jsonPath().getString("token");
    }



    public static List<String> getEmptyPhones(String token){
        AtomicReference<List <String>> resultPhones = new AtomicReference<>();
        await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .until(() -> {
                    Response response = given()
                            .spec(RequestSpecApi.REQUEST_SPECIFICATION_JSON)
                            .header("authToken", token)
                            .get("/simcards/getEmptyPhone")
                            .then()
                            .extract().response();
                    if (response.getStatusCode() == 200){
                        List<String> phones = response.jsonPath().getList("phones.phone", String.class);
                        if (!phones.isEmpty()){
                            resultPhones.set(phones);
                            return true;
                        }
                    } else { String errorMessage = response.jsonPath().getString("errorMessage");
                        System.out.println(errorMessage);
                        return false;
                    }
                    return false;
                });
        return resultPhones.get();
    }

    public String postCustomer(String token, List<String> initialPhones) {
        AtomicBoolean success = new AtomicBoolean(false);
        AtomicReference<String> customerId = new AtomicReference<>();
        AtomicReference<List<String>> phones = new AtomicReference<>();
        phones.set(initialPhones);
        AtomicReference<String> successPhone = new AtomicReference<>();

        await()
                .atMost(30, TimeUnit.SECONDS)
                .pollInterval(300, TimeUnit.MILLISECONDS)
                .until(() -> {
                    for (String phone : phones.get()) {
                        Response response = given()
                                .spec(RequestSpecApi.REQUEST_SPECIFICATION_JSON)
                                .header("authToken", token)
                                .body(createCustomerPOJO
                                        .withName("Петя")
                                        .withPhone(phone)
                                        .withAddParam(new AddParam().withString("string")))
                                .when()
                                .post("/customer/postCustomer")
                                .then().extract().response();

                        if (response.getStatusCode() == 200) {
                            customerId.set(response.jsonPath().getString("id"));
                            successPhone.set(phone);
                            success.set(true);
                            System.out.println("Номер подошел");
                            return true;
                        }
                    }

                    if (!success.get()) {
                        System.out.println("Не один номер не подошел, запрос новых номеров");
                        phones.set(getEmptyPhones(token));  // Запрашиваем новый список номеров
                    }
                    return false;
                });
        createCustomerPOJO = new CreateCustomerPOJO(" ", successPhone.get(), new AddParam(""));
        return customerId.get();
    }

    public void getCustomerById(String token, String customerId){

        await()
                .atMost(245, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .until(() -> {
                    Response response = given()
                            .spec(RequestSpecApi.REQUEST_SPECIFICATION_JSON)
                            .header("authToken", token)
                            .queryParam("customerId", customerId)
                            .when()
                            .get("/customer/getCustomerById")
                            .then()
                            .extract().response();
                    if (response.statusCode() == 200){
                        return "ACTIVE".equals(response.jsonPath().getString("return.status"));
                    }
                    return false;
                });

    }

    public void findByPhoneNumber(String token, String customerID){
        String xmlBody;
        try {
            xmlBody = new String(Files.readAllBytes(Paths.get("src/test/resources/xmlQuery.xml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String responseXmlBody = xmlBody
                .replace("{authToken}", token.trim())
                .replace("{phoneNumber}", createCustomerPOJO.getPhone());
        //System.out.println("FINAL XML BODY: " + responseXmlBody);

        Response response = given()
                .spec(RequestSpecApi.REQUEST_SPECIFICATION_XML)
                .body(responseXmlBody)
                .when()
                .post("/customer/findByPhoneNumber")
                .then().extract().response();
        if (response.statusCode() == 200) {
            XmlPath xmlPath = new XmlPath(response.getBody().asString());
            System.out.println(xmlPath.getString("Envelope.Body.customerId"));
        } else {
            System.err.println("Error: Status code " + response.statusCode());
            System.err.println("Response: " + response.asString());
        }

    }

    public void changeCustomerStatus(String token, String customerId, String status){
        given()
                .spec(RequestSpecApi.REQUEST_SPECIFICATION_JSON)
                .header("authToken", token)
                .pathParam("customerId", customerId)
                .body(changeCustomerStatusPOJO.withStatus(status))
                .when()
                .post("/customer/{customerId}/changeCustomerStatus")
                .then()
                .extract().response();
        System.out.println("Статус изменен на: " + status);
    }


}
