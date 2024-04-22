FROM gradle:8.4-alpine AS build

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN cp .env.template .env
RUN gradle build --no-daemon -x test

FROM eclipse-temurin:17-jdk-alpine

EXPOSE 8080

RUN mkdir /app
RUN env

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-jar","/app/spring-boot-application.jar"]