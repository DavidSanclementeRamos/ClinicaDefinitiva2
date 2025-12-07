
# Plantilla de Descubrimiento de Reglas de Negocio por Agregado

## Agregado: Guardian (Responsable)

## Propósito:
* Representar al adulto responsable legal o clínico de un paciente. Este agregado gestiona
* el consentimiento, la autorización de tratamientos, y la trazabilidad de decisiones clínicas
* en nombre del paciente representado.

------------------------------------------------------------
1) CREACIÓN
- Debe tener nombre completo, documento único y vínculo legal con el paciente.
- Debe registrar al menos un medio de contacto válido.
- No puede crearse sin asociarse a un paciente.
- Debe especificarse el tipo de relación (padre, tutor, representante legal).

2) EDICIÓN / ACTUALIZACIÓN
- No puede modificarse el vínculo si ya ha autorizado tratamientos.
- Cambios sensibles deben registrar fecha, responsable y motivo.
- Solo puede editarse si está activo.
- No puede eliminarse el documento ni el nombre.

3) DESACTIVACIÓN / ELIMINACIÓN
- No puede desactivarse si tiene autorizaciones clínicas vigentes.
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar motivo de desactivación y fecha.

4) OPERACIONES DE DOMINIO
- puedeAutorizarTratamiento(Patient, Treatment): verifica si el paciente está vinculado,
- si el tratamiento requiere consentimiento, y si el responsable está activo.
- confirmarConsentimiento(Treatment): registra la aceptación explícita.
- revocarConsentimiento(Treatment): solo si el tratamiento no ha iniciado.
- esResponsableDe(Patient): valida la relación actual.

5) INVARIANTES GLOBALES
- Un responsable activo debe estar vinculado a al menos un paciente.
- No puede autorizar tratamientos si está inactivo.
- No puede revocar consentimiento una vez iniciado el tratamiento.

6) TRAZABILIDAD Y AUDITORÍA
- Se registra cada autorización, revocación y edición de vínculo.
- Se puede emitir un Outcome al intentar autorizar sin vínculo válido.
- Se registra el motivo y fecha de desactivación o cambio de relación.

------------------------------------------------------------

## Justificación Semántica:
* Estas reglas aseguran que el modelo de responsable sea coherente, trazable y legalmente válido.
* Protegen la integridad de los tratamientos clínicos, evitan autorizaciones inválidas y permiten
*auditar cada decisión tomada en nombre del paciente.

## Ejemplo de Reglas Descubiertas:
- RN-GUARDIAN-001: No puede crearse sin vínculo legal con un paciente.
- RN-GUARDIAN-002: No puede autorizar tratamientos si está inactivo.
- RN-GUARDIAN-003: No puede revocar consentimiento si el tratamiento ya inició.
- RN-GUARDIAN-004: Debe registrar tipo de relación al crearse.
- RN-GUARDIAN-005: No puede desactivarse si tiene autorizaciones vigentes.
    