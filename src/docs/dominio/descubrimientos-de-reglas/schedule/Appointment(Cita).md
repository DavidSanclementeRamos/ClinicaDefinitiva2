
# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Appointment (Cita Clínica)

## Propósito
Representar una cita clínica entre paciente y profesional, asegurando que su creación, modificación, confirmación y ejecución respeten las condiciones clínicas, operativas y éticas del sistema. Este agregado protege la continuidad del servicio y permite trazabilidad completa del flujo de atención.

---

## CREACIÓN
- Debe tener un paciente (Patient) y un odontólogo (Dentist) válidos.
- El odontólogo debe estar en estado ACTIVE.
- La fecha/hora debe estar dentro de la disponibilidad del odontólogo.
- No puede crearse si ya existe una cita en ese horario para ese odontólogo.
- No puede crearse si el paciente ya tiene cita en ese horario.
- Debe especificarse tipo de cita (CONTROL, EMERGENCIA, PRIMERA_VEZ).
- Debe registrar motivo clínico obligatorio.
- Se asigna estado inicial SCHEDULED.
- Se registra fecha de creación y última actualización automáticamente.
- La fecha/hora de la cita no puede estar en el pasado.

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si está en estado SCHEDULED o CONFIRMED.
- No puede modificarse la fecha si está dentro de las 24h previas.
- No puede editarse si está en estado CANCELLED o COMPLETED.
- Cambios en paciente u odontólogo requieren revalidación de disponibilidad.
- Cambios sensibles deben registrar fecha, responsable y motivo.
- No puede cambiarse el tipo de cita una vez confirmada.

---

## CANCELACIÓN / REPROGRAMACIÓN
- Solo puede cancelarse si no está dentro de las 24h previas.
- Debe registrar motivo obligatorio de cancelación.
- Reprogramación requiere nueva disponibilidad y validación de conflictos.
- Se marca como RESCHEDULED y se actualiza fecha de modificación.
- No puede reprogramarse una cita ya completada.
- No puede cancelarse una cita ya completada.

---

## EJECUCIÓN / FINALIZACIÓN
- Solo puede marcarse como COMPLETED si tiene duración real registrada.
- Debe registrar notas clínicas obligatorias al finalizar.
- Debe registrar profesional que atendió (puede diferir del asignado).
- No puede finalizarse si está en estado CANCELLED.
- No puede finalizarse antes de la fecha/hora programada.
- La duración real debe ser mayor a cero.

---

## OPERACIONES DE DOMINIO
- crear(paciente, odontologo, fechaHora, tipo, motivo) → Factory method con validaciones.
- editar(nuevaFecha, nuevoMotivo) → Actualiza si cumple restricciones.
- cancelar(motivo) → Cancela con motivo obligatorio si no está dentro de 24h.
- reprogramar(nuevaFecha) → Cambia fecha con validaciones de disponibilidad.
- confirmar() → Cambia estado a CONFIRMED.
- completar(duracion, notas, profesionalReal) → Finaliza cita con información clínica.
- puedeEditarse() → Verifica si el estado permite edición.
- estaDentro24h() → Verifica si la cita está dentro de las próximas 24 horas.

---

## INVARIANTES GLOBALES
- Una cita debe tener siempre paciente, odontólogo, fecha/hora y tipo válidos.
- No puede haber dos citas en el mismo horario para el mismo odontólogo.
- No puede haber dos citas en el mismo horario para el mismo paciente.
- No puede estar en estado COMPLETED sin duración real ni notas clínicas.
- Una cita cancelada no puede cambiar a ningún otro estado.
- El odontólogo asignado debe estar activo al momento de crear la cita.
- La fecha/hora debe estar dentro de la disponibilidad del odontólogo.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio de estado con fecha, hora y responsable.
- Se registra cada reprogramación con motivo.
- Se registra cada cancelación con motivo obligatorio.
- Se registra odontólogo asignado vs. odontólogo que realmente atendió.
- Se puede emitir un Outcome al intentar agendar en horario no disponible.
- Sistema emite alertas 24h antes de la cita.
- Auditoría completa del ciclo de vida de la cita.

---

## Justificación Semántica
Estas reglas aseguran que el modelo de cita clínica sea coherente, evaluable y trazable. Protegen la agenda profesional, evitan conflictos operativos, garantizan seguimiento clínico adecuado y permiten auditar cada decisión relevante en el ciclo de atención. El modelo está listo para integrarse en reportes de eficiencia, métricas de ocupación, facturación y exhibición internacional.

---

## Reglas Descubiertas (formato estandarizado)

**RN-APPT-001**
- Descripción: No puede crearse si el odontólogo está inactivo.
- Condición: Appointment.dentist.status != ACTIVE al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_APPT_DENTIST_INACTIVE

**RN-APPT-002**
- Descripción: No puede agendarse fuera del horario de disponibilidad.
- Condición: fechaHora no incluida en Dentist.availability al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_APPT_OUTSIDE_AVAILABILITY

**RN-APPT-003**
- Descripción: Debe tener paciente y odontólogo válidos.
- Condición: Appointment.patient == null || Appointment.dentist == null al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_APPT_MISSING_REQUIRED_FIELDS

**RN-APPT-004**
- Descripción: No puede haber dos citas en el mismo horario para el mismo odontólogo.
- Condición: exists(dentistId, fechaHora) == true al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_APPT_DENTIST_TIME_CONFLICT

**RN-APPT-005**
- Descripción: Solo puede finalizarse si tiene duración real y notas clínicas.
- Condición: Appointment.duration == null || Appointment.notes == null al invocar completar().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_APPT_INCOMPLETE_COMPLETION

**RN-APPT-006**
- Descripción: Solo puede editarse si está en estado SCHEDULED o CONFIRMED.
- Condición: Appointment.status not in [SCHEDULED, CONFIRMED] al invocar editar().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_APPT_NOT_EDITABLE

**RN-APPT-007**
- Descripción: No puede cancelarse dentro de las 24h previas.
- Condición: fechaCita - now() < 24h al invocar cancelar().
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_APPT_LATE_CANCELLATION

**RN-APPT-008**
- Descripción: La cancelación requiere motivo obligatorio.
- Condición: cancelar(motivo) con motivo == null || motivo.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_APPT_CANCELLATION_REQUIRES_REASON

**RN-APPT-009**
- Descripción: No puede haber dos citas en el mismo horario para el mismo paciente.
- Condición: exists(patientId, fechaHora) == true al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_APPT_PATIENT_TIME_CONFLICT

**RN-APPT-010**
- Descripción: La fecha/hora de la cita no puede estar en el pasado.
- Condición: fechaHora < LocalDateTime.now() al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_APPT_PAST_DATE

**RN-APPT-011**
- Descripción: Motivo clínico es obligatorio.
- Condición: Appointment.motivo == null || motivo.isBlank() al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_APPT_MISSING_REASON

---

## Relación con ADRs
- ADR-25 (Dominio): Modelado de Persona en el dominio clínico - relación con Patient y Dentist.
- ADR-30 (Dominio): Catálogo de reglas CRUD por rol - permisos para gestionar citas.
- ADR-32 (Dominio): Implementación sistemática de reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas de negocio - validación de agendamiento.

---

## Eventos de Dominio
- AppointmentScheduled: Al crear nueva cita (SCHEDULED).
- AppointmentConfirmed: Al confirmar la cita.
- AppointmentRescheduled: Al reprogramar fecha/hora.
- AppointmentCancelled: Al cancelar la cita.
- AppointmentCompleted: Al finalizar la atención.
- AppointmentReminder24h: Recordatorio 24h antes de la cita.
- AppointmentNoShow: Si el paciente no asiste (no show).

---

## Estados del Ciclo de Vida

**SCHEDULED** (Agendada)
- Estado inicial al crear la cita.
- Permite edición y cancelación (si no está dentro de 24h).
- Puede confirmarse o cancelarse.

**CONFIRMED** (Confirmada)
- Paciente confirmó asistencia.
- Permite edición limitada y cancelación (si no está dentro de 24h).
- Puede completarse o cancelarse.

**RESCHEDULED** (Reprogramada)
- Cita fue reprogramada a nuevo horario.
- Se crea nueva cita y la anterior se vincula.
- Permite cancelación (si no está dentro de 24h).

**COMPLETED** (Completada)
- Atención finalizada con notas clínicas.
- Estado final, inmutable.
- No permite modificaciones.

**CANCELLED** (Cancelada)
- Cita cancelada con motivo registrado.
- Estado final, inmutable.
- No permite modificaciones.

**NO_SHOW** (Inasistencia)
- Paciente no asistió sin cancelar.
- Estado final, inmutable.
- Se registra para métricas de inasistencia.