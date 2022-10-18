FROM eclipse-temurin:17-jre-alpine

WORKDIR /

COPY ["./*", "/"]

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} hazelcast-service.jar

ENTRYPOINT ["java","-jar","/hazelcast-service.jar"]