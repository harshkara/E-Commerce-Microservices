FROM openjdk:22-jdk
WORKDIR /app
COPY target/auth-service.jar auth-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","auth-service.jar"]