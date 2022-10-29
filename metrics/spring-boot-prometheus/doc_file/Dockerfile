FROM eclipse-temurin:17-jre-alpine

WORKDIR /

COPY ["./*", "/"]

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} spring-boot-prometheus.jar

ENTRYPOINT ["java","-jar","/spring-boot-prometheus.jar"]