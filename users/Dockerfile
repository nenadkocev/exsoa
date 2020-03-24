FROM openjdk:13-jdk-alpine
VOLUME /tmp
ADD target/users-0.0.1-SNAPSHOT.jar users.jar
ENTRYPOINT ["java", "-jar", "/users.jar"]