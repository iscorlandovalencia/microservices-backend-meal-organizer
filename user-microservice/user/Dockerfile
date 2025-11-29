FROM eclipse-temurin:17-jdk
LABEL maintainer="user-microservice"
ADD target/user-microservice-0.0.1-SNAPSHOT.jar user-microservice.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} user-microservice.jar
ENTRYPOINT  ["java", "-jar", "user-microservice.jar"]