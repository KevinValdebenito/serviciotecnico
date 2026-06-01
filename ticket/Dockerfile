FROM eclipse-temurin:25-alpine as builder
WORKDIR /workspace
COPY gradle gradle
COPY gradlew .
COPY build.gradle settings.gradle 
COPY src src
RUN chmod +x ./gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /workspace/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
