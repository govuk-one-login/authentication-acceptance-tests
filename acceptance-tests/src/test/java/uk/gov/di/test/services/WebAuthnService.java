package uk.gov.di.test.services;

import org.openqa.selenium.virtualauthenticator.Credential;
import uk.gov.di.test.utils.CoseKeyEncoder;

import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class WebAuthnService {
    public record StartRegistrationOptions(String rpId, String userHandle) {}

    public record AuthenticatorResponse(
            String credentialId, String credentialCoseBase64Url, int signCount) {}

    private static volatile WebAuthnService instance;

    private static final VirtualAuthenticatorLifecycleService
            VIRTUAL_AUTHENTICATOR_LIFECYCLE_SERVICE =
                    VirtualAuthenticatorLifecycleService.getInstance();

    private WebAuthnService() {}

    public static WebAuthnService getInstance() {
        if (instance == null) {
            synchronized (WebAuthnService.class) {
                if (instance == null) {
                    instance = new WebAuthnService();
                }
            }
        }
        return instance;
    }

    public AuthenticatorResponse startRegistration(
            StartRegistrationOptions startRegistrationOptions)
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        var credentialKeyPair = generateWebAuthnKeyPair();

        byte[] credentialIdBytes = new byte[32];
        new SecureRandom().nextBytes(credentialIdBytes);
        String credentialIdBase64Url =
                Base64.getUrlEncoder().withoutPadding().encodeToString(credentialIdBytes);

        String coseKeyBase64Url =
                CoseKeyEncoder.ecPublicKeyToCoseBase64Url(
                        (ECPublicKey) credentialKeyPair.getPublic());

        var startRegistrationResponse =
                new AuthenticatorResponse(credentialIdBase64Url, coseKeyBase64Url, 0);

        Credential credential =
                Credential.createResidentCredential(
                        credentialIdBytes,
                        startRegistrationOptions.rpId(),
                        new PKCS8EncodedKeySpec(credentialKeyPair.getPrivate().getEncoded()),
                        startRegistrationOptions.userHandle().getBytes(),
                        startRegistrationResponse.signCount());
        VIRTUAL_AUTHENTICATOR_LIFECYCLE_SERVICE.putCredentialInAuthenticator(credential);

        return startRegistrationResponse;
    }

    private KeyPair generateWebAuthnKeyPair()
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(new ECGenParameterSpec("secp256r1"));
        return keyGen.generateKeyPair();
    }
}
