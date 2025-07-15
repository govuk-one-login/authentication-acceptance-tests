**GOV.UK One Login Acceptance Tests**

A comprehensive automated testing suite for validating the GOV.UK One Login 
authentication and account management functionality through both UI and API 
acceptance tests using Cucumber.

**The test suite consists of two main types of tests:**
- UI Tests: Browser-based end-to-end tests using Selenium WebDriver to 
validate user journeys through the web interface

- API Tests: Direct validation of account management functionality by 
interacting with AWS services that back the private APIs

The API tests work around the limitation of not being able to directly call 
the private APIs by interacting with the underlying AWS services (DynamoDB, Lambda, etc.) 
through the AWS SDK. This approach allows testing of account management features like 
MFA method updates and user profile changes by manipulating the same backend services that the APIs use.

**Key features include:**
End-to-end testing of user registration and authentication flows
Multifactor authentication testing via both UI and API (SMS and authenticator app)
Account recovery and password reset validation
Security features testing (lockouts, interventions)
Multi-browser support (Chrome, Firefox)
Docker-based test execution
AWS integration for test data management
Welsh language support testing

**TODO:**
Add details of running in the authdev environment.
Add details about ad-hoc test runs via the AWS Console
Accessibility testing using axe-core
Test reports cleanup in tmp/ folder

**Repository Structure**
````
├── acceptance-tests/          # Main test implementation directory
│   ├── src/test/
│   │   ├── java/             # Test implementation in Java
│   │   │   └── uk/gov/di/test/
│   │   │       ├── entity/   # Data model classes for DynamoDB entities
│   │   │       ├── pages/    # Page objects for web UI interaction
│   │   │       ├── services/ # Service layer for AWS interactions
│   │   │       └── utils/    # Utility classes and helpers
│   │   └── resources/        # Test resources and feature files
│   └── build.gradle          # Gradle build configuration for tests
├── docker/                   # Docker configuration for test execution
├── nginx/                    # Nginx configuration for routing
└── scripts/                  # Utility scripts for running tests
````

**Quick Start**
Prerequisites - Ensure the following tools are installed and configured:
- IntelliJ IDEA (optional but recommended)Download from https://www.jetbrains.com/idea/
- Recommended plugins: Cucumber for Java, Gherkin
- Java 17 JDKDownload from https://jdk.java.net/17/ and verify with java -version
- Docker and Docker ComposeInstall Docker Desktop from https://www.docker.com/
with docker -v and docker-compose -v
- AWS CLI configuration and set from https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.htmlConfigure 
with appropriate credentials (Dan - Tech Lead can help with credential access)
- Gradle 7.xDownload from https://gradle.org/releases/ or use the included ./gradlew
- Chrome or Firefox browser Install from https://www.google.com/chrome/ or https://www.mozilla.org/firefox/

**Test Setup**
1. Clone the repository:
git clone <repository-url> below:
   gov-uk-one-login-acceptance-tests
   gov-uk-one-login-authentication-api
cd gov-uk-one-login-acceptance-tests

2. Configure AWS credentials:
aws configure
Access Key ID
Default region (eu-west-2)
Output format (json)

**Run The Tests**
The preferred way to run the tests is using the rundocker.sh script. Due to the 
two-account structure per environment, UI and API tests must be run separately against their respective accounts:
The scripts primarily support running the tests in Chromium.
Firefox can also be used by passing a selenium/standalone-* image as a second CLI argument. For example:
./rundocker.sh dev-ui selenium/standalone-firefox:136.0

**Test Reports**
Test reports can be found in the tmp/ folder. Clean this folder regularly to avoid excessive disk usage.

**AWS Environment Account Structure**
Separate AWS accounts are used for API and UI components:

- Dev:
API: di-auth-development
UI: di-authentication-development

- Build:
API: gds-di-development
UI: di-authentication-build

- Staging:
API: di-auth-staging
UI: di-authentication-staging

CUCUMBER_FILTER_TAGS are used in each environment to run specific feature groups (@UI or @API).

**Environmental Configuration**
Tests depend on environment-specific variables from AWS Systems Manager (SSM) Parameter Store.
These follow a consistent naming pattern:

- /acceptance-tests/dev/CUCUMBER_FILTER_TAGS
- /acceptance-tests/build/RP_URL
These parameters must be present as environment variables during execution.

# Running the tests from the command line #

**UI Tests**
- ./rundocker.sh dev-ui
- ./rundocker.sh build-ui
- ./rundocker.sh staging-ui

**API Tests**
- ./rundocker.sh dev-api
- ./rundocker.sh build-api
- ./rundocker.sh staging-api

The Docker scripts retrieve variables from SSM and export them as environment variables. See:
- docker/run-tests-api.sh
- docker/run-tests-ui.sh

**Overriding Environment Variables**
You can override environment variables using local .env files:
- env-override-api.env
- env-override-ui.env
Example override to skip specific tests:
CUCUMBER_FILTER_TAGS="not (@AccountInterventions or @Reauth or @old-mfa-without-ipv)"
Example for additional concurrency: PARALLEL_BROWSERS=2

# Running the tests from IntelliJ #

**Create a run configuration using a .env file to provide environment variables**
See example below:
SELENIUM_URL=http://localhost:4445/wd/hub (Local selenium server firefox = 4444, chrome = 4445  )
SELENIUM_BROWSER=chrome (Browser used to run tests - firefox or chrome )
SELENIUM_LOCAL=true
SELENIUM_HEADLESS=true ( Run Selenium headless. )
USE_SSM=true (Specifically for running from the IDE. Tells the test runner to use SSM if a required environment variable is undefined locally. )
DEBUG_MODE=false ( debug mode waits for user entry on OTP screens so you can enter OTP yourself.)
ACCESSIBILITY_CHECKS=false
FAIL_FAST_ENABLED=false
PARALLEL_BROWSERS=1
CUCUMBER_FILTER_TAGS=@API
AWS_PROFILE=di-auth-development-admin
ENVIRONMENT=dev

**First-Time Setup Tips**
Use chmod +x gradlew if the Gradle wrapper lacks execution permissions.
Pull latest Selenium images regularly:
docker pull selenium/standalone-chrome
docker pull selenium/standalone-firefox
Ensure Docker is running and not blocked by firewall or VPN.
Clear the tmp/ folder periodically to avoid disk space issues.

**Deployment**
Tests are deployed using GitHub Actions:
- Use "Build and Push to API DEV account" to deploy to API
- Use "Build and Push to UI DEV account" to deploy to UI

**The workflow:**
Builds test containers (Chrome and Firefox)
Pushes images to ECR
Tags for the development environment

**Data Flow**

[Test Runner] --> [Selenium WebDriver] --> [Browser] --> [GOV.UK One Login UI]
|                                                         |
v                                                         v
[AWS Services] <------------------------------------------> [Backend APIs]
(DynamoDB, SSM)

**Component Interactions**
Test Runner: Executes Cucumber features
Selenium WebDriver: Drives browser automation
Page Objects: Abstract web interactions
Service Layer: Handles AWS SDK operations
DynamoDB: Stores test user state
SSM: Stores configuration
Nginx: Handles routing and access control
