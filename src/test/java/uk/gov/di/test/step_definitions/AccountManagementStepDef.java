package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.When;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.pages.StubUserInfoPage;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static uk.gov.di.test.utils.Constants.NON_EXISTENT_EMAIL;

public class AccountManagementStepDef extends BasePage {
    StubUserInfoPage stubUserInfoPage = StubUserInfoPage.getStubUserInfoPage();

    @When(
            "the Account Management Update Password API is called with an email address that does not exist, an error is returned")
    public void theUserIsReturnedToTheService() {
        JSONObject obj = new JSONObject(stubUserInfoPage.getAccessToken());
        String bearerJWT = obj.getString("access_token");
        String bearerToken = "Bearer ".concat(bearerJWT);

        String body =
                "{\n    \"email\":\""
                        + NON_EXISTENT_EMAIL
                        + "\",\n"
                        + "    \"newPassword\":\"testPassword123!\"\n"
                        + "}";
        given().headers(Map.of("Authorization", bearerToken))
                .body(body)
                .when()
                .post(TEST_CONFIG_SERVICE.get("INTERNAL_AM_API").concat("/update-password"))
                .then()
                .statusCode(400)
                .body("code", Matchers.is(1010))
                .body("message", Matchers.is("An account with this email address does not exist"));
    }
}
