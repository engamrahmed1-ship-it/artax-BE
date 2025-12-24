# Use Java 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Change the jar name below to match the service (e.g., crm-service.jar)
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]