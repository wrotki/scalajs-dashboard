FROM vspiewak/sbt-cache-resource

ENV YARN_VERSION 0.20.0
ADD https://yarnpkg.com/downloads/$YARN_VERSION/yarn-v${YARN_VERSION}.tar.gz /opt/yarn.tar.gz
RUN yarnDirectory=/opt/yarn && \
    mkdir -p "$yarnDirectory" && \
    tar -xzf /opt/yarn.tar.gz -C "$yarnDirectory" && \
    ln -s "$yarnDirectory/dist/bin/yarn" /usr/local/bin/ && \
    rm /opt/yarn.tar.gz

RUN apk add -U openjdk8 nodejs

ENV PATH="/usr/lib/jvm/java-1.8-openjdk/bin:${PATH}"
