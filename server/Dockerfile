FROM maven:3.9.8-eclipse-temurin-21-jammy AS build-stage
WORKDIR /solarbank/server
COPY pom.xml .
RUN mvn dependency:go-offline
COPY . .
RUN mvn clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre-jammy
COPY --from=build-stage /solarbank/server/target/*.jar ./server.jar
EXPOSE ${PORT}
CMD ["java", "-jar", "server.jar"]
