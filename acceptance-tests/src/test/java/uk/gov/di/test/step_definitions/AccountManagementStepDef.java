package uk.gov.di.test.step_definitions;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.base64url.Base64Url;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.Authorizer;
import software.amazon.awssdk.services.apigateway.model.GetAuthorizersRequest;
import software.amazon.awssdk.services.apigateway.model.GetAuthorizersResponse;
import software.amazon.awssdk.services.apigateway.model.GetRestApisRequest;
import software.amazon.awssdk.services.apigateway.model.GetRestApisResponse;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import uk.gov.di.test.pages.BasePage;
import uk.gov.di.test.utils.AuthTokenGenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Optional;

import static uk.gov.di.test.services.ApiInteractionsService.authenticate;
import static uk.gov.di.test.services.ApiInteractionsService.sendOtp;
import static uk.gov.di.test.services.ApiInteractionsService.updatePhoneNumber;

public class AccountManagementStepDef extends BasePage {
    private static final Logger LOG = LogManager.getLogger(AccountManagementStepDef.class);
    private final World world;

    public AccountManagementStepDef(World world) {
        this.world = world;
    }

    @Given("the User is Authenticated")
    public void theUserIsAuthenticated() throws ParseException, JOSEException {
        // calculate internal common subject id
        var commonInternalSubjectId =
                calculatePairwiseIdentifier(
                        world.userProfile.getSubjectID(),
                        "https://rp-dev.build.stubs.account.gov.uk",
                        world.userProfile.getSalt());

        var token = AuthTokenGenerator.createJwt(commonInternalSubjectId);

        ApiGatewayClient apiGatewayClient =
                ApiGatewayClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

        var restApiName = "dev-di-account-management-api-method-management";

        // Find rest api by api name
        GetRestApisRequest getRestApisRequest = GetRestApisRequest.builder().build();
        GetRestApisResponse getRestApisResponse = apiGatewayClient.getRestApis(getRestApisRequest);

        LOG.debug("getRestApisResponse {}", getRestApisResponse);

        String restApiId;

        // TODO: I don't have access to the private API so cannot look up its id by name.
        // TODO: Possible solutions provide it as an env var, can TF lookup this value?
        //        restApiId =
        //                getRestApisResponse.items().stream()
        //                        .filter(api -> api.name().equals(restApiName))
        //                        .findFirst()
        //                        .get()
        //                        .id();

        restApiId = "xvzewdo561";

        LOG.debug("restApiId: {}", restApiId);

        GetAuthorizersRequest getAuthorizersRequest =
                GetAuthorizersRequest.builder().restApiId(restApiId).build();

        GetAuthorizersResponse getAuthorizerResponse =
                apiGatewayClient.getAuthorizers(getAuthorizersRequest);

        LOG.debug("getAuthorizerResponse: {}", getAuthorizerResponse);

        Optional<String> lambdaName =
                getAuthorizerResponse.items().stream()
                        .findFirst()
                        .map(Authorizer::authorizerUri)
                        .map(uri -> uri.split(":")[11]);

        LOG.debug("lambdaName: {}", lambdaName.get());

        JsonObject authorizerEvent = new JsonObject();
        authorizerEvent.addProperty("type", "TOKEN");
        authorizerEvent.addProperty("authorizationToken", "Bearer " + token);
        authorizerEvent.addProperty(
                "methodArn",
                "arn:aws:execute-api:eu-west-2:653994557586:xvzewdo561/dev/POST/authenticate");
        Gson gson = new Gson();

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(lambdaName.get())
                        .payload(SdkBytes.fromUtf8String(gson.toJson(authorizerEvent)))
                        .build();

        LambdaClient lambdaClient =
                LambdaClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug("invokeResponse: {}", invokeResponse.statusCode());
        LOG.debug("Invoked authorizer lambda: {}", invokeResponse.payload().asUtf8String());

        var authorizerResponseAsJson = invokeResponse.payload().asUtf8String();
        var authResponseJSONObject = gson.fromJson(authorizerResponseAsJson, JsonObject.class);

        LOG.debug("authResponseJSONObject: {}", authResponseJSONObject);

        var context = authResponseJSONObject.getAsJsonObject("context");
        LOG.debug("context: {}", context);

        var contextAsMap = new HashMap<String, Object>();
        context.entrySet().forEach(entry -> contextAsMap.put(entry.getKey(), entry.getValue()));

        world.setAuthorizerContent(contextAsMap);

        world.setToken(token);

        authenticate(
                world.userProfile.getEmail(),
                world.getUserPassword(),
                world.getAuthorizerContent());
    }

    @When("the User requests an OTP to change their Phone Number to {string}")
    public void theUserRequestsAnOTP(String newPhoneNumber) {
        world.setNewPhoneNumber(newPhoneNumber);
        sendOtp(world.userProfile.getEmail(), newPhoneNumber);
    }

    @When("the User receives the OTP code")
    public void theUserReceivesTheOTPCode() {
        LOG.warn("The User receives the OTP code is not yet implemented");
    }

    @When("the User submits the OTP code to confirm the Phone Number change")
    public void theUserSubmitsTheOTPCodeToConfirmThePhoneNumberChange() {
        updatePhoneNumber(world.userProfile.getEmail(), world.getNewPhoneNumber(), "1111");
    }

    @Then("the User's Phone Number is updated to {string}")
    public void theUserSPhoneNumberIsUpdatedTo(String arg0) {
        LOG.warn("The Users new Phone Number check is not yet implemented");
    }

    private static String calculatePairwiseIdentifier(
            String subjectID, String sectorHost, byte[] salt) {
        try {
            var md = MessageDigest.getInstance("SHA-256");

            md.update(sectorHost.getBytes(StandardCharsets.UTF_8));
            md.update(subjectID.getBytes(StandardCharsets.UTF_8));

            byte[] bytes = md.digest(salt);

            var sb = Base64Url.encode(bytes);

            return "urn:fdc:gov.uk:2022:" + sb;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
