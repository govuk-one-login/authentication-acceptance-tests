package uk.gov.di.test.services;

import uk.gov.di.test.entity.Passkey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PasskeyLifecycleService {
    private static volatile PasskeyLifecycleService instance;

    private static final DynamoDbService DYNAMO_DB_SERVICE = DynamoDbService.getInstance();

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

    public List<Passkey> buildPasskeyAndPutToDynamoDb(String publicSubjectId) {
        var testPasskeyId = UUID.randomUUID().toString();
        var created = LocalDateTime.now().toString();
        var passkey =
                new Passkey()
                        .withPublicSubjectId(publicSubjectId)
                        .withSortKey(buildSortKey(testPasskeyId))
                        .withCredentialId(testPasskeyId)
                        .withCreated(created)
                        .withCredential("testCredential");

        DYNAMO_DB_SERVICE.putPasskey(passkey);
        return List.of(passkey);
    }

    public void deletePasskeysForUser(List<Passkey> passkeys) {
        DYNAMO_DB_SERVICE.deletePasskeys(passkeys);
    }

    public static String buildSortKey(String passkeyId) {
        return "PASSKEY" + "#" + passkeyId;
    }
}
