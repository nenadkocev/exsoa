FROM openjdk:13-jdk-alpine
VOLUME /tmp
ADD target/order-0.0.1-SNAPSHOT.jar order.jar
ENTRYPOINT ["java", "-jar", "/order.jar"]