import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import service.BaseTest;
import service.Employer;
import steps.ActivationSteps;

public class CustomerTest extends BaseTest {
    ActivationSteps activationSteps = new ActivationSteps();

   @ParameterizedTest
   @MethodSource("dataProviderToken")
   @DisplayName("Создание нового абонента")
    public void createNewCustomer(Employer employer){
        activationSteps.postCustomer(employer.getToken(),
                activationSteps.getEmptyPhones(employer.getToken()));
    }

    String status = "DELETE";
    @ParameterizedTest
    @MethodSource("dataProviderToken")
    @DisplayName("Изменения статуса")
    public void changeCustomerStatus(Employer employer){
        activationSteps.changeCustomerStatus(admin.getToken(),
                activationSteps.postCustomer(employer.getToken(),
                        activationSteps.getEmptyPhones(employer.getToken())), status);
    }

    @ParameterizedTest
    @MethodSource("dataProviderToken")
    @DisplayName("Проверка активации нового абонента ")
    public void activatedCustomer(Employer employer){
        activationSteps.getCustomerById(employer.getToken(),
                activationSteps.postCustomer(employer.getToken(),
                        activationSteps.getEmptyPhones(employer.getToken())));
    }

    @ParameterizedTest
    @MethodSource("dataProviderToken")
    @DisplayName("Получение абонента в старой системе")
    public void findCustomerByPhoneNumber(Employer employer){
        activationSteps.findByPhoneNumber(employer.getToken(),
                activationSteps.postCustomer(employer.getToken(),
                        activationSteps.getEmptyPhones(employer.getToken())));
    }
}
