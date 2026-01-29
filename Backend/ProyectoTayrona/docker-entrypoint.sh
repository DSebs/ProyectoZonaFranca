#!/bin/sh
# ============================================
# Script de entrada para Docker
# Maneja la lectura de secretos desde archivos
# ============================================

set -e

# Función para leer secreto desde archivo
read_secret() {
    local secret_file="$1"
    local env_var="$2"
    
    if [ -f "$secret_file" ]; then
        export "$env_var"="$(cat "$secret_file")"
        echo "✓ Secreto cargado: $env_var"
    else
        echo "⚠ Archivo de secreto no encontrado: $secret_file"
    fi
}

echo "=========================================="
echo "Iniciando aplicación de Citas..."
echo "=========================================="

# Cargar secretos desde archivos
echo "Cargando secretos..."

read_secret "/run/secrets/db_password" "DB_PASSWORD"
read_secret "/run/secrets/mail_password" "MAIL_PASSWORD"
read_secret "/run/secrets/admin_password" "ADMIN_PASSWORD"

echo "=========================================="
echo "Configuración:"
echo "  - DB_HOST: ${DB_HOST:-localhost}"
echo "  - DB_PORT: ${DB_PORT:-5432}"
echo "  - DB_NAME: ${DB_NAME:-citas_db}"
echo "  - MAIL_HOST: ${MAIL_HOST:-smtp.gmail.com}"
echo "  - MAIL_ENABLED: ${MAIL_ENABLED:-true}"
echo "  - SERVER_PORT: ${SERVER_PORT:-8080}"
echo "=========================================="

# Ejecutar la aplicación Java
exec java \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-docker} \
    -jar /app/app.jar
