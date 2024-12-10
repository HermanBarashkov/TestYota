package service.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.restassured.builder.ResponseSpecBuilder;


public class RequestSpecApi {

    public static final RequestSpecification REQUEST_SPECIFICATION_JSON =
            new RequestSpecBuilder()
                    .setBaseUri("http://localhost:8090")
                    .setContentType(ContentType.JSON)
                    .build();

    public static final RequestSpecification REQUEST_SPECIFICATION_XML =
            new RequestSpecBuilder()
                    .setBaseUri("http://localhost:8090")
                    .setContentType(ContentType.XML)
                    .build();
}
