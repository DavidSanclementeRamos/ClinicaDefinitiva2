
# Plantilla de Descubrimiento de Reglas de Negocio por Agregado

## Agregado: TimeSlot (Bloque horario mínimo)

## Propósito:
* Representar un intervalo de tiempo específico dentro de la disponibilidad de un profesional clínico.
* Este agregado permite validar si una cita puede ser asignada, protege la coherencia de la agenda
* y facilita la segmentación operativa de los turnos y disponibilidades.

------------------------------------------------------------
1) CREACIÓN
- Debe especificarse fecha, hora de inicio y duración.
- La duración debe ser positiva y dentro de los límites permitidos (ej. 15, 30, 60 minutos).
- No puede crearse si el profesional está inactivo.
- Debe estar contenido dentro de una disponibilidad o turno válido.
- No puede solaparse con otro TimeSlot ya asignado.

2) EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si no tiene cita asignada.
- No puede modificarse si está dentro de las 24h previas.
- Cambios sensibles deben registrar fecha, responsable y motivo.
- No puede extenderse fuera de la disponibilidad original.

3) CANCELACIÓN / DESACTIVACIÓN
- No puede cancelarse si tiene cita activa.
- Debe registrar motivo de cancelación y fecha.
- La eliminación física está prohibida; se maneja como estado lógico.

4) OPERACIONES DE DOMINIO
- cubre(fechaHora): verifica si el bloque incluye ese momento.
- estáDisponible(): verifica si no tiene cita asignada y está activo.
- puedeAsignarseCita(): valida si cumple condiciones clínicas y operativas.
- intersectaCon(otroSlot): detecta conflictos entre bloques.

5) INVARIANTES GLOBALES
- Un TimeSlot válido debe tener duración positiva y estar dentro de una disponibilidad.
- No puede tener más de una cita asignada.
- No puede estar activo si el profesional está inactivo.

6) TRAZABILIDAD Y AUDITORÍA
- Se registra cada creación, edición, cancelación y asignación de cita.
- Se puede emitir un Outcome al intentar agendar en un TimeSlot ocupado o inválido.
- Se registra el profesional, la disponibilidad asociada y la fecha de modificación.

------------------------------------------------------------
## Justificación Semántica:
* Estas reglas aseguran que el modelo de TimeSlot sea coherente, evaluable y trazable.
* Protegen la granularidad de la agenda clínica, evitan conflictos operativos y permiten validar
* condiciones antes de asignar una cita. El modelo está listo para integrarse en flujos de atención,
* reportes de eficiencia y exhibición internacional.

## Ejemplo de Reglas Descubiertas:
- RN-TIMESLOT-001: La duración debe ser positiva y dentro de los límites permitidos.
- RN-TIMESLOT-002: No puede crearse si el profesional está inactivo.
- RN-TIMESLOT-003: No puede solaparse con otro TimeSlot ya asignado.
- RN-TIMESLOT-004: No puede editarse si tiene cita asignada o está dentro de las 24h previas.
- RN-TIMESLOT-005: No puede tener más de una cita asignada.
    