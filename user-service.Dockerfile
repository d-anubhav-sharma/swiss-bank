FROM maven:3.8.4-openjdk-17 AS builder
ARG APP_NAME=user-service
WORKDIR /app/$APP_NAME
COPY $APP_NAME/pom.xml .
COPY $APP_NAME/src ./src
COPY $APP_NAME/*jar .
COPY swiss-bank-common/target/swiss-bank-common-0.0.1-SNAPSHOT.jar .

RUN mkdir -p ~/.m2/repository/com/shaan/swiss-bank-common/0.0.1-SNAPSHOT/
RUN cp swiss-bank-common-0.0.1-SNAPSHOT.jar ~/.m2/repository/com/shaan/swiss-bank-common/0.0.1-SNAPSHOT/ 
RUN ls -lrt ~/.m2/repository/com/shaan/swiss-bank-common/0.0.1-SNAPSHOT/
RUN mvn package -DskipTests

FROM openjdk:17-jdk
ARG APP_NAME=user-service
WORKDIR /app/$APP_NAME
COPY --from=builder /app/$APP_NAME/target/$APP_NAME-*.jar $APP_NAME.jar
EXPOSE 10001
ENTRYPOINT java -jar user-service.jar
