#1. Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#2. Runtime stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=1.0.0

WORKDIR /app
COPY --from=build /build/target/authorization-server-*.jar /app/

EXPOSE 9191

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV DB_URL=jdbc:postgresql://postgres-service:5432/authorization_db

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} authorizezation-server-${JAR_VERSION}.jar

# docker build -t rupesh1997/authorization-server:1.0.0 -f ../docker/backend/authorization-server/Dockerfile .
# docker run -p 9191:9191 --name authorization-server rupesh1997/authorization-server:1.0.0
