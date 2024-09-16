package uk.gov.di.test.utils;

public class Environment {
    public static class EnvironmentException extends RuntimeException {
        public EnvironmentException() {}

        public EnvironmentException(String message) {
            super(message);
        }
    }

    public static String getOrThrow(String key) throws EnvironmentException {

        String value = System.getenv(key);
        if (value == null) {
            throw new EnvironmentException("Environment variable " + key + " not set");
        }
        return value;
    }

    public static Integer getIntegerOrThrow(String key) throws EnvironmentException {
        String value = getOrThrow(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new EnvironmentException("Environment variable " + key + " is not an integer");
        }
    }
}
