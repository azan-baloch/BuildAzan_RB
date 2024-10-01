FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/BuildAzan-0.0.1-SNAPSHOT.jar BuildAzan-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "BuildAzan-0.0.1-SNAPSHOT.jar"]