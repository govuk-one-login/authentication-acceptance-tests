package uk.gov.di.test.services;

import org.openqa.selenium.virtualauthenticator.VirtualAuthenticator;
import org.openqa.selenium.virtualauthenticator.VirtualAuthenticatorOptions;
import uk.gov.di.test.utils.Driver;

public class VirtualAuthenticatorLifecycleService {
    private static volatile VirtualAuthenticatorLifecycleService instance;
    private VirtualAuthenticator authenticator;

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
        if (authenticator != null) {
            throw new UnsupportedOperationException(
                    "Cannot create a second virtual authenticator while one is already attached.");
        }

        var driver = Driver.getAsAuthenticator();

        VirtualAuthenticatorOptions options =
                new VirtualAuthenticatorOptions()
                        .setProtocol(VirtualAuthenticatorOptions.Protocol.CTAP2)
                        .setHasResidentKey(true)
                        .setIsUserVerified(true);

        authenticator = driver.addVirtualAuthenticator(options);
    }

    public void destroyVirtualAuthenticator() {
        var driver = Driver.getAsAuthenticator();
        driver.removeVirtualAuthenticator(authenticator);
        authenticator = null;
    }
}
