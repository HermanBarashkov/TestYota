package service;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import steps.ActivationSteps;

public class BaseTest {
    public static Employer admin;
    public static Employer user;

    ActivationSteps activationSteps = new ActivationSteps();

    @BeforeSuite
    public void InitTest(){
        String token = activationSteps.
                getAuthToken(System.getProperty("loginAdmin"),
                        System.getProperty("passwordAdmin"));
        admin = new Employer(token, Employer.roleEnum.ADMIN);
        token = activationSteps
                .getAuthToken(System.getProperty("loginUser"),
                        System.getProperty("passwordUser"));
        user = new Employer(token, Employer.roleEnum.USER);
    }

    @DataProvider(name = "token", parallel = true)
    public Object[][] dataProviderToken(){
        return new Object[][]{
                {admin}, {user}
        };
    }

}
