# Use Java 21 (matches your project)
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom first (for caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execute permission to mvnw
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline

# Copy the rest of the project
COPY . .

# Build the Spring Boot JAR
RUN ./mvnw clean package -DskipTests

# Expose port (Render uses PORT env internally)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/EveryDayHelp-0.0.1-SNAPSHOT.jar"]
