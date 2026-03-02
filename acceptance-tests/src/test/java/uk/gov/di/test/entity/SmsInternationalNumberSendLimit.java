package uk.gov.di.test.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class SmsInternationalNumberSendLimit {

    public static final String ATTRIBUTE_PHONE_NUMBER = "PhoneNumber";

    private String phoneNumber;

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
}
