package uk.gov.di.test.services;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.base64url.Base64Url;
import org.json.JSONException;
import org.openqa.selenium.remote.http.HttpMethod;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.Authorizer;
import software.amazon.awssdk.services.apigateway.model.GetAuthorizersRequest;
import software.amazon.awssdk.services.apigateway.model.GetAuthorizersResponse;
import software.amazon.awssdk.services.apigateway.model.GetIntegrationRequest;
import software.amazon.awssdk.services.apigateway.model.GetIntegrationResponse;
import software.amazon.awssdk.services.apigateway.model.GetMethodRequest;
import software.amazon.awssdk.services.apigateway.model.GetResourcesRequest;
import software.amazon.awssdk.services.apigateway.model.GetResourcesResponse;
import software.amazon.awssdk.services.apigateway.model.GetRestApisResponse;
import software.amazon.awssdk.services.apigateway.model.Resource;
import software.amazon.awssdk.services.apigateway.model.RestApi;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import uk.gov.di.test.step_definitions.World;
import uk.gov.di.test.utils.AuthTokenGenerator;
import uk.gov.di.test.utils.Environment;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ApiInteractionsService {
    private static final Logger LOG = LogManager.getLogger(ApiInteractionsService.class);

    private static final LambdaClient lambdaClient =
            LambdaClient.builder()
                    .region(Region.EU_WEST_2)
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();

    private static final ApiGatewayClient apiGatewayClient =
            ApiGatewayClient.builder()
                    .region(Region.EU_WEST_2)
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();

    public static void authenticate(World world) {

        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/authenticate",
                        HttpMethod.POST.toString());

        LOG.debug("Testing /authenticate integration function: {}", functionName);

        var body =
                """
                        {
                           "email": "%s",
                            "password": "%s"
                        }
                        """
                        .formatted(world.userProfile.getEmail(), world.getUserPassword());

        var event = createApiGatewayProxyRequestEvent(body, null, world.getAuthorizerContent());

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .payload(SdkBytes.fromUtf8String(event))
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug("/authenticate response: {}", invokeResponse);

        assertEquals(200, invokeResponse.statusCode());
    }

    public static void sendOtpNotification(World world) {

        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/send-otp-notification",
                        HttpMethod.POST.toString());

        var body =
                """
                {
                    "notificationType": "VERIFY_PHONE_NUMBER",
                    "email": "%s",
                    "phoneNumber": "%s"
                }
                """
                        .formatted(world.userProfile.getEmail(), world.getNewPhoneNumber());

        LOG.debug("Testing /send-otp-notification integration function: {}", functionName);

        var event = createApiGatewayProxyRequestEvent(body, null, world.getAuthorizerContent());

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .payload(SdkBytes.fromUtf8String(event))
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug("/send-otp-notification response: {}", invokeResponse.payload().asUtf8String());

        assertEquals(200, invokeResponse.statusCode());
    }

    public static void updatePhoneNumber(World world) {

        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/update-phone-number",
                        HttpMethod.POST.toString());

        LOG.debug("Testing /MFA Method: {}", functionName);

        var body =
                """
                        {
                        "email": "%s",
                        "phoneNumber": "%s",
                        "otp": "%s"
                        }
                        """
                        .formatted(
                                world.userProfile.getEmail(),
                                world.getNewPhoneNumber(),
                                world.getOtp());

        var event = createApiGatewayProxyRequestEvent(body, null, world.getAuthorizerContent());

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .payload(SdkBytes.fromUtf8String(event))
                        .build();

        LambdaClient lambdaClient =
                LambdaClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug("/update-phone-number response: {}", invokeResponse.payload().asUtf8String());
    }

    public static void checkUserHasBackupMFA(World world) throws JsonProcessingException {
        String mfaMethods = retrieveUsersMFAMethods(world);
    }

    private static String retrieveUsersMFAMethods(World world) throws JsonProcessingException {
        LOG.debug("Testing /retrieve-users-mfa methods");
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/mfa-methods/{publicSubjectId}",
                        HttpMethod.GET.toString());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());

        var event =
                createApiGatewayProxyRequestEvent(
                        "{}", pathParameters, world.getAuthorizerContent());

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .payload(SdkBytes.fromUtf8String(event))
                        .build();

        LambdaClient lambdaClient =
                LambdaClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
        String responseBody = invokeResponse.payload().asUtf8String();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseBody);
        JsonNode bodyArray = objectMapper.readTree(rootNode.path("body").asText());

        if (bodyArray.isArray() && bodyArray.size() > 0) {
            JsonNode firstElement = bodyArray.get(0);
            String apriorityIdentifier = firstElement.path("priorityIdentifier").asText();
            assertNotEquals("BACKUP", apriorityIdentifier);
        } else {
            System.err.println("Error: 'body' is not a valid array or is empty.");
            return null;
        }
//     catch (Exception Throwable e) {
//        e.printStackTrace();
//        return null;
//    }
//        LOG.debug(
//                "/mfa-methods: handled by: {} response: {}",
//                functionName,
//                invokeResponse.payload().asUtf8String());
//        return invokeResponse.payload().asUtf8String();
        return functionName;
    }

    public static void addBackupPhoneno(World world) {

        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/mfa-methods/{publicSubjectId}",
                        HttpMethod.POST.toString());

        var body =
                """
                {
                    "priorityIdentifier": "BACKUP",
                    "method": {
                        "mfaMethodType": "SMS",
                        "credential": "%s"
                    }
                }
                """
                        .formatted(world.getNewPhoneNumber());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());

        var event =
                createApiGatewayProxyRequestEvent(
                        body, pathParameters, world.getAuthorizerContent());

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .payload(SdkBytes.fromUtf8String(event))
                        .build();

        LambdaClient lambdaClient =
                LambdaClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug(
                "/update-backup-phone-number response: {}",
                invokeResponse.payload().asUtf8String());
    }

    public static void updateBackupPhoneno(World world) {

        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/mfa-methods/{publicSubjectId}",
                        HttpMethod.PUT.toString());

        var body =
                """
                {
                    "priorityIdentifier": "BACKUP",
                    "method": {
                        "mfaMethodType": "SMS",
                        "credential": "%s"
                    }
                }
                """
                        .formatted(world.getNewPhoneNumber());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());

        var event =
                createApiGatewayProxyRequestEvent(
                        body, pathParameters, world.getAuthorizerContent());

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .payload(SdkBytes.fromUtf8String(event))
                        .build();

        LambdaClient lambdaClient =
                LambdaClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug(
                "/update-backup-phone-number response: {}",
                invokeResponse.payload().asUtf8String());
    }

    public static void addBackupAuthApp(World world) {

        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/mfa-methods/{publicSubjectId}",
                        HttpMethod.POST.toString());

        var body =
                """
                {
                    "priorityIdentifier": "BACKUP",
                    "method": {
                        "mfaMethodType": "AUTH_APP",
                        "credential": "%s"
                    }
                }
                """
                        .formatted(world.getNewPhoneNumber());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());

        var event =
                createApiGatewayProxyRequestEvent(
                        body, pathParameters, world.getAuthorizerContent());

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .payload(SdkBytes.fromUtf8String(event))
                        .build();

        LambdaClient lambdaClient =
                LambdaClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug(
                "/update-backup-phone-number response: {}",
                invokeResponse.payload().asUtf8String());
    }

    public static void updateBackupAuthApp(World world) {

        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/mfa-methods/{publicSubjectId}",
                        HttpMethod.PUT.toString());

        var body =
                """
                {
                    "priorityIdentifier": "BACKUP",
                    "method": {
                        "mfaMethodType": "AUTH_APP",
                        "credential": "%s"
                    }
                }
                """
                        .formatted(world.getNewPhoneNumber());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());

        var event =
                createApiGatewayProxyRequestEvent(
                        body, pathParameters, world.getAuthorizerContent());

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .payload(SdkBytes.fromUtf8String(event))
                        .build();

        LambdaClient lambdaClient =
                LambdaClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug(
                "/update-backup-phone-number response: {}",
                invokeResponse.payload().asUtf8String());
    }

    public static String backupMFAPhoneAdded(World world) throws JsonProcessingException {
        LOG.debug("Testing /retrieve-updated-mfa methods");
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/mfa-methods/{publicSubjectId}",
                        HttpMethod.GET.toString());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());

        var event =
                createApiGatewayProxyRequestEvent(
                        "{}", pathParameters, world.getAuthorizerContent());

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .payload(SdkBytes.fromUtf8String(event))
                        .build();

        LambdaClient lambdaClient =
                LambdaClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

//        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
//        String responseBody = invokeResponse.payload().asUtf8String();
//        ObjectMapper objectMapper = new ObjectMapper();
//        JsonNode rootNode = objectMapper.readTree(responseBody);
//        JsonNode bodyArray = objectMapper.readTree(rootNode.path("body").asText());
//
//        if (bodyArray.isArray() && bodyArray.size() > 0) {
//            JsonNode firstElement = bodyArray.get(0);
//            JsonNode secondElement = bodyArray.get(1);
//            String firstpriorityIdentifier = firstElement.path("priorityIdentifier").asText();
//            String secondpriorityIdentifier = secondElement.path("priorityIdentifier").asText();
//            assertEquals("DEFAULT", firstpriorityIdentifier);
//            assertEquals("BAACKUP", secondpriorityIdentifier);
//        } else {
//            System.err.println("Error: 'body' is not a valid array or is empty.");
//            return null;
//        }
//     catch (Exception Throwable e) {
//        e.printStackTrace();
//        return null;
//    }
        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
        LOG.debug(
                "/mfa-methods: Updated: {} response: {}",
                functionName,
                invokeResponse.payload().asUtf8String());
        return invokeResponse.payload().asUtf8String();
//        return functionName;
    }

    public static String deleteBackupMFA(World world) {
        LOG.debug("Testing /Delete-users-mfa method");
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/mfa-methods/{publicSubjectId}/{mfaIdentifier}",
                        HttpMethod.DELETE.toString());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());

        var event =
                createApiGatewayProxyRequestEvent(
                        "{}", pathParameters, world.getAuthorizerContent());

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .payload(SdkBytes.fromUtf8String(event))
                        .build();

        LambdaClient lambdaClient =
                LambdaClient.builder()
                        .region(Region.EU_WEST_2)
                        .credentialsProvider(DefaultCredentialsProvider.create())
                        .build();

        InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);

        LOG.debug(
                "/mfa-method: Deleted: {} response: {}",
                functionName,
                invokeResponse.payload().asUtf8String());
        return invokeResponse.payload().asUtf8String();

    }

    public static void authorizeApiGatewayUse(World world) throws ParseException, JOSEException {
        // calculate internal common subject id
        var commonInternalSubjectId =
                calculatePairwiseIdentifier(
                        world.userProfile.getSubjectID(),
                        "https://rp-authdev1.build.stubs.account.gov.uk",
                        world.userProfile.getSalt());

        var token = AuthTokenGenerator.createJwt(commonInternalSubjectId);

        world.setToken(token);

        var env = Environment.getOrThrow("ENVIRONMENT");
        var restApiName = "%s-di-account-management-api-method-management".formatted(env);

        String restApiId = getRestApiIdByName(restApiName);

        LOG.debug("testing restApiId: {}", restApiId);

        var context = executeAuthorizerToObtainAuthorizerContext(restApiId, token);

        var contextAsMap = new HashMap<String, Object>();
        context.entrySet().forEach(entry -> contextAsMap.put(entry.getKey(), entry.getValue()));

        world.setAuthorizerContent(contextAsMap);
        world.setMethodManagementApiId(restApiId);
    }

    public static String getRestApiIdByName(String restApiName) {
        // Find rest api by api name

        String nextToken = null;
        String restApiId = null;

        do {
            String finalNextToken = nextToken;
            GetRestApisResponse getRestApisResponse =
                    apiGatewayClient.getRestApis(
                            builder -> {
                                if (finalNextToken != null) {
                                    builder.position(finalNextToken);
                                }
                            });

            for (RestApi api : getRestApisResponse.items()) {
                if (api.name().equals(restApiName)) {
                    restApiId = api.id();
                    break;
                }
            }

            nextToken = getRestApisResponse.position();
        } while (nextToken != null);
        return restApiId;
    }

    public static JsonObject executeAuthorizerToObtainAuthorizerContext(
            String restApiId, String token) {
        GetAuthorizersRequest getAuthorizersRequest =
                GetAuthorizersRequest.builder().restApiId(restApiId).build();

        GetAuthorizersResponse getAuthorizerResponse =
                apiGatewayClient.getAuthorizers(getAuthorizersRequest);

        JsonObject authorizerEvent = new JsonObject();
        authorizerEvent.addProperty("type", "TOKEN");
        authorizerEvent.addProperty("authorizationToken", "Bearer " + token);
        authorizerEvent.addProperty("methodArn", "arn:aws:execute-api:eu-west-2:*:*/*/*/*");

        Optional<String> lambdaName =
                getAuthorizerResponse.items().stream()
                        .findFirst()
                        .map(Authorizer::authorizerUri)
                        .map(uri -> uri.split(":")[11]);

        if (lambdaName.isEmpty()) {
            LOG.error("Could not determine authorizer lambda name");
            throw new RuntimeException("Could not determine authorizer lambda name");
        }

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

        LOG.debug("/authorizer-response response: {}", invokeResponse.payload().asUtf8String());

        var authorizerResponseAsJson = invokeResponse.payload().asUtf8String();
        var authResponseJSONObject = gson.fromJson(authorizerResponseAsJson, JsonObject.class);

        return authResponseJSONObject.getAsJsonObject("context");
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

    private static String getLambda(String restApiId, String apiPath, String httpMethod) {
        // Find the name of the lambda from the API
        GetResourcesRequest getResourcesRequest =
                GetResourcesRequest.builder().restApiId(restApiId).build();

        GetResourcesResponse getResourcesResponse =
                apiGatewayClient.getResources(getResourcesRequest);

        String resourceId = null;

        for (Resource resource : getResourcesResponse.items()) {
            if (resource.path().equals(apiPath)) {
                resourceId = resource.id();
                break;
            }
        }

        GetMethodRequest getMethodRequest =
                GetMethodRequest.builder()
                        .restApiId(restApiId)
                        .resourceId(resourceId)
                        .httpMethod(httpMethod)
                        .build();

        apiGatewayClient.getMethod(getMethodRequest);

        GetIntegrationRequest getIntegrationRequest =
                GetIntegrationRequest.builder()
                        .restApiId(restApiId)
                        .resourceId(resourceId)
                        .httpMethod(httpMethod)
                        .build();

        GetIntegrationResponse getIntegrationResponse =
                apiGatewayClient.getIntegration(getIntegrationRequest);

        if ("AWS".equals(getIntegrationResponse.typeAsString())
                || "AWS_PROXY".equals(getIntegrationResponse.typeAsString())) {
            String uri = getIntegrationResponse.uri();
            if (uri != null && uri.contains("arn:aws:lambda:")) {
                return uri.split(":")[11];
            } else {
                LOG.error("Could not find AWS Lambda function");
            }
        }
        return "";
    }

    private static String createApiGatewayProxyRequestEvent(
            String body, Map<String, String> pathParams, Map<String, Object> authorizerContent) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("txma-audit-encoded", "encoded-string");
        headers.put("X-Forwarded-For", "0.0.0.0");

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setHeaders(headers);
        event.setPathParameters(pathParams);
        event.setBody(body);
        event.setHttpMethod("GET");

        APIGatewayProxyRequestEvent.ProxyRequestContext proxyRequestContext =
                new APIGatewayProxyRequestEvent.ProxyRequestContext();
        proxyRequestContext.setAuthorizer(authorizerContent);
        event.setRequestContext(proxyRequestContext);
        return new Gson().toJson(event);
    }
}
