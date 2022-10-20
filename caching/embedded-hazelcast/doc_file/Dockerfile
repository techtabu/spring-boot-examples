FROM eclipse-temurin:17-jre-alpine

WORKDIR /

COPY ["./*", "/"]

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} embedded-hazelcast.jar

ENTRYPOINT ["java","-jar","/embedded-hazelcast.jar"]