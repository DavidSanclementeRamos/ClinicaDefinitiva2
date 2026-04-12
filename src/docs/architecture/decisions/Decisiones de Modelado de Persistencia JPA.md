
# ADR-Decisiones de Modelado de Persistencia JPA

**Estado:** Aceptado  
**Fecha:** 2026-03-10  
**Autor:** David  
**Contexto:** ClinicaDeFinitiva — capa de infraestructura / persistencia

---

## Contexto

Al pasar del modelo de dominio (hexagonal, DDD) a entidades JPA surgieron varias decisiones de diseño no triviales que afectan el esquema físico de la base de datos. Este ADR documenta cada decisión, la alternativa considerada y la razón del resultado elegido.

---

## Decisión 1: `Person` como `@Embeddable` dentro de cada actor

### Problema
`Person` es un Value Object compartido por `Dentist`, `Patient`, `Guardian` y `Receptionist`. Hay dos formas de persistirlo:

**Opción A — Tabla separada `persona_datos` (polimórfica)**  
Una sola tabla con columnas `id_propietario` (FK) y `tipo_propietario` (discriminador). Un JOIN por cada lectura de actor.

**Opción B — `@Embeddable` (elegida)**  
Las columnas de `Person` se incorporan directamente en la tabla de cada actor (`dentista`, `paciente`, `responsable`, `recepcionista`).

### Decisión
`@Embeddable` con `@AttributeOverrides` donde sea necesario.

### Justificación
- Las lecturas de actores son frecuentes y siempre necesitan los datos de persona. Un JOIN extra implica coste sin beneficio.
- `Person` no tiene identidad propia en el dominio; como VO puro, su ciclo de vida está ligado al actor.
- La tabla polimórfica dificulta las constraints `NOT NULL` y los índices por tipo de actor.
- El modelo de datos no requiere consultar `Person` de forma independiente del actor.

### Consecuencias
- Hay duplicación de columnas a nivel DDL (las mismas columnas aparecen en cuatro tablas), pero esto es correcto porque cada actor es un agregado independiente.
- Si en el futuro se necesita buscar actores por documento de identidad de forma transversal, se deberá agregar un índice compuesto o una vista.

---

## Decisión 2: `ServiceDetails` como tablas hijas con `@OneToOne` + `@MapsId`

### Problema
`ServiceDetails` es una interfaz del dominio con seis implementaciones (`AestheticDetails`, `ImplantologyDetails`, `OrthodonticDetails`, `PediatricDetails`, `ProstheticDetails`, `SurgicalDetails`). Cada una tiene campos completamente distintos y validaciones de negocio propias.

**Opción A — Columna JSON `service_details_json`**  
Un solo campo `TEXT` en `servicio_odontologico` con el JSON del detalle correspondiente.

**Opción B — Tabla única con columnas nullables (Single Table)**  
Una tabla `servicio_detalle` con todas las columnas de todos los subtipos, con muchas columnas `NULL`.

**Opción C — Tablas separadas por subtipo con `@OneToOne` (elegida)**  
Seis tablas (`servicio_detalle_estetico`, `servicio_detalle_implantologia`, etc.), cada una con PK = FK a `servicio_odontologico`.

### Decisión
Tablas separadas por subtipo (Joined 1:1), usando `@MapsId` para que el PK de la tabla hija sea el mismo que el de `servicio_odontologico`.

### Justificación
- Cada subtipo tiene validaciones de negocio activas (ej. `requiresBoneGraft → healingTimeMonths >= 4`). Con JSON o Single Table se pierde la posibilidad de agregar constraints `CHECK` en la base de datos.
- Las consultas por tipo de servicio (ej. "todos los servicios de cirugía con complejidad CRITICAL") son posibles con índices normales; con JSON requieren funciones JSON o full-text search.
- No hay desperdicio de columnas `NULL` como en Single Table.
- El campo `tipo_servicio` en `servicio_odontologico` actúa como discriminador lógico para saber qué tabla hija consultar, sin necesidad de probar las seis relaciones.
- `@MapsId` garantiza que la FK es también la PK, evitando un `id` secundario en las tablas hijas y reforzando la integridad referencial.

### Consecuencias
- Una lectura de servicio con detalles requiere un JOIN adicional a la tabla hija correspondiente. Se mitiga con `FetchType.LAZY` y carga explícita solo cuando se necesitan los detalles.
- Al crear un `ProvidedService`, se debe crear también la entidad de detalle correspondiente en el mismo `EntityManager` dentro de la misma transacción.

---

## Decisión 3: `Permission` como `@ElementCollection` en `Rol`

### Problema
`Set<Permission>` en el agregado `Rol` puede persistirse de dos formas:

**Opción A — Entidad `PermisoJpaEntity`**  
Una tabla `rol_permiso` con su propia PK UUID, entidad Java completa y repositorio.

**Opción B — `@ElementCollection` (elegida)**  
La misma tabla `rol_permiso` pero gestionada directamente por JPA desde `RolJpaEntity`, sin entidad ni repositorio propio.

### Decisión
`@ElementCollection` con `@CollectionTable`.

### Justificación
- `Permission` es un Value Object sin identidad. En el dominio se accede siempre desde `Rol`, nunca de forma independiente.
- Crear una entidad con repositorio implicaría romper la encapsulación del agregado `Rol`.
- `@ElementCollection` borra y recrea la colección en bloque al actualizar los permisos, lo cual es correcto porque los permisos se asignan como conjunto atómico.

### Consecuencias
- No es posible hacer queries JPQL directamente sobre permisos individuales sin pasar por `Rol`.
- La operación de actualización de permisos hace `DELETE + INSERT` en bloque, lo cual es aceptable dado que los roles no cambian con alta frecuencia.

---

## Decisión 4: `Invoice` con columnas `id_paciente` e `id_contrato` ambas nullable (discriminación por tipo)

### Problema
`Invoice` tiene dos modos de creación:
- `createParticular`: tiene `patientId`, no tiene `contractId`.
- `createInstitutional`: tiene `contractId`, no tiene `patientId`.

**Opción A — Tablas separadas `factura_particular` y `factura_institucional`**  
Dos tablas con herencia o discriminador.

**Opción B — Una sola tabla con FKs nullable (elegida)**  
Tabla única `factura` donde `id_paciente` y `id_contrato` son ambas opcionales, y el estado del objeto garantiza que siempre hay al menos una.

### Decisión
Tabla única con FKs nullable, validación de regla de negocio en el dominio.

### Justificación
- Las consultas sobre facturas (reportes, estados de pago) aplican a ambos tipos de forma uniforme. Tener dos tablas complicaría los `UNION` o las vistas de reportería.
- La invariante "al menos una de las dos FK debe estar poblada" se garantiza en el factory method del dominio, no en la base de datos. Esto es coherente con la arquitectura hexagonal: las reglas de negocio viven en el dominio.
- Un constraint `CHECK (id_paciente IS NOT NULL OR id_contrato IS NOT NULL)` se puede agregar a nivel de DDL si se desea una segunda línea de defensa.

### Consecuencias
- Los mappers de persistencia deben manejar ambas FKs como opcionales.
- Una consulta "facturas de un paciente" filtra por `id_paciente IS NOT NULL AND id_paciente = :id`.

---

## Decisión 5: `Payer` VO polimórfico como columnas `tipo_pagador` + `id_referencia_pagador`

### Problema
`Payer` en el agregado `Payment` puede ser un paciente o una institución (representada por un contrato). Es un VO con polimorfismo de tipo.

**Opción A — FK a `paciente` y FK a `contrato` nullables**  
Dos columnas de FK, igual que la decisión de `Invoice`.

**Opción B — Columnas de tipo + referencia BIGINT (elegida)**  
`tipo_pagador VARCHAR(30)` + `id_referencia_pagador BIGINT`. La FK queda implícita, sin constraint declarado en DDL.

### Decisión
Columnas de tipo + referencia BIGINT sin FK declarada.

### Justificación
- `Payer` en el dominio es un VO, no una entidad. No tiene una tabla propia ni una FK natural.
- El `tipo_pagador` actúa como discriminador: `PATIENT` → `id_referencia_pagador` referencia `paciente.id`; `INSTITUTION` → referencia `contrato.id`.
- Esta flexibilidad es intencional: si en el futuro se agrega un nuevo tipo de pagador (ej. `INSURANCE`), solo se agrega un nuevo valor al enum, sin alterar el esquema.
- La integridad referencial queda en manos del dominio y de los tests de integración, lo cual es aceptable en arquitectura hexagonal donde la BD es un detalle de infraestructura.

### Consecuencias
- No hay FK declarada en DDL para `id_referencia_pagador`, lo que significa que la base de datos no garantiza automáticamente que el BIGINT exista en alguna tabla.
- Se deben agregar tests de integración que verifiquen la consistencia de esta relación.

---

## Decisión 6: `AgeRange` descompuesto en dos columnas en `servicio_detalle_pediatria`

### Problema
`AgeRange` es un VO con dos campos enteros: `minAge` e `maxAge`. Puede persistirse de dos formas:

**Opción A — Tabla `rango_edad` separada**  
Una tabla con PK propia, FK desde `servicio_detalle_pediatria`.

**Opción B — Dos columnas directas (elegida)**  
`rango_edad_min INT` y `rango_edad_max INT` directamente en `servicio_detalle_pediatria`.

### Decisión
Dos columnas directas.

### Justificación
- `AgeRange` es un VO extremadamente simple (dos enteros). Crear una tabla separada para él no agrega expresividad al modelo y sí agrega un JOIN innecesario.
- El patrón correcto en JPA para VOs simples sin identidad es `@Embeddable`, y en este caso la tabla de detalle ya es una tabla 1:1 con el servicio, por lo que las columnas se incorporan directamente.
- Los constraints de negocio (`minAge >= 0`, `maxAge <= 18`, `minAge <= maxAge`) se aplican en el VO del dominio y pueden reforzarse con `CHECK` constraints en DDL.

### Consecuencias
- Si `AgeRange` se utiliza en otros contextos en el futuro, el patrón podría cambiar a `@Embeddable` reutilizable. Por ahora, la duplicación es inexistente porque solo `PediatricDetails` lo usa.

---

## Decisión 7: `WorkingHours` en `Dentist` como JSON

### Problema
`WorkingHours` es un VO complejo que representa el horario semanal de un dentista. Su estructura interna puede evolucionar.

### Decisión
Columna `horas_trabajo_json TEXT` serializada como JSON.

### Justificación
- `WorkingHours` es un VO consultado siempre en el contexto del dentista, nunca de forma independiente.
- Su estructura puede cambiar con el tiempo (agregar franjas, excepciones, festivos) sin requerir migraciones de esquema.
- JPA con Hibernate 6 soporta tipos JSON nativamente con `@JdbcTypeCode(SqlTypes.JSON)` si se desea tipado fuerte.

### Consecuencias
- No es posible filtrar dentistas por horario directamente en SQL sin funciones JSON. Si se necesita esa capacidad, se deberá refactorizar a columnas relacionales o agregar índices JSON (disponibles en PostgreSQL y MySQL 8+).

---

## Decisión 8: IDs como `Long` (`BIGINT`) en lugar de `UUID`

### Problema
Los IDs de todos los agregados y entidades de valor persistidas pueden representarse como `UUID` (128 bits, generado por la aplicación) o como `Long` con autoincremento delegado a la base de datos.

**Opción A — UUID con `GenerationType.UUID`**  
IDs opacos generados por Hibernate 6. Independientes de la secuencia de la BD. Distribuibles (sin colisión en sharding).

**Opción B — Long con `GenerationType.IDENTITY` (elegida)**  
La BD asigna el ID en cada `INSERT`. Tipo `BIGINT AUTO_INCREMENT` (MySQL) o `BIGSERIAL` (PostgreSQL).

### Decisión
`Long` con `GenerationType.IDENTITY` en todas las entidades JPA.

### Justificación
- `BIGINT` ocupa 8 bytes vs 16 bytes de UUID. En tablas de alto volumen (`cita`, `pago`, `linea_asiento`) el ahorro en índices es significativo.
- Los JOINs sobre `BIGINT` son más eficientes en todos los motores relacionales; los índices B-tree funcionan mejor con claves enteras ordenadas.
- `GenerationType.IDENTITY` simplifica la configuración de Hibernate: no se requiere ninguna dependencia adicional ni configuración de dialect para la generación de UUIDs.
- La compatibilidad con `@MapsId` (tablas de detalle `servicio_detalle_*`, `resultado_cita`) es directa: el campo `@Id` pasa de `UUID` a `Long` sin cambiar la semántica de la relación 1:1.
- ClinicaDefinitiva es un sistema clínico de una sola instancia (no distribuido), por lo que las ventajas de UUID para sharding no aplican en este contexto.

### Consecuencias
- Los IDs son secuenciales y predecibles, lo que permite a un actor externo inferir el volumen de registros si los endpoints exponen el `id` directamente. **Mitigación:** en endpoints REST públicos se puede exponer un campo adicional `codigoPublico` (String/UUID generado por la aplicación) manteniendo el `id Long` como identificador interno exclusivo.
- La generación de ID depende de la BD: no se puede conocer el ID de un agregado antes de persistirlo. Los eventos de dominio que incluyan el ID deben publicarse después del `save()`.
- Al migrar a una arquitectura distribuida en el futuro, se deberá evaluar el cambio a UUIDs o a snowflake IDs.

---

## Relación con otras ADRs

| ADR    | Relación |
|--------|----------|
| ADR-50 | Simplificación de VOs — refuerza el uso de `@Embeddable` para VOs simples |
| ADR-51 | Validación nula en agregados — las validaciones de dominio reemplazan constraints DB |
| ADR-38 | RBAC/ABAC — `Permission` como VO justifica `@ElementCollection` |
| ADR-002 | `UserStatus` vs `AvailabilityStatus` — dos columnas de estado en entidades de actor |

---

## Notas de implementación

- Todos los IDs primarios son `Long` generados con `GenerationType.IDENTITY`, mapeados a `BIGINT AUTO_INCREMENT` (MySQL) o `BIGSERIAL` (PostgreSQL). Ver Decisión 8.
- Los campos de auditoría (`creado_en`, `actualizado_en`) se gestionan con `@PrePersist` / `@PreUpdate` en un `@MappedSuperclass` de auditoría (pendiente de implementar).
- El esquema DDL se genera con `spring.jpa.hibernate.ddl-auto=validate` en producción y `create-drop` en el perfil `test`.
- Los nombres de tablas y columnas están en español para homogeneidad con el lenguaje ubicuo del negocio en Colombia.


