FROM openjdk:17
LABEL maintainer="user"
ADD target/user-0.0.1-SNAPSHOT.jar user.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} user.jar
ENTRYPOINT  ["java", "-jar", "user.jar"]