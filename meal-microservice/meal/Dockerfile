FROM openjdk:17
LABEL maintainer="meal"
ADD target/meal-0.0.1-SNAPSHOT.jar meal.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} meal.jar
ENTRYPOINT  ["java", "-jar", "meal.jar"]