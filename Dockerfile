# Use Java 21
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# FIX: give execute permission to mvnw
RUN chmod +x mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline

# Copy rest of the project
COPY . .

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port (Render maps this automatically)
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "target/EveryDayHelp-0.0.1-SNAPSHOT.jar"]
