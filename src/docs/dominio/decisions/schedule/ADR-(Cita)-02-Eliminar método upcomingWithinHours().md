# ADR-02 (Dominio): Eliminación del método upcomingWithinHours(int hours)

- Estado: Superseded by ADR-(Cita)-03
- Fecha: 2025-10-06
- Autor: David

## Contexto
El método upcomingWithinHours(int hours) fue diseñado para devolver una lista de citas programadas que comienzan dentro de las próximas hours horas.  
Sin embargo, esta lógica:
- No vive en un agregado legítimo.
- No representa una decisión de negocio trazable.
- Duplica validaciones ya delegadas en Patient, Dentist y Appointment.
- No respeta la semántica completa de los intervalos (solo filtra por start, ignora duración y solapamiento).
- No es exhibible ni justificable dentro del modelo clínico.

## Decisión
Eliminar el método upcomingWithinHours(int hours) del sistema.  
Toda lógica de validación de disponibilidad debe estar encapsulada en los agregados correspondientes (Patient, Dentist, Schedule) mediante métodos como:
- canScheduleBetween(...)
- appointmentsOverlappingWith(...)

## Justificación
- Trazabilidad ética: las reglas viven en agregados legítimos.
- Evita duplicación: se centraliza la lógica en un solo lugar.
- Respeto a arquitectura hexagonal: las decisiones se mantienen en el dominio.
- Exhibición profesional: las reglas de negocio se documentan y se muestran como parte del modelo clínico.

## Consecuencias
- Se mejora la trazabilidad ética del sistema.
- Se evita la duplicación de lógica y dispersión de reglas.
- Se refuerza la arquitectura hexagonal.
- Se facilita la documentación y exhibición de reglas de negocio.

## Plan de implementación
1. Identificar todas las llamadas a upcomingWithinHours.
2. Eliminar el método de la clase correspondiente.
3. Refactorizar llamadas para usar canScheduleBetween(start, end) o appointmentsOverlappingWith(interval).
4. Añadir pruebas unitarias para validar los nuevos métodos.
5. Actualizar documentación (docs/dominio/reglas-de-negocio/agendamiento.md).

## Ejemplo de reemplazo
```java
// Antes
List<Appointment> upcoming = schedule.upcomingWithinHours(24);

// Después
List<Appointment> upcoming = schedule.appointmentsOverlappingWith(
TimeInterval.of(LocalDateTime.now(), LocalDateTime.now().plusHours(24))
);
```

##Referencias
- Reemplazo legítimo: canScheduleBetween(LocalDateTime start, LocalDateTime end)
- Delegación ética: TimeIntervalRules.overlaps(...)
- Agregados responsables: Patient, Dentist, Appointment

##Relación con otros ADR
- ADR-07 (Dominio): Delegación semántica para validar estado de usuario en agendamiento.
- ADR-08 (Dominio): Refactorización semántica con canScheduleAt(...).
- ADR-10 (Dominio): Separación de Value Objects para AvailabilityStatus en Dentist y Availability.  
  