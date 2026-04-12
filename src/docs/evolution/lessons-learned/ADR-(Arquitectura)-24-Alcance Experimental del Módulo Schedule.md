# ADR-24 (Arquitectura) – Lección aprendida: Los errores del módulo Schedule y el arte de saber qué no construir

**Estado:** Lección aprendida (retrospectiva)  
**Fecha original:** 2024-12-25 (ADR-24) / 2025-10-08 (ADR-07 Cita, unificado aquí)  
**Reescrito:** 2026-04-11

---

## Contexto: el segundo módulo, la misma trampa con diferente forma

El módulo de citas fue el segundo que construí. Con la experiencia del módulo Actor asumí que cometería menos errores. Y en parte fue así — no repetí los mismos. Pero sí cometí unos nuevos, y con una característica importante: **los errores de este módulo no los detecté dentro del módulo, sino cuando empecé a cruzar lógica con otros que aún no estaban construidos**. Esa es una de las propiedades más peligrosas del diseño incorrecto en sistemas con múltiples agregados: el error se siembra en un módulo y se manifiesta en otro.

Los dos errores principales que narro aquí, aunque parecen independientes, tienen la misma causa raíz: intentar modelar todo de una vez, sin requerimientos reales que guíen las decisiones. El primero es delegar responsabilidades en el lugar incorrecto. El segundo es crear abstracciones que nadie pidió.

---

## Error 1: la cita validaba el estado del usuario

El error más grave que cometí en este módulo aparece en la operación de creación. En el archivo de descubrimiento de reglas de aquella época yo había escrito:

> *"El odontólogo debe estar en estado ACTIVE."*

A primera vista tiene sentido. Pero lo que yo modelé fue: la propia cita verifica si el profesional y el paciente están activos en el sistema. Para eso, `Appointment` le preguntaba a `Dentist`, `Dentist` le preguntaba a `UserIdentity`, y `UserIdentity` consultaba su VO `UserStatus`.

```java
// ❌ Flujo incorrecto: Appointment → Dentist → UserIdentity → UserStatus

public void canScheduleBetween(UserIdentity user, LocalDateTime start, LocalDateTime end) {
    UserStatus.from(user).mustBeActive(
        PatientError.ERR_PATIENT_INVALID_AGE,
        EntityContext.PATIENT
    );
}
```

El mismo patrón estaba duplicado en `Patient.canScheduleBetween()` y en `Dentist.canScheduleBetween()`. Cada agregado repetía la misma validación de estado que no le correspondía.

El problema es conceptual: si un usuario llegó a la operación de agendar una cita, **ya pasó por autenticación**. Ya se verificó que está activo. Pedirle a `Appointment` que lo verifique de nuevo es duplicar una responsabilidad que no le pertenece, y hacerlo en el lugar equivocado.

La validación de estado del usuario es responsabilidad de la capa de autenticación, que ocurre una sola vez cuando el usuario inicia sesión, a través de `UserAccessValidator`. El dominio de agendamiento solo necesita verificar si hay cobertura de turno y si no hay conflictos de horario. Nada más.

> 📌 La solución implementada está en [ADR-(Cita)-09](../../architecture/decisions/domain/schedule/ADR-%28Cita%29-09-Ubicaci%C3%B3n%20de%20validaciones%20de%20estado%20y%20disponibilidad%20en%20el%20m%C3%B3dulo%20de%20citas.md)

---

## Error 2: seis abstracciones para representar un turno

El segundo error fue crear demasiados conceptos para resolver un problema que no requería tanta complejidad. En cierto punto del módulo, el sistema tenía **seis puntos de referencia** para determinar si una cita podía realizarse en un horario dado:

| Concepto | Lo que pretendía representar |
|----------|------------------------------|
| `DentistAvailabilityStatus` | Estado operacional del profesional |
| `WorkingHours` | Jornada laboral contractual |
| `WeeklyAvailability` | Disponibilidad semanal recurrente |
| `Availability` | Bloques de tiempo disponibles para citas |
| `TimeSlot` | Intervalo atómico dentro de la disponibilidad |
| `Shift` | Bloque de tiempo asignado al profesional |

Seis abstracciones para responder una pregunta: *¿puede el Dr. García atender una cita el lunes a las 10 a.m.?*

La proliferación no fue accidental. Cada concepto nació de intentar modelar una "necesidad futura" que en ese momento no tenía requerimientos concretos detrás. `TimeSlot` porque quizás algún día se necesitaría granularidad de minutos. `WeeklyAvailability` porque quizás la disponibilidad sería recurrente. `Availability` porque quizás habría bloques que no correspondieran a un turno completo.

Ninguna de esas "quizás" se concretó. Y mientras tanto, el modelo era incomprensible.

La solución fue eliminar `Availability`, `WeeklyAvailability` y `TimeSlot`, y hacer que `Shift` fuera la **única fuente de verdad operativa**. `WorkingHours` sobrevivió porque tiene una responsabilidad concreta: validar coherencia contractual cuando se crea el turno, no durante el agendamiento.

```
WorkingHours (VO)  → valida coherencia contractual al crear el Shift
        ↓
Shift (Agregado)   → única fuente de verdad operativa
        ↓
Appointment        → debe estar contenida dentro de un Shift activo
```

`Shift` se extendió con `ExcludedBlock` para manejar los casos que antes requerían `Availability`:

```java
public class Shift {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<ExcludedBlock> excludedBlocks; // almuerzo, reuniones, etc.

    public boolean canAccommodateAppointment(LocalDateTime start, LocalDateTime end) {
        return coversInterval(start, end)
            && !fallsInExcludedBlock(start, end);
    }
}
```

Un concepto menos, más claridad, misma funcionalidad.

> 📌 La decisión está en [ADR-(Cita)-07](../../architecture/decisions/domain/schedule/ADR-%28Cita%29-07-Consolidaci%C3%B3n%20de%20Shift%20como%20%C3%BAnica%20fuente%20de%20verdad%20temporal.md) y [ADR-(Cita)-08](../../architecture/decisions/domain/schedule/ADR-%28Cita%29-08-Transformaci%C3%B3n%20de%20Schedule%20en%20ScheduleQueryService.md)

---

## La lección que conecta los dos errores: saber qué no construir

Estos dos errores tienen la misma causa raíz. En el primero, delegué en `Appointment` una validación que pertenecía a otro lugar, porque no tenía claro el flujo completo del sistema. En el segundo, creé seis abstracciones para cubrir casos de uso hipotéticos que nadie había pedido.

Ambos son síntomas de lo mismo: **modelar sin requerimientos reales que anclen las decisiones**.

Ese reconocimiento llevó a una decisión deliberada para el resto del módulo: no modelar lo que no tiene un caso de uso concreto y presente. En particular, el sistema de agendamiento tiene reglas que dependen de recursos físicos específicos de cada clínica — equipos quirúrgicos, salas, personal adicional. Modelar eso en un proyecto experimental, sin datos reales de ninguna clínica concreta, hubiera repetido exactamente el patrón de `TimeSlot` y `WeeklyAvailability`: abstracciones construidas sobre suposiciones que luego hay que desmantelar.

La decisión fue mantener el dominio de agendamiento en las invariantes que son universales y no dependen de configuración organizacional:

- Validez de intervalos de tiempo.
- Que la cita esté contenida dentro de un `Shift` activo.
- Que no haya solapamiento con otras citas del mismo profesional o paciente.

Las validaciones de recursos concretos (`EquipmentSchedule`, `RoomSchedule`) quedaron fuera del alcance. No porque sean irrelevantes en un sistema real — lo son — sino porque modelarlas sin requerimientos reales produce exactamente el tipo de diseño que acababa de desmantelar.

Esto no es una limitación del sistema. Es una decisión de diseño consciente, documentada, y reversible cuando un caso de uso real lo justifique. La arquitectura está preparada para incorporar esos agregados sin refactorización disruptiva. Lo que no está, y no debería estar, es código construido sobre suposiciones.

---

## Lecciones aprendidas

**1. La validación de estado del usuario no pertenece al dominio de negocio.**  
Si el usuario llegó a una operación de negocio, ya está autenticado y activo. No repitas esa validación en los agregados. Pertenece a `UserAccessValidator`, se ejecuta una vez, y no se propaga hacia abajo.

**2. Antes de crear una abstracción, pregúntate qué requerimiento concreto la justifica.**  
`TimeSlot`, `WeeklyAvailability` y `Availability` nacieron de necesidades hipotéticas. Ninguna tuvo un caso de uso real que la respaldara. Si no puedes nombrar un requerimiento concreto que justifique una clase, probablemente no debería existir todavía.

**3. Una sola fuente de verdad para el tiempo es suficiente.**  
`Shift` con `ExcludedBlock` resuelve lo que seis abstracciones no lograban resolver de forma coherente.

**4. Los errores de diseño en módulos con múltiples agregados no se detectan dentro del módulo.**  
Se detectan cuando ese módulo cruza lógica con otro. Esa es la razón por la que en un proyecto experimental conviene construir una base mínima que funcione correctamente, y agregar complejidad solo cuando un caso real la demande — no cuando una suposición la sugiera.

**5. Eliminar código es una decisión técnica tan válida como agregarlo.**  
Eliminar `Availability`, `TimeSlot` y `WeeklyAvailability` no fue deuda técnica resuelta. Fue reconocer que esas clases no debían haber existido. La arquitectura mejoró quitando, no agregando.

---

## Reflexión final

El módulo de Schedule me enseñó algo que el módulo de Actor no pudo: que los errores de diseño más costosos no son los que se cometen dentro de un módulo, sino los que se descubren cuando ese módulo necesita hablar con otro.

La explosión de conceptos temporales y la validación de estado en el lugar incorrecto tenían el mismo origen: querer modelar todo de una vez, antes de tener el contexto para hacerlo bien. En un proyecto experimental eso es especialmente peligroso, porque "experimental" no significa hacer lo mínimo — significa construir con rigor pero sin pretender que se conocen todos los requerimientos desde el primer día.

La estrategia que funciona es construir la base mínima que resuelva el requerimiento más importante con corrección, y agregar complejidad cuando un caso real la justifique. No cuando una suposición la sugiera, no cuando parezca que "en algún momento se va a necesitar". Un módulo bien modelado con dos agregados simples es más útil que uno sobrediseñado con seis abstracciones que colisionan entre sí.

El ADR-24 original era un inventario de lo que se implementó. Este documento es lo que costó implementarlo.

---

*Ver decisiones vigentes: [ADR-(Cita)-07](../../architecture/decisions/domain/schedule/ADR-%28Cita%29-07-Consolidaci%C3%B3n%20de%20Shift%20como%20%C3%BAnica%20fuente%20de%20verdad%20temporal.md), [ADR-(Cita)-08](../../architecture/decisions/domain/schedule/ADR-%28Cita%29-08-Transformaci%C3%B3n%20de%20Schedule%20en%20ScheduleQueryService.md), [ADR-(Cita)-09](../../architecture/decisions/domain/schedule/ADR-%28Cita%29-09-Ubicaci%C3%B3n%20de%20validaciones%20de%20estado%20y%20disponibilidad%20en%20el%20m%C3%B3dulo%20de%20citas.md)*
