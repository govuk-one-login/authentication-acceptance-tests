package uk.gov.di.test.services;

import uk.gov.di.test.entity.Passkey;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class PasskeyLifecycleService {
    private static volatile PasskeyLifecycleService instance;

    private static final DynamoDbService DYNAMO_DB_SERVICE = DynamoDbService.getInstance();
    private static final WebAuthnService WEB_AUTHN_SERVICE = WebAuthnService.getInstance();

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
        var startRegistrationOptions =
                new WebAuthnService.StartRegistrationOptions("dev.account.gov.uk", publicSubjectId);
        var authenticatorResponse = WEB_AUTHN_SERVICE.startRegistration(startRegistrationOptions);
        var passkey = buildPasskeyRecord(startRegistrationOptions, authenticatorResponse);
        putPasskeyInDynamo(passkey);
    }

    private Passkey buildPasskeyRecord(
            WebAuthnService.StartRegistrationOptions startRegistrationOptions,
            WebAuthnService.AuthenticatorResponse authenticatorResponse) {
        var created = LocalDateTime.now().toString();

        return new Passkey()
                .withPublicSubjectId(startRegistrationOptions.userHandle())
                .withSortKey(buildSortKey(authenticatorResponse.credentialId()))
                .withCreated(created)
                //                .withLastUsed()
                .withCredential(authenticatorResponse.credentialCoseBase64Url())
                .withCredentialId(authenticatorResponse.credentialId())
                .withPasskeyAaguid("00000000-0000-0000-0000-000000000000") // TODO
                //                .withPasskeyIsAttested()
                .withPasskeySignCount(authenticatorResponse.signCount())
                .withPasskeyTransports(Collections.singletonList("internal")) // TODO
                //                .withPasskeyBackupEligible()
                //                .withPasskeyBackedUp()
                .withPasskeyIsResidentKey(true) // TODO
                .withPasskeyAlgorithm(-7) // TODO
        ;
    }

    private void putPasskeyInDynamo(Passkey passkey) {
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
