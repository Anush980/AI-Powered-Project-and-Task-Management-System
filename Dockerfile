# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 1 — Dependency cache
# ═══════════════════════════════════════════════════════════════════════════════
FROM maven:3.9-eclipse-temurin-17 AS deps
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -q

# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 2 — Build the JAR
# ═══════════════════════════════════════════════════════════════════════════════
FROM deps AS builder
WORKDIR /app
COPY src ./src
RUN mvn package -DskipTests -q

# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 3 — Final production image
# ═══════════════════════════════════════════════════════════════════════════════
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
WORKDIR /app
COPY --from=builder /app/target/app.jar app.jar
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-XX:+UseG1GC", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=60s CMD wget -q --spider http://localhost:8080/v1/health || exit 1