package uk.gov.di.test.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class UserInterventions {
    public static final String ATTRIBUTE_INTERNAL_PAIRWISE_ID = "InternalPairwiseId";
    public static final String ATTRIBUTE_EQUIVALENT_PLAIN_EMAIL_ADDRESS =
            "EquivalentPlainEmailAddress";
    public static final String ATTRIBUTE_BLOCKED = "Blocked";
    public static final String ATTRIBUTE_SUSPENDED = "Suspended";
    public static final String ATTRIBUTE_RESET_PASSWORD = "ResetPassword";
    public static final String ATTRIBUTE_REPROVE_IDENTITY = "ReproveIdentity";

    private String internalPairwiseID;
    private String equivalentPlainEmailAddress;
    private Boolean blocked;
    private Boolean suspended;
    private Boolean resetPassword;
    private Boolean reproveIdentity;

    public UserInterventions() {}

    @DynamoDbPartitionKey
    @DynamoDbAttribute(ATTRIBUTE_INTERNAL_PAIRWISE_ID)
    public String getInternalPairwiseID() {
        return internalPairwiseID;
    }

    public UserInterventions withInternalPairwiseID(String internalPairwiseID) {
        this.internalPairwiseID = internalPairwiseID;
        return this;
    }

    public void setInternalPairwiseID(String internalPairwiseID) {
        this.internalPairwiseID = internalPairwiseID;
    }

    @DynamoDbAttribute(ATTRIBUTE_EQUIVALENT_PLAIN_EMAIL_ADDRESS)
    public String getEquivalentPlainEmailAddress() {
        return equivalentPlainEmailAddress;
    }

    public UserInterventions withEquivalentPlainEmailAddress(String equivalentPlainEmailAddress) {
        this.equivalentPlainEmailAddress = equivalentPlainEmailAddress;
        return this;
    }

    public void setEquivalentPlainEmailAddress(String equivalentPlainEmailAddress) {
        this.equivalentPlainEmailAddress = equivalentPlainEmailAddress;
    }

    @DynamoDbAttribute(ATTRIBUTE_BLOCKED)
    public Boolean getBlocked() {
        return blocked;
    }

    public UserInterventions withBlocked(Boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    @DynamoDbAttribute(ATTRIBUTE_SUSPENDED)
    public Boolean getSuspended() {
        return suspended;
    }

    public UserInterventions withSuspended(Boolean suspended) {
        this.suspended = suspended;
        return this;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    @DynamoDbAttribute(ATTRIBUTE_RESET_PASSWORD)
    public Boolean getResetPassword() {
        return resetPassword;
    }

    public UserInterventions withResetPassword(Boolean resetPassword) {
        this.resetPassword = resetPassword;
        return this;
    }

    public void setResetPassword(Boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    @DynamoDbAttribute(ATTRIBUTE_REPROVE_IDENTITY)
    public Boolean getReproveIdentity() {
        return reproveIdentity;
    }

    public UserInterventions withReproveIdentity(Boolean reproveIdentity) {
        this.reproveIdentity = reproveIdentity;
        return this;
    }

    public void setReproveIdentity(Boolean reproveIdentity) {
        this.reproveIdentity = reproveIdentity;
    }

    public void disableAllInterventions() {
        this.setBlocked(false);
        this.setSuspended(false);
        this.setResetPassword(false);
        this.setReproveIdentity(false);
    }
}
