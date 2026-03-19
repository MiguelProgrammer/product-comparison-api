FROM eclipse-temurin:21-jre-alpine

# Mantenha o Alpine atualizado com os patches de segurança mais recentes
# Isso resolve os erros de HIGH e CRITICAL do Trivy
RUN apk update && apk upgrade --no-cache

WORKDIR /app

# Copia o JAR gerado pelo Maven
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]