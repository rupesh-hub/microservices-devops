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
COPY --from=build /build/target/cart-service-*.jar /app/

EXPOSE 9292

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV DB_URL=jdbc:mysql://mysql-service:3306/cart_db

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} cart-service-${JAR_VERSION}.jar

# docker build -t rupesh1997/cart-service:1.0.0 -f ../docker/backend/cart-service/Dockerfile .
# docker run -d -p 9292:9292 --name cart-service rupesh1997/cart-service:1.0.0

