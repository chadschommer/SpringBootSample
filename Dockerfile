#
# Build stage
#
FROM maven:3.6.3-adoptopenjdk-8 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -fX /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
# ARG JAR_FILE=/home/app/target/*.jar
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]