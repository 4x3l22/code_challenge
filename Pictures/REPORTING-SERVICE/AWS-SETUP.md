# 🚀 CONFIGURACIÓN AWS PARA DOCKER

## ⚠️ PASOS IMPORTANTES ANTES DE CONTINUAR

### 1. **Configurar Credenciales AWS**

Edita el archivo `.env.aws` y reemplaza estas líneas:
```env
AWS_ACCESS_KEY_ID=AKIA...tu_access_key_aqui
AWS_SECRET_ACCESS_KEY=tu_secret_key_aqui
```

Con tus credenciales reales de AWS.

### 2. **Verificar Recursos AWS**

Asegúrate de que estos recursos existen en tu cuenta AWS:

#### **Tabla DynamoDB:**
- **Nombre**: `crediya-reports`
- **Región**: `us-east-2`
- **Partition Key**: `pk` (String)
- **Global Secondary Index**: `metricId-updatedAt-index`
  - **Hash Key**: `metricId` (String)
  - **Range Key**: `updatedAt` (String)

#### **Cola SQS:**
- **URL**: `https://sqs.us-east-2.amazonaws.com/889522049804/generate-reports-queue`
- **Región**: `us-east-2`

### 3. **Permisos IAM Necesarios**

Tu usuario/rol AWS debe tener permisos para:
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "dynamodb:PutItem",
        "dynamodb:GetItem",
        "dynamodb:UpdateItem",
        "dynamodb:DeleteItem",
        "dynamodb:Query",
        "dynamodb:Scan"
      ],
      "Resource": [
        "arn:aws:dynamodb:us-east-2:*:table/crediya-reports",
        "arn:aws:dynamodb:us-east-2:*:table/crediya-reports/index/*"
      ]
    },
    {
      "Effect": "Allow",
      "Action": [
        "sqs:ReceiveMessage",
        "sqs:DeleteMessage",
        "sqs:SendMessage",
        "sqs:GetQueueAttributes"
      ],
      "Resource": "arn:aws:sqs:us-east-2:889522049804:generate-reports-queue"
    }
  ]
}
```

## 🐳 **Comandos para Ejecutar**

### **Opción 1: Usar el script automático**
```cmd
run-docker.bat
# Selecciona opción B (AWS REAL)
```

### **Opción 2: Comandos manuales**
```cmd
# Con archivo .env.aws
docker-compose --env-file .env.aws up --build -d

# Ver logs
docker-compose logs -f reporting-service

# Parar servicio
docker-compose down
```

## 🔍 **Verificar Conexión**

Una vez que el contenedor esté ejecutándose:

```cmd
# Check health
curl http://localhost:8080/actuator/health

# Ver API documentation
# http://localhost:8080/swagger-ui
```

## ⚡ **Troubleshooting AWS**

### **Error de credenciales:**
```
Unable to load AWS credentials from any provider in the chain
```
**Solución**: Verifica que las credenciales en `.env.aws` sean correctas.

### **Error de tabla no encontrada:**
```
Table not found: crediya-reports
```
**Solución**: Crea la tabla DynamoDB en AWS Console o usando AWS CLI:
```bash
aws dynamodb create-table \
  --table-name crediya-reports \
  --attribute-definitions \
    AttributeName=pk,AttributeType=S \
    AttributeName=metricId,AttributeType=S \
    AttributeName=updatedAt,AttributeType=S \
  --key-schema \
    AttributeName=pk,KeyType=HASH \
  --global-secondary-indexes \
    'IndexName=metricId-updatedAt-index,KeySchema=[{AttributeName=metricId,KeyType=HASH},{AttributeName=updatedAt,KeyType=RANGE}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5}' \
  --provisioned-throughput \
    ReadCapacityUnits=5,WriteCapacityUnits=5 \
  --region us-east-2
```

### **Error de cola SQS:**
```
Queue does not exist or you do not have permission to access it
```
**Solución**: Crea la cola SQS en AWS Console o usando AWS CLI:
```bash
aws sqs create-queue \
  --queue-name generate-reports-queue \
  --region us-east-2
```

## 🔐 **Seguridad**

- ✅ `.env.aws` está en `.gitignore`
- ✅ Usa credenciales con permisos mínimos
- ✅ Considera usar IAM Roles en lugar de Access Keys
- ✅ Rota credenciales regularmente

## 📋 **Checklist Previo**

- [ ] Credenciales AWS configuradas en `.env.aws`
- [ ] Tabla DynamoDB `crediya-reports` existe
- [ ] Cola SQS `generate-reports-queue` existe
- [ ] Permisos IAM configurados correctamente
- [ ] Docker Desktop ejecutándose

¡Una vez completado este checklist, ejecuta `run-docker.bat` y selecciona opción B!
