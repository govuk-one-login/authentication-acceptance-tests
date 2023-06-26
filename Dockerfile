FROM docker

RUN apk update && apk add --update --no-cache \
    bash \
    curl \
    openjdk17 \
    jq \
    firefox \
    aws-cli \
    uuidgen \
    argon2

# install geckodriver
RUN curl -L https://github.com/mozilla/geckodriver/releases/download/v0.33.0/geckodriver-v0.33.0-linux64.tar.gz | tar xz -C /usr/local/bin

COPY . /test

ENTRYPOINT ["/test/run-acceptance-tests.sh"]
