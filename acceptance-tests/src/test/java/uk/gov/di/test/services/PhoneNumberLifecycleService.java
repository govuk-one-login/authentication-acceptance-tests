package uk.gov.di.test.services;

public class PhoneNumberLifecycleService {
    private static volatile PhoneNumberLifecycleService instance;

    private static final DynamoDbService DYNAMO_DB_SERVICE = DynamoDbService.getInstance();

    private PhoneNumberLifecycleService() {
        // Private constructor to prevent direct instantiation
    }

    public static PhoneNumberLifecycleService getInstance() {
        if (instance == null) {
            synchronized (PhoneNumberLifecycleService.class) {
                if (instance == null) {
                    instance = new PhoneNumberLifecycleService();
                }
            }
        }
        return instance;
    }

    public void resetSmsInternationalPhoneNumberSendLimit(String phoneNumber) {
        DYNAMO_DB_SERVICE.deleteSmsInternationalPhoneNumberSendLimit(phoneNumber);
    }

    public void setSmsInternationalPhoneNumberSendLimit(String phoneNumber, int requestCount) {
        DYNAMO_DB_SERVICE.setSmsInternationalPhoneNumberSendLimit(phoneNumber, requestCount);
    }
}
