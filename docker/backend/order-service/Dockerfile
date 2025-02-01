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
COPY --from=build /build/target/order-service-*.jar /app/

ARG PROFILE=dev
ARG APP_VERSION=1.0.0

EXPOSE 8484

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV DB_URL=jdbc:postgresql://postgres-service:5432/order_db

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} order-service-${JAR_VERSION}.jar

#docker build -t rupesh1997/order-service:1.0.0 -f ../docker/backend/order-service/Dockerfile .
#docker run -d -p 8484:8484 --name order-service rupesh1997/order-service:1.0.0