FROM maven:3.8-openjdk-11 AS build
COPY /src /usr/src/bookstore/src
COPY pom.xml /usr/src/bookstore
COPY Dockerfile /usr/src/bookstore
RUN mvn -f /usr/src/bookstore/pom.xml clean package -DskipTests


FROM openjdk:11.0.10-jre
COPY --from=build /usr/src/bookstore/target/bookstore-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]
