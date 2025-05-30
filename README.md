# GOV.UK One Login Acceptance Tests

A comprehensive automated testing suite for validating the GOV.UK One Login authentication and account management functionality through both UI and API acceptance tests using Cucumber.

The test suite consists of two main types of tests:
- UI Tests: Browser-based end-to-end tests using Selenium WebDriver to validate user journeys through the web interface
- API Tests: Direct validation of account management functionality by interacting with AWS services that back the private APIs

The API tests work around the limitation of not being able to directly call the private APIs by interacting with the underlying AWS services (DynamoDB, Lambda, etc.) through the AWS SDK. This approach allows testing of account management features like MFA method updates and user profile changes by manipulating the same backend services that the APIs use.

Key features include:
- End-to-end testing of user registration and authentication flows
- Multi-factor authentication testing via both UI and API (SMS and authenticator app)
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

## Usage Instructions

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

### Quick Start

The preferred way to run the tests is using the rundocker.sh script. Due to the two-account structure per environment, `UI` and `API` tests must be run separately against their respective accounts:

For UI Tests (run against the -sp accounts):
```bash
# Run UI tests against dev environment (dev-sp account)
./rundocker.sh dev-sp
# Run UI tests against build environment (build-sp account)
./rundocker.sh build-sp
# Run UI tests against staging environment (staging-sp account)
./rundocker.sh staging-sp
```

For API Tests (run against the core accounts):
```bash
# Run API tests against dev environment (dev account)
./rundocker.sh dev
# Run API tests against build environment (build account)
./rundocker.sh build
# Run API tests against staging environment (staging account)
./rundocker.sh staging
```

The script supports running tests with either Chrome or Firefox browsers by specifying the browser as a second argument (e.g., `./rundocker.sh dev chrome`).

Test reports can be found in the tmp folder within the project.  Note: this folder can fill up quickly so regular purging is recommended.

### AWS Account Structure

The deployment infrastructure uses separate AWS accounts for API and UI components in each environment:

- Development Environment:
    - API deployment: dev account
    - UI deployment: dev-sp account

- Build Environment:
    - API deployment: build account
    - UI deployment: build-sp account

- Staging Environment:
    - API deployment: staging account
    - UI deployment: staging-sp account

The CUCUMBER_FILTER_TAGS define in each environment broadly focus on features tagged `@UI` or `@API`.

### Environment Variables

The acceptance tests require various environment variables that are stored in AWS Systems Manager (SSM) Parameter Store.
These variables are downloaded when running the tests:

1. For the API tests `rundocker.sh` executes `run-acceptance-test.sh` with the `-e` option which executes the `fetch_enthat downloads the parameters from SSM.
2. For the UI tests the `fetch_envars.sh` script is run when building the docker image

The script retrieves all parameters from the "/acceptance-tests/${ENVIRONMENT}" path in SSM Parameter Store and exports them as environment variables. This ensures consistent configuration across different environments and secure storage of sensitive values.

#### Over-riding Environment Variables

You can override any environment variable downloaded from parameter store using a local override file:

1. api-env-override
2. ui-env-override

For example if you want to run a sub-set of tests:

```shell
CUCUMBER_FILTER_TAGS="@MY_TAG"
```

Or if you want to run with additonal con-currency:

```shell
PARALLEL_BROWSERS=2
```

Note.  For the UI tests be cautious when overriding PARALLEL_BROWSERS as the number to use is already calculated dynamically in the scripts.

## Deployment

The acceptance tests are deployed using GitHub Actions workflows. To deploy the tests to the development environment:

1. Use the "Build and Push Acceptance Tests to DEV" workflow to deploy them to the API environment
2. Use the "Build and Push Acceptance Tests to DEV" workflow to deploy them to the UI environment

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
