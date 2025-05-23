ARG SELENIUM_BASE=selenium/standalone-chromium:132.0
FROM gradle:7-jdk17 AS builder

WORKDIR /test

COPY build.gradle settings.gradle gradlew gradle.properties ./
COPY gradle ./gradle

RUN chmod u+x ./gradlew && ./gradlew --no-daemon --console=plain clean

COPY acceptance-tests ./acceptance-tests
COPY generated.env .env

RUN chmod u+x ./gradlew \
    && ./gradlew --no-daemon --console=plain \
        build \
        assemble \
        :acceptance-tests:compileTestJava \
        :acceptance-tests:testClasses \
        downloadCucumberDependencies \
        -x :acceptance-tests:test -x spotlessApply -x spotlessCheck

FROM ${SELENIUM_BASE} AS base
ARG SEL_USER=seluser
ARG SEL_GROUP=${SEL_USER}

USER root

RUN apt-get update \
    && apt-get -y install \
        openjdk-17-jdk-headless \
    && apt-get remove -y \
        openjdk-21-jdk-headless \
        openjdk-21-jre-headless \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /awscli
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-$(uname -m).zip" -o "awscliv2.zip" \
    && unzip awscliv2.zip \
    && sudo ./aws/install \
    && rm -rf awscliv2.zip aws

USER ${SEL_USER}

ENV GRADLE_USER_HOME=/home/${SEL_USER}/.gradle
COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /root/.gradle /home/${SEL_USER}/.gradle

WORKDIR /test
COPY generated.env .env

COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/build.gradle /test/settings.gradle /test/gradlew /test/gradle.properties ./
COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/.gradle /test/.gradle
COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/gradle ./gradle
COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/build /test/build
COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/acceptance-tests /test/acceptance-tests
COPY --chown=${SEL_USER}:${SEL_GROUP} --from=builder /test/.env .env

RUN chmod u+x ./gradlew

ENV SELENIUM_URL="http://localhost:4444/wd/hub"
ENV SELENIUM_LOCAL=true
ENV SELENIUM_HEADLESS=true
ENV DEBUG_MODE=false

COPY --chown=${SEL_USER}:${SEL_GROUP} docker/run-acceptance-tests.sh /test/run-acceptance-tests.sh
RUN chmod 500 /test/run-acceptance-tests.sh

ENTRYPOINT ["/test/run-acceptance-tests.sh"]

FROM base AS secure-pipelines
COPY --chown=${SEL_USER}:${SEL_GROUP} scripts/fetch_envars.sh /test/scripts/fetch_envars.sh
COPY --chown=${SEL_USER}:${SEL_GROUP} docker/run-tests-sp.sh /run-tests.sh
RUN chmod 500 /test/scripts/fetch_envars.sh /run-tests.sh

ENTRYPOINT ["/run-tests.sh"]
