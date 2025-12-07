# Plantilla de Descubrimiento de Reglas de Negocio por Agregado

##  Agregado: Dentist

##  Propósito:
* Representar al profesional clínico encargado de atender citas odontológicas. Este agregado gestiona
* su disponibilidad, estado operativo, y expone reglas que protegen la continuidad del servicio y la
* coherencia clínica del sistema.

------------------------------------------------------------
1) CREACIÓN
- El odontólogo debe tener al menos 25 años.
- Debe registrar disponibilidad inicial.
- No puede crearse con estado INACTIVO.
- Debe tener nombre válido y documento único.

2) EDICIÓN / ACTUALIZACIÓN
- La disponibilidad no puede quedar vacía.
- No puede reducirse la edad.
- No puede editarse si está inactivo.

3) DESACTIVACIÓN / ELIMINACIÓN
- No puede desactivarse si tiene citas activas en las próximas 24 horas.
- La desactivación se realiza mediante cambio de estado (UserStatus).
- La eliminación física está prohibida; se maneja como estado lógico.

4) OPERACIONES DE DOMINIO
- puedeAgendar(fechaHora): verifica si el odontólogo está activo, tiene disponibilidad registrada,
y no tiene otra cita en ese horario.
- tieneCitaEn(fechaHora): verifica si ya tiene una cita en ese momento.
- citasActivasEnLasProximas24Horas(): devuelve las citas que bloquean la desactivación.

5) INVARIANTES GLOBALES
- Un odontólogo activo siempre debe tener disponibilidad registrada.
- No puede tener dos citas en el mismo horario.
- No puede estar activo sin edad válida.

6) TRAZABILIDAD Y AUDITORÍA
- Se registra el rechazo al desactivar si tiene citas activas.
- Se puede emitir un Outcome al intentar agendar en horario no disponible.

------------------------------------------------------------
## Justificación Semántica:
* Estas reglas protegen la integridad clínica del sistema, evitan estados inconsistentes como
* odontólogos activos sin disponibilidad o con citas duplicadas, y aseguran que el modelo sea
* evaluable, trazable y listo para exhibición internacional.

## Ejemplo de Reglas Descubiertas:
- RN-DENTIST-001: Un odontólogo debe tener al menos 25 años al crearse.
- RN-DENTIST-002: Debe registrar disponibilidad inicial.
- RN-DENTIST-003: No puede desactivarse si tiene citas activas en las próximas 24 horas.
- RN-DENTIST-004: No puede tener dos citas en el mismo horario.
- RN-DENTIST-005: Solo puede agendar si está activo y disponible.
    