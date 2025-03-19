package uk.gov.di.test.services;

import io.restassured.RestAssured;

public class ApiInteractionsService {
    public static void authenticate(String token, String email, String password) {
        var body = """
                {
                   "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, password);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post("https://91ttse4tee.execute-api.eu-west-2.amazonaws.com/dev/authenticate");
    }

    public static void sendOtp(String token, String email, String phoneNumber) {
        var body = """
                {
                "notificationType": "VERIFY_PHONE_NUMBER",
                "email": "%s",
                "phoneNumber": "%s"
                }
                """.formatted(email, phoneNumber);

        RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post("https://91ttse4tee.execute-api.eu-west-2.amazonaws.com/dev/send-otp-notification")
                .then()
                .assertThat()
                .statusCode(204);
    }

}
