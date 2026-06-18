package uk.gov.di.test.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class PasskeyConfig {
    private final byte[] credentialIdAsBytes;
    private final String credentialIdAsBase64Url;
    private final PKCS8EncodedKeySpec privateKeyAsPkcs8;
    private final String publicKeyAsCoseBase64Url;

    public static PasskeyConfig generatePasskeyConfig() throws Exception {
        return new PasskeyConfig();
    }

    private PasskeyConfig() throws Exception {
        this.credentialIdAsBytes = generateCredentialIdBytes();
        this.credentialIdAsBase64Url =
                Base64.getUrlEncoder().withoutPadding().encodeToString(credentialIdAsBytes);

        KeyPair keyPair = generateWebAuthnKeyPair();
        this.privateKeyAsPkcs8 = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
        this.publicKeyAsCoseBase64Url =
                CoseKeyEncoder.ecPublicKeyToCoseBase64Url((ECPublicKey) keyPair.getPublic());
    }

    private static byte[] generateCredentialIdBytes() {
        byte[] credentialIdBytes = new byte[32];
        new SecureRandom().nextBytes(credentialIdBytes);
        return credentialIdBytes;
    }

    private static KeyPair generateWebAuthnKeyPair()
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(new ECGenParameterSpec("secp256r1"));
        return keyGen.generateKeyPair();
    }

    public byte[] credentialIdAsBytes() {
        return credentialIdAsBytes;
    }

    public String credentialIdAsBase64Url() {
        return credentialIdAsBase64Url;
    }

    public PKCS8EncodedKeySpec privateKeyAsPkcs8() {
        return privateKeyAsPkcs8;
    }

    public String publicKeyAsCoseBase64Url() {
        return publicKeyAsCoseBase64Url;
    }
}
