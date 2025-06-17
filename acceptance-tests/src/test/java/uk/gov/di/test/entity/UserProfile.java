package uk.gov.di.test.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class UserProfile {

    public static final String ATTRIBUTE_EMAIL = "Email";
    public static final String ATTRIBUTE_SUBJECT_ID = "SubjectID";
    public static final String ATTRIBUTE_EMAIL_VERIFIED = "EmailVerified";
    public static final String ATTRIBUTE_PHONE_NUMBER = "PhoneNumber";
    public static final String ATTRIBUTE_PHONE_NUMBER_VERIFIED = "PhoneNumberVerified";
    public static final String ATTRIBUTE_CREATED = "Created";
    public static final String ATTRIBUTE_UPDATED = "Updated";
    public static final String ATTRIBUTE_TERMS_AND_CONDITIONS = "termsAndConditions";
    public static final String ATTRIBUTE_PUBLIC_SUBJECT_ID = "PublicSubjectID";
    public static final String ATTRIBUTE_LEGACY_SUBJECT_ID = "LegacySubjectID";
    public static final String ATTRIBUTE_SALT = "salt";
    public static final String ATTRIBUTE_ACCOUNT_VERIFIED = "accountVerified";
    public static final String ATTRIBUTE_MFA_METHODS_MIGRATED = "mfaMethodsMigrated";
    public static final String ATTRIBUTE_TEST_USER = "testUser";
    public static final String ATTRIBUTE_MFA_IDENTIFIER = "MFAIdentifier";

    private String email;
    private String subjectID;
    private boolean emailVerified;
    private String phoneNumber;
    private String backupPhoneNumber;
    private boolean phoneNumberVerified;
    private String created;
    private String updated;
    private TermsAndConditions termsAndConditions;
    private String publicSubjectID;
    private String getmfaIdentifier;
    private String legacySubjectID;
    private byte[] salt;
    private int accountVerified;
    private int testUser;
    private boolean mfaMethodsMigrated;

    public UserProfile() {}

    @DynamoDbPartitionKey
    @DynamoDbAttribute(ATTRIBUTE_EMAIL)
    public String getEmail() {
        System.out.println("Email : " + email);
        return email;
    }

    public String getBackupPhoneNumber() {
        return backupPhoneNumber = "07700900111";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserProfile withEmail(String email) {
        this.email = email;
        return this;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"SubjectIDIndex"})
    @DynamoDbAttribute(ATTRIBUTE_SUBJECT_ID)
    public String getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(String subjectID) {
        this.subjectID = subjectID;
    }

    public UserProfile withSubjectID(String subjectID) {
        this.subjectID = subjectID;
        return this;
    }

    @DynamoDbConvertedBy(BooleanToIntAttributeConverter.class)
    @DynamoDbAttribute(ATTRIBUTE_EMAIL_VERIFIED)
    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public UserProfile withEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    @DynamoDbAttribute(ATTRIBUTE_PHONE_NUMBER)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserProfile setBackupPhoneNumber(String backupPhoneNumber) {
        this.backupPhoneNumber = backupPhoneNumber;
        return this;
    }

    public UserProfile withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @DynamoDbConvertedBy(BooleanToIntAttributeConverter.class)
    @DynamoDbAttribute(ATTRIBUTE_PHONE_NUMBER_VERIFIED)
    public boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public void setPhoneNumberVerified(boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    public UserProfile withPhoneNumberVerified(boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
        return this;
    }

    @DynamoDbAttribute(ATTRIBUTE_CREATED)
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public UserProfile withCreated(String created) {
        this.created = created;
        return this;
    }

    @DynamoDbAttribute(ATTRIBUTE_UPDATED)
    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public UserProfile withUpdated(String updated) {
        this.updated = updated;
        return this;
    }

    @DynamoDbAttribute(ATTRIBUTE_TERMS_AND_CONDITIONS)
    public TermsAndConditions getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(TermsAndConditions termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public UserProfile withTermsAndConditions(TermsAndConditions termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
        return this;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"PublicSubjectIDIndex"})
    @DynamoDbAttribute(ATTRIBUTE_PUBLIC_SUBJECT_ID)
    public String getPublicSubjectID() {
        return publicSubjectID;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = {"MFAIdentifierIndex"})
    @DynamoDbAttribute(ATTRIBUTE_MFA_IDENTIFIER)
    public String getmfaIdentifier() {
        return getmfaIdentifier;
    }

    public void setPublicSubjectID(String publicSubjectID) {
        this.publicSubjectID = publicSubjectID;
    }

    public UserProfile withPublicSubjectID(String publicSubjectID) {
        this.publicSubjectID = publicSubjectID;
        return this;
    }

    @DynamoDbAttribute(ATTRIBUTE_LEGACY_SUBJECT_ID)
    public String getLegacySubjectID() {
        return legacySubjectID;
    }

    public void setLegacySubjectID(String legacySubjectID) {
        this.legacySubjectID = legacySubjectID;
    }

    public UserProfile withLegacySubjectID(String legacySubjectID) {
        this.legacySubjectID = legacySubjectID;
        return this;
    }

    @DynamoDbAttribute(ATTRIBUTE_SALT)
    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public UserProfile withSalt(byte[] salt) {
        this.salt = salt;
        return this;
    }

    @DynamoDbAttribute(ATTRIBUTE_ACCOUNT_VERIFIED)
    public int getAccountVerified() {
        return accountVerified;
    }

    public void setAccountVerified(int accountVerified) {
        this.accountVerified = accountVerified;
    }

    public UserProfile withAccountVerified(int accountVerified) {
        this.accountVerified = accountVerified;
        return this;
    }

    @DynamoDbAttribute(ATTRIBUTE_MFA_METHODS_MIGRATED)
    public boolean isMigratedUser() {
        return mfaMethodsMigrated;
    }

    public void setMfaMigratedUser(boolean isMigrated) {
        this.mfaMethodsMigrated = isMigrated;
    }

    public UserProfile withMfaMigratedUser(boolean isMigrated) {
        this.mfaMethodsMigrated = isMigrated;
        return this;
    }

    @DynamoDbAttribute(ATTRIBUTE_TEST_USER)
    @DynamoDbSecondaryPartitionKey(indexNames = {"TestUserIndex"})
    public int getTestUser() {
        return testUser;
    }

    public void setTestUser(int isTestUser) {
        this.testUser = isTestUser;
    }

    public UserProfile withTestUser(int isTestUser) {
        this.setTestUser(isTestUser);
        return this;
    }
}
