import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import service.BaseTest;
import service.Employer;
import steps.ActivationSteps;

public class ActivatedCustomerTest extends BaseTest {
    ActivationSteps activationSteps = new ActivationSteps();

    @ParameterizedTest
    @MethodSource("dataProviderToken")
    public void activatedCustomer(Employer employer){
        System.out.println(activationSteps.getCustomerById(employer.getToken(),
                activationSteps.postCustomer(employer.getToken(),
                        activationSteps.getEmptyPhones(employer.getToken()))));
    }

}
