FROM eclipse-temurin:17-jdk
ARG JAR_FILE=target/shopcart-service-0.0.1.jar
COPY ${JAR_FILE} app_shopcart.jar
ENTRYPOINT ["java", "-jar", "app_shopcart.jar"]