FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR gerado pelo Maven
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]