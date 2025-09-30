
# Plantilla de Descubrimiento de Reglas de Negocio por Agregado

## Agregado: Availability (Disponibilidad)

## Propósito:
* Representar los bloques de tiempo en los que un profesional clínico está disponible para
* agendar citas. Este agregado protege la coherencia de la agenda, evita conflictos y permite
* validar condiciones operativas antes de asignar una cita.

------------------------------------------------------------
1) CREACIÓN
- Debe especificarse día, hora de inicio y hora de fin.
- La hora de inicio debe ser anterior a la hora de fin.
- No puede crearse disponibilidad vacía ni con duración negativa.
- Debe estar asociada a un profesional activo.

2) EDICIÓN / ACTUALIZACIÓN
- No puede modificarse si ya tiene citas agendadas dentro del bloque.
- Solo puede editarse si el profesional está activo.
- Cambios sensibles deben registrar fecha y responsable.
- No puede extenderse sobre otro bloque ya registrado.

3) ELIMINACIÓN / DESACTIVACIÓN
- No puede eliminarse si tiene citas activas asociadas.
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar motivo de desactivación y fecha.

4) OPERACIONES DE DOMINIO
- cubre(fechaHora): verifica si la disponibilidad incluye ese momento.
- esVálida(): verifica que el bloque tenga duración positiva y no esté solapado.
- intersectaCon(otraDisponibilidad): detecta conflictos entre bloques.
- puedeAsignarseCita(fechaHora): valida si ese horario está libre y cubierto.

5) INVARIANTES GLOBALES
- Una disponibilidad válida debe tener duración positiva.
- No puede haber dos bloques que se solapen para el mismo profesional.
- No puede eliminarse si tiene citas activas.

6) TRAZABILIDAD Y AUDITORÍA
- Se registra cada creación, edición y desactivación.
- Se puede emitir un Outcome al intentar agendar fuera de disponibilidad.
- Se registra el profesional asociado y la fecha de modificación.

------------------------------------------------------------
## Justificación Semántica:
* Estas reglas aseguran que el modelo de disponibilidad sea coherente, evaluable y trazable.
* Protegen la agenda clínica, evitan conflictos operativos y permiten validar condiciones
* antes de asignar una cita. El modelo está listo para integrarse en flujos de atención,
* reportes de eficiencia y exhibición internacional.

## Ejemplo de Reglas Descubiertas:
- RN-AVAIL-001: La hora de inicio debe ser anterior a la hora de fin.
- RN-AVAIL-002: No puede crearse disponibilidad vacía ni con duración negativa.
- RN-AVAIL-003: No puede modificarse si tiene citas agendadas dentro del bloque.
- RN-AVAIL-004: No puede haber dos bloques que se solapen para el mismo profesional.
- RN-AVAIL-005: No puede eliminarse si tiene citas activas asociadas.
    