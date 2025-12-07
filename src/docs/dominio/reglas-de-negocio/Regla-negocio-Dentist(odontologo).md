# Reglas de negocio por agregado: Dentist

## Contexto histórico

Durante la etapa Java EE, el agregado Dentist no tenía reglas de negocio explícitas. Su estructura era técnica, centrada en persistencia, sin validaciones clínicas ni semánticas. En la migración hacia Spring Boot se implementaron tres reglas de forma intuitiva, pero sin reconocerlas como reglas de negocio. Fue recién con la adopción de arquitectura hexagonal que se reconoció su centralidad y se inició su implementación sistemática.

## Propósito

Las reglas de negocio del agregado Dentist definen las condiciones clínicas, operativas y éticas bajo las cuales un odontólogo puede ser registrado, habilitado, asignado, auditado y proyectado. Estas reglas son descubiertas por operación, no por tipo de dato, y están documentadas para exhibición, trazabilidad y evolución legítima.

## Reglas implementadas

### 1. Edad mínima para registro

- *Regla*: Un odontólogo debe tener al menos 18 años para ser registrado.
- *VO involucrado*: DateOfBirth
- *Justificación*: Requisito legal y ético para ejercer la profesión.
- *Error clínico asociado*: ERR_DENTIST_EDAD_INSUFICIENTE

### 2. Especialidad reconocida

- *Regla*: Un odontólogo debe tener una especialidad válida reconocida por el sistema.
- *VO involucrado*: Specialty
- *Justificación*: Garantiza que el profesional esté habilitado para el tipo de atención que ofrece.
- *Error clínico asociado*: ERR_DENTIST_ESPECIALIDAD_INVALIDA

### 3. Disponibilidad semanal no vacía

- *Regla*: Un odontólogo debe tener al menos una jornada semanal registrada para poder generar disponibilidades.
- *VO involucrado*: WeeklyAvailability
- *Justificación*: Evita que se registren profesionales sin planificación clínica.
- *Error clínico asociado*: ERR_DENTIST_SIN_JORNADA

### 4. Estado de usuario activo

- *Regla*: El odontólogo debe tener un estado de usuario activo para operar en el sistema.
- *VO involucrado*: UserStatus
- *Justificación*: Evita asignaciones a profesionales suspendidos, bloqueados o expirados.
- *Error clínico asociado*: ERR_DENTIST_USUARIO_INACTIVO

### 5. Teléfono válido y normalizado

- *Regla*: El número telefónico debe tener entre 7 y 15 dígitos, sin caracteres especiales.
- *VO involucrado*: PhoneNumber
- *Justificación*: Permite contacto confiable y evita duplicados.
- *Error clínico asociado*: ERR_DENTIST_TELEFONO_INVALIDO

### 6. Nombre completo válido

- *Regla*: El nombre y apellido deben tener al menos 2 caracteres y contener solo letras.
- *VO involucrado*: FullName
- *Justificación*: Mejora la trazabilidad clínica y evita errores de formato.
- *Error clínico asociado*: ERR_DENTIST_NOMBRE_INVALIDO

### 7. Dirección completa y válida

- *Regla*: La dirección debe contener calle, ciudad, código postal y país.
- *VO involucrado*: Direccion
- *Justificación*: Permite trazabilidad geográfica y planificación presencial.
- *Error clínico asociado*: ERR_DENTIST_DIRECCION_INCOMPLETA

## Proyección

- Se agregarán reglas para validación cruzada entre especialidad y tipo de cita.
- Se integrarán reglas para justificar cambios de disponibilidad y estado.
- Se documentarán reglas de auditoría por operación (registro, modificación, suspensión).
- Se vincularán reglas con métricas clínicas y reportes éticos.

## Relación con ADR

- [ADR-032: Implementación sistemática de reglas de negocio por agregado](../adr/ADR-032.md)
- [ADR-031: Implementación estratégica de Value Objects](../adr/ADR-031.md)
- [ADR-033: Catálogo de errores clínicos por operación](../adr/ADR-033.md)