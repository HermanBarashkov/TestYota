import Steps.*;
import config.WebConfigProvider;
import io.restassured.RestAssured;
import model.CustomerPOJO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;


public class ActivationTest {

    @BeforeAll
    public static void precondition(){
        RestAssured.baseURI = WebConfigProvider.getProps().yotaUrl();
    }

    @Test
    public void testActivateUser(){

        String token = Activation.getLogin("user", "password");
        System.out.println("User: " + token);

        List<Long> phones = Activation.getEmptyPhone(token);
        System.out.println("User: " + phones);

        String customerId = Activation.postCustomer(token, phones, "Мужик");
        System.out.println("User: " + customerId);

        CustomerPOJO customer = Activation.getCustomerById(customerId, token);
        System.out.println("User: " + customer.getStatus());
        System.out.println("User: " + customer.getPhone());
        System.out.println("User: " + customer.getAddParam());
        System.out.println("User: " + customer.getPd());
        //Activation.findByPhoneNumber(token, customer.getPhone());
    }

    @Test
    public void testActivateAdmin(){

        String token = Activation.getLogin("admin", "password");
        System.out.println("Admin: " + token);

        List<Long> phones = Activation.getEmptyPhone(token);
        System.out.println("Admin: " + phones);

        String customerId = Activation.postCustomer(token, phones, "Женина");
        System.out.println("Admin: " + customerId);

        CustomerPOJO customer = Activation.getCustomerById(customerId, token);
        System.out.println("Admin: " + customer.getStatus());
        System.out.println("Admin: " + customer.getPhone());
        System.out.println("Admin: " + customer.getAddParam());
        System.out.println("Admin: " + customer.getPd());

        //Activation.findByPhoneNumber(token, customer.getPhone());

    }
}


