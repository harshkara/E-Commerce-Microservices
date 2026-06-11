FROM openjdk:22-jdk
WORKDIR /app
COPY target/task-service.jar task-service.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","task-service.jar"]