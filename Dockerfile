# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render specific optimizations:
# 1. Use the PORT environment variable provided by Render
# 2. Add JVM memory limits for Render's 512MB free tier
EXPOSE 8080

ENTRYPOINT ["java", "-Xmx300m", "-Xss512k", "-XX:CICompilerCount=2", "-Dserver.port=${PORT}", "-jar", "app.jar"]
