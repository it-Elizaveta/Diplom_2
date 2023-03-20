package STEPS;

import POJO.SignInPOJO;
import POJO.SignUpPOJO;
import POJO.UpdateUserDataPOJO;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class StepsUsers {
    private static final Faker faker= new Faker();

    public static String getEmail(){
        return faker.internet().emailAddress();
    }

    public static String getPassword(){
        return faker.internet().password(8,16);
    }

    public static String getName(){
        return faker.name().name();
    }

    @Step ("User registration - POST request to endpoint /api/auth/register")
    public static Response signUp(SignUpPOJO signUpPOJO){
        return given()
                .header("Content-type","application/json")
                .and().body(signUpPOJO)
                .post("/api/auth/register");
    }

    @Step ("User logIn - POST request to endpoint /api/auth/login")
    public static Response signIn(SignInPOJO signInPOJO){
        return given()
                .header("Content-type","application/json")
                .and()
                .body(signInPOJO)
                .post("/api/auth/login");
    }

    @Step ("Getting/extracting a accessToken for further use")
    public static String getAccessBearerToken(Response response){
        String accessBearerToken = response.then().extract().body().path("accessToken");
        return accessBearerToken.substring(7);
    }

    @Step("User registration - POST request to endpoint /api/auth/register /n User logIn - POST request to endpoint /api/auth/login /n Getting/extracting a accessToken for further use")
    public static String signUpAndSignInAndGetToken(String email,String password, String name){
        SignUpPOJO signUpPOJO=new SignUpPOJO(email,password,name);
        Response responseSignUp= StepsUsers.signUp(signUpPOJO);
        SignInPOJO signInPOJO=new SignInPOJO(email,password);
        StepsUsers.signIn(signInPOJO);
        return StepsUsers.getAccessBearerToken(responseSignUp);
    }

    @Step("Update user data - PATCH request to endpoint /api/auth/user")
    public static Response updateUserData(UpdateUserDataPOJO updateUserDataPOJO, String accessBearerToken){
        return given()
                .header("Content-type","application/json")
                .auth().oauth2(accessBearerToken)
                .and()
                .body(updateUserDataPOJO)
                .patch("/api/auth/user");
    }

    @Step ("Checking the user registration or authorization status code (200) and the response body")
    public static void verifySignUpSignInStatusCode200(Response response, String email, String name){
        response.then().statusCode(200).and().assertThat()
                .body("success",equalTo(true))
                .body("user.email",equalTo(email))
                .body("user.name",equalTo(name))
                .body("accessToken",startsWith("Bearer "))
                .body("refreshToken",notNullValue());
    }

    @Step ("Checking the user registration status code (403) and the response body")
    public static void verifySignUpStatusCode403(Response response, String message){
        response.then()
                .statusCode(403)
                .and()
                .assertThat()
                .body("success",equalTo(false))
                .body("message",equalTo(message));
    }

    @Step("Checking the user authorization status code (401) and the response body")
    public static void verifySignInStatusCode401(Response response){
        response.then().statusCode(401)
                .and().assertThat()
                .body("success",equalTo(false))
                .body("message",equalTo("email or password are incorrect"));
    }

    @Step("Checking the status code(200) for updating the data of authorized user and the response body")
    public static void verifyUpdateUserDataStatusCode200(Response response,String email, String name){
        response.then()
                .statusCode(200)
                .and().assertThat()
                .body("success",equalTo(true))
                .body("user.email",equalTo(email))
                .body("user.name", equalTo(name));
    }

    @Step("Checking the status code(401) for updating the data of unauthorized user and the response body")
    public  static void verifyUpdateUserDataStatusCode401(Response response){
        response.then().statusCode(401)
                .and().assertThat()
                .body("success",equalTo(false))
                .body("message",equalTo("You should be authorised"));
    }

    @Step ("Deleting a test user")
    public static void deleteUser(String accessBearerToken){
        given()
                .auth().oauth2(accessBearerToken)
                .delete("/api/auth/user");
    }
}
