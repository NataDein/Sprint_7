import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class TestCreateOrder extends BaseTest {

    private final String FIRST_NAME;
    private final String LAST_NAME;
    private final String ADDRESS;
    private final int METRO_STATION;
    private final String PHONE;
    private final int RENT_TIME;
    private final String DELIVERY_DATE;
    private final String COMMENT;
    private final String[] COLOR;

    public TestCreateOrder(
            String firstName,
            String lastName,
            String address,
            int metroStation,
            String phone,
            int rentTime,
            String deliveryDate,
            String comment,
            String[] color
    ) {
        this.FIRST_NAME = firstName;
        this.LAST_NAME = lastName;
        this.ADDRESS = address;
        this.METRO_STATION = metroStation;
        this.PHONE = phone;
        this.RENT_TIME = rentTime;
        this.DELIVERY_DATE = deliveryDate;
        this.COMMENT = comment;
        this.COLOR = color;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {"Мирон","Миронов","улица Миронова 1",4,"+7 999 888 77 66", 5, "2024-07-07", "3 этаж", new String[]{}},
                {"Мила","Йовович","Дом Милы Йовович 2", 4,"+7 999 888 77 66", 5, "2024-07-07", "А почему бы и да?", new String[]{"BLACK"}},
                {"Марк","Антонин","улица Римской империи 5", 4,"+7 999 888 77 66", 5, "2024-07-07", "Думаешь о римской империи?", new String[]{"GREY"}},
                {"Матрона","Матроновна","3 изба слева от монастыря", 4,"+7 999 888 77 66", 5, "2024-07-07", "Тот что с золотыми куполами, не голубыми", new String[]{"BLACK","GREY"}},
        };
    }


    //Проверяем создание заказа с разными данными
    @Test
    @DisplayName("Creating order with correct data")
    @Description("Test for POST /api/v1/orders endpoint")
    public void createOrderWithCorrectDataReturns201() {
        //создаем объект для параметризации полей body
        OrderForRequest order = new OrderForRequest(FIRST_NAME, LAST_NAME, ADDRESS, METRO_STATION, PHONE, RENT_TIME, DELIVERY_DATE, COMMENT, COLOR);

        //Создаем переменную response и помещаем в нее ответ на post-запрос
        Response response = sendPostRequestV1Orders(order, ordersEndpoint);

       //Проверяем, статус
       compareStatusCode(response,201);

       //Проверяем, что поле "track" не пустое
       checkFieldInBodyNotNull(response,"track");
    }


}
