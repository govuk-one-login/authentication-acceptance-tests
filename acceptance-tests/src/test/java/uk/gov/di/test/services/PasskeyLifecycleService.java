package uk.gov.di.test.services;

import uk.gov.di.test.entity.Passkey;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class PasskeyLifecycleService {
    private static volatile PasskeyLifecycleService instance;

    private static final DynamoDbService DYNAMO_DB_SERVICE = DynamoDbService.getInstance();
    private static final WebAuthnCeremonyService WEB_AUTHN_CEREMONY_SERVICE =
            WebAuthnCeremonyService.getInstance();

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

    public void buildPasskeyAndPutToDynamoDb(String publicSubjectId) throws Exception {
        var startRegistrationOptions =
                new WebAuthnCeremonyService.StartRegistrationOptions(
                        "account.gov.uk", publicSubjectId);
        var startRegistrationResponse =
                WEB_AUTHN_CEREMONY_SERVICE.startRegistration(startRegistrationOptions);
        var passkey = buildPasskeyRecord(startRegistrationOptions, startRegistrationResponse);
        putPasskeyInDynamo(passkey);
    }

    private Passkey buildPasskeyRecord(
            WebAuthnCeremonyService.StartRegistrationOptions startRegistrationOptions,
            WebAuthnCeremonyService.StartRegistrationResponse startRegistrationResponse) {
        var created = LocalDateTime.now().toString();

        return new Passkey()
                .withPublicSubjectId(startRegistrationOptions.userHandle())
                .withSortKey(buildSortKey(startRegistrationResponse.credentialId()))
                .withCreated(created)
                //                .withLastUsed()
                .withCredential(
                        Base64.getEncoder()
                                .encodeToString(
                                        startRegistrationResponse.credential().getEncoded()))
                .withCredentialId(startRegistrationResponse.credentialId())
                .withPasskeyAaguid("00000000-0000-0000-0000-000000000000") // TODO
                //                .withPasskeyIsAttested()
                .withPasskeySignCount(startRegistrationResponse.signCount())
                .withPasskeyTransports(Collections.singletonList("internal")) // TODO
        //                .withPasskeyBackupEligible()
        //                .withPasskeyBackedUp()
        //                .withPasskeyIsResidentKey()
        //                .withPasskeyAlgorithm()
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
