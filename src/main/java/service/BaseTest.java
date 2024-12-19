package service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;
import steps.ActivationSteps;
import java.util.stream.Stream;

public class BaseTest {
    public static Employer admin;
    public static Employer user;

    static ActivationSteps activationSteps = new ActivationSteps();

    @BeforeAll
    public static void initTest() {
        String token = activationSteps.getAuthToken("admin", "password");
        admin = new Employer(token, Employer.roleEnum.ADMIN);

        token = activationSteps.getAuthToken("user", "password");
        user = new Employer(token, Employer.roleEnum.USER);
    }

    public static Stream<Arguments> dataProviderToken() {
        return Stream.of(
                Arguments.of(admin),
                Arguments.of(user)
        );
    }
}