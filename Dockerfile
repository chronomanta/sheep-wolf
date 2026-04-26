# --- Build Stage ---
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy parent pom first for dependency caching
COPY pom.xml .
COPY sheepwolf-game/pom.xml sheepwolf-game/pom.xml

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source and build
COPY sheepwolf-game/src sheepwolf-game/src

RUN mvn -B package -DskipTests

# --- Runtime Stage ---
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=build /app/sheepwolf-game/target/sheepwolf-game-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

