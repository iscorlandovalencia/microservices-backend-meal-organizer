FROM openjdk:17
LABEL maintainer="api-gateway"
ADD target/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} api-gateway.jar
ENTRYPOINT  ["java", "-jar", "api-gateway.jar"]