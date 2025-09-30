
#  Plantilla de Descubrimiento de Reglas de Negocio por Agregado

## Agregado: Shift (Turno operativo)

## Propósito:
* Representar un bloque de tiempo asignado a un profesional (clínico o administrativo) para
* cumplir funciones dentro del sistema. Este agregado protege la continuidad operativa,
* evita solapamientos y permite validar disponibilidad legítima para tareas o citas.

------------------------------------------------------------
1) CREACIÓN
- Debe especificarse fecha, hora de inicio y hora de fin.
- La hora de inicio debe ser anterior a la hora de fin.
- No puede crearse si el profesional está inactivo.
- Debe estar asociado a una sede o unidad operativa válida.
- No puede solaparse con otro turno del mismo profesional.

2) EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si no tiene tareas ni citas asignadas.
- No puede modificarse si está dentro de las 24h previas.
- Cambios sensibles deben registrar fecha, responsable y motivo.
- No puede extenderse sobre otro turno ya registrado.

3) CANCELACIÓN / DESACTIVACIÓN
- No puede cancelarse si tiene tareas clínicas o administrativas activas.
- Debe registrar motivo de cancelación y fecha.
- La eliminación física está prohibida; se maneja como estado lógico.

4) OPERACIONES DE DOMINIO
- cubre(fechaHora): verifica si el turno incluye ese momento.
- intersectaCon(otroTurno): detecta conflictos entre turnos.
- puedeAsignarseTarea(fechaHora): valida si ese horario está libre y cubierto.
- esTurnoActivo(): verifica si el turno está vigente y habilitado.

5) INVARIANTES GLOBALES
- Un turno válido debe tener duración positiva y no solaparse con otros.
- No puede estar activo si el profesional está inactivo.
- No puede tener tareas si está cancelado.

6) TRAZABILIDAD Y AUDITORÍA
- Se registra cada creación, edición, cancelación y asignación de tareas.
- Se puede emitir un Outcome al intentar asignar fuera de turno.
- Se registra el profesional, sede y fecha de modificación.

------------------------------------------------------------
## Justificación Semántica:
* Estas reglas aseguran que el modelo de turno sea coherente, evaluable y trazable.
* Protegen la operación clínica y administrativa, evitan conflictos de agenda y permiten
* validar condiciones antes de asignar tareas o citas. El modelo está listo para integrarse
* en flujos de atención, reportes de cobertura y exhibición internacional.

## Ejemplo de Reglas Descubiertas:
- RN-SHIFT-001: La hora de inicio debe ser anterior a la hora de fin.
- RN-SHIFT-002: No puede crearse si el profesional está inactivo.
- RN-SHIFT-003: No puede solaparse con otro turno del mismo profesional.
- RN-SHIFT-004: No puede editarse si tiene tareas asignadas o está dentro de las 24h previas.
- RN-SHIFT-005: No puede cancelarse si tiene tareas activas.
    