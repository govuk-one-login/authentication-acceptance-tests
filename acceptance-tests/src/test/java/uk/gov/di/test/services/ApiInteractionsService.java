package uk.gov.di.test.services;

import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ApiInteractionsService {
    private static final Logger LOG = LogManager.getLogger(ApiInteractionsService.class);

    public static void authenticate(String token, String email, String password) {
        var body =
                """
                        {
                           "email": "%s",
                            "password": "%s"
                        }
                        """
                        .formatted(email, password);

        LOG.debug("/authenticate requested");

        var response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post("https://91ttse4tee.execute-api.eu-west-2.amazonaws.com/dev/authenticate");

        LOG.debug("/authenticate response: {}", response.asPrettyString());
    }

    public static void sendOtp(String token, String email, String phoneNumber) {
        var body = """
                {
                "notificationType": "VERIFY_PHONE_NUMBER",
                "email": "%s",
                "phoneNumber": "%s"
                }
                """
                .formatted(email, phoneNumber);

        LOG.debug("/send-otp-notification requested");

        var response = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post(
                        "https://91ttse4tee.execute-api.eu-west-2.amazonaws.com/dev/send-otp-notification");

        LOG.debug("/send-otp-notification response: {}", response.asPrettyString());
    }
}
