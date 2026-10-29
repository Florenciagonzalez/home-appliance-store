FROM eclipse-temurin:17-jdk
ARG JAR_FILE=target/sales-service-0.0.1.jar
COPY ${JAR_FILE} app_sales.jar
ENTRYPOINT ["java", "-jar", "app_sales.jar"]