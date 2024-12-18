import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import service.BaseTest;
import service.Employer;
import steps.ActivationSteps;

public class ChangeCustomerStatusTest extends BaseTest {


    ActivationSteps activationSteps = new ActivationSteps();
    String status = "DELETE";
    @ParameterizedTest
    @MethodSource("dataProviderToken")
    public void changeCustomerStatus(Employer employer){
        activationSteps.changeCustomerStatus(admin.getToken(),
                activationSteps.postCustomer(admin.getToken(),
                        activationSteps.getEmptyPhones(admin.getToken())),
                            status);
    }

}
