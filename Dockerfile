FROM maven:3.9.6-eclipse-temurin-21 AS build
RUN mkdir -p /app
WORKDIR /app
COPY pom.xml /app
COPY src /app/src
RUN mvn -B -f pom.xml clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /app/target/*.jar app.jar

# copia a pasta de policies como arquivos reais
# (pega de target/classes, que é “resources” empacotado no build)
COPY --from=build /app/target/classes/templates/policies /app/templates/policies

EXPOSE 9999
ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "/app.jar"]