package uk.gov.di.test.utils;

import org.openqa.selenium.WebDriverException;

import java.time.Instant;

public class SessionContextExceptions {
    public static class SessionContextException extends RuntimeException {
        public SessionContextException(
                String message,
                String currentDriverUrl,
                String currentDriverTitle,
                Instant timestamp,
                OneLoginSession session,
                WebDriverException lastException) {
            super(
                    String.format(
                            "%s | Current Page URL: \"%s\" | Current Page Title: \"%s\" | Time: %s | Session: %s",
                            message,
                            currentDriverUrl,
                            currentDriverTitle,
                            timestamp,
                            session.toJSONObject()),
                    lastException);
        }
    }

    public static class WaitForPageLoadException extends SessionContextException {
        public WaitForPageLoadException(
                String titleContains,
                String currentDriverUrl,
                String currentDriverTitle,
                Instant startTime,
                OneLoginSession session,
                WebDriverException e) {
            super(
                    String.format("Error when loading page with title: \"%s\"", titleContains),
                    currentDriverUrl,
                    currentDriverTitle,
                    startTime,
                    session,
                    e);
        }
    }

    public static class FindElementException extends SessionContextException {
        public FindElementException(
                String elementSelector,
                String currentDriverUrl,
                String currentDriverTitle,
                Instant timestamp,
                OneLoginSession session,
                WebDriverException lastException) {
            super(
                    String.format("Element not found: %s", elementSelector),
                    currentDriverUrl,
                    currentDriverTitle,
                    timestamp,
                    session,
                    lastException);
        }

        public FindElementException(
                String elementSelector,
                String currentDriverUrl,
                String currentDriverTitle,
                OneLoginSession session,
                WebDriverException lastException) {
            this(
                    elementSelector,
                    currentDriverUrl,
                    currentDriverTitle,
                    Instant.now(),
                    session,
                    lastException);
        }
    }
}
