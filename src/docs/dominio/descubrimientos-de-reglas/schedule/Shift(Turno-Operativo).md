# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Shift (Turno Operativo)

## Propósito
Representar un bloque de tiempo asignado a un profesional (clínico o administrativo) para cumplir funciones dentro del sistema. Este agregado protege la continuidad operativa, evita solapamientos de turnos y permite validar disponibilidad legítima para tareas o citas. Gestiona la planificación de recursos humanos y cobertura de servicios.

---

## CREACIÓN
- Debe especificarse fecha, hora de inicio y hora de fin.
- La hora de inicio debe ser anterior a la hora de fin.
- No puede crearse si el profesional está inactivo.
- Debe estar asociado a una sede o unidad operativa válida.
- No puede solaparse con otro turno del mismo profesional.
- No puede crearse turno con duración negativa o cero.
- Se registra automáticamente fecha de creación y última modificación.
- Debe especificarse tipo de turno (CLINICO, ADMINISTRATIVO, GUARDIA).

---

## EDICIÓN / ACTUALIZACIÓN
- Solo puede editarse si no tiene tareas ni citas asignadas.
- No puede modificarse si está dentro de las 24h previas.
- No puede extenderse sobre otro turno ya registrado del mismo profesional.
- Cambios sensibles deben registrar fecha, responsable y motivo.
- No puede reducirse el horario si eliminaría asignaciones existentes.
- Solo puede editarse si el profesional sigue activo.

---

## CANCELACIÓN / DESACTIVACIÓN
- No puede cancelarse si tiene tareas clínicas o administrativas activas.
- Debe registrar motivo obligatorio de cancelación.
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar fecha de cancelación.
- No puede cancelarse si está dentro de las 24h previas sin autorización especial.

---

## OPERACIONES DE DOMINIO
- cubre(fechaHora) → Verifica si el turno incluye ese momento específico.
- intersectaCon(otroTurno) → Detecta conflictos entre turnos.
- puedeAsignarseTarea(fechaHora) → Valida si ese horario está libre y cubierto.
- esTurnoActivo() → Verifica si el turno está vigente y habilitado.
- tieneTareasActivas() → Verifica si tiene tareas o citas asignadas.
- getDuracionHoras() → Calcula duración total del turno en horas.

---

## INVARIANTES GLOBALES
- Un turno válido debe tener duración positiva (horaFin > horaInicio).
- No puede haber dos turnos que se solapen para el mismo profesional.
- No puede estar activo si el profesional está inactivo.
- No puede tener tareas asignadas si está cancelado.
- Debe estar asociado a una sede válida.
- No puede cancelarse si tiene tareas activas.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada creación, edición, cancelación y asignación de tareas.
- Se puede emitir un Outcome al intentar asignar fuera de turno.
- Se registra el profesional, sede y fecha de modificación.
- Sistema emite alertas al detectar intentos de solapamiento.
- Auditoría completa de cobertura operativa por sede y período.

---

## Justificación Semántica
Estas reglas aseguran que el modelo de turno sea coherente, evaluable y trazable. Protegen la operación clínica y administrativa, evitan conflictos de agenda, garantizan cobertura adecuada de servicios y permiten validar condiciones antes de asignar tareas o citas. El modelo está listo para integrarse en flujos de atención, reportes de cobertura, gestión de recursos humanos y exhibición internacional.

---

## Reglas Descubiertas (formato estandarizado)

**RN-SHIFT-001**
- Descripción: La hora de inicio debe ser anterior a la hora de fin.
- Condición: Shift.horaInicio >= Shift.horaFin al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_SHIFT_INVALID_TIME_RANGE

**RN-SHIFT-002**
- Descripción: No puede crearse si el profesional está inactivo.
- Condición: Shift.profesional.status != ACTIVE al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SHIFT_PROFESSIONAL_INACTIVE

**RN-SHIFT-003**
- Descripción: No puede solaparse con otro turno del mismo profesional.
- Condición: Shift.intersectaCon(otroTurno) == true para mismo profesionalId.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_SHIFT_OVERLAP_CONFLICT

**RN-SHIFT-004**
- Descripción: No puede editarse si tiene tareas asignadas o está dentro de 24h.
- Condición: Shift.tieneTareasActivas() == true || (fechaTurno - now) < 24h al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SHIFT_CANNOT_EDIT

**RN-SHIFT-005**
- Descripción: No puede cancelarse si tiene tareas activas.
- Condición: Shift.tieneTareasActivas() == true al invocar cancelación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SHIFT_HAS_ACTIVE_TASKS

**RN-SHIFT-006**
- Descripción: Debe estar asociado a una sede válida.
- Condición: Shift.sede == null || sede.status != ACTIVE al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SHIFT_INVALID_LOCATION

**RN-SHIFT-007**
- Descripción: Cancelación requiere motivo obligatorio.
- Condición: cancelar(motivo) con motivo == null || motivo.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SHIFT_CANCELLATION_REQUIRES_REASON

**RN-SHIFT-008**
- Descripción: No puede tener duración negativa o cero.
- Condición: (horaFin - horaInicio) <= 0 al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SHIFT_ZERO_DURATION

**RN-SHIFT-009**
- Descripción: No puede modificarse si está dentro de 24h previas sin autorización.
- Condición: (fechaTurno - now) < 24h && !tieneAutorizacionEspecial al invocar edición/cancelación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_SHIFT_LATE_MODIFICATION

---

## Relación con ADRs
- ADR-25 (Dominio): Modelado de Persona en el dominio clínico - profesionales asignados.
- ADR-30 (Dominio): Catálogo CRUD por rol - permisos para gestionar turnos.
- ADR-32 (Dominio): Implementación sistemática de reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas de negocio - validación de cobertura operativa.

---

## Eventos de Dominio
- ShiftCreated: Al crear nuevo turno operativo.
- ShiftUpdated: Al modificar horarios del turno.
- ShiftCancelled: Al cancelar el turno.
- ShiftTaskAssigned: Al asignar tarea o cita al turno.
- ShiftConflictDetected: Al detectar solapamiento con otro turno.
- ShiftCoverageAlert: Si no hay cobertura suficiente en período.

---

## Tipos de Turno

**CLINICO (Clinical)**
- Profesionales de salud (odontólogos, higienistas).
- Pueden atender citas clínicas.
- Requieren disponibilidad clínica activa.
- Ejemplo: Dr. Gómez - Turno Clínico 08:00-14:00.

**ADMINISTRATIVO (Administrative)**
- Personal administrativo (recepcionistas, coordinadores).
- Gestionan agenda, facturación, atención al cliente.
- No atienden citas clínicas directamente.
- Ejemplo: Secretaria López - Turno Administrativo 07:00-13:00.

**GUARDIA (On-Call)**
- Turnos de emergencia o disponibilidad extendida.
- Pueden estar fuera de horario regular.
- Requieren compensación especial.
- Ejemplo: Dr. Pérez - Guardia Nocturna 18:00-22:00.

**CAPACITACION (Training)**
- Turnos dedicados a formación, no atienden servicios.
- Bloquean disponibilidad para atención.
- Ejemplo: Personal - Capacitación 09:00-12:00.

---

## Ejemplo de Uso

```java
// Crear turno clínico
Shift turnoDrGomez = Shift.create(
    profesional: drGomez,
    fecha: LocalDate.of(2025, 12, 15),
    horaInicio: LocalTime.of(8, 0),
    horaFin: LocalTime.of(14, 0),
    tipo: TipoTurno.CLINICO,
    sede: sedeNorte
);

// Validar solapamiento
Shift otroTurno = Shift.create(
    profesional: drGomez,
    fecha: LocalDate.of(2025, 12, 15),
    horaInicio: LocalTime.of(13, 0),
    horaFin: LocalTime.of(18, 0),
    tipo: TipoTurno.CLINICO,
    sede: sedeSur
);

if (turnoDrGomez.intersectaCon(otroTurno)) {
    throw new InvalidShiftException("El Dr. Gómez no puede estar en dos sedes simultáneamente");
}

// Verificar cobertura
boolean cubreCita = turnoDrGomez.cubre(LocalDateTime.of(2025, 12, 15, 10, 30));
boolean puedeAsignar = turnoDrGomez.puedeAsignarseTarea(LocalDateTime.of(2025, 12, 15, 11, 0));
```

---

## Integración con Availability

- **Shift** define presencia física del profesional en la sede.
- **Availability** define horarios específicos de atención clínica.
- Un profesional puede tener Shift 08:00-14:00 pero Availability solo 09:00-12:00.
- Availability debe estar contenida dentro de un Shift válido.
- Ejemplo: Turno 08:00-14:00 (incluye llegada, preparación, almuerzo), Disponibilidad Clínica 09:00-12:00.

---

## Métricas de Gestión

**Cobertura Operativa**
- Horas totales de turnos programados vs. horas requeridas.
- Profesionales disponibles por sede y horario.
- Detección de períodos con cobertura insuficiente.

**Ocupación de Turno**
- Porcentaje del turno con tareas asignadas.
- Tiempos ociosos dentro del turno.
- Eficiencia operativa por profesional.

**Cumplimiento de Turno**
- Turnos completados vs. turnos cancelados.
- Cancelaciones de último momento (< 24h).
- Motivos de cancelación más frecuentes.