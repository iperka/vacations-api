FROM maven:3.6.3-jdk-11-slim AS build
LABEL maintainer="iperka"

RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean package -DskipTests spring-boot:repackage -DskipTests

FROM adoptopenjdk/openjdk11:jre-11.0.9.1_1-alpine
RUN apk add dumb-init
RUN mkdir /app
RUN addgroup --system spring && adduser -S -s /bin/false -G spring spring
COPY --from=build /project/target/*.jar /app/java-application.jar
WORKDIR /app
RUN chown -R spring:spring /app
USER spring

EXPOSE 8080

CMD "dumb-init" "java" "-jar" "java-application.jar"