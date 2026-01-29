# ADR 32 (Arquitectura): Uso de DTO simplificado y DTO de detalle en capas de aplicación e infraestructura

**Fecha:** 26-01-2026  
**Estado:** Aprobado  
**Decisión:** Adoptar dos tipos de DTOs en la arquitectura:
- **DTO simplificado** para listados y paginación.
- **DTO de detalle** para vistas completas y operaciones específicas.

---

## Contexto
En la infraestructura clínica se manejan consultas que requieren diferentes niveles de información:

- **Listados/paginación:** el cliente necesita solo algunos campos básicos (ej. id, nombre, estado, especialidad) para mostrar en tablas o listados.
- **Detalle individual:** el cliente necesita toda la información del recurso (ej. horarios, dirección, datos clínicos, trazabilidad completa).

Hasta ahora, algunos endpoints devolvían listas con demasiada información, lo que generaba sobrecarga innecesaria, mientras que otros devolvían objetos incompletos en escenarios donde se requería detalle.

---

## Decisión
Se define un patrón estándar de dos niveles de DTOs:

1. **DTO simplificado (ej. `PageDentistDto` → `DentistPageResponse`)**
    - Contiene solo los campos mínimos necesarios para listados.
    - Se usa en endpoints de paginación y búsquedas.
    - Se devuelve siempre dentro de un `PageResponse<T>` para uniformidad.

2. **DTO de detalle (ej. `ReadDentistDto` → `DentistReadResponse`)**
    - Contiene todos los campos relevantes del recurso.
    - Se usa en endpoints de detalle (`findById`, `update`, etc.).
    - Permite trazabilidad clínica completa.

Este patrón se aplicará en **las dos capas superiores**:
- **Aplicación:** casos de uso devolverán `Page<DTO>` simplificado o `DTO` completo según corresponda.
- **Infraestructura (REST):** controladores mapearán a `PageResponse<T>` (simplificado) o `ReadResponse` (detalle).

---

## Motivo del cambio
- **Optimización:** evitar sobrecarga de datos en listados.
- **Claridad:** separar explícitamente entre vistas de listado y vistas de detalle.
- **Escalabilidad:** soportar grandes volúmenes de datos con respuestas ligeras.
- **Trazabilidad clínica:** garantizar que los endpoints de detalle siempre devuelvan información completa.
- **Uniformidad:** aplicar el mismo patrón en todas las capas superiores.

---

## Beneficios
- **Eficiencia:** respuestas más ligeras en listados, mejor rendimiento en consultas grandes.
- **Consistencia:** todos los módulos siguen el mismo patrón DTO dual.
- **Flexibilidad:** cada endpoint devuelve exactamente la información que necesita el cliente.
- **Auditoría:** los DTO de detalle garantizan trazabilidad completa en operaciones críticas.
- **Separación de responsabilidades:** la capa de aplicación define DTOs de negocio, la capa de infraestructura los expone en formato REST.

---

## Consecuencias
- Se requiere mantener dos mappers en cada módulo:
    - `toPageResponse(PageDto dto)`
    - `toResponse(ReadDto dto)`
- Los controladores deben diferenciar claramente entre endpoints de listado y de detalle.
- La documentación del catálogo clínico debe reflejar esta separación.
- Los casos de uso deben devolver el DTO correcto según el tipo de consulta.

---

## Ejemplo de implementación

### DTOs
```java
// Simplificado para listados
public record DentistPageResponse(
   Long dentistId,
   String specialties,
   String dni,
   String first,
   String lastName,
   String phoneNumber,
   String availabilityStatus
) { }

// Detalle completo
public record DentistReadResponse(
   Long dentistId,
   String specialties,
   String availabilityStatus,
   LocalTime start,
   LocalTime end,
   DayOfWeek dayOfWeek,
   String dni,
   String first,
   String lastName,
   String age,
   String phoneNumber,
   LocalDate dateOfBirth,
   String bloodType,
   String documentoEPS,
   String user,
   LocalDateTime lastUpdate,
   String street,
   String city,
   String state,
   String country,
   String postalCode
) { }
```
## controlador
```java
@GetMapping
public PageResponse<DentistPageResponse> findAll(...) { ... }

@GetMapping("/{id}")
public DentistReadResponse findById(@PathVariable Long id) { ... }
```
---

## Decisión arquitectónica
Este ADR establece el uso obligatorio de DTO simplificado y DTO de detalle en todas las capas superiores (aplicación e infraestructura).
Cualquier nuevo módulo deberá seguir esta convención para garantizar eficiencia, trazabilidad y uniformidad en la API clínica.