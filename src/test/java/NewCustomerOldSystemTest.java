import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import service.BaseTest;
import service.Employer;
import steps.ActivationSteps;

public class NewCustomerOldSystemTest extends BaseTest {

    ActivationSteps activationSteps = new ActivationSteps();
    @ParameterizedTest
    @MethodSource("dataProviderToken")
    public void findCustomerByPhoneNumber(Employer employer){
        activationSteps.findByPhoneNumber(employer.getToken(), );
    }
}
