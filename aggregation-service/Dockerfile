FROM amazoncorretto:17-alpine-jdk
COPY target/aggregation-service-1.0.0-SNAPSHOT.jar aggregation-service.jar
ENTRYPOINT ["java","-jar","/aggregation-service.jar"]