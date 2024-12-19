import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Изменения статуса")
    public void changeCustomerStatus(Employer employer){
        activationSteps.changeCustomerStatus(admin.getToken(),
                activationSteps.postCustomer(employer.getToken(),
                        activationSteps.getEmptyPhones(employer.getToken())),
                            status);
    }

}
