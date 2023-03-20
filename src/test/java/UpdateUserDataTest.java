import POJO.UpdateUserDataPOJO;
import STEPS.StepsUsers;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UpdateUserDataTest {
    private String email;
    private String password;
    private String name;
    private String accessBearerToken;

    @Before
    public void init(){
        RestAssured.baseURI=BaseURI.BASE_URI;
        email= StepsUsers.getEmail();
        password= StepsUsers.getPassword();
        name= StepsUsers.getName();
    }

    @DisplayName("Update email and name for authorized user returns status code 200")
    @Description("We update email and name for authorized user and expect to receive the status code 200 and the correct response body")
    @Test
    public void updateEmailAndNameForAuthorizedUserReturnsStatusCode200(){
        accessBearerToken= StepsUsers.signUpAndSignInAndGetToken(email,password,name);
        String newEmail= StepsUsers.getEmail();
        String newName= StepsUsers.getName();
        UpdateUserDataPOJO updateUserDataPOJO=new UpdateUserDataPOJO(newEmail,newName);
        Response responseUpdate = StepsUsers.updateUserData(updateUserDataPOJO,accessBearerToken);
        StepsUsers.verifyUpdateUserDataStatusCode200(responseUpdate,newEmail,newName);
    }

    @DisplayName("Update email for authorized user returns status code 200")
    @Description("We update email for authorized user and expect to receive the status code 200 and the correct response body")
    @Test
    public void updateEmailForAuthorizedUserReturnsStatusCode200(){
        accessBearerToken= StepsUsers.signUpAndSignInAndGetToken(email,password,name);
        String newEmail= StepsUsers.getEmail();
        UpdateUserDataPOJO updateUserDataPOJO=new UpdateUserDataPOJO(newEmail,name);
        Response responseUpdate = StepsUsers.updateUserData(updateUserDataPOJO,accessBearerToken);
        StepsUsers.verifyUpdateUserDataStatusCode200(responseUpdate,newEmail,name);
    }

    @DisplayName("Update name for authorized user returns status code 200")
    @Description("We update name for authorized user and expect to receive the status code 200 and the correct response body")
    @Test
    public void updateNameForAuthorizedUserReturnsStatusCode200(){
        accessBearerToken= StepsUsers.signUpAndSignInAndGetToken(email,password,name);
        String newName= StepsUsers.getName();
        UpdateUserDataPOJO updateUserDataPOJO=new UpdateUserDataPOJO(email,newName);
        Response responseUpdate = StepsUsers.updateUserData(updateUserDataPOJO,accessBearerToken);
        StepsUsers.verifyUpdateUserDataStatusCode200(responseUpdate,email,newName);
    }

    @DisplayName("Update email and name for unauthorized user returns status code 401")
    @Description("We update email for unauthorized user and expect to receive the status code 401 and an error message in the response body")
    @Test
    public void updateEmailAndNameForUnauthorizedUserReturnsStatusCode401(){
        accessBearerToken="";
        String newEmail= StepsUsers.getEmail();
        String newName= StepsUsers.getName();
        UpdateUserDataPOJO updateUserDataPOJO=new UpdateUserDataPOJO(newEmail,newName);
        Response responseUpdate = StepsUsers.updateUserData(updateUserDataPOJO,accessBearerToken);
        StepsUsers.verifyUpdateUserDataStatusCode401(responseUpdate);
    }

    @After
    public void tearDown(){
        StepsUsers.deleteUser(accessBearerToken);
    }
}
