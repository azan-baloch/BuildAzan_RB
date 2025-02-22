# Stage 1: Build the application using Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copy only pom.xml first for caching dependencies
COPY pom.xml .
# Download dependencies (cached if pom.xml hasn't changed)
RUN mvn dependency:go-offline -B
# Copy the rest of the application code
COPY src ./src
# Build the application and package it into a jar (skip tests for speed)
RUN mvn clean package -DskipTests -B

# Stage 2: Run the application with a lightweight JDK image
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copy the jar from the build stage. Adjust the path/name if needed.
COPY --from=build /app/target/BuildAzan-0.0.1-SNAPSHOT.jar app.jar
# Expose the port your application listens on (usually 8080)
EXPOSE 8080
# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
