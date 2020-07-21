FROM openjdk:8-jdk-alpine
RUN ./mvnw package && java -jar target/gs-spring-boot-docker-0.1.0.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]