FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=builder /app/target/user-service-*.jar user-service.jar
EXPOSE 10001
ENTRYPOINT ["java", "-jar", "user-service.jar"]
