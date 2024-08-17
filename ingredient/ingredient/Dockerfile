FROM openjdk:17
LABEL maintainer="ingredient"
EXPOSE 4445
ADD target/ingredient-0.0.1-SNAPSHOT.jar ingredient.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ingredient.jar
ENTRYPOINT  ["java", "-jar", "ingredient.jar"]