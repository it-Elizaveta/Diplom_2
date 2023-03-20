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

public class GetUsersOrdersTest {
    private String email;
    private String password;
    private String name;
    private String accessBearerToken="";

    @Before
    public void init(){
        RestAssured.baseURI=BaseURI.BASE_URI;
        email= StepsUsers.getEmail();
        password=StepsUsers.getPassword();
        name=StepsUsers.getName();
    }

    @DisplayName("Request user's orders with authorization returns status code 200")
    @Description("We are requesting user's orders with authorization and expect to receive the status code 200 and the correct response body")
    @Test
    public void requestUsersOrdersWithAuthorizationReturnsStatusCode200() throws Exception{
        accessBearerToken=StepsUsers.signUpAndSignInAndGetToken(email,password,name);
        MakeOrderPOJO makeOrderPOJO=new MakeOrderPOJO();
        makeOrderPOJO.setIngredients(StepsOrders.getIngredientsList(3));
        StepsOrders.makeOrder(makeOrderPOJO,accessBearerToken);
        Response responseUsersOrders = StepsOrders.getUsersOrders(accessBearerToken);
        StepsOrders.verifyGetAuthorisedUsersOrdersStatusCode200(responseUsersOrders);
    }

    @DisplayName("Request user's orders without authorization returns status code 401")
    @Description("We are requesting user's orders without authorization and expect to receive the status code 401 and an error message in the response body")
    @Test
    public void requestUsersOrdersWithoutAuthorizationReturnsStatusCode401() throws Exception {
        Response responseUsersOrders = StepsOrders.getUsersOrders(accessBearerToken);
        MakeOrderPOJO makeOrderPOJO = new MakeOrderPOJO();
        makeOrderPOJO.setIngredients(StepsOrders.getIngredientsList(3));
        StepsOrders.makeOrder(makeOrderPOJO, accessBearerToken);
        StepsOrders.verifyGetUnauthorisedUsersOrdersStatusCode401(responseUsersOrders);
    }

    @After
    public void tearDown(){
        StepsUsers.deleteUser(accessBearerToken);
    }
}
