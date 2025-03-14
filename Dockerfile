FROM gradle:7-jdk17 AS builder

WORKDIR /test

COPY build.gradle settings.gradle gradlew gradle.properties ./
COPY gradle ./gradle
COPY acceptance-tests ./acceptance-tests

RUN chmod u+x ./gradlew \
    && ./gradlew --no-daemon build \
    assemble \
    :acceptance-tests:testClasses \
    -x :acceptance-tests:test -x spotlessApply -x spotlessCheck

FROM selenium/standalone-chromium:132.0
ARG SEL_USER=seluser
ARG SEL_GROUP=${SEL_USER}

USER root

RUN rm /usr/lib/python3/dist-packages/distutils-precedence.pth

RUN apt-get update \
    && apt-get -y install \
        openjdk-17-jdk-headless \
        awscli \
    && apt-get remove -y \
        openjdk-21-jdk-headless \
        openjdk-21-jre-headless \
    && apt-get autoremove -y \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

USER ${SEL_USER}

COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /root/.gradle /home/${SEL_USER}/.gradle


WORKDIR /test

COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/build.gradle /test/settings.gradle /test/gradlew /test/gradle.properties ./
COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/.gradle /test/.gradle
COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/gradle ./gradle
COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/build /test/build
COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/acceptance-tests ./acceptance-tests

RUN chmod u+x ./gradlew

ENV SELENIUM_URL="http://localhost:4444/wd/hub"
ENV SELENIUM_BROWSER=chrome
ENV SELENIUM_LOCAL=true
ENV SELENIUM_HEADLESS=true
ENV DEBUG_MODE=false

COPY --chown=${SEL_USER}:${SEL_GROUP} docker/run-acceptance-tests.sh /test/run-acceptance-tests.sh
RUN chmod 500 /test/run-acceptance-tests.sh

ENTRYPOINT ["/test/run-acceptance-tests.sh"]
