#1. Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn help:evaluate -Dexpression=project.version -q -DforceStdout > version.txt
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#2. Runtime stage
FROM amazoncorretto:17
WORKDIR /app

ARG PROFILE=dev
RUN export APP_VERSION=$(cat version.txt)

COPY --from=build /build/target/chat-service-*.jar /app/

EXPOSE 9393

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}
ENV DB_URL=mongodb://mongo:27017/chat_db
CMD java -jar -Dsping.profiles.active=${ACTIVE_PROFILE} chat-service-${JAR_VERSION}.jar

# docker build -t rupesh1997/chat-service -f ../docker/backend/chat-service/Dockerfile .
# docker run -d -p 9393:9393 --name chat-service rupesh1997/chat-service:1.0.0