package uk.gov.di.test.step_definitions;

import com.nimbusds.jose.JOSEException;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import org.jose4j.base64url.Base64Url;
import uk.gov.di.test.services.UserLifecycleService;
import uk.gov.di.test.utils.AuthTokenGenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import static uk.gov.di.test.services.ApiInteractionsService.authenticate;
import static uk.gov.di.test.services.ApiInteractionsService.sendOtp;
import static uk.gov.di.test.services.UserLifecycleService.generateValidPassword;

public class MFAMethodsAPIStepdefs {
    private final World world;

    private static final UserLifecycleService userLifecycleService =
            UserLifecycleService.getInstance();

    public MFAMethodsAPIStepdefs(World world) {
        this.world = world;
    }

    @When("a retrieve request is made to the API")
    public void aRetrieveRequestIsMadeToTheAPI() throws ParseException, JOSEException {
        try {
            world.userProfile = userLifecycleService.buildNewUserProfile();
            world.setUserPassword(generateValidPassword());
            userLifecycleService.putUserProfileToDynamodb(world.userProfile);
            world.userCredentials =
                    userLifecycleService.buildNewUserCredentialsAndPutToDynamodb(
                            world.userProfile, world.getUserPassword());

            // calculate internal common subject id
            var commonInternalSubjectId =
                    calculatePairwiseIdentifier(
                            world.userProfile.getSubjectID(),
                            "https://rp-dev.build.stubs.account.gov.uk",
                            world.userProfile.getSalt());

            // create token
            var token = AuthTokenGenerator.createJwt(commonInternalSubjectId);

            authenticate(token, world.userProfile.getEmail(), world.getUserPassword());
            sendOtp(token, world.userProfile.getEmail(), "07700900000");
            updatePhoneNumber(token);
        } finally {
            userLifecycleService.deleteUserProfileFromDynamodb(world.userProfile);
            userLifecycleService.deleteUserCredentialsFromDynamodb(world.userCredentials);
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
