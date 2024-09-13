FROM openjdk:17
LABEL maintainer="ingredient-microservice"
ADD target/ingredient-microservice-0.0.1-SNAPSHOT.jar ingredient-microservice.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ingredient-microservice.jar
ENTRYPOINT  ["java", "-jar", "ingredient-microservice.jar"]