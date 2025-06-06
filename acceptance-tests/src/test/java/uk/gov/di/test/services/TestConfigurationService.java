package uk.gov.di.test.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
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
        boolean useSsm = Boolean.parseBoolean(System.getenv().getOrDefault("USE_SSM", "false"));
        System.out.printf("Using SSM for configuration: %s%n", useSsm ? "Yes" : "No");
        if (useSsm) {
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
                                        try {
                                            return Environment.getOrThrow(key);
                                        } catch (Environment.EnvironmentException e) {
                                            if (ssmClient == null) {
                                                throw e;
                                            }
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
        } catch (UncheckedExecutionException e) {
            if (e.getCause() instanceof Environment.EnvironmentException) {
                System.out.println(e.getCause().toString());
                throw new SsmParameterRetrievalException(String.format("env/%s", key), e);
            } else if (e.getCause() instanceof SsmParameterRetrievalException) {
                throw new SsmParameterRetrievalException(
                        String.format("%s%s", parameterPrefix, key), e);
            }
            throw new SsmParameterRetrievalException(key, e);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unexpected parameter retrieval exception for key: " + key, e);
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
