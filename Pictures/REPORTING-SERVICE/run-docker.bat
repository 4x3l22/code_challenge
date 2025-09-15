@echo off
echo ====================================
echo  CREDIYA REPORTING SERVICE - DOCKER
echo ====================================

echo.
echo Verificando que Docker este corriendo...
docker --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker no esta instalado o no esta corriendo
    echo Por favor instala Docker Desktop y asegurate de que este iniciado
    pause
    exit /b 1
)

echo.
echo Creando la red crediya-network si no existe...
docker network ls | find "crediya-network" >nul
if errorlevel 1 (
    echo Creando red crediya-network...
    docker network create crediya-network
) else (
    echo Red crediya-network ya existe
)

echo.
echo ENTORNOS DISPONIBLES:
echo =====================
echo A) PRODUCCION/AWS - Usa servicios AWS reales (.env)
echo B) AWS REAL - Usa servicios AWS reales con credenciales (.env.aws)
echo C) LOCAL/DESARROLLO - Usa LocalStack + mocks (.env.local)
echo.
set /p env_choice="Selecciona entorno (A/B/C): "

if /i "%env_choice%"=="A" (
    set compose_file=docker-compose.yml
    set env_name=PRODUCCION/AWS
    set env_file=.env
) else if /i "%env_choice%"=="B" (
    set compose_file=docker-compose.yml
    set env_name=AWS REAL
    set env_file=.env.aws
) else if /i "%env_choice%"=="C" (
    set compose_file=docker-compose.local.yml
    set env_name=LOCAL/DESARROLLO
    set env_file=.env.local
) else (
    echo Opcion invalida
    pause
    exit /b 1
)

echo.
echo Entorno seleccionado: %env_name%
echo Archivo compose: %compose_file%
echo.
echo OPCIONES:
echo ========
echo 1: Build y ejecutar
echo 2: Solo ejecutar (usar imagen existente)
echo 3: Build sin cache y ejecutar
echo 4: Ver logs
echo 5: Parar contenedores
echo 6: Limpiar todo (contenedores + imagenes + volumenes)
echo.
set /p choice="Selecciona una opcion (1-6): "

if "%choice%"=="1" goto build_and_run
if "%choice%"=="2" goto run_only
if "%choice%"=="3" goto build_no_cache
if "%choice%"=="4" goto show_logs
if "%choice%"=="5" goto stop
if "%choice%"=="6" goto cleanup
goto invalid_choice

:build_and_run
echo.
echo Construyendo y ejecutando el servicio...
echo Usando archivo de configuracion: %env_file%
docker-compose -f %compose_file% --env-file %env_file% up --build -d
goto check_health

:run_only
echo.
echo Ejecutando el servicio...
echo Usando archivo de configuracion: %env_file%
docker-compose -f %compose_file% --env-file %env_file% up -d
goto check_health

:build_no_cache
echo.
echo Construyendo sin cache y ejecutando...
echo Usando archivo de configuracion: %env_file%
docker-compose -f %compose_file% --env-file %env_file% build --no-cache
docker-compose -f %compose_file% --env-file %env_file% up -d
goto check_health

:show_logs
echo.
echo Mostrando logs en tiempo real (Ctrl+C para salir)...
docker-compose -f %compose_file% logs -f
pause
exit /b 0

:stop
echo.
echo Parando contenedores...
docker-compose -f %compose_file% down
echo Contenedores parados
pause
exit /b 0

:cleanup
echo.
echo ADVERTENCIA: Esto eliminara contenedores, imagenes y volumenes
set /p confirm="Estas seguro? (S/N): "
if /i "%confirm%"=="S" (
    echo Limpiando contenedores...
    docker-compose -f %compose_file% down -v --rmi all
    echo Limpiando imagenes huerfanas...
    docker image prune -f
    echo Limpiando volumenes huerfanos...
    docker volume prune -f
    echo Limpieza completada
) else (
    echo Operacion cancelada
)
pause
exit /b 0

:invalid_choice
echo Opcion invalida
pause
exit /b 1

:check_health
echo.
echo Esperando que el servicio este listo...
timeout /t 10 /nobreak >nul

echo.
echo Estado del contenedor:
docker-compose -f %compose_file% ps

echo.
echo Para ver logs en tiempo real: docker-compose -f %compose_file% logs -f
echo Para parar el servicio: docker-compose -f %compose_file% down
echo.
echo URLs del servicio:
echo - API: http://localhost:8080
echo - Health: http://localhost:8080/actuator/health
echo - Swagger: http://localhost:8080/swagger-ui
if /i "%env_choice%"=="C" (
    echo - LocalStack: http://localhost:4566
    echo - IAM Mock: http://localhost:8082
) else if /i "%env_choice%"=="B" (
    echo - Conectando a AWS real en region: us-east-2
)
echo.
pause
