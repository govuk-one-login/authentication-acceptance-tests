package uk.gov.di.test.step_definitions;

import io.cucumber.java.After;
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
import uk.gov.di.test.entity.*;

import java.util.*;

public class UserLifecycleStepDef {
    private final World world;

    protected static final String ENVIRONMENT = System.getenv().get("ENVIRONMENT");
    protected static final String AWS_REGION = System.getenv().get("AWS_REGION");

    protected static final String TEST_USER_EMAIL_USER =
            System.getenv().get("TEST_USER_EMAIL_USER");
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

    private static final DynamoDbTable<UserProfile> userProfileTable =
            DYNAMODB_CLIENT.table(
                    ENVIRONMENT + "-user-profile", TableSchema.fromBean(UserProfile.class));

    private static final DynamoDbTable<UserCredentials> userCredentialsTable =
            DYNAMODB_CLIENT.table(
                    ENVIRONMENT + "-user-credentials", TableSchema.fromBean(UserCredentials.class));

    public UserLifecycleStepDef(World world) {
        this.world = world;
    }

    @ParameterType("sms|app")
    public String mfaMethodType(String mfaMethodType) {
        return mfaMethodType;
    }

    private String generateEmail() {
        return TEST_USER_EMAIL_USER
                + "+"
                + System.currentTimeMillis()
                + "@"
                + TEST_USER_EMAIL_DOMAIN;
    }

    private void createUserProfile() {
        if (world.userProfile != null) {
            throw new RuntimeException("User profile already exists");
        }
        UUID sub = UUID.randomUUID();
        UUID psub = UUID.randomUUID();
        UserProfile testUserProfile = new UserProfile();
        testUserProfile.setEmail(generateEmail());
        testUserProfile.setPublicSubjectID(psub.toString());
        testUserProfile.setSubjectID(sub.toString());
        userProfileTable.putItem(testUserProfile);
        world.userProfile = testUserProfile;
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
        if (world.userCredentials != null) {
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

        UserCredentials testUserCredentials = new UserCredentials();
        testUserCredentials.setEmail(world.userProfile.getEmail());
        testUserCredentials.setPassword(encodedPassword);
        testUserCredentials.setSubjectID(world.userProfile.getSubjectID());
        userCredentialsTable.putItem(testUserCredentials);
        world.userCredentials = testUserCredentials;

        world.userProfile.setSalt(Base64.getEncoder().encodeToString(salt).getBytes());

        userProfileTable.updateItem(world.userProfile);
    }

    private void updateMfaSMS(String phoneNumber, Boolean phoneNumberVerified) {
        world.userProfile.setAccountVerified(1);
        world.userProfile.setPhoneNumber(phoneNumber);
        world.userProfile.setPhoneNumberVerified(phoneNumberVerified);
        userProfileTable.updateItem(world.userProfile);
    }

    private Map<String, String> buildTermsAndConditions(String version, String timestamp) {
        Map<String, String> termsAndConditions = new HashMap<>();
        termsAndConditions.put("version", version);
        termsAndConditions.put("timestamp", timestamp);
        return termsAndConditions;
    }

    private void updateTermsAndConditionsVersion(String version) {
        TermsAndConditions termsAndConditions =
                new TermsAndConditions(version, "1970-01-01T00:00:00.000000");
        world.userProfile.setTermsAndConditions(termsAndConditions);
        userProfileTable.updateItem(world.userProfile);
    }

    private List<MFAMethod> buildMfaMethods(String secret) {
        List<MFAMethod> mfaMethods = new ArrayList<>();
        mfaMethods.set(
                0,
                new MFAMethod()
                        .withCredentialValue(secret)
                        .withEnabled(true)
                        .withMethodVerified(true)
                        .withMfaMethodType("AUTH_APP")
                        .withUpdated("1970-01-01T00:00:00.000000"));
        return mfaMethods;
    }

    private void updateMfaApp(String secret) {
        world.userCredentials.setMfaMethods(buildMfaMethods(secret));
    }

    @Given("a user exists")
    public void aUserExists() {
        createUserProfile();
        createUserCredentials();
        updateTermsAndConditionsVersion(TEST_USER_LATEST_TERMS_AND_CONDITIONS_VERSION);
    }

    @Given("a user with {mfaMethodType} MFA exists")
    public void aUserWithOtpExists(String mfaMethodType) {
        aUserExists();
        switch (mfaMethodType) {
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

    @After
    public void theUserIsDeleted() {
        if (world.userProfile != null) {
            userProfileTable.deleteItem(world.userProfile);
        }
        if (world.userCredentials != null) {
            userCredentialsTable.deleteItem(world.userCredentials);
        }
    }
}
