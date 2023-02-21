package uk.gov.di.test.rp;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class SigningKey {

    public static RSAPrivateKey signingKey() {
        try {
            return (RSAPrivateKey)
                    KeyFactory.getInstance("RSA")
                            .generatePrivate(
                                    new PKCS8EncodedKeySpec(
                                            Base64.decodeBase64(System.getenv("RP_SIGNING_KEY"))));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
