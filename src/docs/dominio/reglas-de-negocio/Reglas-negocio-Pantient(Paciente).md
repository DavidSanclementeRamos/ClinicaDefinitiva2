# Reglas de negocio por agregado: Patient

## Contexto histórico

Durante la etapa Java EE, el agregado Patient no tenía reglas de negocio explícitas. Su estructura era técnica, centrada en persistencia, sin validaciones clínicas ni semánticas. En la migración hacia Spring Boot se implementaron validaciones más robustas, con excepciones personalizadas y manejo ético de errores, pero aún sin reconocerlas como reglas de negocio. Fue recién con la adopción de arquitectura hexagonal que se reconoció su centralidad y se inició su implementación sistemática.

## Propósito

Las reglas de negocio del agregado Patient definen las condiciones clínicas, operativas y éticas bajo las cuales un paciente puede ser registrado, atendido, vinculado a un responsable, auditado y proyectado. Estas reglas son descubiertas por operación, no por tipo de dato, y están documentadas para exhibición, trazabilidad y evolución legítima.

## Reglas implementadas

### 1. Edad mínima para autonomía

- *Regla*: Un paciente menor de edad debe estar vinculado a un responsable legal.
- *VO involucrado*: DateOfBirth
- *Justificación*: Requisito ético y legal para atención clínica de menores.
- *Error clínico asociado*: ERR_PATIENT_EDAD_INSUFICIENTE

### 2. Teléfono válido y normalizado

- *Regla*: El número telefónico debe tener entre 7 y 15 dígitos, sin caracteres especiales.
- *VO involucrado*: PhoneNumber
- *Justificación*: Permite contacto confiable y evita duplicados.
- *Error clínico asociado*: ERR_PATIENT_TELEFONO_INVALIDO

### 3. Nombre completo válido

- *Regla*: El nombre y apellido deben tener al menos 2 caracteres y contener solo letras.
- *VO involucrado*: FullName
- *Justificación*: Mejora la trazabilidad clínica y evita errores de formato.
- *Error clínico asociado*: ERR_PATIENT_NOMBRE_INVALIDO

### 4. Estado de usuario activo

- *Regla*: El paciente debe tener un estado de usuario activo para operar en el sistema.
- *VO involucrado*: UserStatus
- *Justificación*: Evita asignaciones a pacientes suspendidos, bloqueados o expirados.
- *Error clínico asociado*: ERR_PATIENT_USUARIO_INACTIVO

### 5. Documento de identidad válido

- *Regla*: El documento debe cumplir con longitud, tipo y formato según país.
- *VO involucrado*: Dni
- *Justificación*: Permite trazabilidad legal y evita duplicación de registros.
- *Error clínico asociado*: ERR_PATIENT_DNI_INVALIDO

### 6. Canal de reserva válido

- *Regla*: El canal de reserva debe ser uno de los reconocidos por el sistema (web, presencial, telefónico).
- *VO involucrado*: CanalReserva
- *Justificación*: Permite trazabilidad operativa y auditoría de origen.
- *Error clínico asociado*: ERR_PATIENT_CANAL_INVALIDO

## Proyección

- Se agregarán reglas para validación cruzada entre edad y tipo de cita.
- Se integrarán reglas para justificar cambios de responsable.
- Se documentarán reglas de auditoría por operación (registro, modificación, suspensión).
- Se vincularán reglas con métricas clínicas y reportes éticos.

## Relación con ADR

- [ADR-032: Implementación sistemática de reglas de negocio por agregado](../adr/ADR-032.md)
- [ADR-033: Catálogo de errores clínicos por operación](../adr/ADR-033.md)
- [ADR-031: Implementación estratégica de Value Objects](../adr/ADR-031.md)