package service.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


public class RequestSpecApi {

    public static final RequestSpecification REQUEST_SPECIFICATION_JSON =
            new RequestSpecBuilder()
                    .setBaseUri("http://10.2.1.4:8080")
                    .setContentType(ContentType.JSON)
                    .build();

    public static final RequestSpecification REQUEST_SPECIFICATION_XML =
            new RequestSpecBuilder()
                    .setBaseUri("http://10.2.1.4:8080")
                    .setContentType(ContentType.XML)
                    .build();
}
