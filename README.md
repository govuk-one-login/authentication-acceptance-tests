# di-authentication-acceptance-tests.

This repo contains automated browser based [Cucumber](https://cucumber.io/) tests for [GOV.UK Sign In](https://auth-tech-docs.london.cloudapps.digital/).

 The acceptance tests runs against the build environment in the di-authentication-deployment pipeline. The [acceptance-tests](https://cd.gds-reliability.engineering/teams/verify/pipelines/di-authentication-deployment/jobs/acceptance-tests) job must pass before any code is deployed to the production and integration environments. They can also be run locally in order to build and make changes to the test suite.

## How to run the tests

The tests can be run either in debug mode, where the tests will pause to accept entry of one-time password (OTP) codes before continuing, or in fully automated mode, where a test client is used to handle OTP.  The tests run in fully automated mode in the build pipeline.



### Test configuration

There are two different ways to retrieve configuration environment variables to run the tests:

1. Use the ready-made configuration in AWS SSM Parameter Store
2. Create a local .env file, either by exporting from SSM, or if required by hand.

#### AWS SSM Parameter Store Configuration

This is the default option if running the tests using `./run-acceptance-tests.sh`.  You need to have access to the `digital-identity-dev` AWS account and be on the VPN for it to work.  You do not need to create a local .env file to use this option.

The configuration provided will connect to the build environment.

#### Local .env Configuration

To create a .env file based on the values in AWS SSM Parameter Store:

- Run `./run-acceptance-tests.sh -e`.  This will export a .env file with a timestamp.
- Rename this file to `.env` before use.
- Run `./run-acceptance-tests.sh -l` to make use of the local .env file.

You do not need to be on the VPN to run the tests using an .env file.

### Prerequisites

1. Clone the repo
2. Install Java 16+
3. Install Docker Desktop
4. Choose your configuration method as described in the previous section.

### Clean up

The `./run-acceptance-tests.sh` script will clean up the test data state after a test run by calling `./reset-test-data.sh`.

### Test Clients

A test client is needed to run in fully automated mode.  A test client:

-   Suppresses sending OTP codes and allows usage of known, secret codes
-   Suppresses emails from the [Frontend](https://github.com/alphagov/di-authentication-frontend) application but not from [Account Management](https://github.com/alphagov/di-authentication-account-management)
-   Has an allowlist of known users for whom emails and OTP are suppressed
-   Is only available in test environments
-   Requires database access to setup
-   Cannot be setup using any API

### Debug mode

Using debug mode avoids the need to configure a test client, but means the UI will pause to allow OTP entry.

1.  In `.env` or `./run-acceptance-tests.sh` set:
    - SELENIUM_HEADLESS=false
    - DEBUG_MODE=true
1.  Run: [./run-acceptance-tests.sh](run-acceptance-tests.sh)

Email and SMS OTP notifications will be sent to the email address and phone number configured as long as they have been added to the test [Notify](https://www.notifications.service.gov.uk/) team.

### Fully automated mode

1.  Find a test client in a test environment
1.  Add the test email address to `TestClientEmailAllowlist` in the client-registry database table for the test client
1.  Configure secret email and phone OTP codes for testing
1.  In `.env` or `./run-acceptance-tests.sh` set:
    - SELENIUM_HEADLESS=true
    - DEBUG_MODE=false
1.  Run: [./run-acceptance-tests.sh](run-acceptance-tests.sh)

Email and SMS OTP notifications will be suppressed for [Frontend](https://github.com/alphagov/di-authentication-frontend), but not for [Account Management](https://github.com/alphagov/di-authentication-account-management).

NOTES for now until further implementation (Ability to interact with the DB from the framework) takes place when debugging locally:
- the TermsAndConditions version value for the tc test user will need to be reset to 1.0 in DynamoDB after running the test suite
- the +lock2 test user will need deleting after running the test suite

## Making changes

Cucumber feature files live in the acceptance-tests [resources](acceptance-tests/src/test/resources/uk/gov/di/test/acceptance/) directory.

Java classes backing the tests live in the acceptance-tests java [acceptance](acceptance-tests/src/test/java/uk/gov/di/test/step_definitions/) directory.

If new configuration environment variables or seed test data has been added remember to:

1. Update the configuration under `/acceptance-tests/local` in AWS SSM Parameter Store.
2. Create new test data if required in the [build pipeline](https://github.com/alphagov/di-infrastructure/blob/main/ci/tasks/generate-test-users-seed-data.yml).
3. Pass any new environment variables to the tests for execution in the [build pipeline](https://github.com/alphagov/di-infrastructure/blob/9b1ae3c9a3114790ac758d7ac5cf4a28a470112b/ci/di-deployment-pipeline.yml#L1824).
4. Change the cleanup script `./reset-test-data.sh` to reset any new test data if required.
5. Do the same for the cleanup script in the [build pipeline](https://github.com/alphagov/di-infrastructure/blob/main/ci/tasks/reset-test-data.yml).

## Reporting

Local report:

Report is generated automatically - target/cucumber-report/index.html

## Fail fast

A fail fast mechanism has been added and can be toggled by setting the FAIL_FAST_ENABLED option to true or false as required.
For local runs this variable lives in the .env file.
For a pipeline run this variable can be set/changed in AWS Param Store as required. The default is 'false'.
When tests have been skipped due to the fail fast mechanism being triggered they will show up as PendingExceptions.
