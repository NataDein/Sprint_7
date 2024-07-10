import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

/**
 * Класс общих методов проверки значений в рамках ответов API
 */
public class MethodsForCheckResponse {
    //Шаги:
    //Сравниваем код ответа
    @Step("Compare status code")
    public static void compareStatusCode(Response response, int statusCode) {
        response.then().assertThat()
                .statusCode(statusCode);
    }

    //Проверяем, что поле в теле ответа не пустое
    @Step("Check field from body response not null")
    public static void checkFieldInBodyNotNull(Response response, String field) {
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
    public static void checkValueOfFieldFromBody(Response response, String field, String value){
        response.then().assertThat()
                .body(field, equalTo(value));
    }

    //Проверяем, что поле в теле ответа имеет нужное значение boolean
    @Step("Check a value of a field from body response")
    public static void checkBooleanValueOfFieldFromBody(Response response, String field, boolean value){
        response.then().assertThat()
                .body(field, equalTo(value));
    }

}
