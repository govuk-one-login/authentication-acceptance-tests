package uk.gov.di.test.controllers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import uk.gov.di.test.utils.Environment;

public class SecretsManagerController {
    private static volatile SecretsManagerController instance;

    private final SecretsManagerClient secretsManagerClient;
    private final LoadingCache<String, String> cache;
    private final String secretPrefix;

    private SecretsManagerController() {
        secretsManagerClient = SecretsManagerClient.builder().build();
        secretPrefix = String.format("/deploy/%s/", Environment.getOrThrow("ENVIRONMENT"));

        cache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(
                        new CacheLoader<String, String>() {
                            @Override
                            public String load(String key) throws Exception {
                                return secretsManagerClient.getSecretValue(r -> r.secretId(secretPrefix + key)).secretString();
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

    public String getSecretValue(String secretName) {
        try {
            return cache.get(secretName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
