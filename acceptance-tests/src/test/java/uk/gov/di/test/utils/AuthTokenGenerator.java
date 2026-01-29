package uk.gov.di.test.utils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.impl.ECDSA;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.MessageType;
import software.amazon.awssdk.services.kms.model.SignRequest;
import software.amazon.awssdk.services.kms.model.SignResponse;
import software.amazon.awssdk.services.kms.model.SigningAlgorithmSpec;
import uk.gov.di.test.services.TestConfigurationService;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static com.nimbusds.jose.crypto.impl.ECDSA.getSignatureByteArrayLength;

public class AuthTokenGenerator {
    private static final Region REGION = Region.EU_WEST_2;
    private static final KmsClient KMS_CLIENT =
            KmsClient.builder()
                    .region(REGION)
                    .credentialsProvider(DefaultCredentialsProvider.create())
                    .build();
    protected static final TestConfigurationService TEST_CONFIG_SERVICE =
            TestConfigurationService.getInstance();
    private static final String INTERNAL_COMMON_SUBJECT_ID =
            "urn:fdc:gov.uk:2022:g4UkNSUxoliH1uYaVL7UEh45-pUdrGu2iPVeMz0pYvg";
    public static final String CLIENT_NAME =
            "relying-party-stub-%s-app".formatted(Environment.getOrThrow("ENVIRONMENT"));
    private static final String KEY_ID =
            "true".equals(System.getenv("NEW_AM_ENV"))
                    ? Environment.getOrThrow("AUTH_TEST_ID_TOKEN_KMS_ARN")
                    : "alias/%s-test-id-token-signing-key-alias"
                            .formatted(Environment.getOrThrow("ENVIRONMENT"));

    private static String signWithKms(JWTClaimsSet claims) throws ParseException, JOSEException {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(KEY_ID).build();

        Base64URL encodedHeader = header.toBase64URL();
        Base64URL encodedClaims = Base64URL.encode(claims.toString());

        String message = encodedHeader + "." + encodedClaims;

        SignRequest request =
                SignRequest.builder()
                        .keyId(KEY_ID)
                        .message(SdkBytes.fromByteArray(message.getBytes(StandardCharsets.UTF_8)))
                        .messageType(MessageType.RAW)
                        .signingAlgorithm(SigningAlgorithmSpec.ECDSA_SHA_256)
                        .build();

        SignResponse response = KMS_CLIENT.sign(request);

        Base64URL signature =
                Base64URL.encode(
                        ECDSA.transcodeSignatureToConcat(
                                response.signature().asByteArray(),
                                getSignatureByteArrayLength(JWSAlgorithm.ES256)));

        return SignedJWT.parse(message + "." + signature).serialize();
    }

    public static String createJwt(String internalSubjectId) throws ParseException, JOSEException {
        var clientId = TEST_CONFIG_SERVICE.get("CLIENT_ID");

        var issuer = "https://oidc.integration.account.gov.uk";

        JWTClaimsSet claims =
                new JWTClaimsSet.Builder()
                        .subject(internalSubjectId)
                        .issuer(issuer)
                        .jwtID(UUID.randomUUID().toString())
                        .issueTime(new Date())
                        .expirationTime(Date.from(Instant.now().plus(60, ChronoUnit.MINUTES)))
                        .claim("client_id", clientId)
                        .claim("scope", Arrays.asList("openid", "email", "phone", "am"))
                        .build();

        return signWithKms(claims);
    }

    public static void main(String[] args) {
        try {
            String jwt = createJwt(INTERNAL_COMMON_SUBJECT_ID);
            System.out.println("Auth Token: " + jwt);
        } catch (ParseException | JOSEException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
