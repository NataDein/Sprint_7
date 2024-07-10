import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import static org.apache.http.HttpStatus.*;
import org.junit.Test;




public class TestOrderList extends BaseTest {


    public TestOrderList() {
        super();

        this.methodsForTestsOrdersAPI = new MethodsForTestsOrdersAPI();
        this.methodsForTestsCourierAPI = new MethodsForTestsCourierAPI();

    }

    //Получаем список заказов
    @Test
    @DisplayName("Get a list of orders")
    @Description("Test for GET /api/v1/orders endpoint")
    public void getOrderListReturns200(){
        //Создаем переменную response и помещаем в нее ответ на get-запрос
        Response response = methodsForTestsOrdersAPI.sendGetRequestV1Orders(null);
        //Сравниваем код ответа
        MethodsForCheckResponse.compareStatusCode(response,SC_OK);
        //Проверяем, что в теле ответа возвращается непустой массив с заказами. Т.е. что в теле ответа возвращается список заказов.
        MethodsForCheckResponse.checkFieldInBodyNotNull(response,"orders");
        System.out.println("Тело ответа: " + response.body().asString());
    }

    //Получаем список заказов конкретного курьера
    @Test
    @DisplayName("Get a list of orders with correct courier's id")
    @Description("Test with correct id for GET /api/v1/orders?courierId={courierId} endpoint")
    public void getOrderListWithCorrectId() {
        //Создаем курьера
        Courier courier = new Courier("Курьер", "1234", "Марья");
        methodsForTestsCourierAPI.sendPostRequestV1Courier(courier);
        //Получаем id курьера
        CourierId courierIdFromResponse = (methodsForTestsCourierAPI.getResponsePostV1CourierLogin(new Courier("Курьер","1234")));
        String courierId = courierIdFromResponse.getId();
        //Создаем заказ
        OrderForRequest order = new OrderForRequest("Мирон","Миронов","улица Миронова 1",4,"+7 999 888 77 66", 5, "2024-07-07", "3 этаж", new String[]{"BLACK"});
        OrdersTrackFromResponse responseCreateOrder = methodsForTestsOrdersAPI.getResponsePostV1Orders(order);
        String ordersTrack = responseCreateOrder.getTrack();
        //Ищем заказ по track, чтобы получить его id
        OrderFromResponse orderFromResponse = methodsForTestsOrdersAPI.getResponseGetV1OrdersTrack(ordersTrack);
        String ordersId = orderFromResponse.getOrderFromResponse().getId();
        //Назначаем заказ на курьера
        methodsForTestsOrdersAPI.sendPutRequestV1OrdersAcceptId(ordersId, courierId);
        //Получаем список заказов
        Response response = methodsForTestsOrdersAPI.getOrdersListByCourierId(courierId);
        //Сравниваем код ответа
        MethodsForCheckResponse.compareStatusCode(response,SC_OK);
        //Сверяем, что список не пустой
        MethodsForCheckResponse.checkFieldInBodyNotNull(response,"orders");
        System.out.println("Тело ответа: " + response.body().asString());

        //Удалить курьера с указанием id
        methodsForTestsCourierAPI.sendDeleteRequestV1CourierID(courierId);
    }

    //Пытаемся получить список заказов, используя несуществующий id курьера
    @Test
    @DisplayName("404 Not Found when GET request with incorrect courier id")
    @Description("Test with incorrect id for GET /api/v1/orders?courierId={courierId} endpoint")
    public void getErrorWhenInGetRequestIncorrectID() {
        //Формируем endpoint с указанием несуществующего courierID
        String courierId = "1";

        //Создаем переменную response и помещаем в нее ответ на get-запрос
        Response response = methodsForTestsOrdersAPI.getOrdersListByCourierId(courierId);

        //Сравниваем код ответа
        MethodsForCheckResponse.compareStatusCode(response,SC_NOT_FOUND);

        //Проверяем, что в теле ответа возвращается нужное значение message
        MethodsForCheckResponse.checkValueOfFieldFromBody(response,"message","Курьер с идентификатором " + courierId + " не найден");
    }
}
