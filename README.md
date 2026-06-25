# GOV.UK One Login Acceptance Tests

A comprehensive automated testing suite for validating the GOV.UK One Login
authentication and account management functionality through both UI and API
acceptance tests using Cucumber.

**The test suite consists of two main types of tests:**
- UI Tests: Browser-based end-to-end tests using Selenium WebDriver to
validate user journeys through the web interface. These are generally run in
both the frontend and the internal / external api pipelines.

- Account Management API Tests: Direct validation of account management functionality by
interacting with AWS services that back the private APIs

All tests will need to be tagged as either @UI or @AccountManagementAPI in order to run in an environment.

The API tests work around the limitation of not being able to directly call
the private APIs by interacting with the underlying AWS services (DynamoDB, Lambda, etc.)
through the AWS SDK. This approach allows testing of account management features like
MFA method updates and user profile changes by manipulating the same backend services that the APIs use.

### Key features include:
- End-to-end testing of user registration and authentication flows
- Multifactor authentication testing via both UI and API (SMS and authenticator app)
- Account recovery and password reset validation
- Security features testing (lockouts, interventions)
- Docker-based test execution
- AWS integration for test data management
- Welsh language support testing

### TODO:
- Add details of running in the auth dev environment.
- Add details about ad-hoc test runs via the AWS Console
- Accessibility testing using axe-core

### Quick Start
Prerequisites - Ensure the following tools are installed and configured:
- IntelliJ IDEA (optional but recommended) - Download from https://www.jetbrains.com/idea/
- Recommended plugins: Cucumber for Java, Gherkin
- Java 17 JDK - Download from https://jdk.java.net/17/ and verify with java -version
- Docker and Docker Compose - Install Docker Desktop from https://www.docker.com/
with docker -v and docker-compose -v
- AWS CLI configuration - Download from https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html
Configure with appropriate credentials (Dan - Tech Lead can help with credential access)
- Gradle 8.x - Download from https://gradle.org/releases/ or use the included ./gradlew
- Chrome browser - Install from https://www.google.com/chrome/

### Test Setup
#### Clone the repository:
```` bash
git clone <repository-url>
cd authentication-acceptance-tests
````
#### Configure AWS credentials:
````bash
aws configure
# Access Key ID
# Default region (eu-west-2)
# Output format (json)
````

**Run The Tests**
The preferred way to run the tests is using the rundocker.sh script. Due to the
two-account structure per environment, UI and API tests must be run separately against their respective accounts:
The scripts primarily support running the tests in Chromium.

**Test Reports**
Test reports are automatically generated in the `test-reports/` directory with timestamped folders.
The system automatically keeps only the 10 most recent test runs to prevent excessive disk usage.

**AWS Environment Account Structure**
Separate AWS accounts are used for API and UI components:

- Development:
API: di-auth-development
UI: di-authentication-development

- Build:
API: gds-di-development
UI: di-authentication-build

- Staging:
API: di-auth-staging
UI: di-authentication-staging

CUCUMBER_FILTER_TAGS are used in each environment to run specific feature groups (@UI or @AccountManagementAPI).

#### Environmental Configuration
Tests depend on environment-specific variables from AWS Systems Manager (SSM) Parameter Store.
These follow a consistent naming pattern:

- /acceptance-tests/dev/CUCUMBER_FILTER_TAGS
- /acceptance-tests/build/RP_URL

These parameters must be present as environment variables during execution.

Refer to https://govukverify.atlassian.net/wiki/spaces/LO/pages/6624215042/Acceptance+test+tags for the current values
of these tags. Please update this document if updating tags.

### Running the tests from the command line

**UI Tests**
- ./rundocker.sh dev-ui
- ./rundocker.sh build-ui
- ./rundocker.sh staging-ui

**API Tests**
- ./rundocker.sh dev-api
  - As the account management API is private and in a VPC, a bastion host is used as a proxy to allow access.
  - The proxy session is set up by calling the `api-proxy.sh` script in the authentication-api project.
    Because of this, **the authentication-api repository must be cloned in the parent directory.**
  - `api-proxy.sh` is called when running `rundocker.sh dev-api`, but the user must first be signed into the `di-auth-development`
    account (via AWS SSO) with AWS access environment variables set.
- ./rundocker.sh build-api
- ./rundocker.sh staging-api

**The Docker scripts retrieve variables from SSM and export them as environment variables. See:**
- docker/run-tests-api.sh
- docker/run-tests-ui.sh

**Overriding Environment Variables**
You can override environment variables using local .env files:
- env-override-api.env
- env-override-ui.env

These files are appended to the generated env file after SSM parameters are fetched,
so any variable defined here takes precedence over SSM values. The files are created
automatically if they don't exist and are gitignored.

Example override to hit the account management API:
````
NEW_AM_ENV=true
````
Example override to skip specific tests:
````
CUCUMBER_FILTER_TAGS="not (@AccountInterventions or @Re-auth)"
````
Example for additional concurrency:
````
PARALLEL_BROWSERS=2
````

### Running the tests from IntelliJ

You can run individual tests locally from IntelliJ against the dev environment.
This uses a local browser via Selenium rather than the Dockerised setup.

#### Prerequisites
- AWS CLI configured with SSO profile `di-authentication-development-AdministratorAccessPermission`
- Connected to the VPN

#### Step 1: Log in to AWS via SSO

Run the following commands to log in to your AWS account as an admin in the dev account

````bash
aws sso login --profile di-authentication-development-AdministratorAccessPermission
````

You will need to re-run this when your AWS session expires or SSM parameters change.

#### Step 2: Copy .env.sample into a local .env file

The shared Cucumber run configuration template (`.run/Template Cucumber Java.run.xml`) is already set up to read from `.env`.

#### Step 3: Run a test from IntelliJ

__Note__: If tests fail with authentication errors, run commands in step 1 to refresh credentials.

### Deployment

Tests are deployed using GitHub Actions:
- Use "Build and Push to API DEV account" to deploy to API
- Use "Build and Push to UI DEV account" to deploy to UI

The workflow:
- Builds test containers (Chrome)
- Pushes images to ECR
- Tags for the development environment

### Build System

The project uses Gradle 8 with modern best practices:
- Version catalog (`gradle/libs.versions.toml`) for centralized dependency management
- No root build.gradle (single subproject structure)
- Optimized build performance with caching and parallel execution
- Java 17 compatibility

### Architecture

**Data Flow**

````
[Test Runner] --> [Selenium WebDriver] --> [Browser] --> [GOV.UK One Login UI]
|                                                         |
v                                                         v
[AWS Services] <------------------------------------------> [Backend APIs]
(DynamoDB, SSM)
````

**Component Interactions**
- **Test Runner:** Executes Cucumber features using Gradle 8
- **Selenium WebDriver:** Drives browser automation
- **Page Objects:** Abstract web interactions
- **Service Layer:** Handles AWS SDK operations
- **DynamoDB:** Stores test user state
- **SSM:** Stores configuration
