FROM openjdk:17-jdk-slim

RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

ARG JAR_FILE=bootstrap/target/fisioterapia-app.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]