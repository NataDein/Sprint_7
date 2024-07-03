import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;




public class TestOrderList extends BaseTest {

    //Получаем список заказов
    @Test
    @DisplayName("Get a list of orders")
    @Description("Test for GET /api/v1/orders endpoint")
    public void getOrderListReturns200(){

        //Создаем переменную response и помещаем в нее ответ на get-запрос
        Response response = sendGetRequestV1Orders(ordersEndpoint);
        //Сравниваем код ответа
        compareStatusCode(response,200);
        //Проверяем, что в теле ответа возвращается непустой массив с заказами. Т.е. что в теле ответа возвращается список заказов.
        checkFieldInBodyNotNull(response,"orders");
        System.out.println("Тело ответа: " + response.body().asString());
    }

    //Получаем список заказов конкретного курьера
    @Test
    @DisplayName("Get a list of orders with correct courier's id")
    @Description("Test with correct id for GET /api/v1/orders?courierId={courierId} endpoint")
    public void getOrderListWithCorrectId() {
        //Создаем курьера
        Courier courier = new Courier("Курьер", "1234", "Марья");
        sendPostRequestV1Courier(courier, courierEndpoint);
        //Получаем id курьера
        CourierId courierIdFromResponse = (getResponsePostV1CourierLogin(new Courier("Курьер","1234"),courierLoginEndpoint));
        String courierId =courierIdFromResponse.getId();
        //Создаем заказ
        OrderForRequest order = new OrderForRequest("Мирон","Миронов","улица Миронова 1",4,"+7 999 888 77 66", 5, "2024-07-07", "3 этаж", new String[]{"BLACK"});
        OrdersTrackFromResponse responseCreateOrder = getResponsePostV1Orders(order, ordersEndpoint);
        String ordersTrack = responseCreateOrder.getTrack();
        //Ищем заказ по track, чтобы получить его id
        OrderFromResponse orderFromResponse = getResponseGetV1OrdersTrack(ordersEndpoint, ordersTrack);
        String ordersId = orderFromResponse.getOrderFromResponse().getId();
        //Назначаем заказ на курьера
        sendPutRequestV1OrdersAcceptId(ordersEndpoint, ordersId, courierId);
        //Получаем список заказов
        String endpoint = makeOrdersEndpoint(courierId);
        Response response = sendGetRequestV1Orders(endpoint);
        //Сравниваем код ответа
        compareStatusCode(response,200);
        //Сверяем, что список не пустой
        checkFieldInBodyNotNull(response,"orders");
        System.out.println("Тело ответа: " + response.body().asString());




        //Удалить курьера с указанием id
        sendDeleteRequestV1CourierID(courierEndpoint,courierId);


    }

    //Пытаемся получить список заказов, используя несуществующий id курьера
    @Test
    @DisplayName("404 Not Found when GET request with incorrect courier id")
    @Description("Test with incorrect id for GET /api/v1/orders?courierId={courierId} endpoint")
    public void getErrorWhenInGetRequestIncorrectID() {

        //Формируем endpoint с указанием несуществующего courierID
        courierId = "1";
        String endpoint = makeOrdersEndpoint(courierId);

        //Создаем переменную response и помещаем в нее ответ на get-запрос
        Response response = sendGetRequestV1Orders(endpoint);

        //Сравниваем код ответа
        compareStatusCode(response,404);
        //Проверяем, что в теле ответа возвращается нужное значение message
        valueOfField = "Курьер с идентификатором " + courierId + " не найден";
        checkValueOfFieldFromBody(response,"message",valueOfField);

    }








}
