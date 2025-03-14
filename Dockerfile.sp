ARG BASE_IMAGE
FROM ${BASE_IMAGE}
ARG SEL_USER=seluser
ARG SEL_GROUP=${SEL_USER}

COPY --chown=${SEL_USER}:${SEL_GROUP} ./run-tests.sh /test/run-tests.sh
RUN chmod 500 /test/run-tests.sh

ENTRYPOINT ["/test/run-tests.sh"]
