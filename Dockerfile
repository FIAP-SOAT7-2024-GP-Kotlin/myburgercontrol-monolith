FROM eclipse-temurin:21-jdk-alpine AS build

COPY . /source/
COPY .editorconfig /source/

WORKDIR /source/

RUN cp .env.example .env
RUN ./gradlew clean build --no-daemon -x test

FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8080

RUN mkdir /app
RUN env

COPY --from=build /source/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar","/app/spring-boot-application.jar"]
