package uk.gov.di.test.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class SmsInternationalNumberSendLimit {

    public static final String ATTRIBUTE_PHONE_NUMBER = "PhoneNumber";
    public static final String ATTRIBUTE_SENT_COUNT = "SentCount";

    private String phoneNumber;
    private int sentCount;

    public SmsInternationalNumberSendLimit() {
        // Required by DynamoDB mapper for deserialization
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute(ATTRIBUTE_PHONE_NUMBER)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public SmsInternationalNumberSendLimit withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @DynamoDbAttribute(ATTRIBUTE_SENT_COUNT)
    public int getSentCount() {
        return sentCount;
    }

    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }

    public SmsInternationalNumberSendLimit withSentCount(int sentCount) {
        this.sentCount = sentCount;
        return this;
    }
}
