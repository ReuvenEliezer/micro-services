FROM amazoncorretto:17-alpine-jdk
COPY target/admin-0.0.1-SNAPSHOT.jar admin-service.jar
ENTRYPOINT ["java","-jar","/admin-service.jar"]