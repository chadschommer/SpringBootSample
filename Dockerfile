FROM openjdk:8-jdk-alpine

RUN mkdir src
COPY . /src
WORKDIR /src

RUN apk add --no-cache maven
RUN ./mvnw package && java -jar target/sampleapp-*.jar

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]