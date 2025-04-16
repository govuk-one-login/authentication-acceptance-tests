# Developer Guide: Writing API Acceptance Tests

## 1. Project Structure
The API acceptance tests are built using:
- Java 17
- Cucumber for BDD testing
- AWS SDK for interacting with AWS services
- JUnit for assertions
- Log4j2 for logging

### Key Components:
- `acceptance-tests/src/test/resources/uk/gov/di/test/features/api/` - Feature files
- `acceptance-tests/src/test/java/uk/gov/di/test/services/` - Service classes
- `acceptance-tests/src/test/java/uk/gov/di/test/step_definitions/` - Step definitions

## 2. Writing Feature Files

Example from `mfa-methods.feature`:

```gherkin
@under-development @API
Feature: MFA Method Management API
  Check the MFA Method Management API
  Scenario: Authenticated User successfully Add Backup Phone Number as MFA
    Given a Migrated User exists
    And the User is Authenticated
    And the User has no backup MFA Method
    When the User requests to add a BACKUP MFA Phone Number "07700900112"
    Then the User's BACKUP MFA Phone Number is updated to "07700900112"
```

### Feature File Best Practices

- Capitalize domain terms, e.g.`User` or `Phone Number`.

## 3. Implementing Step Definitions

### Adding new Step Definitions

1. For API tests add to `*ApiStepDefs.java`, e.g. for the method-management API we have `MFAMethodsAPIStepDefs.java`
2. Use Cucumber annotations (`@Given`, `@When`, `@Then`, `@And`)
3. Inject the `World` class for state management

### Step Definition Best Practices

- Use high-level business terminology in method names and parameters
- Keep step definitions focused on business logic rather than technical implementation
- Delegate all technical implementation details to `ApiInteractionsService.java`
- Each step should map to a single business action or verification

Example from `MFAMethodsAPIStepdefs.java`:

```java
@When("the User requests to add a BACKUP MFA Phone Number {string}")
public void theUserRequestsToAddABackupMFAPhoneNumber(String phoneNumber) {
    addBackupSMS(world);
    sendOtp(phoneNumber);
}
```

## 4. Implementing Service Classes

### API Gateway and Lambda Integration

The `ApiInteractionsService` uses the AWS SDK to interact with private APIs through API Gateway. This approach allows testing of private APIs without direct access to their endpoints.

#### Finding the Lambda Function

First, get the API Gateway REST API ID:

```java
String restApiId = getRestApiIdByName("your-api-name");
```

Then, find the resource ID for the specific endpoint:

```java
GetResourcesRequest getResourcesRequest = GetResourcesRequest.builder()
    .restApiId(restApiId)
    .build();
GetResourcesResponse getResourcesResponse = apiGatewayClient.getResources(getResourcesRequest);
String resourceId = null;
for (Resource resource : getResourcesResponse.items()) {
    if (resource.path().equals("/your-endpoint")) {
        resourceId = resource.id();
        break;
    }
}
```

Get the integration details for the HTTP method:

```java
GetIntegrationRequest getIntegrationRequest = GetIntegrationRequest.builder()
    .restApiId(restApiId)
    .resourceId(resourceId)
    .httpMethod("POST")
    .build();
GetIntegrationResponse getIntegrationResponse = apiGatewayClient.getIntegration(getIntegrationRequest);
```

Extract the Lambda function name from the integration URI:

```java
String uri = getIntegrationResponse.uri();
String functionName = uri.split(":")[11]; // The Lambda function name is the 12th part of the ARN
```

#### Making the API Call

Once you have the Lambda function name, you can invoke it directly:

```java
// Create the API Gateway proxy request event
String event = createApiGatewayProxyRequestEvent(
    requestBody,
    pathParameters,
    authorizerContent
);
// Invoke the Lambda function
InvokeRequest invokeRequest = InvokeRequest.builder()
    .functionName(functionName)
    .payload(SdkBytes.fromUtf8String(event))
    .build();
InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
```

#### Complete Example

Here's a complete example of making an API call to a private endpoint:

```java
public static void callPrivateEndpoint(World world, String endpoint, String method, String body) {
    // 1. Get the API Gateway REST API ID
    String restApiId = world.getMethodManagementApiId();
    // 2. Get the Lambda function name for this endpoint
    String functionName = getLambda(restApiId, endpoint, method);
    // 3. Create the API Gateway proxy request event
    Map<String, String> pathParameters = new HashMap<>();
    pathParameters.put("userId", world.userProfile.getPublicSubjectID());
    String event = createApiGatewayProxyRequestEvent(
        body,
        pathParameters,
        world.getAuthorizerContent()
    );
    // 4. Invoke the Lambda function
    InvokeRequest invokeRequest = InvokeRequest.builder()
        .functionName(functionName)
        .payload(SdkBytes.fromUtf8String(event))
        .build();
    InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
    // 5. Process the response
    String responseBody = invokeResponse.payload().asUtf8String();
    // ... process response ...
}
```

## 5. State Management

### Using the World Class

- The `World` class maintains state between steps
- Access it through constructor injection in step definitions
- Store test data and API responses

- Example:

```java
public class YourStepDefs {
    private final World world;
    public YourStepDefs(World world) {
        this.world = world;
    }
}
```

## 6. Authentication and Authorization

### Setting Up Authentication

- Use `authorizeApiGatewayUse` to get API Gateway authorization
- Store tokens in the World class
- Include authorization in API requests

Example:

```java
public static void authorizeApiGatewayUse(World world) throws ParseException, JOSEException {
    var token = AuthTokenGenerator.createJwt(commonInternalSubjectId);
    world.setToken(token);
    // ... setup authorizer context
}
```

## 7. Best Practices

### 1. Tagging
Always tag API test features with `@API` to distinguish them from UI tests
Use additional tags like `@under-development` for features still in development

### 2. Separation of Concerns

**Feature Files**: Use business/user terminology only. Focus on what the user wants to achieve, not how it's implemented.

```gherkin
  @API
  Feature: User Profile Management
    Scenario: Update User's Phone Number
      Given the User is Logged In
      When they request to change their Phone Number to "07700900111"
      Then their Phone Number is updated successfully
```

**Step Definitions**: Keep them simple and focused on business logic. Include explicit assertions here.

```java
  @Then("their Phone Number is updated successfully")
  public void verifyPhoneNumberUpdated() {
      String response = getPhoneNumber(world);
      assertThat(response).isEqualTo(world.getNewPhoneNumber());
  }
```

**ApiInteractionsService**: Handle all technical implementation details here.

```java
  public static String getPhoneNumber(World world) {
      // All AWS SDK, API Gateway, and Lambda interaction details here
      // No business logic or assertions
  }
```

## 8. Common Patterns

### API Request Structure

```java
// 1. Get Lambda function name
var functionName = getLambda(apiId, path, method);
// 2. Create request body
var body = createRequestBody();
// 3. Create API Gateway event
var event = createApiGatewayProxyRequestEvent(body, params, auth);
// 4. Make request
var response = lambdaClient.invoke(request);
// 5. Process response
processResponse(response);
```

### Response Handling

```java
String responseBody = invokeResponse.payload().asUtf8String();
ObjectMapper objectMapper = new ObjectMapper();
JsonNode rootNode = objectMapper.readTree(responseBody);
// The parsed response is available as a JSON object that can be queried and checked as per the tests requirements.
```
