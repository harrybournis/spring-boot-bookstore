FROM maven:3.9.1-sapmachine-17 AS build
COPY /src /usr/src/bookstore/src
COPY pom.xml Dockerfile /usr/src/bookstore
RUN mvn -f /usr/src/bookstore/pom.xml clean package -DskipTests

FROM openjdk:17-alpine
COPY --from=build /usr/src/bookstore/target/bookstore-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
