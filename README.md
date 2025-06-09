# GOV.UK One Login Acceptance Tests

A comprehensive automated testing suite for validating the GOV.UK One Login authentication and account management functionality through both UI and API acceptance tests using Cucumber.

The test suite consists of two main types of tests:
- UI Tests: Browser-based end-to-end tests using Selenium WebDriver to validate user journeys through the web interface
- API Tests: Direct validation of account management functionality by interacting with AWS services that back the private APIs

The API tests work around the limitation of not being able to directly call the private APIs by interacting with the underlying AWS services (DynamoDB, Lambda, etc.) through the AWS SDK. This approach allows testing of account management features like MFA method updates and user profile changes by manipulating the same backend services that the APIs use.

Key features include:
- End-to-end testing of user registration and authentication flows
- Multifactor authentication testing via both UI and API (SMS and authenticator app)
- Account recovery and password reset validation
- Security features testing (lockouts, interventions)
- Accessibility testing using axe-core
- Multi-browser support (Chrome, Firefox)
- Docker-based test execution
- AWS integration for test data management
- Welsh language support testing

TODO:
- Add details of running in the authdev environment.
- Add details about ad-hoc test runs via the AWS Console

## Repository Structure
```
.
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
```

## Quick Start

### Prerequisites
- Java 17 JDK
- Docker and Docker Compose
- AWS CLI configured with appropriate credentials
- Gradle 7.x
- Chrome or Firefox browser

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd gov-uk-one-login-acceptance-tests
```

2. Install dependencies:
```bash
./gradlew build
```

3. Configure AWS credentials:
```bash
aws configure
```
### Run The Tests

The preferred way to run the tests is using the rundocker.sh script. Due to the two-account structure per environment, `UI` and `API`, tests must be run separately against their respective accounts:

For UI Tests:
```bash
# Run UI tests against dev environment
./rundocker.sh dev-ui
```

For API Tests:
```bash
# Run API tests against dev environment
./rundocker.sh dev-api
```

## Developer Guide

### Overview

The scripts support running tests with either Chrome or Firefox browsers by specifying the browser as a second argument (e.g., `./rundocker.sh dev-ui chrome`).

Test reports can be found in the tmp folder within the project.  Note: this folder can fill up quickly so regular purging is recommended.

#### AWS Environment Account Structure

The deployment infrastructure uses separate AWS accounts for API and UI components in each environment:

- Development Environment:
  - API deployment: di-auth-development account
  - UI deployment: di-authentication-development account

- Build Environment:
  - API deployment: gds-di-development account
  - UI deployment: di-authentication-build account

- Staging Environment:
  - API deployment: di-auth-staging account
  - UI deployment: di-authentication-staging account

The CUCUMBER_FILTER_TAGS define in each environment broadly focus on features tagged `@UI` or `@API`.

#### Environmental Configuration

The acceptance tests require various environment specific variables.  These variables are stored in AWS Systems Manager (SSM) Parameter Store.

The names of the parameters follow a standard pattern: `/acceptance-tests/${ENVIRONMENT}/variable_name`.  Examples are:

```shell
/acceptance-tests/dev/CUCUMBER_FILTER_TAGS
/acceptance-tests/build/RP_URL
```

The acceptance tests expect these values to be provided as environment variables in the execution environment.

### Running the tests from the command line

To run the tests from the commandline use the `rundocker.sh` script with a parameter to indicate which
tests to run and in which account.

For UI Tests:
```bash
# Run UI tests against dev environment
./rundocker.sh dev-ui
# Run UI tests against build environment
./rundocker.sh build-ui
# Run UI tests against staging environment
./rundocker.sh staging-ui
```

For API Tests:
```bash
# Run API tests against dev environment
./rundocker.sh dev-api
# Run API tests against build environment
./rundocker.sh build-api
# Run API tests against staging environment
./rundocker.sh staging-api
```

The scripts that execute inside the docker container retrieve all parameters from SSM and export them as environment variables. This ensures consistent configuration across different environments and secure storage of sensitive values.  See the following for details:

```shell
docker/run-tests-api.sh
docker/run-tests-ui.sh
```

Tests can be against either the UI or API account in any of the following environments:

* dev
* build
* staging

#### Overriding Environment Variables

When running locally it can be helpful to limit the number of tests being executed.  This can be achieved
by providing a local file of overridden values.  There is an override file for each type of test:

1. env-override-api.env
2. env-override-ui.env

For example if you want to run a sub-set of tests:

```shell
CUCUMBER_FILTER_TAGS="not (@AccountInterventions or @Reauth or @old-mfa-without-ipv)"
```

Note.  See the [cucumber docs](https://cucumber.io/docs/cucumber/api/#tags) for how tom define multiple filter tags.

Or if you want to run with additional con-currency:

```shell
PARALLEL_BROWSERS=2
```

Note.  For the UI tests be cautious when overriding PARALLEL_BROWSERS as the number to use is already calculated dynamically in the scripts.

### Running the tests from an IDE

IntelliJ is the preferred IDE on the auth team.  It allows individual tests to be run directly from the
editor and it allows the step definitions to be run in the debugger.

In order to run tests in IntelliJ you will need to update the run template to refer to a local .env file.  This
file provides a small number of environment variables to the test execution environment:

| Environment Variable | Example Value                | Purpose                                                                                                                           |
|----------------------|------------------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| SELENIUM_URL         | http://localhost:4445/wd/hub | Local selenium server firefox = 4444, chrome = 4445                                                                               |
| SELENIUM_BROWSER     | chrome                       | Browser used to run tests - firefox or chrome                                                                                     |
| SELENIUM_LOCAL       | true                         |                                                                                                                                   |
| SELENIUM_HEADLESS    | true                         | Run Selenium headless.                                                                                                            |
| USE_SSM              | true                         | Specifically for running from the IDE.  Tells the test runner to use SSM if a required environment variable is undefined locally. |
| DEBUG_MODE           | false                        | debug mode waits for user entry on OTP screens so you can enter OTP yourself.                                                     |
| ACCESSIBILITY_CHECKS | false                        |                                                                                                                                   |
| FAIL_FAST_ENABLED    | false                        |                                                                                                                                   |
| PARALLEL_BROWSERS    | 1                            |                                                                                                                                   |
| CUCUMBER_FILTER_TAGS | "@API"                       |                                                                                                                                   |
| AWS_PROFILE          | di-auth-development-admin    |                                                                                                                                   |
| ENVIRONMENT          | dev                          |                                                                                                                                   |

## Deployment

The acceptance tests are deployed using GitHub Actions workflows. To deploy the tests to the development environment:

1. Use the "Build and Push to API DEV account" workflow to deploy them to the API environment
2. Use the "Build and Push to UI DEV account" workflow to deploy them to the UI environment

The workflow will:
- Build the test containers for both Chrome and Firefox browsers
- Push the images to the development ECR repository
- Tag the images appropriately for use in the development environment

## Data Flow
The acceptance tests interact with multiple components to validate the GOV.UK One Login service functionality.

```ascii
[Test Runner] --> [Selenium WebDriver] --> [Browser] --> [GOV.UK One Login UI]
       |                                                         |
       v                                                         v
[AWS Services] <------------------------------------------> [Backend APIs]
(DynamoDB, SSM)
```

Component Interactions:
- Test Runner executes Cucumber feature files
- Selenium WebDriver controls browser interactions
- Page Objects abstract UI interactions
- Service layer manages AWS resource interactions
- DynamoDB stores test user data and state
- AWS Systems Manager provides environment configuration
- Nginx handles request routing and access control
