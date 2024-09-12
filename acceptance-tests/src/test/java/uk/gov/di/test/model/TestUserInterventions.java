package uk.gov.di.test.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class TestUserInterventions {
    String internalPairwiseId;
    String equivalentPlainEmailAddress;
    Boolean blocked;
    Boolean suspended;
    Boolean resetPassword;
    Boolean reproveIdentity;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("InternalPairwiseId")
    public String getInternalPairwiseId() {
        return internalPairwiseId;
    }

    @DynamoDbAttribute("InternalPairwiseId")
    public void setInternalPairwiseId(String internalPairwiseId) {
        this.internalPairwiseId = internalPairwiseId;
    }

    @DynamoDbAttribute("EquivalentPlainEmailAddress")
    public String getEquivalentPlainEmailAddress() {
        return equivalentPlainEmailAddress;
    }

    @DynamoDbAttribute("EquivalentPlainEmailAddress")
    public void setEquivalentPlainEmailAddress(String equivalentPlainEmailAddress) {
        this.equivalentPlainEmailAddress = equivalentPlainEmailAddress;
    }

    @DynamoDbAttribute("Blocked")
    public Boolean getBlocked() {
        return blocked;
    }

    @DynamoDbAttribute("Blocked")
    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    @DynamoDbAttribute("Suspended")
    public Boolean getSuspended() {
        return suspended;
    }

    @DynamoDbAttribute("Suspended")
    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    @DynamoDbAttribute("ResetPassword")
    public Boolean getResetPassword() {
        return resetPassword;
    }

    @DynamoDbAttribute("ResetPassword")
    public void setResetPassword(Boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    @DynamoDbAttribute("ReproveIdentity")
    public Boolean getReproveIdentity() {
        return reproveIdentity;
    }

    @DynamoDbAttribute("ReproveIdentity")
    public void setReproveIdentity(Boolean reproveIdentity) {
        this.reproveIdentity = reproveIdentity;
    }
}
