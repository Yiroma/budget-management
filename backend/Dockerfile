FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:resolve

COPY src ./src

EXPOSE 8080

CMD ["./mvnw", "spring-boot:run"]
