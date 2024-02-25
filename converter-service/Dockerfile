FROM amazoncorretto:17-alpine-jdk
COPY target/converter-service-1.0-SNAPSHOT.jar converter-service.jar
ENTRYPOINT ["java","-jar","/converter-service.jar"]