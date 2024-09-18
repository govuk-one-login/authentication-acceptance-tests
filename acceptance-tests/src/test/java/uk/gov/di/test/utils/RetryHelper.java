package uk.gov.di.test.utils;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RetryHelper {

    private static volatile RetryHelper instance;

    private final HashMap<String, AtomicInteger> retryCounters;

    private RetryHelper() {
        retryCounters = new HashMap<>();
    }

    public static RetryHelper getInstance() {
        if (instance == null) {
            synchronized (RetryHelper.class) {
                if (instance == null) {
                    instance = new RetryHelper();
                }
            }
        }
        return instance;
    }

    public Integer getAndIncrement(String identifier) {
        this.retryCounters.putIfAbsent(identifier, new AtomicInteger(1));
        return this.retryCounters.get(identifier).getAndIncrement();
    }
}
