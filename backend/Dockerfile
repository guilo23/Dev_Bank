FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine

ARG DATABASE_JDBC
ARG DATABASE_USERNAME
ARG DATABASE_PASSWORD
ENV DATABASE_JDBC=${DATABASE_JDBC}
ENV DATABASE_USERNAME=${DATABASE_USERNAME}
ENV DATABASE_PASSWORD=${DATABASE_PASSWORD}

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

CMD ["java", "-Dspring.profiles.active=preview", "-jar", "app.jar"]
