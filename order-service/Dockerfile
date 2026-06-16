FROM openjdk:22-jdk
WORKDIR /app
COPY target/order-service.jar order-service.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","order-service.jar"]