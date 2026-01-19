# ============================================
# Dockerfile para la aplicación de Citas
# Zona Franca - Sistema de Agendamiento
# ============================================

# --- Etapa 1: Build ---
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copiar archivos de Maven
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Dar permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Descargar dependencias (cache de Docker)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente
COPY src src

# Construir la aplicación
RUN ./mvnw clean package -DskipTests -B

# --- Etapa 2: Runtime ---
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Copiar el JAR desde la etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Crear directorio para secretos
RUN mkdir -p /run/secrets && \
    chown -R appuser:appgroup /run/secrets

# Script de entrada para manejar secretos
COPY --chmod=755 docker-entrypoint.sh /docker-entrypoint.sh

# Cambiar al usuario no-root
USER appuser

# Exponer puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/actuator/health || exit 1

# Punto de entrada
ENTRYPOINT ["/docker-entrypoint.sh"]
