package uk.gov.di.test.utils;

import java.io.ByteArrayOutputStream;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;

/**
 * When storing the passkey public key in the database, it's required in COSE Base64URL encoded
 * format. This is a utility class to an EC P-256 public key into that format.
 */
public class CoseKeyEncoder {

    private CoseKeyEncoder() {}

    public static String ecPublicKeyToCoseBase64Url(ECPublicKey publicKey) {
        byte[] x = toUnsignedFixedLength(publicKey.getW().getAffineX().toByteArray(), 32);
        byte[] y = toUnsignedFixedLength(publicKey.getW().getAffineY().toByteArray(), 32);

        // CBOR map with 5 entries: {1:2, 3:-7, -1:1, -2:x, -3:y}
        ByteArrayOutputStream out = new ByteArrayOutputStream(77);
        out.write(0xa5); // map(5)
        out.write(0x01); // key: 1 (kty)
        out.write(0x02); // value: 2 (EC2)
        out.write(0x03); // key: 3 (alg)
        out.write(0x26); // value: -7 (ES256)
        out.write(0x20); // key: -1 (crv)
        out.write(0x01); // value: 1 (P-256)
        out.write(0x21); // key: -2 (x)
        out.write(0x58); // bytes, 1-byte length follows
        out.write(0x20); // length: 32
        out.writeBytes(x);
        out.write(0x22); // key: -3 (y)
        out.write(0x58); // bytes, 1-byte length follows
        out.write(0x20); // length: 32
        out.writeBytes(y);

        return Base64.getUrlEncoder().withoutPadding().encodeToString(out.toByteArray());
    }

    private static byte[] toUnsignedFixedLength(byte[] bigIntBytes, int length) {
        if (bigIntBytes.length == length) return bigIntBytes;
        byte[] result = new byte[length];
        if (bigIntBytes.length > length) {
            System.arraycopy(bigIntBytes, bigIntBytes.length - length, result, 0, length);
        } else {
            System.arraycopy(
                    bigIntBytes, 0, result, length - bigIntBytes.length, bigIntBytes.length);
        }
        return result;
    }
}
