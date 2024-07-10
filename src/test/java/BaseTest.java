import io.restassured.RestAssured;
import org.junit.Before;

public class BaseTest {
    protected MethodsForTestsOrdersAPI methodsForTestsOrdersAPI;
    protected MethodsForTestsCourierAPI methodsForTestsCourierAPI;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
}
