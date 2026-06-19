package uk.gov.di.test.services;

import org.openqa.selenium.virtualauthenticator.Credential;
import uk.gov.di.test.entity.Passkey;
import uk.gov.di.test.utils.PasskeyConfig;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class PasskeyLifecycleService {
    private static volatile PasskeyLifecycleService instance;

    private static final DynamoDbService DYNAMO_DB_SERVICE = DynamoDbService.getInstance();
    private static final VirtualAuthenticatorLifecycleService
            VIRTUAL_AUTHENTICATOR_LIFECYCLE_SERVICE =
                    VirtualAuthenticatorLifecycleService.getInstance();
    private static final TestConfigurationService TEST_CONFIG_SERVICE =
            TestConfigurationService.getInstance();

    private PasskeyLifecycleService() {}

    public static PasskeyLifecycleService getInstance() {
        if (instance == null) {
            synchronized (PasskeyLifecycleService.class) {
                if (instance == null) {
                    instance = new PasskeyLifecycleService();
                }
            }
        }
        return instance;
    }

    public void buildAndStorePasskey(String publicSubjectId) throws Exception {
        var passkeyConfig = PasskeyConfig.generatePasskeyConfig();

        putPasskeyInAuthenticator(publicSubjectId, passkeyConfig);
        putPasskeyInDynamo(publicSubjectId, passkeyConfig);
    }

    private static void putPasskeyInAuthenticator(
            String publicSubjectId, PasskeyConfig passkeyConfig) {
        Credential credential =
                Credential.createResidentCredential(
                        passkeyConfig.credentialIdAsBytes(),
                        TEST_CONFIG_SERVICE.get("WEBAUTHN_RELYING_PARTY_ID"),
                        passkeyConfig.privateKeyAsPkcs8(),
                        publicSubjectId.getBytes(),
                        0);

        VIRTUAL_AUTHENTICATOR_LIFECYCLE_SERVICE.putCredentialInAuthenticator(credential);
    }

    private void putPasskeyInDynamo(String publicSubjectId, PasskeyConfig passkeyConfig) {
        var created = LocalDateTime.now().toString();
        var passkey =
                new Passkey()
                        .withPublicSubjectId(publicSubjectId)
                        .withSortKey(buildSortKey(passkeyConfig.credentialIdAsBase64Url()))
                        .withCreated(created)
                        .withCredential(passkeyConfig.publicKeyAsCoseBase64Url())
                        .withCredentialId(passkeyConfig.credentialIdAsBase64Url())
                        .withPasskeyAaguid("00000000-0000-0000-0000-000000000000")
                        .withPasskeySignCount(0)
                        .withPasskeyTransports(Collections.singletonList("usb"))
                        .withPasskeyIsResidentKey(true)
                        .withPasskeyAlgorithm(-7);

        DYNAMO_DB_SERVICE.putPasskey(passkey);
    }

    public void deleteAllPasskeysForUser(String publicSubjectId) {
        List<Passkey> passkeys = DYNAMO_DB_SERVICE.queryPasskeysByPublicSubjectId(publicSubjectId);
        if (!passkeys.isEmpty()) {
            DYNAMO_DB_SERVICE.deletePasskeys(passkeys);
        }
    }

    public static String buildSortKey(String passkeyId) {
        return "PASSKEY" + "#" + passkeyId;
    }
}
