package uk.gov.di.test.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class TestUserCredentials {
    private String email;
    private String created;
    private MfaMethod[] mfaMethods;
    private String password;
    private String subjectId;
    private Integer testUser;
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

    @DynamoDbAttribute("Created")
    public String getCreated() {
        return created;
    }

    @DynamoDbAttribute("Created")
    public void setCreated(String created) {
        this.created = created;
    }

    @DynamoDbAttribute("MfaMethods")
    public MfaMethod[] getMfaMethods() {
        return mfaMethods;
    }

    @DynamoDbAttribute("MfaMethods")
    public void setMfaMethods(MfaMethod[] mfaMethods) {
        this.mfaMethods = mfaMethods;
    }

    @DynamoDbAttribute("Password")
    public String getPassword() {
        return password;
    }

    @DynamoDbAttribute("Password")
    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDbAttribute("SubjectID")
    public String getSubjectId() {
        return subjectId;
    }

    @DynamoDbAttribute("SubjectID")
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getTestUser() {
        return testUser;
    }

    public void setTestUser(Integer testUser) {
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
