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
    chromium \
    chromium-chromedriver

ENV PATH="/usr/bin/chromedriver:${PATH}"

COPY . /test

RUN addgroup -S auth_user_group
RUN adduser -S authuser -G auth_user_group
RUN chown -R authuser: /test
RUN chmod -R u+rwx /test
RUN mv /test/run-tests.sh /run-tests.sh
USER authuser

ENTRYPOINT ["/run-tests.sh"]
