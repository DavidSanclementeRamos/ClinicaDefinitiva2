# ADR: Eliminación del método `upcomingWithinHours(int hours)`

## Estado
- Estado: Decidido
- Fecha: 2025-10-06

## Contexto
El método `upcomingWithinHours(int hours)` fue diseñado para devolver una lista de citas programadas que comienzan dentro de las próximas `hours` horas. Sin embargo, esta lógica:

- No vive en un agregado legítimo.
- No representa una decisión de negocio trazable.
- Duplica validaciones que ya están delegadas éticamente en `Patient`, `Dentist`, y `Appointment`.
- No respeta la semántica completa de los intervalos (solo filtra por `start`, ignora duración y solapamiento).
- No es exhibible ni justificable dentro del modelo clínico.

## Decisión
Eliminar el método `upcomingWithinHours(int hours)` del sistema.  
Toda lógica de validación de disponibilidad debe estar encapsulada en los agregados correspondientes (`Patient`, `Dentist`, `Schedule`) mediante métodos como `canScheduleBetween(...)`, `appointmentsOverlappingWith(...)`, etc.

## Consecuencias
- Se mejora la trazabilidad ética del sistema.
- Se evita la duplicación de lógica y la dispersión de reglas.
- Se refuerza la arquitectura hexagonal, manteniendo las decisiones dentro de sus agregados legítimos.
- Se facilita la documentación y exhibición de reglas de negocio.

## Referencias
- Reemplazo legítimo: `canScheduleBetween(LocalDateTime start, LocalDateTime end)`
- Delegación ética: `TimeIntervalRules.overlaps(...)`
- Agregados responsables: `Patient`, `Dentist`, `Appointment`