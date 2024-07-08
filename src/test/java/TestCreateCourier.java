import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import static org.apache.http.HttpStatus.*;
import org.junit.After;
import org.junit.Test;

public class TestCreateCourier extends CourierAPITest {

    //Создаем курьера с полным набором данных
    @Test
    @DisplayName("Create courier with a full set of data")
    @Description("Test for POST /api/v1/courier endpoint")
    public void postCouriersWithFullDataReturns201() {
        //Создаем объект
        Courier courier = new Courier("Курьер", "1234", "Марья");
        //Направляем POST запрос на /api/v1/courier для создания курьера
        Response response = sendPostRequestV1Courier(courier);
        //Сверяем полученный код ответа
        Assertions.compareStatusCode(response,SC_CREATED);
        //Сверяем body
        Assertions.checkBooleanValueOfFieldFromBody(response,"ok",true);
    }

    //Создаем курьера только с обязательными полями
    @Test
    @DisplayName("Create courier only with required fields")
    @Description("Test for POST /api/v1/courier endpoint")
    public void postCouriersWithRequiredFieldsOnlyReturns201() {
        //Создаем объект
        Courier courier2 = new Courier("Иннокентий", "4321");
        //Направляем POST запрос на /api/v1/courier для создания курьера
        Response response = sendPostRequestV1Courier(courier2);
        //Сверяем полученный код ответа
        Assertions.compareStatusCode(response,SC_CREATED);
        //Сверяем body
        Assertions.checkBooleanValueOfFieldFromBody(response,"ok",true);
    }

    //Пытаемся создать курьера только с login
    @Test
    @DisplayName("400 Bad Request when create only with the courier's login")
    @Description("Test for POST /api/v1/courier endpoint")
    public void postCouriersWithOnlyLoginReturns400() {

        //Cоздаем объект для использования при авторизации
        Object courier = new String("{\"login\": \"Курьер\"}");

        //Создаём курьера
        Response response = sendPostRequestV1Courier(courier);

        //Сверяем код ответа
        Assertions.compareStatusCode(response,SC_BAD_REQUEST);
        //Убеждаемся, что в ответе возвращается нужный текст ошибки
        Assertions.checkValueOfFieldFromBody(response,"message","Недостаточно данных для создания учетной записи");
    }

    //Пытаемся создать курьера только с password
    @Test
    @DisplayName("400 Bad Request when create only with the courier's password")
    @Description("Test for POST /api/v1/courier endpoint")
    public void postCouriersWithOnlyPasswordReturns400() {

        //Cоздаем объект для использования при авторизации
        Object courier = new String("{\"password\": \"1234\"}");

        //Создаём курьера
        Response response = sendPostRequestV1Courier(courier);

        //Сверяем код ответа
        Assertions.compareStatusCode(response,SC_BAD_REQUEST);
        //Убеждаемся, что в ответе возвращается нужный текст ошибки
        Assertions.checkValueOfFieldFromBody(response,"message","Недостаточно данных для создания учетной записи");
    }

    //Пытаемся создать курьера только с firstName
    @Test
    @DisplayName("400 Bad Request when create only with the courier's firstName")
    @Description("Test for POST /api/v1/courier endpoint")
    public void postCouriersWithOnlyFirstNamedReturns400() {

        //Cоздаем объект для использования при авторизации
        Object courier = new String("{\"firstName\": \"Марья\"}");

        //Создаём курьера
        Response response = sendPostRequestV1Courier(courier);

        //Сверяем код ответа
        Assertions.compareStatusCode(response,SC_BAD_REQUEST);
        //Убеждаемся, что в ответе возвращается нужный текст ошибки
        Assertions.checkValueOfFieldFromBody(response,"message","Недостаточно данных для создания учетной записи");
    }

    //Пытаемся создать курьера c пустым body
    @Test
    @DisplayName("400 Bad Request when create with empty body")
    @Description("Test for POST /api/v1/courier endpoint")
    public void postCouriersWithEmptyBody400() {

        //Cоздаем объект для использования при авторизации
        Object courier = new String("{ }");

        //Создаём курьера
        Response response = sendPostRequestV1Courier(courier);

        //Сверяем код ответа
        Assertions.compareStatusCode(response,SC_BAD_REQUEST);
        //Убеждаемся, что в ответе возвращается нужный текст ошибки
        Assertions.checkValueOfFieldFromBody(response,"message","Недостаточно данных для создания учетной записи");
    }

    //Пытаемся создать 2 одинаковых курьера
    @Test
    @DisplayName("Create 2 couriers with the same set of data")
    @Description("Test for POST /api/v1/courier endpoint")
    public void postCouriersLoginWith2TheSameDataReturns409() {
        //Создаем объект
        Courier courier = new Courier("Курьер", "1234");
        //Направляем POST запрос на /api/v1/courier для создания курьера
        sendPostRequestV1Courier(courier);
        Response response = sendPostRequestV1Courier(courier);
        //Сверяем полученный код ответа
        Assertions.compareStatusCode(response,SC_CONFLICT);
        //Сверяем body
        Assertions.checkValueOfFieldFromBody(response,"message","Этот логин уже используется");
    }

    @After
    public void deleteCourier() {
        //Удаляем курьера
        //Вытащить id курьера 1
        CourierId courierIdFromResponse = (getResponsePostV1CourierLogin(new Courier("Курьер","1234")));
        String id =courierIdFromResponse.getId();
        //Вытащить id курьера 2
        CourierId courierIdFromResponse2 = (getResponsePostV1CourierLogin(new Courier("Иннокентий","4321")));
        String id2 = courierIdFromResponse2.getId();
        //Удалить курьера с указанием id
        sendDeleteRequestV1CourierID(id);
        sendDeleteRequestV1CourierID(id2);
    }
}
