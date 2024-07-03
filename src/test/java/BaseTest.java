import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Before;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BaseTest {

    protected String ordersEndpoint = "/api/v1/orders";
    protected String courierEndpoint = "/api/v1/courier";
    protected String courierLoginEndpoint = "/api/v1/courier/login";
    protected String courierId;
    protected String valueOfField;


    //Конструктор для формирования URL для orders
    public String makeOrdersEndpoint(HashMap<String,String> queryParams) {
        ArrayList<NameValuePair> paramsList = new ArrayList<>();

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            paramsList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        URIBuilder builder = new URIBuilder()
                .setPath(ordersEndpoint)
                .setParameters(paramsList);

        return builder.toString();
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    //Шаги:

    //Отправить POST запрос на /api/v1/orders для создания заказа
    @Step("Send POST request to /api/v1/orders")
    public Response sendPostRequestV1Orders(OrderForRequest order, String endpoint) {
        Response response =
                given()
                        .header("Content-type","application/json")
                        .and()
                        .body(order)
                        .when()
                        .post(endpoint);
        return response;
    }

    //Получить тело ответа на POST запрос на /api/v1/orders
    @Step("Get Body from response POST /api/v1/orders")
    public OrdersTrackFromResponse getResponsePostV1Orders(OrderForRequest order, String endpoint) {
        OrdersTrackFromResponse response =
                given()
                        .header("Content-type","application/json")
                        .and()
                        .body(order)
                        .when()
                        .post(endpoint)
                        .body().as(OrdersTrackFromResponse.class);
        return response;
    }

    //Отправить GET запрос на /api/v1/orders для получения списка заказов
    @Step("Send GET request to /api/v1/orders")
    public Response sendGetRequestV1Orders( String endpoint) {
        Response response =
                given()
                        .header("Content-type","application/json")
                        .get(endpoint);
        return response;
    }

    //Получить тело ответа на GET запрос на /api/v1/orders
    @Step("Send GET request to /api/v1/orders")
    public OrderList getResponseGetV1Orders(String endpoint) {
            OrderList response =
                given()
                        .header("Content-type","application/json")
                        .get(endpoint)
                        .body().as(OrderList.class);
        return response;
    }

    //Получить тело ответа на GET запрос на /api/v1/orders/track?t=<track>
    @Step("Get Body from response GET /api/v1/orders")
    public OrderFromResponse getResponseGetV1OrdersTrack(String endpoint, String track) {
//        OrderFromResponse response =
          ResponseBody response = given()
                        .header("Content-type","application/json")
                        .get(endpoint + "/track?t=" + track)
                        .body();

        return response.as(OrderFromResponse.class);
    }


    //Сформировать endpoint для использования доп.параметров при получении списка заказов
    @Step("Make orders endpoint for /api/v1/orders with courierId ")
    public String makeOrdersEndpoint(String valueCourierId) {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("courierId", valueCourierId);
        return makeOrdersEndpoint(queryParams);
    }

    //Отправить POST запрос на /api/v1/courier для создания курьера и отправить POST запрос на /api/v1/courier/login для авторизации
    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestV1Courier(Object courier, String endpoint) {
        Response response =
                given()
                        .header("Content-type","application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(endpoint);
        return response;
    }

    //Получить тело ответа на POST запрос на /api/v1/courier/login
    public CourierId getResponsePostV1CourierLogin(Courier courier, String endpoint) {
        CourierId response =
                given()
                        .header("Content-type","application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(endpoint)
                        .body().as(CourierId.class);
        return response;
    }

    //Отправить DELETE запрос на /api/v1/courier/:id для удаления курьера
    @Step("Send DELETE request to /api/v1/courier/:id")
    public Response sendDeleteRequestV1CourierID(String endpoint, String id) {
        Response response =
                given()
                        .delete(endpoint+"/"+id);
        return response;
    }
    //Отправить PUT запрос на /api/v1/orders/accept/:id для принятия заказа
    @Step("Send PUT request to /api/v1/orders/accept/:id")
    public Response sendPutRequestV1OrdersAcceptId(String endpoint, String ordersId, String courierId) {
        Response response =
                given()
                        .delete(endpoint+ "/accept/" + ordersId+ "?courierId=" + courierId);
        return response;
    }

    //Сравниваем код ответа
    @Step("Compare status code")
    public void compareStatusCode(Response response,int statusCode){
        response.then().assertThat()
                .statusCode(statusCode);
    }

    //Проверяем, что поле в теле ответа не пустое
    @Step("Check field from body response not null")
    public void checkFieldInBodyNotNull(Response response, String field) {
        System.out.println("Тело ответа: " + response.body().asString());

        response.then().assertThat()
                .body(
                        field,
                        allOf(
                                notNullValue(),
                                not(empty())
                        )
                );
    }

    //Проверяем, что поле в теле ответа имеет нужное значение
    @Step("Check a value of a field from body response")
    public void checkValueOfFieldFromBody(Response response, String field, String value){
        response.then().assertThat()
                .body(field, equalTo(value));
    }

    //Проверяем, что поле в теле ответа имеет нужное значение boolean
    @Step("Check a value of a field from body response")
    public void checkBooleanValueOfFieldFromBody(Response response, String field, boolean value){
        response.then().assertThat()
                .body(field, equalTo(value));
    }

}
