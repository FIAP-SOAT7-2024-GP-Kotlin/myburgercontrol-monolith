FROM eclipse-temurin:21-jdk-alpine AS build

COPY . /source/
COPY .editorconfig /source/

WORKDIR /source/

RUN ./gradlew clean build --no-daemon -x test

FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8080

RUN mkdir /app

COPY --from=build /source/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-jar","/app/spring-boot-application.jar"]
