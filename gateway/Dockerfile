FROM amazoncorretto:21-alpine-jdk
COPY target/gateway-1.0.0-SNAPSHOT.jar gateway.jar
ENTRYPOINT ["java","-jar","/gateway.jar"]
