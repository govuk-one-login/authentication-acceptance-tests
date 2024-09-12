package uk.gov.di.test.step_definitions;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import uk.gov.di.test.model.MfaMethod;
import uk.gov.di.test.model.TestUserCredentials;
import uk.gov.di.test.model.TestUserProfile;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserCreationStepDef {
    private final World world;

    protected static final String ENVIRONMENT = System.getenv().get("ENVIRONMENT");
    protected static final String AWS_REGION = System.getenv().get("AWS_REGION");

    protected static final String TEST_USER_EMAIL_USER =
            System.getenv().get("TEST_USER_EMAIL_NAME");
    protected static final String TEST_USER_EMAIL_DOMAIN =
            System.getenv().get("TEST_USER_EMAIL_DOMAIN");
    protected static final String TEST_USER_PHONE_NUMBER =
            System.getenv().get("TEST_USER_PHONE_NUMBER");
    protected static final String TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION =
            System.getenv().get("TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION");
    protected static final String ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET =
            System.getenv("ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET");

    private static final DynamoDbClient standardClient =
            DynamoDbClient.builder().region(Region.of(AWS_REGION)).build();

    private static final DynamoDbEnhancedClient DYNAMODB_CLIENT =
            DynamoDbEnhancedClient.builder().dynamoDbClient(standardClient).build();

    private static final DynamoDbTable<TestUserProfile> userProfileTable =
            DYNAMODB_CLIENT.table(
                    ENVIRONMENT + "-user-profile", TableSchema.fromBean(TestUserProfile.class));

    private static final DynamoDbTable<TestUserCredentials> userCredentialsTable =
            DYNAMODB_CLIENT.table(
                    ENVIRONMENT + "-user-credentials",
                    TableSchema.fromBean(TestUserCredentials.class));

    //    private static final DynamoDbTable<TestUserInterventions> userInterventionsTable =
    //            DYNAMODB_CLIENT.table(
    //                    ENVIRONMENT + "-stub-account-interventions",
    //                    TableSchema.fromBean(TestUserInterventions.class));

    public UserCreationStepDef(World world) {
        this.world = world;
    }

    @ParameterType("sms|app")
    public String otpType(String otpType) {
        return otpType;
    }

    @ParameterType("temporarily suspended|permanently blocked|password reset")
    public String intervention(String intervention) {
        return intervention;
    }

    private String generateEmail() {
        return TEST_USER_EMAIL_USER
                + "+"
                + System.currentTimeMillis()
                + "@"
                + TEST_USER_EMAIL_DOMAIN;
    }

    private void createUserProfile() {
        if (world.testUserProfile != null) {
            throw new RuntimeException("User profile already exists");
        }
        UUID sub = UUID.randomUUID();
        UUID psub = UUID.randomUUID();
        TestUserProfile testUserProfile = new TestUserProfile();
        testUserProfile.setEmail(generateEmail());
        testUserProfile.setPublicSubjectId(psub.toString());
        testUserProfile.setSubjectId(sub.toString());
        userProfileTable.putItem(testUserProfile);
        world.testUserProfile = testUserProfile;
    }

    private static String encodeArgon2Hash(byte[] hash, Argon2Parameters parameters)
            throws IllegalArgumentException {
        StringBuilder stringBuilder = new StringBuilder();
        Base64.Encoder b64encoder = Base64.getEncoder().withoutPadding();
        String var10000;
        switch (parameters.getType()) {
            case 0 -> var10000 = "$argon2d";
            case 1 -> var10000 = "$argon2i";
            case 2 -> var10000 = "$argon2id";
            default -> throw new IllegalArgumentException(
                    "Invalid algorithm type: " + parameters.getType());
        }

        String type = var10000;
        stringBuilder.append(type);
        stringBuilder
                .append("$v=")
                .append(parameters.getVersion())
                .append("$m=")
                .append(parameters.getMemory())
                .append(",t=")
                .append(parameters.getIterations())
                .append(",p=")
                .append(parameters.getLanes());
        if (parameters.getSalt() != null) {
            stringBuilder.append("$").append(b64encoder.encodeToString(parameters.getSalt()));
        }

        stringBuilder.append("$").append(b64encoder.encodeToString(hash));
        return stringBuilder.toString();
    }

    private void createUserCredentials() {
        if (world.testUserCredentials != null) {
            throw new RuntimeException("User credentials already exists");
        }
        world.password = UUID.randomUUID().toString();
        BytesKeyGenerator saltGenerator = KeyGenerators.secureRandom(32);
        byte[] salt = saltGenerator.generateKey();
        byte[] hash = new byte[32];
        Argon2Parameters parameters =
                (new Argon2Parameters.Builder(2))
                        .withSalt(salt)
                        .withParallelism(1)
                        .withMemoryAsKB(15360)
                        .withIterations(2)
                        .build();
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);
        generator.generateBytes(world.password.toCharArray(), hash);
        String encodedPassword = encodeArgon2Hash(hash, parameters);

        TestUserCredentials testUserCredentials = new TestUserCredentials();
        testUserCredentials.setEmail(world.testUserProfile.getEmail());
        testUserCredentials.setPassword(encodedPassword);
        testUserCredentials.setSubjectId(world.testUserProfile.getSubjectId());
        userCredentialsTable.putItem(testUserCredentials);
        world.testUserCredentials = testUserCredentials;

        world.testUserProfile.setSalt(Base64.getEncoder().encodeToString(salt));

        userProfileTable.updateItem(world.testUserProfile);
    }

    private void updateMfaSMS(String phoneNumber, Boolean phoneNumberVerified) {
        world.testUserProfile.setAccountVerified(1);
        world.testUserProfile.setPhoneNumber(phoneNumber);
        world.testUserProfile.setPhoneNumberVerified(phoneNumberVerified ? 1 : 0);
        userProfileTable.updateItem(world.testUserProfile);
    }

    private Map<String, String> buildTermsAndConditions(String version, String timestamp) {
        Map<String, String> termsAndConditions = new HashMap<>();
        termsAndConditions.put("version", version);
        termsAndConditions.put("timestamp", timestamp);
        return termsAndConditions;
    }

    private void updateTermsAndConditionsVersion(String version) {
        Map<String, String> termsAndConditions =
                buildTermsAndConditions(version, "1970-01-01T00:00:00.000000");
        world.testUserProfile.setTermsAndConditions(termsAndConditions);
        userProfileTable.updateItem(world.testUserProfile);
    }

    private MfaMethod[] buildMfaMethods(String secret) {
        MfaMethod[] mfaMethods = new MfaMethod[1];
        mfaMethods[0] = new MfaMethod();
        mfaMethods[0].setCredentialValue(secret);
        mfaMethods[0].setEnabled(1);
        mfaMethods[0].setMethodVerified(1);
        mfaMethods[0].setMfaMethodType("AUTH_APP");
        mfaMethods[0].setUpdated("1970-01-01T00:00:00.000000");
        return mfaMethods;
    }

    private void updateMfaApp(String secret) {
        world.testUserCredentials.setMfaMethods(buildMfaMethods(secret));
    }

    @Given("a user with {word} OTP exists")
    public void aUserWithOtpExists(String otpType) {
        createUserProfile();
        createUserCredentials();
        updateTermsAndConditionsVersion(TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION);
        switch (otpType) {
            case "sms":
                updateMfaSMS(TEST_USER_PHONE_NUMBER, true);
                break;
            case "app":
                updateMfaApp(ACCOUNT_RECOVERY_USER_AUTH_APP_SECRET);
                break;
            default:
                throw new RuntimeException("Invalid OTP type");
        }
    }

    //    private void updateUserIntervention(
    //            Boolean blocked, Boolean suspended, Boolean resetPassword, Boolean
    // reproveIdentity) {
    //        String sector = String.format("identity.%s.account.gov.uk", ENVIRONMENT);
    //        TestUserInterventions testUserInterventions = new TestUserInterventions();
    //        testUserInterventions.setBlocked(blocked);
    //        testUserInterventions.setSuspended(suspended);
    //        testUserInterventions.setResetPassword(resetPassword);
    //        testUserInterventions.setReproveIdentity(reproveIdentity);
    //
    // testUserInterventions.setEquivalentPlainEmailAddress(world.testUserProfile.getEmail());
    //        userInterventionsTable.putItem(testUserInterventions);
    //    }

    @Given("and the user has a {intervention} intervention")
    public void andHasAnIntervention(String intervention) {
        if (world.testUserProfile == null || world.testUserCredentials == null) {
            throw new RuntimeException("User profile or credentials do not exist");
        }
    }
}
