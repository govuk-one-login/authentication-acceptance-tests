package uk.gov.di.test.step_definitions;

import io.cucumber.java.en.When;
import io.restassured.RestAssured;

public class MFAMethodsAPIStepdefs {
    @When("a retrieve request is made to the API")
    public void aRetrieveRequestIsMadeToTheAPI() {
        System.out.println("Request is made to the API");
        var response =
                RestAssured.get("http://localhost:8080/v1/mfa-methods/two-mfa")
                        .then()
                        .assertThat()
                        .statusCode(200);
        System.out.println("Response is: " + response.extract().asString());
    }
}
