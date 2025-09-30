
# Plantilla de Descubrimiento de Reglas de Negocio por Agregado

## Agregado: Receptionist

## Propósito:
* Representar al personal administrativo encargado de gestionar la agenda clínica, validar
* condiciones operativas para el agendamiento, y facilitar la interacción entre pacientes y profesionales.
* Este agregado no atiende clínicamente, pero protege la coherencia operativa del sistema.

------------------------------------------------------------
1) CREACIÓN
- Debe tener nombre completo, documento único y credenciales de acceso.
- No puede crearse con estado INACTIVO.
- Debe estar asociado a una sede o unidad operativa.
- Debe registrar al menos un medio de contacto válido.

2) EDICIÓN / ACTUALIZACIÓN
- No puede modificarse la sede si tiene citas asignadas en curso.
- No puede eliminarse el documento ni el nombre.
- Solo puede editarse si está activo.
- Cambios sensibles deben registrar fecha de actualización y responsable.

3) DESACTIVACIÓN / ELIMINACIÓN
- No puede desactivarse si tiene tareas pendientes (citas por confirmar, pacientes en espera).
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar motivo de desactivación y fecha.

4) OPERACIONES DE DOMINIO
- puedeAgendarPara(Dentist, Patient, fechaHora): verifica si el odontólogo está activo, disponible,
- y si el paciente está habilitado.
- confirmarCita(Appointment): valida que la cita cumpla condiciones clínicas y operativas.
- cancelarCita(Appointment): solo si no está dentro de las 24h previas.
- reasignarCita(Appointment, nuevoHorario): solo si hay disponibilidad y no hay conflicto.

5) INVARIANTES GLOBALES
- Un recepcionista activo debe estar asociado a una sede válida.
- No puede confirmar citas para odontólogos inactivos.
- No puede agendar citas duplicadas para el mismo paciente en el mismo horario.

6) TRAZABILIDAD Y AUDITORÍA
- Se registra cada acción de agendamiento, confirmación, cancelación y reasignación.
- Se puede emitir un Outcome al intentar agendar en condiciones inválidas.
- Se registra el motivo y fecha de desactivación o bloqueo.

------------------------------------------------------------
## Justificación Semántica:
* Estas reglas aseguran que el modelo de recepcionista sea operativo, trazable y coherente con
* las restricciones clínicas. Protegen la agenda, evitan errores administrativos y permiten auditar
* cada decisión relevante en el flujo de atención.

## Ejemplo de Reglas Descubiertas:
- RN-RECEPTIONIST-001: No puede confirmar citas para odontólogos inactivos.
- RN-RECEPTIONIST-002: No puede agendar citas duplicadas para el mismo paciente en el mismo horario.
- RN-RECEPTIONIST-003: Solo puede cancelar citas si no están dentro de las 24h previas.
- RN-RECEPTIONIST-004: Debe estar asociado a una sede válida.
- RN-RECEPTIONIST-005: Solo puede editarse si está activo.
  