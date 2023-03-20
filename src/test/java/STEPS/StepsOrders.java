package STEPS;

import POJO.MakeOrderPOJO;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static java.lang.String.valueOf;
import static org.hamcrest.CoreMatchers.*;

public class StepsOrders {
    public static String getWrongIngredient(){
        Faker faker=new Faker();
        return valueOf(faker.internet().hashCode());
    }

    public static List<String>  getIngredientsList(int amount) throws Exception{
        Random random=new Random();
        List<String>validIngredients=List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6e");
        List<String>finalIngredients=new ArrayList<>();
        if (amount>0){
            for (int i=0;i<amount;i++){
                finalIngredients.add(validIngredients.get(random.nextInt(validIngredients.size())));
            }
        } else throw new Exception("Please, enter a number greater than 0");
        return finalIngredients;
    }

    @Step("Make order - POST request to endpoint /api/orders")
    public static Response makeOrder(MakeOrderPOJO makeOrderPOJO, String accessBearerToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessBearerToken)
                .and()
                .body(makeOrderPOJO)
                .post("/api/orders");
    }

    @Step("Checking creation order with valid ingredients to authorized user, status code (200) and the response body")
    public static void verifyMakeOrderStatusCode200(Response response){
        response.then().statusCode(200)
                .and().assertThat()
                .body("name",notNullValue())
                .body("order.number",notNullValue())
                .body("success",equalTo(true));
    }

    @Step("Checking creation order without ingredients to authorized user, status code (400) and the response body")
    public static void verifyMakeOrderStatusCode400(Response response){
        response.then()
                .statusCode(400)
                .and().assertThat()
                .body("success",equalTo(false))
                .body("message",equalTo("Ingredient ids must be provided"));
    }

    @Step("Checking creation order without wrong ingredients to authorized user, status code (500) and the response body")
    public static void verifyMakeOrderStatusCode500(Response response) {
        response.then()
                .statusCode(500)
                .and().assertThat()
                .body(containsString("Internal Server Error"));
    }

    @Step("Get user's orders - GET request to endpoint /api/orders")
    public  static Response getUsersOrders(String accessBearerToken){
        return given()
                .auth().oauth2(accessBearerToken)
                .get("/api/orders");
    }

    @Step("Checking receipt orders list to authorized user, status code (200) and the response body")
    public static void verifyGetAuthorisedUsersOrdersStatusCode200(Response response){
        response.then().statusCode(200)
                .and().assertThat()
                .body("success",equalTo(true))
                .body("orders",notNullValue())
                .body("total",notNullValue())
                .body("totalToday",notNullValue());
    }
    @Step("Checking receipt orders list to unauthorized user, status code (401) and the response body")
    public static void verifyGetUnauthorisedUsersOrdersStatusCode401(Response response){
        response.then().statusCode(401)
                .and().assertThat()
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
