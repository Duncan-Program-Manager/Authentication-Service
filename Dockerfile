FROM openjdk:11 as rabbitmq
EXPOSE 8084 3306
ADD target/Authentication-API-0.0.1-SNAPSHOT.jar authapi-docker.jar
ENTRYPOINT ["java","-jar","authapi-docker.jar"]