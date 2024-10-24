package uk.gov.di.test.utils;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthAppStub {
    private static final int CODE_DIGITS = 6;
    private static final long TIME_WINDOW_IN_MILLISECONDS = TimeUnit.SECONDS.toMillis(30);

    public String getAuthAppOneTimeCode(String secret) {
        return getAuthAppOneTimeCode(secret, NowHelper.now().getTime());
    }

    public String getAuthAppOneTimeCode(String secret, long time) {
        int codeAsInt = getCodeAsInt(decodeBase32Secret(secret), getTimeWindowFromTime(time));
        return String.format("%06d", codeAsInt);
    }

    int getCodeAsInt(byte[] secret, long time) {
        byte[] data = new byte[8];

        for (int i = 8; i-- > 0; time >>>= 8) {
            data[i] = (byte) time;
        }

        SecretKeySpec signKey = new SecretKeySpec(secret, "HmacSHA1");

        try {
            Mac mac = Mac.getInstance("HmacSHA1");

            mac.init(signKey);

            byte[] hash = mac.doFinal(data);

            int offset = hash[hash.length - 1] & 0xF;

            long truncatedHash = 0;

            for (int i = 0; i < 4; ++i) {
                truncatedHash <<= 8;
                truncatedHash |= (hash[offset + i] & 0xFF);
            }

            truncatedHash &= 0x7FFFFFFF;
            truncatedHash %= (int) Math.pow(10, CODE_DIGITS);

            return (int) truncatedHash;
        } catch (Exception ex) {
            return 0;
        }
    }

    public byte[] decodeBase32Secret(String secret) {
        Base32 codec32 = new Base32();
        return codec32.decode(secret);
    }

    private long getTimeWindowFromTime(long time) {
        return time / TIME_WINDOW_IN_MILLISECONDS;
    }

    public static String getAuthAppCode(String authAppSecretKey) {
        AuthAppStub authAppStub = new AuthAppStub();
        String securityCode = authAppStub.getAuthAppOneTimeCode(authAppSecretKey);
        if (securityCode.length() != 6) {
            System.out.println("Auth App Security Code: " + securityCode);
        }
        assertTrue(securityCode.length() == 6);
        return securityCode;
    }
}
