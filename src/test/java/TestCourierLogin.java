
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import static org.apache.http.HttpStatus.*;
import org.junit.*;


public class TestCourierLogin extends CourierAPITest {
    //Предварительно создаем курьера
    @Before
    public void createCourier(){
       Courier courier = new Courier("Курьер", "1234");
       sendPostRequestV1Courier(courier);
    }

    //Авторизуемся по корректному логину и паролю
    @Test
    @DisplayName("Authorization with a full set of data")
    @Description("Test for POST /api/v1/courier/login endpoint")
    public void postCouriersLoginWithFullDataReturns200() {
        //Авторизуемся
        Response response = sendPostRequestV1CourierLogin(new Courier("Курьер","1234"));

        //Сверяем код ответа
        Assertions.compareStatusCode(response, SC_OK);
        //Убеждаемся, что в ответе возвращается непустое поле id
        Assertions.checkFieldInBodyNotNull(response,"id");
        System.out.println("Тело ответа: " + response.body().asString());
    }

    //Пытаемся авторизоваться только с логином
    @Test
    @DisplayName("400 Bad Request when log in only with the courier's login")
    @Description("Test for POST /api/v1/courier/login endpoint")
    public void postCouriersLoginWithOnlyLoginReturns400() {

       //Cоздаем объект для использования при авторизации
        Object courier = new String("{\"login\": \"Курьер\"}");

        //Авторизуемся
        Response response = sendPostRequestV1CourierLogin(courier);

        //Сверяем код ответа
        Assertions.compareStatusCode(response,SC_BAD_REQUEST);
        //Убеждаемся, что в ответе возвращается нужный текст ошибки
        Assertions.checkValueOfFieldFromBody(response,"message","Недостаточно данных для входа");
    }

    //Пытаемся авторизоваться только с паролем
    @Test
    @DisplayName("400 Bad Request when log in only with the courier's password")
    @Description("Test for POST /api/v1/courier/login endpoint")
    public void postCouriersLoginWithOnlyPasswordReturns400() {

        //Cоздаем объект для использования при авторизации
        Object courier = new String("{\"password\": \"1234\"}");

        //Авторизуемся
        Response response = sendPostRequestV1CourierLogin(courier);

        //Сверяем код ответа
        Assertions.compareStatusCode(response,SC_BAD_REQUEST);
        //Убеждаемся, что в ответе возвращается нужный текст ошибки
        Assertions.checkValueOfFieldFromBody(response,"message","Недостаточно данных для входа");
    }

    //Пытаемся авторизоваться с пустым body
    @Test
    @DisplayName("400 Bad Request when log in with empty body")
    @Description("Test for POST /api/v1/courier/login endpoint")
    public void postCouriersLoginWithEmptyBody400() {

        //Cоздаем объект для использования при авторизации
        Object courier = new String("{ }");

        //Авторизуемся
        Response response = sendPostRequestV1CourierLogin(courier);

        //Сверяем код ответа
        Assertions.compareStatusCode(response,SC_BAD_REQUEST);
        //Убеждаемся, что в ответе возвращается нужный текст ошибки
        Assertions.checkValueOfFieldFromBody(response,"message","Недостаточно данных для входа");
    }

    //Пытаемся авторизоваться с несуществующим логином
    @Test
    @DisplayName("Authorization with non-existent login")
    @Description("Test for POST /api/v1/courier/login endpoint")
    public void postCouriersLoginWithNonExistentLoginReturns404() {

        //Авторизуемся
        Response response = sendPostRequestV1CourierLogin(new Courier("Курер","1234"));

        //Сверяем код ответа
        Assertions.compareStatusCode(response,SC_NOT_FOUND);
        //Убеждаемся, что в ответе возвращается нужный текст ошибки
        Assertions.checkValueOfFieldFromBody(response,"message","Учетная запись не найдена");
    }

    //Удаляем предварительно созданного курьера
    @After
    public void deleteCourier() {
        //Вытащить id курьера
        CourierId courierIdFromResponse = (getResponsePostV1CourierLogin(new Courier("Курьер","1234")));
        String id =courierIdFromResponse.getId();
        //Удалить курьера с указанием id
        sendDeleteRequestV1CourierID(id);
    }
}
