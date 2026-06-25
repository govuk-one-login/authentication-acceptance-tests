package uk.gov.di.test.services;

import org.openqa.selenium.virtualauthenticator.Credential;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticatorOptions;
import uk.gov.di.test.utils.Driver;

public class VirtualAuthenticatorLifecycleService {
    private static volatile VirtualAuthenticatorLifecycleService instance;
    private static final InheritableThreadLocal<VirtualAuthenticator> authenticatorPool =
            new InheritableThreadLocal<>();

    private VirtualAuthenticatorLifecycleService() {}

    public static VirtualAuthenticatorLifecycleService getInstance() {
        if (instance == null) {
            synchronized (VirtualAuthenticatorLifecycleService.class) {
                if (instance == null) {
                    instance = new VirtualAuthenticatorLifecycleService();
                }
            }
        }
        return instance;
    }

    public void createVirtualAuthenticator() {
        if (authenticatorPool.get() != null) {
            throw new UnsupportedOperationException(
                    "Cannot create a second virtual authenticator while one is already attached");
        }

        var driver = Driver.getOrCreate();

        VirtualAuthenticatorOptions options =
                new VirtualAuthenticatorOptions()
                        .setHasResidentKey(true)
                        .setHasUserVerification(true)
                        .setIsUserVerified(true);

        authenticatorPool.set(driver.addVirtualAuthenticator(options));
    }

    public void destroyVirtualAuthenticator() {
        var authenticator = authenticatorPool.get();
        if (authenticator == null) return;

        Driver.get()
                .ifPresent(chromeDriver -> chromeDriver.removeVirtualAuthenticator(authenticator));

        authenticatorPool.remove();
    }

    public void putCredentialInAuthenticator(Credential credential) {
        authenticatorPool.get().addCredential(credential);
    }

    public void enableUserVerification() {
        authenticatorPool.get().setUserVerified(true);
    }

    public void disableUserVerification() {
        authenticatorPool.get().setUserVerified(false);
    }
}
