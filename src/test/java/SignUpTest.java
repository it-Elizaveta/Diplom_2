import POJO.SignUpPOJO;
import STEPS.StepsUsers;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SignUpTest {
    private String email;
    private String password;
    private String name;
    private String accessBearerToken="";

    @Before
    public void init(){
        RestAssured.baseURI=BaseURI.BASE_URI;
        email= StepsUsers.getEmail();
        password= StepsUsers.getPassword();
        name= StepsUsers.getName();
        }

    @DisplayName("SignUp with correct data returns status code 200")
    @Description("We enter valid data for user registration and expect to receive the status code 200 and the correct response body")
    @Test
    public void signUpWithCorrectDataReturnsStatusCode200(){
        SignUpPOJO signUpPOJO = new SignUpPOJO(email,password,name);
        Response response= StepsUsers.signUp(signUpPOJO);
        accessBearerToken= StepsUsers.getAccessBearerToken(response);
        StepsUsers.verifySignUpSignInStatusCode200(response,email,name);
    }

    @DisplayName("SignUp existing user returns status code 403")
    @Description("We register an already registered user and expect to receive a 403 status code and an error message in the response body")
    @Test
    public void signUpExistingUserReturnsStatusCode403() {
        SignUpPOJO signUpPOJO = new SignUpPOJO(email, password, name);
        Response responseFirstSignUp = StepsUsers.signUp(signUpPOJO);
        accessBearerToken = StepsUsers.getAccessBearerToken(responseFirstSignUp);
        Response responseSecondSignUp = StepsUsers.signUp(signUpPOJO);
        StepsUsers.verifySignUpStatusCode403(responseSecondSignUp, "User already exists");
        if (responseSecondSignUp.statusCode() == 200) {
            String accessBearerTokenSecondUser = StepsUsers.getAccessBearerToken(responseSecondSignUp);
            StepsUsers.deleteUser(accessBearerTokenSecondUser);
        }
    }

    @DisplayName("SignUp without email returns status code 403")
    @Description("We register a user without an email and expect to receive a 403 status code and an error message in the response body")
    @Test
    public void signUpWithoutEmailReturnsStatusCode403(){
        SignUpPOJO signUpPOJO = new SignUpPOJO("",password,name);
        Response response= StepsUsers.signUp(signUpPOJO);
        StepsUsers.verifySignUpStatusCode403(response,"Email, password and name are required fields");
        if (response.statusCode() == 200) {
            accessBearerToken = StepsUsers.getAccessBearerToken(response);
        }
    }

    @DisplayName("SignUp without password returns status code 403")
    @Description("We register a user without password and expect to receive a 403 status code and an error message in the response body")
    @Test
    public void signUpWithoutPasswordReturnsStatusCode403(){
        SignUpPOJO signUpPOJO = new SignUpPOJO(email,"",name);
        Response response= StepsUsers.signUp(signUpPOJO);
        StepsUsers.verifySignUpStatusCode403(response,"Email, password and name are required fields");
        if (response.statusCode() == 200) {
            accessBearerToken = StepsUsers.getAccessBearerToken(response);
        }
    }

    @DisplayName("SignUp without name returns status code 403")
    @Description("We register a user without a name and expect to receive a 403 status code and an error message in the response body")
    @Test
    public void signUpWithoutNameReturnsStatusCode403(){
        SignUpPOJO signUpPOJO=new SignUpPOJO(email,password,"");
        Response response= StepsUsers.signUp(signUpPOJO);
        StepsUsers.verifySignUpStatusCode403(response,"Email, password and name are required fields");
        if (response.statusCode() == 200) {
            accessBearerToken = StepsUsers.getAccessBearerToken(response);
        }
    }

    @After
    public void tearDown(){
        StepsUsers.deleteUser(accessBearerToken);
    }
}
