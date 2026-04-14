

# Guía de configuración del entorno de desarrollo

Esta guía te ayudará a poner en marcha el proyecto localmente, tanto para ejecución como para desarrollo.

---

## Requisitos previos

- **Java 17** o superior (recomendado: Eclipse Temurin o OpenJDK)
- **Maven 3.8+** (o usar el wrapper `./mvnw`)
- **Base de datos**: MySQL 8+ o PostgreSQL 14+
- **Git** (para clonar el repositorio)
- (Opcional) **Docker** y **Docker Compose** para levantar la base de datos rápidamente

---

## 1. Clonar el repositorio

```bash
git clone https://github.com/DavidSanclementeRamos/ClinicaDefinitiva2.git
cd ClinicaDefinitiva2
```

---

## 2. Configurar la base de datos

### Opción A: Usar Docker (recomendado para pruebas rápidas)

```bash
# Levantar MySQL
docker run --name clinica-mysql -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=clinica -p 3306:3306 -d mysql:8

# O PostgreSQL
docker run --name clinica-postgres -e POSTGRES_PASSWORD=root -e POSTGRES_DB=clinica -p 5432:5432 -d postgres:15
```

### Opción B: Base de datos local

Asegúrate de que tu servidor MySQL/PostgreSQL esté corriendo. Crea una base de datos llamada `clinica` (o el nombre que prefieras).

---

## 3. Configurar el archivo `application.properties`

Copia el archivo de ejemplo y edítalo:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Edita las propiedades según tu entorno (cambia usuario, contraseña, URL de la BD).

Ejemplo para MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clinica?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

Para PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/clinica
spring.datasource.username=postgres
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```


---

## 4. Ejecutar la aplicación

### Con Maven wrapper (recomendado)

```bash
./mvnw spring-boot:run
```

### Con Maven instalado localmente

```bash
mvn spring-boot:run
```

La aplicación arrancará en `http://localhost:8080`.

---

## 5. Verificar que todo funciona

Abre tu navegador o usa `curl`:

```bash
curl http://localhost:8080/actuator/health
```

Deberías recibir `{"status":"UP"}`.

Si la API tiene Swagger habilitado, visita:  
`http://localhost:8080/swagger-ui.html`

---

## 6. Estructura de paquetes (para orientarte)

```
src/main/java/com/clinica/
├── domain/               # Agregados, VOs, servicios de dominio
├── application/          # Casos de uso, DTOs, mappers
├── infrastructure/       # Controladores REST, repositorios JPA, seguridad
└── shared/               # Utilidades comunes
```

Consulta [`../architecture/overview.md`](../architecture/overview.md) para más detalles.

---

## 7. Comandos útiles

| Acción | Comando |
|--------|---------|
| Ejecutar tests | `./mvnw test` |
| Ejecutar tests de integración | `./mvnw verify` |
| Compilar sin tests | `./mvnw clean compile -DskipTests` |
| Generar el JAR | `./mvnw clean package` |
| Ejecutar el JAR | `java -jar target/clinica-*.jar` |

---

## 8. Solución de problemas comunes

### Error de conexión a BD

- Verifica que el servidor de BD esté corriendo (`docker ps` o `systemctl status mysql`).
- Comprueba que las credenciales en `application.properties` sean correctas.
- Asegúrate de que el puerto no esté bloqueado por un firewall.

### Tests fallan por tiempo de espera

Puede ser que la BD de integración no esté disponible. Revisa la configuración de `testcontainers` (si se usa) o ejecuta los tests con perfiles específicos.

### La aplicación no arranca por clases no encontradas

Ejecuta `./mvnw clean compile` y luego `./mvnw spring-boot:run`.

---

## 9. Siguientes pasos

- Lee [`CONTRIBUTING.md`](../../CONTRIBUTING.md) si quieres aportar código.
- Revisa [Estrategia de Pruebas.md](test/ADR-Estrategia%20de%20Pruebas.md) para entender la estrategia de pruebas.
- Explora los ADRs en [`../architecture/decisions/`](../architecture/decisions/).





