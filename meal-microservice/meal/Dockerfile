FROM openjdk:17
LABEL maintainer="meal-microservice"
ADD target/meal-microservice-0.0.1-SNAPSHOT.jar meal-microservice.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} meal-microservice.jar
ENTRYPOINT  ["java", "-jar", "meal-microservice.jar"]