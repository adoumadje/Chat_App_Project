# Stage 1: Build the application
FROM maven:3.9.5 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install

# Stage 2: Run the application
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build /app/target/chat-app-0.0.1-SNAPSHOT.jar ./chat-app.jar
CMD ["/bin/sh", "-c", "while ! nc -z chatdb_c0 3306; do sleep 1; done && java -jar chat-app.jar"]