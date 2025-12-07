
# Plantilla de Descubrimiento de Reglas de Negocio por Agregado

## Agregado: Patient

## Propósito:
* Representar al paciente dentro del sistema clínico, asegurando que su información sea válida,
* trazable y que sus interacciones (citas, tratamientos, historial) respeten la semántica del dominio.
*
------------------------------------------------------------
1) CREACIÓN
- Debe tener nombre completo, documento único y fecha de nacimiento válida.
- No puede crearse con estado INACTIVO.
- Debe registrar al menos un medio de contacto (email o teléfono).
- La edad calculada no puede ser negativa ni mayor a 120 años.

2) EDICIÓN / ACTUALIZACIÓN
- No puede modificarse la fecha de nacimiento si ya tiene citas registradas.
- No puede eliminarse el documento ni el nombre.
- Solo puede editarse si está activo.
- Cambios sensibles deben registrar fecha de actualización y responsable.

3) DESACTIVACIÓN / ELIMINACIÓN
- No puede desactivarse si tiene citas activas o tratamientos en curso.
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar motivo de desactivación y fecha.

4) OPERACIONES DE DOMINIO
- puedeAgendar(): verifica si el paciente está activo y no tiene bloqueos clínicos.
- tieneCitaEn(fechaHora): verifica si ya tiene una cita en ese momento.
- historialCompleto(): devuelve todas las citas y tratamientos ordenados cronológicamente.
- esMenorDeEdad(): útil para validar reglas de consentimiento.

5) INVARIANTES GLOBALES
- Un paciente activo debe tener al menos un medio de contacto válido.
- No puede tener dos citas en el mismo horario.
- No puede tener tratamientos abiertos sin citas asociadas.

6) TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio en datos sensibles (documento, contacto, estado).
- Se puede emitir un Outcome al intentar agendar si está inactivo o bloqueado.
- Se registra el motivo y fecha de desactivación.

------------------------------------------------------------
## Justificación Semántica:
* Estas reglas aseguran que el modelo de paciente sea coherente, evaluable y trazable. Protegen
* la continuidad clínica, evitan estados inválidos como pacientes sin contacto o con citas duplicadas,
* y permiten auditar cada decisión relevante en el ciclo de vida del paciente.

## Ejemplo de Reglas Descubiertas:
- RN-PATIENT-001: Un paciente debe tener nombre, documento y fecha de nacimiento válida.
- RN-PATIENT-002: No puede desactivarse si tiene citas activas o tratamientos en curso.
- RN-PATIENT-003: No puede tener dos citas en el mismo horario.
- RN-PATIENT-004: Solo puede editarse si está activo.
- RN-PATIENT-005: Debe registrar al menos un medio de contacto válido.
  