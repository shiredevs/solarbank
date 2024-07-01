FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /app
COPY target/server-1.0.0.jar /app/app.jar
COPY src/main/resources/localhost.p12 /app/certs/localhost.p12
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-Dserver.ssl.key-store=/app/certs/localhost.p12", "-Dserver.ssl.key-store-password=changeit", "-jar", "/app/app.jar"]
