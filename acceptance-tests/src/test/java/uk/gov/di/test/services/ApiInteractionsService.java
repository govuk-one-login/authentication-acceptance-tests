package uk.gov.di.test.services;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nimbusds.jose.JOSEException;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.utils.URIUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jose4j.base64url.Base64Url;
import org.openqa.selenium.remote.http.HttpMethod;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
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
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import uk.gov.di.test.step_definitions.World;
import uk.gov.di.test.utils.AuthTokenGenerator;
import uk.gov.di.test.utils.Environment;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ApiInteractionsService {
    private static final Logger LOG = LogManager.getLogger(ApiInteractionsService.class);

    private static final TestConfigurationService TEST_CONFIG_SERVICE =
            TestConfigurationService.getInstance();

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

    public static void authenticateUser(World world) {
        var body =
                """
                        {
                           "email": "%s",
                            "password": "%s"
                        }
                        """
                        .formatted(world.userProfile.getEmail(), world.getUserPassword());

        makeApiCallAndAssertStatusCode(world, body, "/authenticate", HttpMethod.POST, 204);
    }

    public static void sendSmsOtpNotification(World world) {
        String requestBody =
                """
            {
                "notificationType": "VERIFY_PHONE_NUMBER",
                "email": "%s",
                "phoneNumber": "%s"
            }
            """
                        .formatted(world.userProfile.getEmail(), world.getNewPhoneNumber());

        String apiPath = "/send-otp-notification";
        int expectedStatusCode = 204;

        makeApiCallAndAssertStatusCode(
                world, requestBody, apiPath, HttpMethod.POST, expectedStatusCode);
    }

    public static void sendEmailOtpNotification(World world) {
        String requestBody =
                """
            {
                "notificationType": "VERIFY_EMAIL",
                "email": "%s"
            }
            """
                        .formatted(world.getNewEmailAddress());

        String apiPath = "/send-otp-notification";
        int expectedStatusCode = 204;

        makeApiCallAndAssertStatusCode(
                world, requestBody, apiPath, HttpMethod.POST, expectedStatusCode);
    }

    private static Response makeApiCallAndReturnResponse(
            World world,
            String requestBody,
            String apiPath,
            HttpMethod httpMethod,
            Map<String, Object> pathParams) {
        String token = world.getToken();
        if (token == null) {
            throw new RuntimeException("Token is null - ensure user is authenticated first");
        }

        /*
           HOST_ENVIRONMENT indicates where the tests are running from (local machine vs CI/CD pipeline)
           This is different from ENVIRONMENT which indicates the target environment (dev/build/staging)
        */
        String hostEnvironment = System.getenv("HOST_ENVIRONMENT");
        String runningSpecificApiPath;
        RequestSpecification requestSpec;

        if ("local".equals(hostEnvironment)) {
            // Local environment - use RestAssured with Docker
            requestSpec =
                    buildBaseRequestSpecification(
                            "http://host.docker.internal:"
                                    + System.getenv().getOrDefault("API_PROXY_PORT", "8123"),
                            token,
                            requestBody);

            runningSpecificApiPath = apiPath;

        } else {
            // Non-local environment - use VPCE approach
            requestSpec =
                    buildBaseRequestSpecification(
                                    TEST_CONFIG_SERVICE.get("AUTH_INTERNAL_VPCE_URL"),
                                    token,
                                    requestBody)
                            .header(
                                    "Host",
                                    world.getMethodManagementApiId()
                                            + ".execute-api.eu-west-2.amazonaws.com");

            runningSpecificApiPath = "/" + Environment.getOrThrow("ENVIRONMENT") + apiPath;
        }

        if (pathParams != null) {
            requestSpec.pathParams(pathParams);
        }

        return (switch (httpMethod) {
                    case GET -> requestSpec.when().get(runningSpecificApiPath);
                    case POST -> requestSpec.when().post(runningSpecificApiPath);
                    case PUT -> requestSpec.when().put(runningSpecificApiPath);
                    case DELETE -> requestSpec.when().delete(runningSpecificApiPath);
                    default -> throw new IllegalArgumentException(
                            "Unsupported HTTP method: " + httpMethod);
                })
                .then()
                .time(org.hamcrest.Matchers.lessThan(30000L))
                .extract()
                .response();
    }

    private static RequestSpecification buildBaseRequestSpecification(
            String baseUri, String token, String requestBody) {
        return given().baseUri(baseUri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .header("txma-audit-encoded", "encoded-string")
                .header("X-Forwarded-For", "0.0.0.0")
                .body(requestBody);
    }

    public static Response makeApiCall(
            World world, String requestBody, String apiPath, HttpMethod httpMethod) {
        return makeApiCall(world, requestBody, apiPath, httpMethod, null);
    }

    public static Response makeApiCall(
            World world,
            String requestBody,
            String apiPath,
            HttpMethod httpMethod,
            Map<String, Object> pathParams) {
        return makeApiCallAndReturnResponse(world, requestBody, apiPath, httpMethod, pathParams);
    }

    public static void makeApiCallAndAssertStatusCode(
            World world,
            String requestBody,
            String apiPath,
            HttpMethod httpMethod,
            int expectedStatusCode) {
        Response response = makeApiCall(world, requestBody, apiPath, httpMethod);
        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    public static void cannotSendSmsOtpNotification(World world) {
        String requestBody =
                """
            {
                "notificationType": "VERIFY_PHONE_NUMBER",
                "email": "%s",
                "phoneNumber": "%s"
            }
            """
                        .formatted(world.userProfile.getEmail(), world.getNewPhoneNumber());

        String apiPath = "/send-otp-notification";
        int expectedStatusCode = 400;

        makeApiCallAndAssertStatusCode(
                world, requestBody, apiPath, HttpMethod.POST, expectedStatusCode);
    }

    public static String getOtp(String email) {
        var s3client = S3Client.builder().region(Region.of(Region.EU_WEST_2.toString())).build();
        var bucketName = Environment.getOrThrow("ENVIRONMENT") + "-am-api-acceptance-tests-otp";
        var getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(email).build();

        ResponseBytes<GetObjectResponse> response =
                await().atMost(Duration.ofSeconds(10))
                        .pollInterval(Duration.ofSeconds(1))
                        .until(
                                () -> {
                                    try {
                                        return s3client.getObjectAsBytes(getObjectRequest);
                                    } catch (NoSuchKeyException nsk) {
                                        return null;
                                    }
                                },
                                Objects::nonNull);

        var code = response.asUtf8String();

        DeleteObjectRequest deleteObjectRequest =
                DeleteObjectRequest.builder().bucket(bucketName).key(email).build();

        s3client.deleteObject(deleteObjectRequest);

        return code;
    }

    public static void updatePhoneNumber(World world) {
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

        makeApiCallAndAssertStatusCode(
                world, body, "/v1/update-phone-number", HttpMethod.POST, 204);
    }

    public static boolean userHasAuthAppAsDefault(World world) {
        try {
            String mfaMethods = retrieveUsersMFAMethods(world);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(mfaMethods);
            JsonNode bodyArray = objectMapper.readTree(rootNode.path("body").asText());

            if (bodyArray.isArray() && !bodyArray.isEmpty()) {
                JsonNode firstElement = bodyArray.get(0);
                String apriorityIdentifier = firstElement.path("priorityIdentifier").asText();
                if (apriorityIdentifier.equalsIgnoreCase("DEFAULT")) {
                    JsonNode method = firstElement.path("method");
                    if (method.get("mfaMethodType").asText().equalsIgnoreCase("AUTH_APP")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return false;
    }

    public static String checkUserHasBackupMFA(World world) {

        Response response =
                makeApiCallAndReturnResponse(
                        world,
                        """
                {}
                """,
                        "/v1/mfa-methods/{publicSubjectId}",
                        HttpMethod.GET,
                        Map.of("publicSubjectId", world.userProfile.getPublicSubjectID()));

        world.setApiResponse(response);

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error from api gateway: " + response.statusCode());
        }
        return response.asString();
    }

    private static String retrieveUsersMFAMethods(World world) throws JsonProcessingException {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}",
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

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }

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

        return responseBody;
    }

    public static String addBackupSMS(World world) {
        String requestBody =
                """
                {
                    "mfaMethod": {
                        "priorityIdentifier": "BACKUP",
                        "method": {
                            "mfaMethodType": "SMS",
                            "phoneNumber": "%s",
                            "otp": "%s"
                        }
                    }
                }
                """
                        .formatted(world.getNewPhoneNumber(), world.getOtp());

        String apiPath = "/v1/mfa-methods/" + world.userProfile.getPublicSubjectID();
        int expectedStatusCode = 200;

        makeApiCallAndAssertStatusCode(
                world, requestBody, apiPath, HttpMethod.POST, expectedStatusCode);

        world.userCredentials =
                DynamoDbService.getInstance().getUserCredentials(world.userProfile.getEmail());

        // Return a mock response since makeApiCall doesn't return the response body
        return "{\"statusCode\": 200}";
    }

    public static String addBackupSMSInvalidReq(World world) {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}",
                        HttpMethod.POST.toString());

        var body =
                """
                    {
                                mfaMethod: {
                                  priorityIdentifier: "BACKUP",
                                  method: {
                                    mfaMethodType: "SMS",
                                    phoneNumber: "%s"
                                  }
                                }
                              }
               """
                        .formatted(world.getNewPhoneNumber(), world.getOtp());

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

        world.userCredentials =
                DynamoDbService.getInstance().getUserCredentials(world.userProfile.getEmail());

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }

        return invokeResponse.payload().asUtf8String();
    }

    public static String addBackupSMSUserNotFound(World world) {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}",
                        HttpMethod.POST.toString());

        var body =
                """
                    {
                                mfaMethod: {
                                  priorityIdentifier: "BACKUP",
                                  method: {
                                    mfaMethodType: "SMS",
                                    phoneNumber: "%s",
                                    otp: "%s"
                                  }
                                }
                              }
               """
                        .formatted(world.getNewPhoneNumber(), world.getOtp());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", "ABCDFERGH");

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

        world.userCredentials =
                DynamoDbService.getInstance().getUserCredentials(world.userProfile.getEmail());

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }

        return invokeResponse.payload().asUtf8String();
    }

    public static void updateDefaultPhoneNumber(World world) {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}/{mfaIdentifier}",
                        HttpMethod.PUT.toString());

        var body =
                """
                    {
                          "mfaMethod": {
                            "priorityIdentifier": "DEFAULT",
                            "method": {
                              "mfaMethodType": "SMS",
                              "phoneNumber": "%s"
                            }
                          }
                    }
                """
                        .formatted(world.getNewPhoneNumber());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());
        pathParameters.put("mfaIdentifier", "2");

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

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }
    }

    public static String updateDefaultMfaToAuthApp(World world) {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}/{mfaIdentifier}",
                        HttpMethod.PUT.toString());

        var body =
                """
                    {
                          "mfaMethod": {
                            "priorityIdentifier": "DEFAULT",
                            "method": {
                              "mfaMethodType": "AUTH_APP",
                              "credential": "111222333"
                            }
                          }
                    }
                """;

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());
        pathParameters.put("mfaIdentifier", "1");

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

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }

        return invokeResponse.payload().asUtf8String();
    }

    public static String addBackupAuthApp(World world) {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}",
                        HttpMethod.POST.toString());

        var body =
                """
                {
                    "mfaMethod": {
                        "priorityIdentifier": "BACKUP",
                        "method": {
                            "mfaMethodType": "AUTH_APP",
                            "credential": "AAAABBBBCCCCCDDDDD55551111EEEE2222FFFF3333GGGG4444"
                        }
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

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }

        return invokeResponse.payload().asUtf8String();
    }

    public static String switchMFAMethods(World world) {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}/{mfaIdentifier}",
                        HttpMethod.PUT.toString());

        var body =
                """
                    {
                          "mfaMethod": {
                            "priorityIdentifier": "DEFAULT"
                          }
                    }
                """;

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());

        var backupMfa =
                world.userCredentials.getMfaMethods().stream()
                        .filter(method -> "BACKUP".equals(method.getPriority()))
                        .findFirst();

        if (backupMfa.isPresent()) {
            pathParameters.put("mfaIdentifier", backupMfa.get().getMfaIdentifier());
        } else {
            LOG.error("No BACKUP method found.");
            throw new RuntimeException("No BACKUP method found.");
        }

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
        LOG.debug("/Backup Auth is not updated: {}", invokeResponse.payload().asUtf8String());

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }

        return invokeResponse.payload().asUtf8String();
    }

    public static boolean checkUserHasDefaultMfa(World world, String mfaMethodType) {
        var normalizedValue = mfaMethodType.replace(" ", "_");
        try {
            String mfaMethods = retrieveUsersMFAMethods(world);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(mfaMethods);
            JsonNode bodyArray = objectMapper.readTree(rootNode.path("body").asText());

            if (bodyArray.isArray() && !bodyArray.isEmpty() && bodyArray.size() == 1) {
                JsonNode firstElement = bodyArray.get(0);
                String priorityIdentifier = firstElement.path("priorityIdentifier").asText();
                if (priorityIdentifier.equalsIgnoreCase("DEFAULT")) {
                    JsonNode method = firstElement.path("method");
                    if (method.get("mfaMethodType").asText().equalsIgnoreCase(normalizedValue)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        return false;
    }

    public static void updateBackupAuthApp(World world) {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}",
                        HttpMethod.PUT.toString());

        var body =
                """
                {
                    "mfaMethod": {
                        "priorityIdentifier": "BACKUP",
                        "method": {
                            "mfaMethodType": "AUTH_APP",
                            "credential": "%s"
                        }
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

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }
    }

    public static String backupSMSMFAAdded(World world) {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}",
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

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }
        return invokeResponse.payload().asUtf8String();
    }

    public static String backupAuthMFAAdded(World world) {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}",
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

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }

        world.userCredentials =
                DynamoDbService.getInstance().getUserCredentials(world.userProfile.getEmail());

        return invokeResponse.payload().asUtf8String();
    }

    public static String deleteBackupMFA(World world) {
        var functionName =
                getLambda(
                        world.getMethodManagementApiId(),
                        "/v1/mfa-methods/{publicSubjectId}/{mfaIdentifier}",
                        HttpMethod.DELETE.toString());

        Map<String, String> pathParameters = new HashMap<>();
        pathParameters.put("publicSubjectId", world.userProfile.getPublicSubjectID());
        pathParameters.put("mfaIdentifier", world.userProfile.getmfaIdentifier());

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

        if (invokeResponse.statusCode() != 200) {
            LOG.error("Error from lambda {}.", invokeResponse.statusCode());
            throw new RuntimeException("Error from lambda: " + invokeResponse.statusCode());
        }

        return invokeResponse.payload().asUtf8String();
    }

    public static void authorizeUser(World world) {
        String sectorHost;

        try {
            sectorHost =
                    URIUtils.extractHost(new URI(TEST_CONFIG_SERVICE.get("INTERNAL_SECTOR_URI")))
                            .getHostName();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        var commonInternalSubjectId =
                calculatePairwiseIdentifier(
                        world.userProfile.getSubjectID(), sectorHost, world.userProfile.getSalt());

        try {
            var token = AuthTokenGenerator.createJwt(commonInternalSubjectId);
            world.setToken(token);
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }

        var env = Environment.getOrThrow("ENVIRONMENT");
        var restApiName = "%s-di-account-management-api-method-management".formatted(env);

        String restApiId = getRestApiIdByName(restApiName);

        var authorizerContext =
                executeAuthorizerToObtainAuthorizerContext(restApiId, world.getToken());

        var authorizerContextAsMap = new HashMap<String, Object>();
        authorizerContext
                .entrySet()
                .forEach(entry -> authorizerContextAsMap.put(entry.getKey(), entry.getValue()));

        if (authorizerContext.has("context") && authorizerContext.get("context").isJsonObject()) {
            authorizerContextAsMap.put(
                    "clientId",
                    authorizerContext
                            .get("context")
                            .getAsJsonObject()
                            .get("clientId")
                            .getAsString());
        } else {
            LOG.error("Authorizer failed - no context returned: {}", authorizerContext);
            throw new RuntimeException(
                    "Authorizer validation failed: "
                            + authorizerContext.get("errorMessage").getAsString());
        }

        world.setAuthorizerContent(authorizerContextAsMap);
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

        var authorizerResponseAsJson = invokeResponse.payload().asUtf8String();
        return gson.fromJson(authorizerResponseAsJson, JsonObject.class);
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
