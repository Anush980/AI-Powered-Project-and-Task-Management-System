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


# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 3 — Extract Spring Boot layers       ← YOUR VERSION SKIPPED THIS
# ───────────────────────────────────────────────────────────────────────────────
# Spring Boot 3 builds "layered JARs". Instead of one 80MB fat jar,
# it splits into 4 layers sorted by how often they change:
#
#   dependencies/          → 3rd party jars (Spring, Jackson etc)   — rarely changes
#   spring-boot-loader/    → Spring Boot bootstrap code              — almost never
#   snapshot-dependencies/ → your -SNAPSHOT deps                     — occasionally
#   application/           → YOUR actual code                        — every push (~2MB)
#
# Result: first deploy = 80MB transfer. Every deploy after = ~2MB transfer.
# Without this, every single push transfers the full 80MB image.
# ═══════════════════════════════════════════════════════════════════════════════
FROM eclipse-temurin:17-jre-alpine AS extractor
WORKDIR /app

COPY --from=builder /app/target/app.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract


# ═══════════════════════════════════════════════════════════════════════════════
# STAGE 4 — Final production image
# ───────────────────────────────────────────────────────────────────────────────
# eclipse-temurin:17-jre-alpine  →  ~190MB  (runtime only, no compiler)
# eclipse-temurin:17-alpine      →  ~340MB  (full JDK, not needed in prod)
# maven:3.9-eclipse-temurin-17   →  ~580MB  (Maven + JDK, definitely not needed)
#
# We throw away all 3 previous stages. Only this image gets pushed to ECR.
# ═══════════════════════════════════════════════════════════════════════════════
FROM eclipse-temurin:17-jre-alpine

# ── Security: never run as root inside containers ──────────────────────────────
# If someone exploits your app, they get 'appuser' not root.
# Your version ran as root — easy fix, always do this.
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /app

# Copy each layer as a separate Docker layer.
# Docker diffs layers on push — only changed layers are transferred.
# Since 'application' is the only thing that changes each push,
# ECR only receives that ~2MB layer, not the 80MB image.
COPY --from=extractor /app/dependencies          ./dependencies
COPY --from=extractor /app/spring-boot-loader    ./spring-boot-loader
COPY --from=extractor /app/snapshot-dependencies ./snapshot-dependencies
COPY --from=extractor /app/application           ./application

# ── JVM flags tuned for containers ────────────────────────────────────────────
# -XX:+UseContainerSupport   → JVM reads Docker memory limits correctly.
#                              Without this, JVM sees host RAM (e.g. 16GB)
#                              and sets heap to 4GB on a 1GB container → OOM kill.
#
# -XX:MaxRAMPercentage=75.0  → Use 75% of container RAM for heap.
#                              Leaves 25% for OS, threads, off-heap.
#                              On t3.micro (1GB): heap = ~700MB.
#
# -XX:+UseG1GC               → G1 garbage collector. Best for web apps.
#                              Balances throughput vs pause time.
#
# -Djava.security.egd=...    → Faster startup on Linux.
#                              /dev/random blocks waiting for entropy.
#                              /dev/urandom never blocks.
#
# -cp ... JarLauncher        → Use the extracted layers instead of the fat jar.
#                              This is how Spring Boot's layered mode works.
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-cp", "dependencies/*:spring-boot-loader/*:snapshot-dependencies/*:application/*", \
  "org.springframework.boot.loader.launch.JarLauncher"]

# Documents which port the app uses (informational only — doesn't publish it)
EXPOSE 8080

# ── Health check ───────────────────────────────────────────────────────────────
# Docker and AWS use this to know if your container is actually ready.
# --start-period=60s → don't check for the first 60s (Spring Boot startup time)
# --interval=30s     → check every 30s after that
# --timeout=10s      → give it 10s to respond
# --retries=3        → mark unhealthy after 3 consecutive failures
# wget is built into alpine; curl is not (keeps image smaller)
HEALTHCHECK --interval=30s --timeout=10s --retries=3 --start-period=60s \
  CMD wget -q --spider http://localhost:8080/v1/health || exit 1