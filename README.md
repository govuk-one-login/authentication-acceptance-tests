# di-authentication-acceptance-tests

This repo contains automated browser based [Cucumber](https://cucumber.io/) tests for [GOV.UK Sign In](https://auth-tech-docs.london.cloudapps.digital/).

 The acceptance tests runs against the build environment in the di-authentication-deployment pipeline. The [acceptance-tests](https://cd.gds-reliability.engineering/teams/verify/pipelines/di-authentication-deployment/jobs/acceptance-tests) job must pass before any code is deployed to the production and integration environments. They can also be run locally in order to build and make changes to the test suite.

## How to run the tests

The tests can be run either in debug mode, where the tests will pause to accept entry of one-time password (OTP) codes before continuing, or in fully automated mode, where a test client is used to handle OTP.  The tests run in fully automated mode in the build pipeline.

### Prerequisites

1.  Clone the repo
1.  Install Java 16+
1.  Install Docker Desktop
1.  Take a copy of [`.env.sample`](.env.sample) and rename it `.env`
1.  Read the comments in `.env` for an explanation of the required configuration
1.  Add the test email address and test phone number to the test [Notify](https://www.notifications.service.gov.uk/) team

The configuration provided will connect to the build environment.

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

1.  In `.env` set values for:
    - TEST_USER_EMAIL
    - TEST_USER_PASSWORD
    - TEST_USER_PHONE_NUMBER
    - TEST_USER_NEW_PASSWORD
1.  Set:
    - SELENIUM_HEADLESS=false
    - DEBUG_MODE=true
1.  Run: [./run-acceptance-tests.sh](run-acceptance-tests.sh)

Email and SMS OTP notifications will be sent to the email address and phone number configured as long as they have been added to the test [Notify](https://www.notifications.service.gov.uk/) team.

### Fully automated mode

1.  Find a test client in a test environment
1.  Add the test email address to `TestClientEmailAllowlist` in the client-registry database table for the test client
1.  Configure secret email and phone OTP codes for testing
1.  In `.env` set values for:
    - TEST_USER_EMAIL
    - TEST_USER_PASSWORD
    - TEST_USER_PHONE_NUMBER
    - TEST_USER_NEW_PASSWORD
    - TEST_USER_EMAIL_CODE
    - TEST_USER_PHONE_CODE
1.  Set:
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

## Reporting

Local report:

Report is generated automatically - target/cucumber-report/index.html