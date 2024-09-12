package uk.gov.di.test.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Map;

@DynamoDbBean
public class TestUserProfile {
    private String email;
    private String publicSubjectId;
    private String subjectId;
    private Integer accountVerified;
    private Integer emailVerified;
    private String salt;
    private String phoneNumber;
    private Integer phoneNumberVerified;
    private Map<String, String> termsAndConditions;
    private String testUser;
    private String updated;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("Email")
    public String getEmail() {
        return email;
    }

    @DynamoDbAttribute("Email")
    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDbAttribute("PublicSubjectID")
    public String getPublicSubjectId() {
        return publicSubjectId;
    }

    @DynamoDbAttribute("PublicSubjectID")
    public void setPublicSubjectId(String publicSubjectId) {
        this.publicSubjectId = publicSubjectId;
    }

    @DynamoDbAttribute("SubjectID")
    public String getSubjectId() {
        return subjectId;
    }

    @DynamoDbAttribute("SubjectID")
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    @DynamoDbAttribute("accountVerified")
    public Integer getAccountVerified() {
        return accountVerified;
    }

    @DynamoDbAttribute("accountVerified")
    public void setAccountVerified(Integer accountVerified) {
        this.accountVerified = accountVerified;
    }

    @DynamoDbAttribute("EmailVerified")
    public Integer getEmailVerified() {
        return emailVerified;
    }

    @DynamoDbAttribute("EmailVerified")
    public void setEmailVerified(Integer emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @DynamoDbAttribute("PhoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @DynamoDbAttribute("PhoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @DynamoDbAttribute("PhoneNumberVerified")
    public Integer getPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    @DynamoDbAttribute("PhoneNumberVerified")
    public void setPhoneNumberVerified(Integer phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    public Map<String, String> getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(Map<String, String> termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public String getTestUser() {
        return testUser;
    }

    public void setTestUser(String testUser) {
        this.testUser = testUser;
    }

    @DynamoDbAttribute("Updated")
    public String getUpdated() {
        return updated;
    }

    @DynamoDbAttribute("Updated")
    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
