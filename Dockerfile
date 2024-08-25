## alpine linux with JRE
FROM eclipse-temurin:17-jre-alpine

## create a nonroot user and group
RUN addgroup -S spring && adduser -S spring -G spring

## copy the spring jar
COPY target/*.jar /opt/app.jar

## set the nonroot user as the default user
USER spring:spring

## set the working directory
WORKDIR /opt

ENTRYPOINT ["java", "-jar", "app.jar"]

## expose the port to the external world
EXPOSE 8080