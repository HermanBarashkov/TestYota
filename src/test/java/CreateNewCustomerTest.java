import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import service.BaseTest;
import service.Employer;
import steps.ActivationSteps;

public class CreateNewCustomerTest extends BaseTest {
    ActivationSteps activationSteps = new ActivationSteps();

   @ParameterizedTest
   @MethodSource("dataProviderToken")
    public void createNewCustomer(Employer employer){
        System.out.println(activationSteps.postCustomer(employer.getToken(),
                activationSteps.getEmptyPhone(employer.getToken())));
    }
}
