package uk.gov.di.test.services;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.nimbusds.jose.shaded.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiInteractionsService {
    private static final Logger LOG = LogManager.getLogger(ApiInteractionsService.class);
    private static final LambdaClient lambdaClient =
            LambdaClient.builder()
                    .region(Region.EU_WEST_2)
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();

    public static void authenticate(
            String email, String password, Map<String, Object> authorizerContent) {
        var body =
                """
                        {
                           "email": "%s",
                            "password": "%s"
                        }
                        """
                        .formatted(email, password);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("txma-audit-encoded", "endoded-string");
        headers.put("X-Forwarded-For", "0.0.0.0");

        LOG.debug("/authenticate requested");

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setHeaders(headers);
        event.setBody(body);

        APIGatewayProxyRequestEvent.ProxyRequestContext proxyRequestContext =
                new APIGatewayProxyRequestEvent.ProxyRequestContext();
        proxyRequestContext.setAuthorizer(authorizerContent);
        event.setRequestContext(proxyRequestContext);

        LOG.debug("payload: {}", event);

        var eventJson = new Gson().toJson(event);

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName("dev-authenticate-lambda")
                        .payload(SdkBytes.fromUtf8String(eventJson))
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug("/authenticate response: {}", invokeResponse.statusCode());

        assertEquals(200, invokeResponse.statusCode());
    }

    public static void sendOtp(String email, String phoneNumber) {
        var body =
                """
                {
                    "notificationType": "VERIFY_PHONE_NUMBER",
                    "email": "%s",
                    "phoneNumber": "%s"
                }
                """
                        .formatted(email, phoneNumber);

        LOG.debug("/send-otp-notification requested");

        APIGatewayProxyRequestEvent event = createApiGatewayProxyRequestEvent(body);

        LOG.debug("payload: {}", event);

        var eventJson = new Gson().toJson(event);

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName("dev-send-otp-notification-lambda")
                        .payload(SdkBytes.fromUtf8String(eventJson))
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug("/send-otp-notification response: {}", invokeResponse.payload().asUtf8String());

        assertEquals(200, invokeResponse.statusCode());
    }

    private static APIGatewayProxyRequestEvent createApiGatewayProxyRequestEvent(String body) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("txma-audit-encoded", "endoded-string");
        headers.put("X-Forwarded-For", "0.0.0.0");

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setHeaders(headers);
        event.setBody(body);

        Map<String, Object> authorizerContent = new HashMap<>();
        authorizerContent.put("clientId", "J3tedNRsfssnsf4STuc2NNIV1C1gdxBB");

        APIGatewayProxyRequestEvent.ProxyRequestContext proxyRequestContext =
                new APIGatewayProxyRequestEvent.ProxyRequestContext();
        proxyRequestContext.setAuthorizer(authorizerContent);
        event.setRequestContext(proxyRequestContext);
        return event;
    }

    public static void updatePhoneNumber(String email, String phoneNumber, String otp) {
        String body;

        body =
                """
                        {
                        "email": "%s",
                        "phoneNumber": "%s",
                        "otp": "%s"
                        }
                        """
                        .formatted(email, phoneNumber, otp);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("txma-audit-encoded", "endoded-string");
        headers.put("X-Forwarded-For", "0.0.0.0");

        System.out.println("/update-phone-number request");

        APIGatewayProxyRequestEvent event = createApiGatewayProxyRequestEvent(body);

        LOG.debug("payload: {}", event);

        var eventJson = new Gson().toJson(event);

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName("dev-update-phone-number-lambda")
                        .payload(SdkBytes.fromUtf8String(eventJson))
                        .build();

        LambdaClient lambdaClient =
                LambdaClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug("/update-phone-number response: {}", invokeResponse.payload().asUtf8String());
    }
}
