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

ARG PROFILE=dev
ARG APP_VERSION=1.0.0

COPY --from=build /build/target/inventory-service-*.jar /app/

EXPOSE 8282

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV DB_URL=jdbc:postgresql://postgres-service:5432/inventory_db
CMD java -jar -Dsping.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} inventory-service-${JAR_VERSION}.jar

# docker build -t rupesh1997/inventory-service:1.0.0 -f ../docker/backend/inventory-service/Dockerfile .
# docker run -d -p 9494:9494 --name inventory-service rupesh1997/inventory-service:1.0.0