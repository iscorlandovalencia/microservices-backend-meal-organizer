FROM openjdk:17
LABEL maintainer="discovery-microservice"
ADD target/discovery-microservice-0.0.1-SNAPSHOT.jar discovery-microservice.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} discovery-microservice.jar
ENTRYPOINT  ["java", "-jar", "discovery-microservice.jar"]