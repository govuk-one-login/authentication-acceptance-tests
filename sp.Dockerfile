# This dockerfile is used to build the image for our Secure Pipelines deployment.
# It uses an image built from `Dockerfile` as a base image, and then adds the necessary files for our SP environment.
# To build this, first build the base image from `Dockerfile`: `docker build . -t 'some-tag:latest', and then build this
# image from it: `docker build -t sp -f sp.Dockerfile . --build-arg BASE_IMAGE='some-tag:latest'`
ARG BASE_IMAGE
FROM ${BASE_IMAGE}
ARG SEL_USER=seluser
ARG SEL_GROUP=${SEL_USER}

COPY --chown=${SEL_USER}:${SEL_GROUP} scripts/fetch_envars.sh /test/scripts/fetch_envars.sh
COPY --chown=${SEL_USER}:${SEL_GROUP} docker/run-tests-sp.sh /test/run-tests.sh
RUN chmod 500 /test/scripts/fetch_envars.sh /test/run-tests.sh

ENTRYPOINT ["/test/run-tests.sh"]
