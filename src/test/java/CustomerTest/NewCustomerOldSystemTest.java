package CustomerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import service.BaseTest;
import service.Employer;
import steps.ActivationSteps;

public class NewCustomerOldSystemTest extends BaseTest {

    ActivationSteps activationSteps = new ActivationSteps();
    @ParameterizedTest
    @MethodSource("dataProviderToken")
    @DisplayName("Получение абонента в старой системе")
    public void findCustomerByPhoneNumber(Employer employer){
        activationSteps.findByPhoneNumber(employer.getToken(),
                activationSteps.postCustomer(employer.getToken(),
                        activationSteps.getEmptyPhones(employer.getToken())));
    }
}
