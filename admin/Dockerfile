FROM amazoncorretto:21-alpine-jdk
COPY target/admin-1.0.0-SNAPSHOT.jar admin-server.jar
ENTRYPOINT ["java","-jar","/admin-server.jar"]