# 🐳 CREDIYA REPORTING SERVICE - DOCKER

Guía completa para ejecutar el servicio de reportes en contenedores Docker.

## 📋 Prerrequisitos

- **Docker Desktop** instalado y ejecutándose
- **Windows 10/11** con PowerShell o CMD
- Al menos **4GB RAM** disponible
- Puertos **8080**, **4566**, **8081**, **9324** disponibles

## 🚀 Inicio Rápido

### Opción 1: Usar el Script Automático (Recomendado)
```cmd
# Ejecutar el script interactivo
run-docker.bat
```

### Opción 2: Comandos Manuales
```cmd
# Crear red de Docker
docker network create crediya-network

# Entorno de PRODUCCIÓN/AWS
docker-compose up --build -d

# Entorno LOCAL/DESARROLLO
docker-compose -f docker-compose.local.yml up --build -d
```

## 🌍 Entornos Disponibles

### 🔥 Entorno PRODUCCIÓN/AWS
- **Archivo**: `docker-compose.yml` + `.env`
- **Servicios**: Solo el reporting-service
- **AWS**: Usa servicios AWS reales (DynamoDB, SQS)
- **Para**: Testing con servicios reales

### 🔧 Entorno LOCAL/DESARROLLO
- **Archivo**: `docker-compose.local.yml` + `.env.local`
- **Servicios**: reporting-service + LocalStack + Wiremock
- **AWS**: Usa LocalStack (DynamoDB y SQS locales)
- **Para**: Desarrollo sin dependencias externas

## 📦 Servicios Incluidos

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| reporting-service | 8080 | API principal del servicio |
| LocalStack | 4566 | Simula servicios AWS (solo desarrollo) |
| Wiremock | 8081 | Mock del servicio IAM (solo desarrollo) |

## 🔧 Variables de Entorno

### Archivo `.env` (Producción/AWS)
```env
# Puerto y configuración básica
HOST_PORT=8080
REPORTING_SERVER_PORT=8080

# AWS - Configuración real
AWS_REGION=us-east-2
AWS_ACCESS_KEY_ID=changeme
AWS_SECRET_ACCESS_KEY=changeme

# DynamoDB
APP_DYNAMO_TABLE=crediya-reports

# SQS
ENTRYPOINT_SQS_QUEUEURL=https://sqs.us-east-2.amazonaws.com/889522049804/generate-reports-queue
```

### Archivo `.env.local` (Desarrollo)
```env
# Puerto y configuración básica
HOST_PORT=8080
REPORTING_SERVER_PORT=8080

# AWS - LocalStack
AWS_REGION=us-east-1
AWS_ACCESS_KEY_ID=test
AWS_SECRET_ACCESS_KEY=test

# DynamoDB Local
APP_DYNAMO_TABLE=local-crediya-reports

# SQS Local
ENTRYPOINT_SQS_QUEUEURL=http://localhost:9324/queue/generate-reports-queue
```

## 📍 URLs Importantes

### 🌐 Endpoints del Servicio
- **API Base**: http://localhost:8080
- **Health Check**: http://localhost:8080/actuator/health
- **Swagger UI**: http://localhost:8080/swagger-ui
- **Metrics**: http://localhost:8080/actuator/prometheus

### 🔧 Servicios de Desarrollo (solo entorno local)
- **LocalStack**: http://localhost:4566
- **IAM Mock**: http://localhost:8081

## 🛠 Comandos Útiles

### Gestión de Contenedores
```cmd
# Ver estado de contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f

# Ver logs específicos del servicio
docker-compose logs -f reporting-service

# Parar servicios
docker-compose down

# Reconstruir sin caché
docker-compose build --no-cache
```

### Troubleshooting
```cmd
# Verificar que Docker está corriendo
docker --version

# Ver redes de Docker
docker network ls

# Inspeccionar la red crediya
docker network inspect crediya-network

# Limpiar contenedores parados
docker container prune

# Limpiar imágenes huérfanas
docker image prune

# Limpieza completa (¡CUIDADO!)
docker system prune -a --volumes
```

## 🏗 Estructura del Proyecto

```
REPORTING-SERVICE/
├── applications/app-service/      # Aplicación principal
├── domain/
│   ├── model/                    # Modelos de dominio
│   └── usecase/                  # Casos de uso
├── infrastructure/
│   ├── driven-adapters/          # Adaptadores externos
│   ├── entry-points/             # Puntos de entrada
│   └── helpers/                  # Utilidades
├── Dockerfile                    # Configuración Docker
├── docker-compose.yml            # Compose para producción
├── docker-compose.local.yml      # Compose para desarrollo
├── .env                          # Variables producción
├── .env.local                    # Variables desarrollo
└── run-docker.bat               # Script automático
```

## 🔍 Health Checks

El servicio incluye health checks automáticos:

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 5
  start_period: 120s
```

## 🐛 Solución de Problemas

### ❌ Puerto 8080 en uso
```cmd
# Encontrar proceso usando el puerto
netstat -ano | findstr :8080

# Cambiar puerto en .env
HOST_PORT=8081
```

### ❌ Error de memoria
```cmd
# Reducir memoria JVM en .env
JAVA_OPTS=-XX:MaxRAMPercentage=50.0
```

### ❌ LocalStack no inicia
```cmd
# Verificar Docker Desktop está corriendo
# Verificar puertos 4566 y 9324 están libres
netstat -ano | findstr :4566
```

### ❌ Contenedor no arranca
```cmd
# Ver logs detallados
docker-compose logs reporting-service

# Verificar configuración
docker-compose config
```

## 📚 Recursos Adicionales

- [Docker Documentation](https://docs.docker.com/)
- [LocalStack Documentation](https://docs.localstack.cloud/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Clean Architecture](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

## 🤝 Contribución

Para contribuir al proyecto:

1. Haz cambios en el código
2. Reconstruye la imagen: `docker-compose build`
3. Prueba en entorno local: `docker-compose -f docker-compose.local.yml up`
4. Commit y push de cambios

---

**¿Tienes problemas?** Revisa la sección de troubleshooting o contacta al equipo de desarrollo.
