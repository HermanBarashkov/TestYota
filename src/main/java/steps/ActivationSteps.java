package steps;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private static final Logger log = LoggerFactory.getLogger(ActivationSteps.class);
    AuthRequestPOJO authRequestPOJO = new AuthRequestPOJO();
    CreateCustomerPOJO createCustomerPOJO = new CreateCustomerPOJO();
    ChangeCustomerStatusPOJO changeCustomerStatusPOJO = new ChangeCustomerStatusPOJO();


    public String getAuthToken(String login, String password){

        Response response = given()
                .spec(RequestSpecApi.REQUEST_SPECIFICATION_JSON)
                .body(authRequestPOJO.withLogin(login).withPassword(password))
                .when()
                .post("/login")
                .then()
                .statusCode(200)
                .extract().response();
        String token = response.jsonPath().getString("token");
        log.info("Токен для роли {}: {}", login, token);
        return token;

    }



    public List<String> getEmptyPhones(String token){
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
                            log.info("Список телефонов для токена {}: {}", token, resultPhones);
                            return true;
                        }
                    } else{log.error(response.jsonPath().getString("errorMessage"));
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
                                .then()
                                .extract().response();

                        if (response.getStatusCode() == 200) {
                            customerId.set(response.jsonPath().getString("id"));
                            successPhone.set(phone);
                            success.set(true);
                            log.info("Для абонента с токеном: {}, выдан ID: {} и номер: {}", token, customerId, successPhone);
                            return true;
                        }
                    }

                    if (!success.get()) {
                        log.info("Для пользователя с токеном: {}, ни один номер не подошел, запрос нового списка номеров", token);
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
                    if ("ACTIVE".equals(response.jsonPath().getString("return.status"))){
                        log.info("Абонент с ID: {}, статус: {}, паспортные данные: {}",
                                customerId,
                                    response.jsonPath().getString("return.status"),
                                        response.jsonPath().getString("return.pd"));
                        return true;
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
                .then()
                .extract().response();
        if (response.statusCode() == 200) {
            XmlPath xmlPath = new XmlPath(response.getBody().asString());
            log.info("ID: {} сохранился в старой системе",xmlPath.getString("Envelope.Body.customerId"));
        } else {
            log.info("Error: Status code {}", response.statusCode());
            log.info("Response: {}", response.asString());
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
        log.info("Статус изменен на: {}", status);
    }


}
