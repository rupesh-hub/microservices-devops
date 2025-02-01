#1. Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#2. Runtime stage
FROM amazoncorretto:17
WORKDIR /app
COPY --from=build /build/target/user-service-*.jar /app/
ARG PROFILE=dev
ARG APP_VERSION=1.0.0
ARG DATABASE_URL=jdbc:postgresql://postgres-service:5432/user_db

EXPOSE 8989

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV DB_URL=${DATABASE_URL}

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} user-service-${JAR_VERSION}.jar