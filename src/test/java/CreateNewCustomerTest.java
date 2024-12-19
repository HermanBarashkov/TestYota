import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import service.BaseTest;
import service.Employer;
import steps.ActivationSteps;

public class CreateNewCustomerTest extends BaseTest {
    ActivationSteps activationSteps = new ActivationSteps();

   @ParameterizedTest
   @MethodSource("dataProviderToken")
   @DisplayName("Создание нового абонента")
    public void createNewCustomer(Employer employer){
        activationSteps.postCustomer(employer.getToken(),
                activationSteps.getEmptyPhones(employer.getToken()));
    }
}
