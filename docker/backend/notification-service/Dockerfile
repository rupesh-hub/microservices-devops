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

COPY --from=build /build/target/notification-service-*.jar /app/
EXPOSE 8383

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV DB_URL=mongodb://mongo-service:27017/notification_db
CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} notification-service-${JAR_VERSION}.jar

# docker build -t rupesh1997/notification-service:1.0.0 -f ../docker/backend/notification-service/Dockerfile .
# docker run -d -p 8383:8383 --name notification-service rupesh1997/notification-service:1.0.0