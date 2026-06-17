package uk.gov.di.test.services;

import org.openqa.selenium.virtualauthenticator.Credential;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.UUID;

public class WebAuthnCeremonyService {
    public record StartRegistrationOptions(String rpId, String userHandle) {}

    public record StartRegistrationResponse(
            String credentialId, PublicKey credential, int signCount) {}

    private static volatile WebAuthnCeremonyService instance;

    private static final VirtualAuthenticatorLifecycleService virtualAuthenticatorLifecycleService =
            VirtualAuthenticatorLifecycleService.getInstance();

    private WebAuthnCeremonyService() {
        // Private constructor to prevent direct instantiation
    }

    public static WebAuthnCeremonyService getInstance() {
        if (instance == null) {
            synchronized (WebAuthnCeremonyService.class) {
                if (instance == null) {
                    instance = new WebAuthnCeremonyService();
                }
            }
        }
        return instance;
    }

    public StartRegistrationResponse startRegistration(
            StartRegistrationOptions startRegistrationOptions)
            throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        var credentialKeyPair = generateWebAuthnKeyPair();

        var startRegistrationResponse =
                new StartRegistrationResponse(
                        UUID.randomUUID().toString(), credentialKeyPair.getPublic(), 0);

        Credential credential =
                Credential.createResidentCredential(
                        startRegistrationResponse.credentialId().getBytes(),
                        startRegistrationOptions.rpId(),
                        new PKCS8EncodedKeySpec(credentialKeyPair.getPrivate().getEncoded()),
                        startRegistrationOptions.userHandle().getBytes(),
                        startRegistrationResponse.signCount());
        virtualAuthenticatorLifecycleService.putCredentialInAuthenticator(credential);

        return startRegistrationResponse;
    }

    /** Returns an ES256 (COSE ID: -7) key pair */
    private KeyPair generateWebAuthnKeyPair()
            throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        // "secp256r1" is the NIST P-256 curve required for ES256
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        keyGen.initialize(ecSpec);

        return keyGen.generateKeyPair();
    }
}
