package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.When;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import uk.gov.di.test.pages.UserInformationPage;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class AccountManagementStepDef {
    UserInformationPage userInformationPage = new UserInformationPage();

    @When(
            "the test calls the Account Management Update Password API with the access token and validates response")
    public void theUserIsReturnedToTheService() {
        JSONObject obj = new JSONObject(userInformationPage.getAccessToken());
        String bearerJWT = obj.getString("access_token");
        String bearerToken = "Bearer ".concat(bearerJWT);

        String body =
                "{\n    \"email\":\""
                        + System.getenv("NONEXISTENT_USER_ACCOUNT_MANAGEMENT_EMAIL")
                        + "\",\n"
                        + "    \"newPassword\":\"testPassword123!\"\n"
                        + "}";
        given().headers(Map.of("Authorization", bearerToken))
                .body(body)
                .when()
                .post(System.getenv("INTERNAL_AM_API").concat("/update-password"))
                .then()
                .statusCode(400)
                .body("code", Matchers.is(1010))
                .body("message", Matchers.is("An account with this email address does not exist"));
    }
}
