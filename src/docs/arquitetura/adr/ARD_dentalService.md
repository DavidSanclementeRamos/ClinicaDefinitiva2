# ADR 001: Estrategia de modelado y persistencia para ProvidedService con campos específicos consultables
Fecha: 2025-11-13

## Contexto
El módulo dental debe representar múltiples tipos de servicios (consulta general, ortodoncia, cirugía, prótesis, etc.). Hay requerimientos funcionales que exigen consultas/filtrados por campos específicos de ciertos tipos (por ejemplo, `treatment_duration_months` en ortodoncia) con rendimiento aceptable. El dominio debe ser expresivo y extensible, y la persistencia debe permitir consultas indexadas sobre esos campos específicos.

## Decisión
Adoptamos un único agregado raíz llamado `ProvidedService` que contiene los atributos comunes de cualquier servicio (id, name, code, catalog, baseRate, duration, requiresAuthorization, description, status, createdAt/updatedAt) y que modela los aspectos específicos mediante composición: un objeto de detalles por tipo (`ServiceDetails`) persistido en tablas separadas en relación one-to-one con la fila principal. Ejemplo concreto: tabla `provided_service` (campos comunes) + tabla `provided_service_orthodontic` (campos ortodoncia como `treatment_duration_months`, `appliance_type`, `requires_followup`). Se usa JPA/Hibernate y Spring Data JPA para la implementación. Se mantiene un único repositorio de dominio `ProvidedServiceRepository` con consultas (JOINs) hacia las tablas de detalles para permitir filtrados eficientes por campos específicos.

## Razonamiento
- Mantener una sola entidad raíz preserva la identidad única del servicio y las invariantes que aplican a todos los servicios.
- La composición con tablas dedicadas permite indexar y consultar eficientemente campos tipo-específicos sin sacrificar modelado fuerte en el dominio.
- Evita duplicación de repositorios/identidades que complicarían consistencia y transacciones.
- Permite añadir nuevos tipos añadiendo una tabla de detalles y mapeo, con mínimo impacto en la lógica compartida.

## Alternativas consideradas
1. Columna JSON (detalles en JSON dentro de la tabla principal)
    - Pros: alta flexibilidad, fácil extensión sin migraciones.
    - Contras: difícil indexar campos específicos; consultas por esos campos son menos eficientes o requieren mecanismos solo disponibles en ciertas BD; peor rendimiento para filtros frecuentes.

2. Single table con columnas opcionales para todos los tipos (discriminator + columnas para cada tipo)
    - Pros: consultas SQL directas sin joins.
    - Contras: tabla muy ancha y difícil de mantener; muchas columnas nulas; cada nuevo tipo requiere migración de esquema que degrada claridad.

3. Repositorios distintos por subtipo (agregados separados)
    - Pros: separación clara por tipo.
    - Contras: problemas de identidad y consistencia si un servicio debe ser tratado como concepto único; duplicación de lógica y consultas cruzadas más complejas.

## Consecuencias
- Ventajas:
    - Consultas intensivas por campos específicos son eficientes (índices en tablas de detalles).
    - Modelado claro: datos comunes en `ProvidedService`, datos específicos en sus propias tablas/VOs.
    - Extensibilidad controlada: agregar un nuevo tipo exige añadir una tabla de detalles y mapping, sin romper el agregado raíz.
- Inconvenientes:
    - Mayor complejidad en la persistencia (más tablas, mapping one-to-one, cascades).
    - Necesidad de escribir y mantener queries/joins para búsquedas por campos específicos.
    - Migraciones por cada nuevo tipo (crear tabla y añadir índices).

## Requisitos de implementación derivados
- Esquema SQL: tabla principal `provided_service` y tablas one-to-one por tipo (ej. `provided_service_orthodontic`). Índices en columnas consultadas frecuentemente (p. ej. `treatment_duration_months`, `code`, `service_type`, `catalog_category`).
- Entidad JPA `ProvidedService` como agregado raíz y entidades JPA para detalles (ej. `OrthodonticDetailsEntity`) con `@OneToOne` y `@MapsId`.
- Repositorio `ProvidedServiceRepository` (Spring Data JPA) con métodos para búsquedas por tipo y para consultas que hagan JOIN con tablas de detalles.
- Fábrica de creación que construya la entidad principal y su entidad de detalles según `serviceType`.
- DTOs: DTO base para campos comunes y DTOs específicos para detalles por tipo (validación con Bean Validation).
- Mappers para convertir entre DTOs y dominio, gestionando la asociación con la entidad de detalles.
- Documentar en ADR este patrón y el procedimiento para añadir nuevos tipos (migración, mapper, DTO, pruebas).

## Reglas operativas y recomendaciones
- Indexar columnas específicas que se esperan consultar con frecuencia.
- Mantener las invariantes de negocio en el agregado raíz y delegar validaciones tipo-específicas a las clases de detalle o a fábricas/domain services.
- Gestionar transacciones a nivel de application service para asegurar atomicidad al crear/actualizar ProvidedService y su detalles.
- Proveer tests de integración que cubran consultas por campos tipo-específicos y la correcta persistencia cascada.
- Documentar la adición de un nuevo tipo en un checklist (DDL, entidad, repositorio si necesario, mapper, DTOs y tests).

## Estado
Decisión tomada: implementar composición con tablas separadas (one-to-one) y repositorio único para `ProvidedService`. Este ADR documenta la motivación técnica y los pasos para seguir el patrón en futuras extensiones.
