FROM amazoncorretto:21-alpine-jdk
COPY target/converter-service-1.0.0-SNAPSHOT.jar converter-service.jar
ENTRYPOINT ["java","-jar","/converter-service.jar"]
#ENTRYPOINT ["java","-Dspring.profiles.active=integration-tests","-jar","/converter-service.jar"]
