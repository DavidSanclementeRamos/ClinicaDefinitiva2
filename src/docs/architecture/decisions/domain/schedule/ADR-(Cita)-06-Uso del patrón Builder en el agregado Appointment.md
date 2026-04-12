


# ADR-06(Cita): Uso del patrón Builder en el agregado Appointment

- **Estado:** Aprobado
- **Fecha:** 2025-12-28
- **Autor:** David Stiven Sanclemente

## Contexto
En [ADR-(Actores)-13-Eliminación del patrón Builder en Receptionist.md](../actor/ADR-%28Actores%29-13-Eliminaci%C3%B3n%20del%20patr%C3%B3n%20Builder%20en%20Receptionist.md) se decidió eliminar el patrón *Builder* en los agregados del módulo Actor, dado que su ciclo de vida y reglas de negocio no requerían la flexibilidad del *Builder*. La construcción de esos agregados se resolvió mediante métodos de fábrica y constructores privados, garantizando invariantes sin necesidad de estados intermedios.

Sin embargo, el agregado **Appointment** del módulo **Schedule** presenta características distintas:
- Requiere múltiples atributos obligatorios (`AppointmentId`, `DentistId`, `PatientId`, `start`, `end`, `AppointmentType`, `reason`).
- Debe validar invariantes complejas en el momento de construcción (ej. `start < end`, motivo obligatorio, actores válidos, cita no en el pasado).
- Se utiliza intensivamente en el **AppointmentSchedulingService**, donde se orquestan reglas de negocio entre agregados y se necesita una forma clara y segura de construir instancias válidas.

## Problema
Si se eliminara el *Builder* en Appointment:
- La construcción de citas quedaría expuesta a constructores con múltiples parámetros, reduciendo legibilidad y aumentando riesgo de errores.
- La validación de invariantes se dispersaría entre el servicio y el agregado, rompiendo la cohesión.
- Se perdería expresividad semántica en el servicio de dominio, que actualmente delega la validación al *Builder*.

## Decisión
Se mantiene el patrón *Builder* en el agregado **Appointment** como excepción a la regla general aplicada en el módulo Actor.
- El *Builder* valida invariantes críticas en el método `build()`.
- El *AppointmentSchedulingService* utiliza el *Builder* para construir citas de manera segura y expresiva.
- Se documenta esta excepción como caso especial, dado que Appointment es un agregado rico en reglas de negocio y construcción flexible.

## Justificación
- **Protección de invariantes:** el *Builder* asegura que una cita siempre nace válida, sin estados parciales.
- **Claridad semántica:** la construcción refleja directamente la intención clínica (crear cita con atributos obligatorios).
- **Consistencia con otros agregados clínicos ricos:** Appointment comparte complejidad con agregados como Dentist, donde el *Builder* ya fue aprobado en ADR-06.
- **Necesidad en el servicio de dominio:** el *AppointmentSchedulingService* requiere un mecanismo seguro y expresivo para construir citas tras validar reglas externas (Shift, Availability, Conflictos).

## Consecuencias
- Appointment se construye exclusivamente mediante el *Builder*.
- Se mantiene la coherencia con ADR-06 (Dentist) y se documenta la excepción respecto a ADR-(Actores)-13.
- Exhibición más profesional del modelo, evitando constructores con múltiples parámetros y validaciones dispersas.
- En el futuro, si se simplifica el ciclo de vida de Appointment, se podrá eliminar el *Builder* siguiendo la línea de Receptionist.

## Ejemplo
```java
Appointment appointment = new Appointment.Builder()
    .withId(AppointmentId.generate())
    .withDentistId(dentist.getDentistId())
    .withPatientId(patient.getPatientId())
    .withStart(start)
    .withEnd(end)
    .withAppointmentType(type)
    .withReason(reason)
    .withServiceDuration(duration)
    .build();
```

## Relación con otros ADR
 
- [ADR-(Actores)-13-Eliminación del patrón Builder en Receptionist.md](../actor/ADR-%28Actores%29-13-Eliminaci%C3%B3n%20del%20patr%C3%B3n%20Builder%20en%20Receptionist.md)
- [ADR-(actores)-07-Ubicación del patrón Builder en la entidad Dentist.md](../actor/ADR-%28actores%29-07-Ubicaci%C3%B3n%20del%20patr%C3%B3n%20Builder%20en%20la%20entidad%20Dentist.md)


