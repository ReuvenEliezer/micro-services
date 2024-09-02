FROM amazoncorretto:17-alpine-jdk
COPY target/eureka-server-1.0.0-SNAPSHOT.jar eureka-server.jar
ENTRYPOINT ["java","-jar","/eureka-server.jar"]
#ENTRYPOINT ["java","-Dspring.profiles.active=integration-tests","-jar","/converter-service.jar"]
