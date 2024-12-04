import org.testng.annotations.Test;
import service.BaseTest;
import service.Employer;
import steps.ActivationSteps;


public class CreateNewCustomerTest extends BaseTest {
    ActivationSteps activationSteps = new ActivationSteps();

    @Test(description = "ТК№11,30 Создание кастомера",
            dataProvider = "token",  groups = "active")
    public void createNewCusomer(Employer employer){
        activationSteps.postCustomer(employer.getToken(),
                activationSteps.getEmptyPhone(employer.getToken()));
    }
}
