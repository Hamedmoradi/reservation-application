FROM adoptopenjdk/openjdk11

RUN apt-get update \
    && apt-get -yq  install apt-utils \
    && apt-get -yq  install curl \
    && apt-get -yq  install telnet \
    && apt-get -yq  install net-tools
RUN echo "Env variable :  $JAVA_OPTS"
ENV TZ=Asia/Tehran
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ARG JAR_FILE=target/azki-reservation-application.jar
COPY ${JAR_FILE} /opt/app.jar
#ARG time=$(date "+%Y%m%d%s")
WORKDIR /opt
ENTRYPOINT ["java","-jar","app.jar"]