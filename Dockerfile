# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 1 — Dependency cache
# ───────────────────────────────────────────────────────────────────────────────
# Why a separate stage just for dependencies?
# Docker caches each layer. If pom.xml hasn't changed, this entire stage
# is restored from cache → skips downloading the internet every build.
# Your version did this too — good. We keep it.
# ═══════════════════════════════════════════════════════════════════════════════
FROM maven:3.9-eclipse-temurin-17 AS deps
WORKDIR /app

COPY pom.xml .
# -q = quiet mode, cleaner CI logs
RUN mvn dependency:go-offline -q


# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 2 — Build the JAR
# ───────────────────────────────────────────────────────────────────────────────
# Separate from deps stage so if src changes but pom.xml doesn't,
# we skip Stage 1 cache entirely and jump straight here.
# ═══════════════════════════════════════════════════════════════════════════════
FROM deps AS builder
WORKDIR /app

COPY src ./src
# -DskipTests → tests already ran in GitHub Actions, don't run again here
# -DfinalName=app → always outputs target/app.jar, not target/aiproject-0.0.1-SNAPSHOT.jar
RUN mvn package -DskipTests -q -DfinalName=app


# Remove the extractor stage entirely, and change the final stage to:

FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

COPY --from=builder /app/target/app.jar app.jar

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=60s \
  CMD wget -q --spider http://localhost:8080/v1/health || exit 1
