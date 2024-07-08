import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;

/**
 * Класс для отправки запросов API Заказов
 */
public class OrdersAPITest extends BaseTest {
    protected String ordersEndpoint = "/api/v1/orders";

    public Response getOrdersListByCourierId(String courierId) {
        String endpoint = makeOrdersEndpoint(courierId);

        // Возвращаем ответ на get-запрос
        return sendGetRequestV1Orders(endpoint);
    }

    //Шаги:

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

    //Сформировать endpoint для использования доп.параметров при получении списка заказов
    @Step("Make orders endpoint for /api/v1/orders with courierId")
    public String makeOrdersEndpoint(String valueCourierId) {
        HashMap<String, String> queryParams = new HashMap<>();

        queryParams.put("courierId", valueCourierId);

        return makeOrdersEndpoint(queryParams);
    }

    //Отправить POST запрос на /api/v1/orders для создания заказа
    @Step("Send POST request to /api/v1/orders")
    public Response sendPostRequestV1Orders(OrderForRequest order, String endpoint) {
        return given()
                .header("Content-type","application/json")
                .and()
                .body(order)
                .when()
                .post(endpoint);
    }

    //Получить тело ответа на POST запрос на /api/v1/orders
    @Step("Get Body from response POST /api/v1/orders")
    public OrdersTrackFromResponse getResponsePostV1Orders(OrderForRequest order) {
        return given()
                .header("Content-type","application/json")
                .and()
                .body(order)
                .when()
                .post(ordersEndpoint)
                .body().as(OrdersTrackFromResponse.class);
    }

    /**
     * Отправить GET запрос на /api/v1/orders для получения списка заказов
     *
     * @param endpoint опциональный параметр эндпоинта с query-параметрами
     */
    @Step("Send GET request to /api/v1/orders (may be with queryParams)")
    public Response sendGetRequestV1Orders(String endpoint) {
        String endpointPath = endpoint != null ? endpoint : ordersEndpoint;

        return given()
                .header("Content-type","application/json")
                .get(endpointPath);
    }

    //Получить тело ответа на GET запрос на /api/v1/orders
    @Step("Send GET request to /api/v1/orders")
    public OrderList getResponseGetV1Orders(String endpoint) {
        return given()
                .header("Content-type","application/json")
                .get(endpoint)
                .body().as(OrderList.class);
    }

    //Получить тело ответа на GET запрос на /api/v1/orders/track?t=<track>
    @Step("Get Body from response GET /api/v1/orders")
    public OrderFromResponse getResponseGetV1OrdersTrack(String track) {
        return given()
                .header("Content-type","application/json")
                .get(ordersEndpoint + "/track?t=" + track)
                .body().as(OrderFromResponse.class);
    }

    //Отправить PUT запрос на /api/v1/orders/accept/:id для принятия заказа
    @Step("Send PUT request to /api/v1/orders/accept/:id")
    public void sendPutRequestV1OrdersAcceptId(String ordersId, String courierId) {
        given().delete(ordersEndpoint + "/accept/" + ordersId + "?courierId=" + courierId);
    }
}
