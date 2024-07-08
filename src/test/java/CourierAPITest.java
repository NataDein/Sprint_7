import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/**
 * Класс для отправки запросов API Курьеров
 */
public class CourierAPITest extends BaseTest {
    protected String courierEndpoint = "/api/v1/courier";
    protected String courierLoginEndpoint = courierEndpoint + "/login";

    //Шаги:

    //Отправить POST запрос на /api/v1/courier для создания курьера
    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestV1Courier(Object courier) {
        Response response =
                given()
                        .header("Content-type","application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(courierEndpoint);
        return response;
    }

    //Отправить POST запрос на /api/v1/courier/login для авторизации
    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestV1CourierLogin(Object courier) {
        Response response =
                given()
                        .header("Content-type","application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(courierLoginEndpoint);
        return response;
    }

    //Получить тело ответа на POST запрос на /api/v1/courier/login
    public CourierId getResponsePostV1CourierLogin(Courier courier) {
        CourierId response =
                given()
                        .header("Content-type","application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(courierLoginEndpoint)
                        .body().as(CourierId.class);
        return response;
    }

    //Отправить DELETE запрос на /api/v1/courier/:id для удаления курьера
    @Step("Send DELETE request to /api/v1/courier/:id")
    public Response sendDeleteRequestV1CourierID(String id) {
        Response response =
                given()
                        .delete(courierEndpoint + "/" + id);
        return response;
    }

}
