package uk.gov.di.test.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import software.amazon.awssdk.services.ssm.SsmClient;
import uk.gov.di.test.utils.Environment;

public class TestConfigurationService {
    private static volatile TestConfigurationService instance;

    private SsmClient ssmClient;
    private String parameterPrefix;
    private final LoadingCache<String, String> cache;

    public static class SsmParameterRetrievalException extends RuntimeException {
        public SsmParameterRetrievalException(String key, Exception e) {
            super(String.format("Failed to retrieve parameter '%s'", key), e);
        }
    }

    private TestConfigurationService() {
        if (Boolean.parseBoolean(System.getenv().getOrDefault("USE_SSM", "true"))) {
            ssmClient = SsmClient.builder().build();
            parameterPrefix =
                    String.format("/acceptance-tests/%s/", Environment.getOrThrow("ENVIRONMENT"));
        }
        cache =
                CacheBuilder.newBuilder()
                        .maximumSize(1000)
                        .build(
                                new CacheLoader<>() {
                                    @Override
                                    public String load(String key) {
                                        if (ssmClient == null) {
                                            return Environment.getOrThrow(key);
                                        }
                                        return ssmClient
                                                .getParameter(r -> r.name(parameterPrefix + key))
                                                .parameter()
                                                .value();
                                    }
                                });
    }

    public static TestConfigurationService getInstance() {
        if (instance == null) {
            synchronized (TestConfigurationService.class) {
                if (instance == null) {
                    instance = new TestConfigurationService();
                }
            }
        }
        return instance;
    }

    public String get(String key) {
        try {
            return cache.get(key);
        } catch (Exception e) {
            throw new SsmParameterRetrievalException(key, e);
        }
    }

    public String getOrDefault(String key, String defaultValue) {
        try {
            return this.get(key);
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
