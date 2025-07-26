# code_challenge

```bash
docker run -d --name mysql-db -e MYSQL_ROOT_PASSWORD=mysecretpassword -e MYSQL_DATABASE=prueba -e MYSQL_USER=usuario -e MYSQL_PASSWORD=mysecretpassword -p 3306:3306 mysql:8.0

docker exec -it mysql-db mysql -u root -pmysecretpassword -e "ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'mysecretpassword'; ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'mysecretpassword'; FLUSH PRIVILEGES;"
```

Excelente! Tu contenedor MySQL está completamente configurado. Aquí tienes toda la información:

---

## 🛠️ Configuraciones específicas por gestor

### MySQL Workbench
1. Connection Method: Standard (TCP/IP)
2. Hostname: localhost
3. Port: 3306
4. Username: root
5. Password: mysecretpassword
6. IMPORTANTE: En "Advanced" → "Others" agregar: `allowPublicKeyRetrieval=true&useSSL=false`

### DBeaver
1. Server Host: localhost
2. Port: 3306
3. Database: prueba
4. Username: root
5. Password: mysecretpassword
6. En "Connection settings" → "Driver properties":
    - allowPublicKeyRetrieval: `true`
    - useSSL: `false`

### DataGrip
1. Host: localhost
2. Port: 3306
3. User: root
4. Password: mysecretpassword
5. En "Advanced" → "VM options": `-Dallowpublickeyretrieval=true`

### HeidiSQL
*(Configurar como conexión MySQL TCP/IP con usuario y clave)*

---

## 📊 Configuración del Contenedor MySQL

- **Contenedor:** mysql-db (✅ corriendo)
- **Imagen:** mysql:8.0
- **Puerto:** 3306 (host) → 3306 (contenedor)
- **Base de datos:** prueba (✅ creada automáticamente)

---

## 👤 Credenciales de acceso

**Usuario root:**
- Usuario: `root`
- Contraseña: `mysecretpassword`

**Usuario aplicación:**
- Usuario: `usuario`
- Contraseña: `mysecretpassword`

---

## 🔧 Configuración para tu `application.properties` de Spring Boot

```properties
spring.application.name=prueba
server.servlet.context-path=/prueba
server.port=8080
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Configuración MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/prueba
spring.datasource.username=usuario
spring.datasource.password=mysecretpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configuración específica para MySQL
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

---

## 📦 Dependencia necesaria en tu `pom.xml`

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## 🔗 Comandos útiles para gestionar el contenedor

```bash
# Parar el contenedor
docker stop mysql-db

# Iniciar el contenedor
docker start mysql-db

# Conectarse a MySQL
docker exec -it mysql-db mysql -u usuario -pmysecretpassword prueba

# Ver logs del contenedor
docker logs mysql-db
```
