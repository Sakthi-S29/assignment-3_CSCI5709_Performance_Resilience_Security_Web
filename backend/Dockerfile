# Stage 1: Build the application using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a lightweight JRE
FROM eclipse-temurin:21-jre
RUN apt-get update && apt-get install -y --no-install-recommends ca-certificates && update-ca-certificates
ENV JAVA_TOOL_OPTIONS="-Djdk.tls.client.protocols=TLSv1.2"
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]

#Docker build command
# docker build -t dineconnect-backend .

#Docker run command
# docker run -d -p 8080:8080 --name my-backend-app --env-file .env  dineconnect-backend

#Manual Deployment DockerHub
#docker build --platform linux/amd64 -t 9626492176/dineconnect-backend:latest .
#docker push 9626492176/dineconnect-backend:latest
