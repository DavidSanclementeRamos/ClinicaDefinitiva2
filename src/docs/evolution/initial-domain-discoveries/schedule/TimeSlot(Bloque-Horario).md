# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: TimeSlot (Bloque Horario Mínimo)

## Propósito
Representar un intervalo de tiempo específico y atómico dentro de la disponibilidad de un profesional clínico. Este agregado permite validar con granularidad fina si una cita puede ser asignada, protege la coherencia de la agenda mediante segmentación precisa y facilita la gestión operativa de turnos y disponibilidades a nivel de minutos.

---

## CREACIÓN
- Debe especificarse fecha, hora de inicio y duración exacta.
- La duración debe ser positiva y dentro de límites permitidos (15, 30, 60 minutos típicamente).
- No puede crearse si el profesional está inactivo.
- Debe estar contenido completamente dentro de una Availability o Shift válido.
- No puede solaparse con otro TimeSlot ya asignado del mismo profesional.
- Debe validarse coherencia con disponibilidad padre (no puede exceder límites).
- Se registra automáticamente fecha de creación y estado inicial (DISPONIBLE).

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si no tiene cita asignada.
- No puede modificarse si está dentro de las 24h previas.
- No puede extenderse fuera de la disponibilidad original (padre).
- Cambios sensibles deben registrar fecha, responsable y motivo.
- No puede reducirse la duración si hay cita asignada.

---

## CANCELACIÓN / DESACTIVACIÓN
- No puede cancelarse si tiene cita activa asignada.
- Debe registrar motivo obligatorio de cancelación.
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar fecha de cancelación.

---

## OPERACIONES DE DOMINIO
- cubre(fechaHora) → Verifica si el bloque incluye ese momento exacto.
- estaDisponible() → Verifica si no tiene cita asignada y está activo.
- puedeAsignarseCita() → Valida si cumple condiciones clínicas y operativas.
- intersectaCon(otroSlot) → Detecta conflictos entre bloques.
- asignarCita(appointment) → Marca el slot como ocupado.
- liberarCita() → Marca el slot como disponible nuevamente.
- getDuracionMinutos() → Retorna duración en minutos.

---

## INVARIANTES GLOBALES
- Un TimeSlot válido debe tener duración positiva definida.
- Debe estar completamente contenido dentro de una Availability válida.
- No puede tener más de una cita asignada simultáneamente.
- No puede estar activo si el profesional está inactivo.
- No puede solaparse con otro TimeSlot del mismo profesional.
- La duración debe ser múltiplo de la granularidad mínima del sistema (ej: 15 min).

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada creación, edición, cancelación y asignación de cita.
- Se puede emitir un Outcome al intentar agendar en TimeSlot ocupado o inválido.
- Se registra el profesional, la disponibilidad asociada y fecha de modificación.
- Sistema emite alertas al detectar intentos de solapamiento.
- Auditoría completa de uso de slots (ocupación vs. disponibilidad).

---

## Justificación Semántica
Estas reglas aseguran que el modelo de TimeSlot sea coherente, evaluable y trazable. Protegen la granularidad precisa de la agenda clínica, evitan conflictos operativos a nivel de minutos, garantizan que no se asignen múltiples citas al mismo slot y permiten validar condiciones detalladas antes de asignar una cita. El modelo está listo para integrarse en flujos de atención, reportes de ocupación granular, optimización de recursos y exhibición internacional.

---

## Reglas Descubiertas (formato estandarizado)

**RN-TIMESLOT-001**
- Descripción: La duración debe ser positiva y dentro de límites permitidos.
- Condición: TimeSlot.duracion <= 0 || duracion not in [15, 30, 60] al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_TIMESLOT_INVALID_DURATION

**RN-TIMESLOT-002**
- Descripción: No puede crearse si el profesional está inactivo.
- Condición: TimeSlot.profesional.status != ACTIVE al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_TIMESLOT_PROFESSIONAL_INACTIVE

**RN-TIMESLOT-003**
- Descripción: No puede solaparse con otro TimeSlot ya asignado.
- Condición: TimeSlot.intersectaCon(otroSlot) == true para mismo profesionalId.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_TIMESLOT_OVERLAP_CONFLICT

**RN-TIMESLOT-004**
- Descripción: No puede editarse si tiene cita asignada o está dentro de 24h previas.
- Condición: TimeSlot.tieneCitaAsignada() == true || (fechaSlot - now) < 24h al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_TIMESLOT_CANNOT_EDIT

**RN-TIMESLOT-005**
- Descripción: No puede tener más de una cita asignada.
- Condición: TimeSlot.appointment != null && intento de asignar otra cita.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_TIMESLOT_ALREADY_BOOKED

**RN-TIMESLOT-006**
- Descripción: Debe estar contenido dentro de una disponibilidad válida.
- Condición: TimeSlot no contenido completamente en Availability.horaInicio-horaFin.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_TIMESLOT_OUTSIDE_AVAILABILITY

**RN-TIMESLOT-007**
- Descripción: Cancelación requiere motivo obligatorio.
- Condición: cancelar(motivo) con motivo == null || motivo.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_TIMESLOT_CANCELLATION_REQUIRES_REASON

**RN-TIMESLOT-008**
- Descripción: No puede cancelarse si tiene cita activa.
- Condición: TimeSlot.appointment != null && appointment.status == ACTIVE al invocar cancelación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_TIMESLOT_HAS_ACTIVE_APPOINTMENT

**RN-TIMESLOT-009**
- Descripción: No puede extenderse fuera de la disponibilidad original.
- Condición: Nueva duración excede límites de Availability padre al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_TIMESLOT_EXCEEDS_AVAILABILITY

---

## Relación con ADRs
- ADR-25 (Dominio): Modelado de Persona en el dominio clínico - profesionales con slots.
- ADR-30 (Dominio): Catálogo CRUD por rol - permisos para gestionar slots.
- ADR-32 (Dominio): Implementación sistemática de reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas de negocio - validación granular de agenda.

---

## Eventos de Dominio
- TimeSlotCreated: Al crear nuevo bloque horario.
- TimeSlotBooked: Al asignar cita al slot.
- TimeSlotReleased: Al liberar slot (cita cancelada).
- TimeSlotCancelled: Al cancelar el slot.
- TimeSlotConflictDetected: Al detectar solapamiento.

---

## Estados del TimeSlot

**DISPONIBLE (Available)**
- Estado inicial al crear el slot.
- No tiene cita asignada.
- Puede ser reservado para nueva cita.

**RESERVADO (Reserved)**
- Slot bloqueado temporalmente durante proceso de agendamiento.
- No asignable por otros usuarios/procesos.
- Expira automáticamente si no se confirma en X minutos.

**OCUPADO (Booked)**
- Tiene cita asignada confirmada.
- No disponible para nuevas asignaciones.
- Se libera solo si la cita se cancela.

**BLOQUEADO (Blocked)**
- Bloqueado intencionalmente (ej: descanso, reunión, emergencia).
- No disponible para citas.
- Requiere motivo de bloqueo.

**CANCELADO (Cancelled)**
- Slot cancelado y no utilizable.
- Estado final.

---

## Ejemplo de Uso

```java
// Crear TimeSlots desde una Availability
Availability disponibilidad = Availability.create(
    dentist: drGomez,
    diaSemana: DayOfWeek.MONDAY,
    horaInicio: LocalTime.of(9, 0),
    horaFin: LocalTime.of(12, 0)
);

// Generar slots de 30 minutos
List<TimeSlot> slots = TimeSlotGenerator.generateSlots(
    disponibilidad,
    duracionMinutos: 30
);

// Resultado:
// [09:00-09:30, 09:30-10:00, 10:00-10:30, 10:30-11:00, 11:00-11:30, 11:30-12:00]

// Asignar cita a un slot
TimeSlot slot0930 = slots.get(0);
if (slot0930.estaDisponible()) {
    slot0930.asignarCita(nuevaCita);
}

// Validar cobertura específica
LocalDateTime momento = LocalDateTime.of(2025, 12, 15, 9, 15);
boolean cubreExacto = slot0930.cubre(momento); // true si está dentro de 09:00-09:30
```

---

## Relación Jerárquica

```
Shift (Turno Operativo - 8 horas)
 └── Availability (Disponibilidad Clínica - 3 horas)
      └── TimeSlot (Bloque Horario - 30 minutos)
           └── Appointment (Cita - duración variable)
```

**Ejemplo Concreto:**
- **Shift**: Dr. Gómez 08:00-16:00 (turno completo, incluye pausas).
- **Availability**: Atención clínica 09:00-12:00 (horario de citas).
- **TimeSlots**: 09:00-09:30, 09:30-10:00, ..., 11:30-12:00 (bloques reservables).
- **Appointment**: Cita específica asignada a TimeSlot 09:30-10:00.

---

## Generación Automática de Slots

El sistema puede generar automáticamente TimeSlots a partir de Availability:

```java
public List<TimeSlot> generateSlots(
    Availability availability,
    int duracionMinutos
) {
    List<TimeSlot> slots = new ArrayList<>();
    LocalTime current = availability.getHoraInicio();
    
    while (current.plusMinutes(duracionMinutos).isBefore(availability.getHoraFin()) 
           || current.plusMinutes(duracionMinutos).equals(availability.getHoraFin())) {
        
        TimeSlot slot = TimeSlot.create(
            profesional: availability.getDentist(),
            fecha: availability.getFecha(),
            horaInicio: current,
            duracion: duracionMinutos,
            availability: availability
        );
        
        slots.add(slot);
        current = current.plusMinutes(duracionMinutos);
    }
    
    return slots;
}
```

---

## Métricas de Ocupación

**Tasa de Ocupación por Slot**
```
Ocupación = (Slots Ocupados / Slots Totales) * 100
```

**Tiempo Promedio de Reserva**
- Tiempo entre creación del slot y asignación de cita.
- Indica rapidez de llenado de agenda.

**Slots Desperdiciados**
- Slots que quedaron sin asignar en horario ya pasado.
- Oportunidad perdida de atención.