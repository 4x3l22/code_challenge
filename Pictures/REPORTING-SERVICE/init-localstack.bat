@echo off
echo =====================================
echo  INICIALIZANDO RECURSOS EN LOCALSTACK
echo =====================================

echo.
echo Esperando que LocalStack este listo...
timeout /t 10 /nobreak >nul

echo.
echo Configurando AWS CLI para LocalStack...
set AWS_ACCESS_KEY_ID=test
set AWS_SECRET_ACCESS_KEY=test
set AWS_DEFAULT_REGION=us-east-1

echo.
echo Creando tabla DynamoDB...
aws --endpoint-url=http://localhost:4566 dynamodb create-table ^
  --table-name local-crediya-reports ^
  --attribute-definitions ^
    AttributeName=pk,AttributeType=S ^
    AttributeName=metricId,AttributeType=S ^
    AttributeName=updatedAt,AttributeType=S ^
  --key-schema ^
    AttributeName=pk,KeyType=HASH ^
  --global-secondary-indexes ^
    "IndexName=metricId-updatedAt-index,KeySchema=[{AttributeName=metricId,KeyType=HASH},{AttributeName=updatedAt,KeyType=RANGE}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5}" ^
  --provisioned-throughput ^
    ReadCapacityUnits=5,WriteCapacityUnits=5

echo.
echo Creando cola SQS...
aws --endpoint-url=http://localhost:4566 sqs create-queue ^
  --queue-name generate-reports-queue

echo.
echo Verificando recursos creados...
echo.
echo Tablas DynamoDB:
aws --endpoint-url=http://localhost:4566 dynamodb list-tables

echo.
echo Colas SQS:
aws --endpoint-url=http://localhost:4566 sqs list-queues

echo.
echo Insertando datos de prueba en DynamoDB...
aws --endpoint-url=http://localhost:4566 dynamodb put-item ^
  --table-name local-crediya-reports ^
  --item "{\"pk\":{\"S\":\"local-global-report\"},\"metricId\":{\"S\":\"test-metric-001\"},\"updatedAt\":{\"S\":\"2025-01-01T00:00:00Z\"},\"reportType\":{\"S\":\"DAILY\"},\"status\":{\"S\":\"COMPLETED\"},\"data\":{\"S\":\"{\\\"totalUsers\\\":100,\\\"activeUsers\\\":85}\"}}"

echo.
echo Enviando mensaje de prueba a SQS...
aws --endpoint-url=http://localhost:4566 sqs send-message ^
  --queue-url http://localhost:4566/000000000000/generate-reports-queue ^
  --message-body "{\"reportType\":\"DAILY\",\"metricId\":\"test-metric-001\",\"requestedAt\":\"2025-01-01T00:00:00Z\"}"

echo.
echo =========================================
echo  LOCALSTACK INICIALIZADO CORRECTAMENTE
echo =========================================
echo.
echo URLs importantes:
echo - DynamoDB Admin: http://localhost:4566/_aws/dynamodb
echo - SQS Admin: http://localhost:4566/_aws/sqs
echo.
pause
