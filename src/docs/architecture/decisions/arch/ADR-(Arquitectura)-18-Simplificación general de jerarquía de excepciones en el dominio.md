# ADR-18 (Arquitectura): Simplificación general de jerarquía de excepciones en el dominio

- **Estado:** Aprobado
- **Fecha:** 2025-12-07  
- **Autor:** David Stiven Sanclemente
---

##  Contexto
En el diseño inicial del sistema se definieron múltiples clases de excepción específicas para cada regla de negocio y cada campo de los distintos módulos.  
Ejemplo: `BlankFullNameException`, `NullAddressException`, `InvalidDateRangeException`.

Este enfoque ofrecía granularidad semántica, pero generaba una **fragmentación excesiva** y un alto costo de mantenimiento. Cada nuevo campo o regla requería una nueva clase de excepción, lo que dificultaba la escalabilidad y la claridad del código en todo el proyecto.

---

##  Decisión
- Se mantiene una **jerarquía principal de excepciones por agregado o contexto de dominio** (ej. `BusinessRuleViolationException` como raíz).
- Se reemplazan las múltiples clases hijas por **excepciones parametrizadas**, capaces de recibir:
    - El campo afectado.
    - El valor inválido.
    - Un mensaje descriptivo.
- Ejemplo ilustrativo:
  ```java
  throw new ValueObjectValidationException(
      "email",
      "correo@invalido",
      "El formato del correo electrónico no es válido."
  );
  ```
 ## Diagrama comparativo de excepciones
   Antes (fragmentado, muchas clases específicas)

```ClinicaDefinitivaException
└── ModelException
├── ValueObjectValidationException
│    ├── SharedPersonBusinessRuleViolationException
│    │    ├── MissingSemanticValueException
│    │    │    ├── BlankValueException
│    │    │    │    ├── BlankAddressException
│    │    │    │    ├── BlankAgeException
│    │    │    │    ├── BlankDateOfBirthException
│    │    │    │    ├── BlankFullNameException
│    │    │    │    └── BlankPhoneNumberException
│    │    │    ├── InvalidFormatException
│    │    │    │    ├── AgeOutOfRangeException
│    │    │    │    ├── InvalidAddressException
│    │    │    │    ├── InvalidDateOfBirthException
│    │    │    │    ├── InvalidDentistAvailabilityException
│    │    │    │    ├── InvalidFullNameException
│    │    │    │    └── InvalidPhoneNumberException
│    │    │    └── NullValueException
│    │    │         ├── NullAddressException
│    │    │         ├── NullAgeException
│    │    │         ├── NullDateOfBirthException
│    │    │         ├── NullDentistAvailabilityStatus
│    │    │         ├── NullFullNameException
│    │    │         └── NullPhoneNumberException
│    │    ├── TemporalInvalidationException
│    │    │    └── DateOfBirthInFutureException
│    │    └── ...
│
├── DentistBusinessRuleViolationException
│    ├── DentistMinimumAgeException
│    ├── EmptySpecialtySetException
│    ├── InvalidSpecialtyValueException
│    ├── NullSpecialtySetException
│    ├── NullWorkingHoursException
│    ├── TemporalInconsistencyException
│    │    └── StartTimeAfterEndTimeException
│    └── ...
│
├── PatientBusinessRuleViolationException
│    ├── AgeBelowMinimumForRegistrationException
│    └── UnassignedResponsibleException
│
├── ReceptionistBusinessRuleViolationException
│    ├── BlankSectorException
│    ├── NullSectorException
│    └── SectorNotAllowedException
│
└── AppointmentBusinessRuleViolationException
├── AppointmentInvalidDatesException
├── AppointmentTimeNotAvailableException
├── FutureAppointmentsExistException
├── SchedulingException
│    ├── NoShiftAssignedException
│    └── ShiftNotAvailableException
├── PendingAppointmentsException
│    └── PendingAppointmentsWithinHoursException
├── AppointmentEndDateMissingException
├── AppointmentStartDateMissingException
├── AppointmentInvalidDateRangeException
└── AppointmentOutsideAvailabilityException
```
Después (refactorizado y simplificado)

```
ClinicaDefinitivaException
 └── ModelException
      ├── ValueObjectValidationException
      │    (campo, valor inválido, mensaje)
      │    Ejemplo: "El nombre completo no puede estar vacío."
      │
      ├── TemporalValidationException
      │    (campo, rango temporal inválido, mensaje)
      │    Ejemplo: "La fecha de fin no puede ser anterior a la fecha de inicio."
      │
      ├── BusinessRuleViolationException
      │    (agregado, regla violada, mensaje)
      │    Ejemplo: "El odontólogo no cumple la edad mínima para ser registrado."
      │
      ├── SchedulingException
      │    (recurso, conflicto, mensaje)
      │    Ejemplo: "El turno solicitado no está disponible."
      │
      └── DomainAggregateException
           (para excepciones específicas de cada agregado: Appointment, Dentist, Patient, Receptionist)
           Ejemplo: "Paciente requiere un responsable asignado."
```
 ## Consecuencias
  • 	Simplificación: Se reduce el número de clases de excepción en todos los módulos, facilitando el mantenimiento.
  • 	Mensajes más ricos: Cada excepción incluye información contextual (campo, valor, mensaje).
  • 	Escalabilidad: Nuevas reglas pueden expresarse sin necesidad de crear nuevas clases.
  • 	Consistencia: Se mantiene la semántica de dominio al conservar excepciones raíz por agregado.
  • 	Trade-off: Se pierde algo de granularidad en la jerarquía de clases, pero se gana claridad y pragmatismo.

## Relacionados
- [ADR-(Arquitectura)-02-Catálogo de errores clínicos por operación.md](../../../evolution/deprecated-adrs/arch/ADR-%28Arquitectura%29-02-Cat%C3%A1logo%20de%20errores%20cl%C3%ADnicos%20por%20operaci%C3%B3n.md)
