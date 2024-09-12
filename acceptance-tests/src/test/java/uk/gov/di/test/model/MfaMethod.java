package uk.gov.di.test.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public class MfaMethod {
    private String credentialValue;
    private Integer enabled;
    private Integer methodVerified;
    private String mfaMethodType;
    private String updated;

    @DynamoDbAttribute("CredentialValue")
    public String getCredentialValue() {
        return credentialValue;
    }

    @DynamoDbAttribute("CredentialValue")
    public void setCredentialValue(String credentialValue) {
        this.credentialValue = credentialValue;
    }

    @DynamoDbAttribute("Enabled")
    public Integer getEnabled() {
        return enabled;
    }

    @DynamoDbAttribute("Enabled")
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    @DynamoDbAttribute("MethodVerified")
    public Integer getMethodVerified() {
        return methodVerified;
    }

    @DynamoDbAttribute("MethodVerified")
    public void setMethodVerified(Integer methodVerified) {
        this.methodVerified = methodVerified;
    }

    @DynamoDbAttribute("MfaMethodType")
    public String getMfaMethodType() {
        return mfaMethodType;
    }

    @DynamoDbAttribute("MfaMethodType")
    public void setMfaMethodType(String mfaMethodType) {
        this.mfaMethodType = mfaMethodType;
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
