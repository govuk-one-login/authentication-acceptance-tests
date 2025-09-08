@UI @mfa-reset

Feature: The MFA reset process.
  Begins in Authentication, when a user initiates an MFA reset,
  they are redirected to Identity to determine their verification status. Identity verifies
  whether the user is classified as authentication-only or identity-verified.

@AUT-3825 @new-mfa-reset-with-ipv
Scenario: User with Auth App Default MFA and SMS Backup MFA resets their MFAs and remains migrated
Given a Migrated User with a Default MFA of "AUTH APP"
And the User is Authenticated
And the User does not have a Backup MFA method
When the User adds "07700900111" as their SMS Backup MFA
Then the system sends an OTP to "07700900111"
When the User provides the correct otp
Then "+447700900111" is added as a verified Backup MFA Method
When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
And the user selects sign in
Then the user is taken to the "Enter your email" page
When the user enters their email address
Then the user is taken to the "Enter your password" page
When the user enters their password
Then the user is taken to the "Enter the 6 digit security code shown in your authenticator app" page
And the user selects "I do not have access to the authenticator app" link
And the user selects "try another way to get a security code" link
Then the user is taken to the "How do you want to get a security code?" page
And the user selects "check if you can change how you get security codes" link
When the user clicks the continue button
Then the user is taken to the "How do you want to get security codes" page
When the user chooses text message to get security codes
And the user enters their mobile phone number
And the user enters the six digit security code from their phone
Then the user is taken to the "You’ve changed how you get security codes" page
When the user clicks the continue button
Then the user is returned to the service
And the User only has a default MFA of "SMS" and remains migrated

@AUT-3825 @new-mfa-reset-with-ipv
Scenario: User with SMS Default MFA and Auth App Backup MFA resets their MFAs and remains migrated
Given a Migrated User with a Default MFA of "SMS"
And the User is Authenticated
And the User does not have a Backup MFA method
When the User requests to add a backup MFA Auth App
Then the User's back up MFA Auth App is updated
When the user comes from the stub relying party with default options and is taken to the "Create your GOV.UK One Login or sign in" page
And the user selects sign in
And the user enters their email address
And the user enters their password
And the user selects "Problems with the code?" link
And the user selects "try another way to get a security code" link
Then the user is taken to the "How do you want to get a security code?" page
And the user selects "check if you can change how you get security codes" link
Then the user is taken to the IPV stub page
When the user clicks the continue button
Then the user is taken to the "How do you want to get security codes" page
When the user chooses auth app to get security codes
Then the user is taken to the "Set up an authenticator app" page
When the user adds the secret key on the screen to their auth app
And the user enters the security code from the auth app
Then the user is taken to the "You’ve changed how you get security codes" page
When the user clicks the continue button
Then the user is returned to the service
And the User only has a default MFA of "AUTH_APP" and remains migrated
