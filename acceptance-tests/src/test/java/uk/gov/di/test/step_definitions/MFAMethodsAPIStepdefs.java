package uk.gov.di.test.step_definitions;

import com.nimbusds.jose.JOSEException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.json.JSONObject;
import uk.gov.di.test.services.TestConfigurationService;
import uk.gov.di.test.services.UserLifecycleService;
import uk.gov.di.test.utils.AuthTokenGenerator;
import uk.gov.di.test.utils.AwsApiGatewayUtils;
import uk.gov.di.test.utils.Crypto;

import java.text.ParseException;

import static uk.gov.di.test.services.ApiInteractionsService.authenticate;
import static uk.gov.di.test.services.ApiInteractionsService.sendOtp;
import static uk.gov.di.test.services.UserLifecycleService.generateValidPassword;

public class MFAMethodsAPIStepdefs {
    private final World world;
    private String apiGatewayResponse;
    protected static final TestConfigurationService TEST_CONFIG_SERVICE =
            TestConfigurationService.getInstance();

    private static final UserLifecycleService userLifecycleService =
            UserLifecycleService.getInstance();

    private static final AwsApiGatewayUtils apiGatewayUtils = new AwsApiGatewayUtils();

    public MFAMethodsAPIStepdefs(World world) {
        this.world = world;
    }

    @When("a retrieve request is made to the API")
    public void aRetrieveRequestIsMadeToTheAPI() throws ParseException, JOSEException {
        // calculate internal common subject id
        var commonInternalSubjectId =
                calculatePairwiseIdentifier(
                        world.userProfile.getSubjectID(),
                        TEST_CONFIG_SERVICE.get("SECTOR_HOST"),
                        world.userProfile.getSalt());

        // create token
        var token = AuthTokenGenerator.createJwt(commonInternalSubjectId);

        authenticate(token, world.userProfile.getEmail(), world.getUserPassword());
        sendOtp(token, world.userProfile.getEmail(), world.userProfile.getPhoneNumber());
        updatePhoneNumber(token);
    }

    @When("I invoke the {string} API Gateway endpoint with path {string} using method {string}")
    public void invokeApiGatewayEndpoint(String restApiId, String endpointPath, String httpMethod)
            throws ParseException, JOSEException {
        // calculate internal common subject id
        var commonInternalSubjectId =
                calculatePairwiseIdentifier(
                        world.userProfile.getSubjectID(),
                        TEST_CONFIG_SERVICE.get("SECTOR_HOST"),
                        world.userProfile.getSalt());

        // create token
        var token = AuthTokenGenerator.createJwt(commonInternalSubjectId);

        // Invoke the API Gateway endpoint using AWS SDK
        world.apiGatewayResponse =
                apiGatewayUtils.testApiGatewayEndpoint(
                        restApiId,
                        endpointPath,
                        httpMethod,
                        token,
                        world.userProfile.getEmail(),
                        generateValidPassword());

        System.out.println("API Gateway response: " + apiGatewayResponse);
    }

    @Then("the API Gateway status should be {int}")
    public void verifyApiGatewayStatus(int expectedStatus) {
        if (world.apiGatewayResponse == null) {
            throw new AssertionError("No API Gateway response was received");
        }

        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(world.apiGatewayResponse);
        } catch (Exception e) {
            throw new AssertionError(
                    "Expected response to be JSON but got: " + world.apiGatewayResponse);
        }

        int responseStatus =
                jsonResponse.has("statusCode") ? jsonResponse.getInt("statusCode") : -1;
        if (responseStatus != expectedStatus) {
            throw new AssertionError(
                    "Expected status code to be " + expectedStatus + " but got: " + responseStatus);
        }
    }

    @Then("the API Gateway response should contain {string}")
    public void verifyApiGatewayResponse(String expectedContent) {
        if (apiGatewayResponse == null) {
            throw new AssertionError("No API Gateway response was received");
        }

        JSONObject jsonResponse;
        try {
            jsonResponse = new JSONObject(apiGatewayResponse);
        } catch (Exception e) {
            // If not valid JSON, check the raw response
            if (!apiGatewayResponse.contains(expectedContent)) {
                throw new AssertionError(
                        "Expected response to contain '"
                                + expectedContent
                                + "' but got: "
                                + apiGatewayResponse);
            }
            return;
        }

        String responseBody =
                jsonResponse.has("body")
                        ?
                        // If body is JSON string, try to parse it
                        (jsonResponse.get("body") instanceof String
                                ? jsonResponse.getString("body")
                                : jsonResponse.get("body").toString())
                        : apiGatewayResponse;

        if (!responseBody.contains(expectedContent)) {
            throw new AssertionError(
                    "Expected response to contain '"
                            + expectedContent
                            + "' but got: "
                            + responseBody);
        }
    }

    private void updatePhoneNumber(String token) {
        String body;
        io.restassured.response.ValidatableResponse response;

        body =
                """
                        {
                        "email": "%s",
                        "phoneNumber": "%s",
                        "otp": "111111"
                        }
                        """
                        .formatted(world.userProfile.getEmail(), "07700900000");

        System.out.println("/update-phone-number request");

        response =
                RestAssured.given()
                        .header("Authorization", "Bearer " + token)
                        .body(body)
                        .when()
                        .post(
                                "https://91ttse4tee.execute-api.eu-west-2.amazonaws.com/dev/update-phone-number")
                        .then()
                        .assertThat()
                        .statusCode(400);

        System.out.println(
                "/update-phone-number response is: " + response.extract().asPrettyString());
    }

    private static String calculatePairwiseIdentifier(
            String subjectID, String sectorHost, byte[] salt) {

        var sb = Crypto.generatePairwiseIdDigest(sectorHost, subjectID, salt);

        return "urn:fdc:gov.uk:2022:" + sb;
    }
}
