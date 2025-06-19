package uk.gov.di.test.entity;

public enum MFAMethodType {
    EMAIL("EMAIL"),
    AUTH_APP("AUTH_APP"),
    SMS("SMS"),
    NONE("NONE");

    private String value;

    MFAMethodType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MFAMethodType fromString(String value) {
        String normalizedValue = value.replace(" ", "_");
        return MFAMethodType.valueOf(normalizedValue);
    }
}
