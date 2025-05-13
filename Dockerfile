# Stage 1: Build the JAR (if you're using multi-stage builds)
# Optional - skip if you already have the .jar built

# Stage 2: Runtime image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container (adjust the name as needed)
COPY target/BuildAzan-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your app runs on (usually 8080)
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
