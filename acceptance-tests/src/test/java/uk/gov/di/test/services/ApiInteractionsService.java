package uk.gov.di.test.services;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import uk.gov.di.test.utils.Crypto;
import uk.gov.di.test.utils.Environment;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiInteractionsService {
    protected static final TestConfigurationService TEST_CONFIG_SERVICE =
            TestConfigurationService.getInstance();

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

        var functionName = getLambda(world.getMethodManagementApiId(), "/authenticate");

        LOG.debug("Testing /authenticate integration function: {}", functionName);

        var body =
                """
                        {
                           "email": "%s",
                            "password": "%s"
                        }
                        """
                        .formatted(world.userProfile.getEmail(), world.getUserPassword());

        var event = createApiGatewayProxyRequestEvent(body, world.getAuthorizerContent());

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

        var functionName = getLambda(world.getMethodManagementApiId(), "/send-otp-notification");

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

        var event = createApiGatewayProxyRequestEvent(body, world.getAuthorizerContent());

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

        var functionName = getLambda(world.getMethodManagementApiId(), "/update-phone-number");

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

        var event = createApiGatewayProxyRequestEvent(body, world.getAuthorizerContent());

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

    public static void authorizeApiGatewayUse(World world) throws ParseException, JOSEException {
        // calculate internal common subject id
        var commonInternalSubjectId =
                Crypto.calculatePairwiseIdentifier(
                        world.userProfile.getSubjectID(),
                        TEST_CONFIG_SERVICE.get("SECTOR_HOST"),
                        world.userProfile.getSalt());

        var token = AuthTokenGenerator.createJwt(commonInternalSubjectId);

        world.setToken(token);

        var env = Environment.getOrThrow("ENVIRONMENT");
        var restApiName = "%s-di-account-management-api-method-management".formatted(env);

        String restApiId = getRestApiIdByName(restApiName);

        LOG.debug("testing restApiId: {}", restApiId);

        String authorizerLambdaArn = getAuthorizerLambdaARN(restApiId);

        Pattern accountPattern = Pattern.compile("arn:aws:lambda:[^:]+:([0-9]+):function:");
        Matcher accountMatcher = accountPattern.matcher(authorizerLambdaArn);
        String accountId = accountMatcher.find() ? accountMatcher.group(1) : null;

        String authorizerMethodArn =
                "arn:aws:execute-api:eu-west-2:%s:%s/%s/*/*"
                        .formatted(accountId, restApiId, Environment.getOrThrow("ENVIRONMENT"));

        var context =
                executeAuthorizerToObtainAuthorizerContext(
                        authorizerLambdaArn, authorizerMethodArn, token);

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

    public static String getAuthorizerLambdaARN(String restApiId) {
        GetAuthorizersRequest getAuthorizersRequest =
                GetAuthorizersRequest.builder().restApiId(restApiId).build();

        GetAuthorizersResponse getAuthorizerResponse =
                apiGatewayClient.getAuthorizers(getAuthorizersRequest);

        Optional<String> authorizerLambdaARN =
                getAuthorizerResponse.items().stream()
                        .findFirst()
                        .map(Authorizer::authorizerUri)
                        .flatMap(ApiInteractionsService::extractLambdaArnFromIntegrationUri);

        if (authorizerLambdaARN.isEmpty()) {
            LOG.error("Could not determine authorizer lambda name");
            throw new RuntimeException("Could not determine authorizer lambda name");
        }

        return authorizerLambdaARN.get();
    }

    public static Optional<String> extractLambdaArnFromIntegrationUri(String uri) {
        Pattern pattern =
                Pattern.compile("arn:aws:lambda:[^:]+:[0-9]+:function:([^/]+)/invocations");
        Matcher matcher = pattern.matcher(uri);
        return matcher.find() ? Optional.of(matcher.group(1)) : Optional.empty();
    }

    public static JsonObject executeAuthorizerToObtainAuthorizerContext(
            String authorizerLambdaArn, String authorizerMethodArn, String token) {
        JsonObject authorizerEvent = new JsonObject();
        authorizerEvent.addProperty("type", "TOKEN");
        authorizerEvent.addProperty("authorizationToken", "Bearer " + token);
        authorizerEvent.addProperty("methodArn", authorizerMethodArn);

        Gson gson = new Gson();

        InvokeRequest invokeRequest =
                InvokeRequest.builder()
                        .functionName(authorizerLambdaArn)
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

    private static String getLambda(String restApiId, String apiPath) {
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
                        .httpMethod("POST")
                        .build();

        apiGatewayClient.getMethod(getMethodRequest);

        GetIntegrationRequest getIntegrationRequest =
                GetIntegrationRequest.builder()
                        .restApiId(restApiId)
                        .resourceId(resourceId)
                        .httpMethod("POST")
                        .build();

        GetIntegrationResponse getIntegrationResponse =
                apiGatewayClient.getIntegration(getIntegrationRequest);

        if ("AWS".equals(getIntegrationResponse.typeAsString())
                || "AWS_PROXY".equals(getIntegrationResponse.typeAsString())) {

            Optional<String> uri = extractLambdaArnFromIntegrationUri(getIntegrationResponse.uri());

            if (uri.isEmpty()) {
                LOG.error("Could not find AWS Lambda function");
            }
            return uri.orElse("");
        }
        return "";
    }

    private static String createApiGatewayProxyRequestEvent(
            String body, Map<String, Object> authorizerContent) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("txma-audit-encoded", "encoded-string");
        headers.put("X-Forwarded-For", "0.0.0.0");

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setHeaders(headers);
        event.setBody(body);

        APIGatewayProxyRequestEvent.ProxyRequestContext proxyRequestContext =
                new APIGatewayProxyRequestEvent.ProxyRequestContext();
        proxyRequestContext.setAuthorizer(authorizerContent);
        event.setRequestContext(proxyRequestContext);
        return new Gson().toJson(event);
    }
}
