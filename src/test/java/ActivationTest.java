import Steps.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;


public class ActivationTest {

    @Test
    public void testActivateUser(){

        String token = ActivationSteps.getLogin("user", "password");
        System.out.println("User: " + token);

        List<Long> phones = ActivationSteps.getEmptyPhone(token);
        System.out.println("User: " + phones);

        String customerId = ActivationSteps.postCustomer(token, phones, "Мужик");
        System.out.println("User: " + customerId);

        String customer = ActivationSteps.getCustomerById(customerId, token);
        System.out.println("User: " + customer);

        /*String findByPhoneNum = CustomerService.findCustomerByPhoneNumber(token, customer);
        System.out.println(findByPhoneNum);*/

    }

    @Test
    public void testActivateAdmin(){

        String token = ActivationSteps.getLogin("admin", "password");
        System.out.println("Admin: " + token);

        List<Long> phones = ActivationSteps.getEmptyPhone(token);
        System.out.println("Admin: " + phones);

        String customerId = ActivationSteps.postCustomer(token, phones, "Женина");
        System.out.println("Admin: " + customerId);

        String customer = ActivationSteps.getCustomerById(customerId, token);
        System.out.println("Admin: " + customer);

        /*String findByPhoneNum = CustomerService.findCustomerByPhoneNumber(token, customer);
        System.out.println(findByPhoneNum);*/

    }
}


