package uk.gov.di.test.controllers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import uk.gov.di.test.utils.Environment;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SecretsManagerController {
    private static volatile SecretsManagerController instance;

    private final SecretsManagerClient secretsManagerClient;
    private final LoadingCache<String, String> cache;
    private final String secretPrefix;

    public static class SecretRetrievalException extends RuntimeException {
        public SecretRetrievalException(String key, Exception e) {
            super(String.format("Failed to retrieve secret '%s'", key), e);
        }
    }

    private SecretsManagerController() {
        secretsManagerClient = SecretsManagerClient.builder().build();
        secretPrefix = String.format("/deploy/%s/", Environment.getOrThrow("ENVIRONMENT"));

        cache =
                CacheBuilder.newBuilder()
                        .maximumSize(1000)
                        .build(
                                new CacheLoader<>() {
                                    @Override
                                    public String load(String key) {
                                        return secretsManagerClient
                                                .getSecretValue(r -> r.secretId(key))
                                                .secretString();
                                    }
                                });
    }

    public static SecretsManagerController getInstance() {
        if (instance == null) {
            synchronized (SecretsManagerController.class) {
                if (instance == null) {
                    instance = new SecretsManagerController();
                }
            }
        }
        return instance;
    }

    public String getSecretValue(String key) {
        try {
            return cache.get(key);
        } catch (Exception e) {
            throw new SecretRetrievalException(key, e);
        }
    }

    public String getDeploySecretValue(String secretName) {
        return getSecretValue(secretPrefix + secretName);
    }
}
