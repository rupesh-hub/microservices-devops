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
COPY --from=build /build/target/shipping-service-*.jar /app/

ARG APP_VERSION=1.0.0
ARG PROFILE=dev

EXPOSE 8787

ENV JAR_VERSION=${APP_VERSION}
ENV ACTIVE_PROFILE=${PROFILE}
ENV DB_URL=jdbc:mysql://mysql-service:3306/shipping_db

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} shipping-service-${JAR_VERSION}.jar