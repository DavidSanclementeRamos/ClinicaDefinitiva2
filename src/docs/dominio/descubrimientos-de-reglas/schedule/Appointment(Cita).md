
# Plantilla de Descubrimiento de Reglas de Negocio por Agregado

## Agregado: Appointment (Cita clínica)

## Propósito:
* Representar una cita clínica entre paciente y profesional, asegurando que su creación,
* modificación, confirmación y ejecución respeten las condiciones clínicas, operativas y éticas
- del sistema. Este agregado protege la continuidad del servicio y permite trazabilidad completa.

------------------------------------------------------------
1) CREACIÓN
- Debe tener un paciente y un odontólogo válidos.
- El odontólogo debe estar activo.
- La fecha/hora debe estar dentro de la disponibilidad del odontólogo.
- No puede crearse si ya existe una cita en ese horario para ese odontólogo.
- Debe especificarse el tipo de cita (Control, Emergencia, Primera vez).
- Debe registrar el motivo clínico.
- Se asigna estado inicial SCHEDULED.
- Se registra fecha de creación y última actualización.

2) EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si está en estado SCHEDULED o CONFIRMED.
- No puede cambiarse la fecha si está dentro de las 24h previas.
- Cambios sensibles deben registrar fecha y responsable.
- No puede editarse si está CANCELLED o COMPLETED.

3) CANCELACIÓN / REPROGRAMACIÓN
- Solo puede cancelarse si no está dentro de las 24h previas.
- Debe registrar motivo de cancelación.
- Reprogramar requiere nueva disponibilidad y validación de conflicto.
- Se marca como rescheduled y se actualiza fecha de modificación.

4) EJECUCIÓN / FINALIZACIÓN
- Solo puede marcarse como COMPLETED si tiene duración real registrada.
- Debe registrar notas clínicas y profesional que atendió.
- No puede finalizarse si está en estado CANCELLED.

5) INVARIANTES GLOBALES
- Una cita debe tener siempre un paciente, un odontólogo y una fecha válida.
- No puede haber dos citas en el mismo horario para el mismo odontólogo.
- No puede estar en estado COMPLETED sin duración ni notas clínicas.

6) TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio de estado, reprogramación, cancelación y finalización.
- Se puede emitir un Outcome al intentar agendar en horario no disponible.
- Se registra el odontólogo asignado y el que realmente atendió (pueden diferir).

------------------------------------------------------------
## Justificación Semántica:
* Estas reglas aseguran que el modelo de cita clínica sea coherente, evaluable y trazable.
* Protegen la agenda, evitan conflictos operativos, y permiten auditar cada decisión relevante
* en el ciclo de atención. El modelo está listo para integrarse en reportes, análisis de eficiencia
* y exhibición internacional.

## Ejemplo de Reglas Descubiertas:
- RN-APPT-001: No puede crearse si el odontólogo está inactivo.
- RN-APPT-002: No puede agendarse fuera del horario de disponibilidad.
- RN-APPT-003: Debe tener paciente y odontólogo válidos.
- RN-APPT-004: No puede haber dos citas en el mismo horario para el mismo odontólogo.
- RN-APPT-005: Solo puede finalizarse si tiene duración real y notas clínicas.
    