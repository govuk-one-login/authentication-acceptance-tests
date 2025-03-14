FROM docker

RUN apk update && apk add --update --no-cache \
    bash \
    curl \
    openjdk17 \
    jq \
    aws-cli \
    uuidgen \
    argon2 \
    alsa-lib \
    cairo \
    cups-libs \
    dbus-libs \
    eudev-libs \
    expat \
    flac \
    gdk-pixbuf \
    glib \
    libgcc \
    libjpeg-turbo \
    libpng \
    libwebp \
    libx11 \
    libxcomposite \
    libxdamage \
    libxext \
    libxfixes \
    tzdata \
    libexif \
    udev \
    xvfb \
    zlib-dev \
    wget && \
    wget -q -O chrome-linux64.zip https://storage.googleapis.com/chrome-for-testing-public/133.0.6943.141/linux64/chrome-linux64.zip && \
    unzip chrome-linux64.zip && \
    rm chrome-linux64.zip && \
    mv chrome-linux64 /opt/chrome/ && \
    ln -s /opt/chrome/chrome /usr/bin/ && \
    wget -q -O chromedriver-linux64.zip https://storage.googleapis.com/chrome-for-testing-public/133.0.6943.141/linux64/chromedriver-linux64.zip && \
    unzip -j chromedriver-linux64.zip chromedriver-linux64/chromedriver && \
    rm chromedriver-linux64.zip && \
    mv chromedriver /usr/bin/


ENV PATH="/usr/bin/chromedriver:${PATH}"

COPY . /test

RUN addgroup -S auth_user_group
RUN adduser -S authuser -G auth_user_group
RUN chown -R authuser: /test
RUN chmod -R u+rwx /test
USER authuser

ENTRYPOINT ["/test/run-acceptance-tests.sh"]
