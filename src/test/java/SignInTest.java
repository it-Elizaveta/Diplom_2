import POJO.SignInPOJO;
import POJO.SignUpPOJO;
import STEPS.StepsUsers;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SignInTest {
    private String email;
    private String password;
    private String name;
    private String accessBearerToken;

    @Before
    public void init() {
        RestAssured.baseURI=BaseURI.BASE_URI;
        email= StepsUsers.getEmail();
        password= StepsUsers.getPassword();
        name= StepsUsers.getName();
        SignUpPOJO signUpPOJO=new SignUpPOJO(email,password,name);
        Response responseSignUp= StepsUsers.signUp(signUpPOJO);
        accessBearerToken= StepsUsers.getAccessBearerToken(responseSignUp);
    }

    @DisplayName("LogIn existing user returns status code 200")
    @Description("We logIn existing user and expect to receive the status code 200 and the correct response body")
    @Test
    public void logInExistingUserReturnsStatusCode200(){
        SignInPOJO signInPOJO=new SignInPOJO(email,password);
        Response responseSignIn= StepsUsers.signIn(signInPOJO);
        StepsUsers.verifySignUpSignInStatusCode200(responseSignIn,email,name);
    }

    @DisplayName("LogIn without email returns status code 401")
    @Description("We logIn user without email and expect to receive the status code 401 and an error message in the response body")
    @Test
    public void logInWithoutEmailReturnsStatusCode401() {
        SignInPOJO signInPOJO=new SignInPOJO("",password);
        Response responseSignIn= StepsUsers.signIn(signInPOJO);
        StepsUsers.verifySignInStatusCode401(responseSignIn);
    }

    @DisplayName("LogIn without password returns status code 401")
    @Description("We logIn user without password and expect to receive the status code 401 and an error message in the response body")
    @Test
    public void logInWithoutPasswordReturnsStatusCode401() {
        SignInPOJO signInPOJO=new SignInPOJO(email,"");
        Response responseSignIn= StepsUsers.signIn(signInPOJO);
        StepsUsers.verifySignInStatusCode401(responseSignIn);
    }

    @DisplayName("LogIn with wrong email returns status code 401")
    @Description("We logIn user with wrong email and expect to receive the status code 401 and an error message in the response body")
    @Test
    public void logInWithWrongEmailReturnsStatusCode401() {
        String wrongEmail= StepsUsers.getEmail();
        SignInPOJO signInPOJO=new SignInPOJO(wrongEmail,password);
        Response responseSignIn= StepsUsers.signIn(signInPOJO);
        StepsUsers.verifySignInStatusCode401(responseSignIn);
    }

    @DisplayName("LogIn with wrong password returns status code 401")
    @Description("We logIn user with wrong password and expect to receive the status code 401 and an error message in the response body")
    @Test
    public void logInWithWrongPasswordReturnsStatusCode401() {
        String wrongPassword = StepsUsers.getPassword();
        SignInPOJO signInPOJO=new SignInPOJO(email,wrongPassword);
        Response responseSignIn= StepsUsers.signIn(signInPOJO);
        StepsUsers.verifySignInStatusCode401(responseSignIn);
    }

    @After
    public void tearDown(){
        StepsUsers.deleteUser(accessBearerToken);
    }

}
