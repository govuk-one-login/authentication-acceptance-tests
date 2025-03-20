package uk.gov.di.test.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.GetAuthorizersRequest;
import software.amazon.awssdk.services.apigateway.model.GetAuthorizersResponse;
import software.amazon.awssdk.services.apigateway.model.GetMethodRequest;
import software.amazon.awssdk.services.apigateway.model.GetMethodResponse;
import software.amazon.awssdk.services.apigateway.model.GetResourcesRequest;
import software.amazon.awssdk.services.apigateway.model.GetResourcesResponse;
import software.amazon.awssdk.services.apigateway.model.Resource;
import software.amazon.awssdk.services.apigateway.model.TestInvokeAuthorizerRequest;
import software.amazon.awssdk.services.apigateway.model.TestInvokeAuthorizerResponse;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvocationType;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AwsApiGatewayUtils {
    private static final Logger LOG = LogManager.getLogger(AwsApiGatewayUtils.class);
    private final ApiGatewayClient apiGatewayClient;
    private final LambdaClient lambdaClient;
    private static final Region REGION = Region.EU_WEST_2;

    public AwsApiGatewayUtils() {
        this.apiGatewayClient = ApiGatewayClient.builder().region(REGION).build();
        this.lambdaClient = LambdaClient.builder().region(REGION).build();
    }

    public String getResourceId(String restApiId, String endpointPath) {
        LOG.debug("Getting resource details for path: {}", endpointPath);

        GetResourcesRequest request = GetResourcesRequest.builder().restApiId(restApiId).build();
        GetResourcesResponse resources = apiGatewayClient.getResources(request);

        LOG.debug("Available resources: {}", resources.items());

        for (Resource resource : resources.items()) {
            if (resource.path().equals(endpointPath)) {
                String resourceId = resource.id();
                String path = resource.path();
                List<String> methods =
                        resource.resourceMethods() != null
                                ? List.copyOf(resource.resourceMethods().keySet())
                                : List.of();

                LOG.debug("Resource ID: {}, Path: {}, Methods: {}", resourceId, path, methods);
                return resourceId;
            }
        }

        LOG.error("Resource {} not found", endpointPath);
        return null;
    }

    public static String extractLambdaArn(String integrationUri) {
        String regex = "arn:aws:lambda:[^:]+:[^:]+:function:[^:/]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(integrationUri);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public String getLambdaFunctionARN(String restApiId, String resourceId, String httpMethod) {
        LOG.debug("Getting lambda function for resource: {} method: {}", resourceId, httpMethod);

        GetMethodRequest request =
                GetMethodRequest.builder()
                        .restApiId(restApiId)
                        .resourceId(resourceId)
                        .httpMethod(httpMethod)
                        .build();

        GetMethodResponse method = apiGatewayClient.getMethod(request);

        String integrationUri = method.methodIntegration().uri();
        if (integrationUri == null || integrationUri.isEmpty()) {
            LOG.error("Could not find Lambda integration URI");
            return null;
        }

        String lambdaArn = extractLambdaArn(integrationUri);
        LOG.debug("Lambda ARN: {}", lambdaArn);
        return lambdaArn;
    }

    public String getAuthorizerId(String restApiId) {
        LOG.debug("Getting authorizer for API: {}", restApiId);

        GetAuthorizersRequest request =
                GetAuthorizersRequest.builder().restApiId(restApiId).build();
        GetAuthorizersResponse authorizers = apiGatewayClient.getAuthorizers(request);

        if (authorizers.items().isEmpty()) {
            LOG.error("No authorizer found for this API");
            return null;
        }

        String authorizerId = authorizers.items().get(0).id();
        String authorizerName = authorizers.items().get(0).name();
        LOG.debug("Using authorizer: {} (ID: {})", authorizerName, authorizerId);
        return authorizerId;
    }

    public String invokeAuthorizer(String restApiId, String authorizerId, String bearerToken) {
        LOG.debug("Invoking authorizer: {}", authorizerId);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + bearerToken);

        TestInvokeAuthorizerRequest request =
                TestInvokeAuthorizerRequest.builder()
                        .restApiId(restApiId)
                        .authorizerId(authorizerId)
                        .headers(headers)
                        .build();

        TestInvokeAuthorizerResponse result = apiGatewayClient.testInvokeAuthorizer(request);

        String principalId = result.principalId();
        if (principalId == null || principalId.isEmpty()) {
            LOG.error("Could not get principal ID from authorizer");
            return null;
        }

        return principalId;
    }

    public String createApiEvent(
            String endpointPath,
            String endpointMethod,
            String authToken,
            String email,
            String newPassword,
            String principalId) {
        JSONObject body = new JSONObject();
        body.put("email", email);
        body.put("newPassword", newPassword);

        JSONObject authorizer = new JSONObject();
        authorizer.put("principalId", principalId);

        JSONObject requestContext = new JSONObject();
        requestContext.put("path", endpointPath);
        requestContext.put("requestId", "test-invoke-request");
        requestContext.put("authorizer", authorizer);

        JSONObject event = new JSONObject();
        event.put("path", endpointPath);
        event.put("httpMethod", endpointMethod);

        JSONObject headers = new JSONObject();
        headers.put("Authorization", authToken);
        event.put("headers", headers);

        event.put("body", body.toString());
        event.put("requestContext", requestContext);

        return event.toString();
    }

    public String invokeLambda(String functionName, String event) {
        LOG.debug("Invoking lambda function: {}", functionName);

        InvokeRequest request =
                InvokeRequest.builder()
                        .functionName(functionName)
                        .invocationType(InvocationType.REQUEST_RESPONSE)
                        .payload(SdkBytes.fromString(event, StandardCharsets.UTF_8))
                        .build();

        InvokeResponse result = lambdaClient.invoke(request);

        String response = result.payload().asString(StandardCharsets.UTF_8);
        LOG.debug("Lambda response: {}", response);
        return response;
    }

    public String testApiGatewayEndpoint(
            String restApiId,
            String endpointPath,
            String endpointMethod,
            String bearerToken,
            String email,
            String newPassword) {
        // Get resource details
        String resourceId = getResourceId(restApiId, endpointPath);
        if (resourceId == null) {
            return "Resource not found";
        }

        // Get Lambda function name
        String lambdaFunctionARN = getLambdaFunctionARN(restApiId, resourceId, endpointMethod);
        if (lambdaFunctionARN == null) {
            return "Lambda function not found";
        }

        // Get authorizer details
        String authorizerId = getAuthorizerId(restApiId);
        if (authorizerId == null) {
            return "Authorizer not found";
        }

        // Test authorizer
        String principalId = invokeAuthorizer(restApiId, authorizerId, bearerToken);
        if (principalId == null) {
            return "Authorizer invocation failed";
        }

        // Create API Gateway event
        String authToken = "Bearer " + bearerToken;
        String apiEvent =
                createApiEvent(
                        endpointPath, endpointMethod, authToken, email, newPassword, principalId);

        // Invoke Lambda function
        return invokeLambda(lambdaFunctionARN, apiEvent);
    }
}
