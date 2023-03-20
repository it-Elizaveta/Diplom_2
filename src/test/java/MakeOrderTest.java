import POJO.MakeOrderPOJO;
import STEPS.StepsOrders;
import STEPS.StepsUsers;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class MakeOrderTest {
    private String email;
    private String password;
    private String name;
    private String accessBearerToken="";
    private final List<String> ingredientsP;

    public MakeOrderTest(List<String> ingredientsP){
        this.ingredientsP=ingredientsP;
    }

    @Parameterized.Parameters
    public static Object[][] getData() throws Exception{
        return new Object[][] {
                {StepsOrders.getIngredientsList(1)},
                {StepsOrders.getIngredientsList(2)},
                {StepsOrders.getIngredientsList(5)},
                {StepsOrders.getIngredientsList(25)}
        };
    }

    @Before
    public void init(){
        RestAssured.baseURI=BaseURI.BASE_URI;
        email= StepsUsers.getEmail();
        password= StepsUsers.getPassword();
        name= StepsUsers.getName();
    }

    @DisplayName("Make order with authorization and with ingredients returns status code 200 parameterized")
    @Description("We are registering user and make order with valid ingredients and expect to receive the status code 200 and the correct response body (Parameterized Test)")
    @Test
    public void makeOrderWithAuthorizationAndWithIngredientsReturnsStatusCode200Param(){
        accessBearerToken= StepsUsers.signUpAndSignInAndGetToken(email,password,name);
        MakeOrderPOJO makeOrderPojo = new MakeOrderPOJO();
        makeOrderPojo.setIngredients(ingredientsP);
        Response responseMakeOrder=StepsOrders.makeOrder(makeOrderPojo,accessBearerToken);
        StepsOrders.verifyMakeOrderStatusCode200(responseMakeOrder);
    }

    @DisplayName("Make order with authorization and without ingredients returns status code 400")
    @Description("We are registering user and make order without ingredients and expect to receive the status code 400 and an error message in the response body")
    @Test
    public void makeOrderWithAuthorizationAndWithoutIngredientsReturnsStatusCode400(){
        accessBearerToken= StepsUsers.signUpAndSignInAndGetToken(email,password,name);
        MakeOrderPOJO makeOrderPOJO=new MakeOrderPOJO();
        Response responseMakeOrder=StepsOrders.makeOrder(makeOrderPOJO,accessBearerToken);
        StepsOrders.verifyMakeOrderStatusCode400(responseMakeOrder);
    }

    @DisplayName("Make order with authorization and with wrong ingredients returns status code 500")
    @Description("We are registering user and make order with not valid ingredients and expect to receive the status code 500 and an error message in the response body")
    @Test
    public void makeOrderWithAuthorizationAndWithWrongIngredientsReturnsStatusCode500(){
        accessBearerToken= StepsUsers.signUpAndSignInAndGetToken(email,password,name);
        MakeOrderPOJO makeOrderPOJO=new MakeOrderPOJO();
        makeOrderPOJO.setIngredients(List.of(StepsOrders.getWrongIngredient()));
        Response responseMakeOrder=StepsOrders.makeOrder(makeOrderPOJO,accessBearerToken);
        StepsOrders.verifyMakeOrderStatusCode500(responseMakeOrder);
    }

    @DisplayName("Make order without authorization and with ingredients returns status code 200 parameterized")
    @Description("We don't log in and make order with valid ingredients and expect to receive the status code 200 and the correct response body (Parameterized Test)")
    @Test
    public void makeOrderWithoutAuthorizationAndWithIngredientsReturnsStatusCode200Param(){
        accessBearerToken= "";
        MakeOrderPOJO makeOrderPojo = new MakeOrderPOJO();
        makeOrderPojo.setIngredients(ingredientsP);
        Response responseMakeOrder=StepsOrders.makeOrder(makeOrderPojo,accessBearerToken);
        StepsOrders.verifyMakeOrderStatusCode200(responseMakeOrder);
    }

    @DisplayName("Make order without authorization and without ingredients returns status code 400")
    @Description("We don't log in and make order without ingredients and expect to receive the status code 400 and an error message in the response body")
    @Test
    public void makeOrderWithoutAuthorizationAndWithoutIngredientsReturnsStatusCode400(){
        accessBearerToken= "";
        MakeOrderPOJO makeOrderPOJO=new MakeOrderPOJO();
        Response responseMakeOrder=StepsOrders.makeOrder(makeOrderPOJO,accessBearerToken);
        StepsOrders.verifyMakeOrderStatusCode400(responseMakeOrder);
    }

    @DisplayName("Make order without authorization and with wrong ingredients returns status code 500")
    @Description("We don't log in and make order with not valid ingredients and expect to receive the status code 500 and an error message in the response body")
    @Test
    public void makeOrderWithoutAuthorizationAndWithWrongIngredientsReturnsStatusCode500(){
        accessBearerToken= "";
        MakeOrderPOJO makeOrderPOJO=new MakeOrderPOJO();
        makeOrderPOJO.setIngredients(List.of(StepsOrders.getWrongIngredient()));
        Response responseMakeOrder=StepsOrders.makeOrder(makeOrderPOJO,accessBearerToken);
        StepsOrders.verifyMakeOrderStatusCode500(responseMakeOrder);
    }

    @After
    public void tearDown(){
        StepsUsers.deleteUser(accessBearerToken);
        }
}
