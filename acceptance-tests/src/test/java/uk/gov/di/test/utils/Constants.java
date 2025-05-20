package uk.gov.di.test.utils;

public final class Constants {

    public static final String INVALID_EMAIL = "joe.bloggs";
    public static final String NON_EXISTENT_EMAIL = "does-not-exist@baddomain.null.local";

    public static final Integer DEFAULT_PASSWORD_LENGTH = 10;
    public static final String WEAK_PASSWORD = "password1";
    public static final String SHORT_DIGIT_PASSWORD = "44445555";
    public static final String SEQUENCE_NUMBER_PASSWORD = "12345678";
    public static final String INVALID_PASSWORD = "qweqweqwe";
    public static final String TOP_100K_PASSWORD = "password123";
    public static final String NEW_VALID_PASSWORD = "new-password1";

    public static final String INCORRECT_EMAIL_OTP_CODE = "123456";
    public static final String INCORRECT_PHONE_CODE = "123456";

    public static final String INTERNATIONAL_MOBILE_PHONE_NUMBER_VALID = "+61412123123";
    public static final String INTERNATIONAL_MOBILE_PHONE_NUMBER_INCORRECT_FORMAT =
            "+123456789123456789123456";
    public static final String INTERNATIONAL_MOBILE_PHONE_NUMBER_NON_DIGIT_CHARS = "/3383838383";

    public static final String UK_MOBILE_PHONE_NUMBER =
            "07700900000"; // Ofcom reserved fictitious number

    public static final String UK_MOBILE_PHONE_NUMBER_WITH_INTERNATIONAL_DIALING_CODE =
            "+447700900000";
    public static final String UK_MOBILE_PHONE_NUMBER_INCORRECT_FORMAT = "077009000000000";
    public static final String UK_MOBILE_PHONE_NUMBER_NON_DIGIT_CHARS = "0770090*a45";

    public static final String DEFAULT_TIMESTAMP = "1970-01-01T00:00:00.000000";

    public static final String AUTH_APP_SECRET =
            "ORIS432QI4ZDSXSSOAWEY23XLVADMI3UIMRWIVDZMNRCSJLPHBRQ";
}
