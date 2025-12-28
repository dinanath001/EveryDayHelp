# Use Java 21
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy entire project
COPY . .

# FIX: give execute permission AFTER copy
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port (Render uses PORT env)
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "target/EveryDayHelp-0.0.1-SNAPSHOT.jar"]
