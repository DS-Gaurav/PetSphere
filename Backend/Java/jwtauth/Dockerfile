# Use an official OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the host to the container
COPY target/jwtauth-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 9090

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
