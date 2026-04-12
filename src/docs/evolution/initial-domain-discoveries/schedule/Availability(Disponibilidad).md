# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Availability (Disponibilidad)

## Propósito
Representar los bloques de tiempo en los que un profesional clínico está disponible para agendar citas. Este agregado protege la coherencia de la agenda, evita conflictos de horarios y permite validar condiciones operativas antes de asignar una cita. Gestiona la disponibilidad recurrente y específica del profesional.

---

## CREACIÓN
- Debe especificarse día de la semana, hora de inicio y hora de fin.
- La hora de inicio debe ser anterior a la hora de fin.
- No puede crearse disponibilidad con duración negativa o cero.
- Debe estar asociada a un profesional (Dentist) activo.
- No puede crearse disponibilidad que solape con otra existente.
- Debe validarse que el rango horario sea coherente (ej: 08:00-18:00).
- Se registra fecha de creación y última modificación automáticamente.

---

## EDICIÓN / ACTUALIZACIÓN
- No puede modificarse si ya tiene citas agendadas dentro del bloque.
- Solo puede editarse si el profesional está activo.
- No puede extenderse sobre otro bloque ya registrado (solapamiento).
- Cambios sensibles deben registrar fecha, responsable y motivo.
- No puede reducirse el horario si eliminaría citas existentes.

---

## ELIMINACIÓN / DESACTIVACIÓN
- No puede eliminarse si tiene citas activas asociadas.
- La eliminación física está prohibida; se maneja como estado lógico (INACTIVE).
- Debe registrar motivo obligatorio de desactivación.
- Debe registrar fecha de desactivación.
- No puede desactivarse si tiene citas confirmadas en las próximas 48h.

---

## OPERACIONES DE DOMINIO
- cubre(fechaHora) → Verifica si la disponibilidad incluye ese momento específico.
- esValida() → Verifica que el bloque tenga duración positiva y no esté solapado.
- intersectaCon(otraDisponibilidad) → Detecta conflictos entre bloques.
- puedeAsignarseCita(fechaHora) → Valida si ese horario está libre y cubierto.
- tieneCitasActivas() → Verifica si tiene citas agendadas en este bloque.
- getDuracionMinutos() → Calcula duración total del bloque en minutos.

---

## INVARIANTES GLOBALES
- Una disponibilidad válida debe tener duración positiva (horaFin > horaInicio).
- No puede haber dos bloques que se solapen para el mismo profesional en el mismo día.
- No puede eliminarse si tiene citas activas.
- Un profesional inactivo no puede tener disponibilidades activas.
- La hora de inicio debe ser anterior a la hora de fin.
- No puede haber disponibilidad sin profesional asociado.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada creación, edición y desactivación.
- Se puede emitir un Outcome al intentar agendar fuera de disponibilidad.
- Se registra el profesional asociado y la fecha de modificación.
- Sistema emite alertas al detectar intentos de solapamiento.
- Auditoría completa de citas asociadas al bloque.

---

## Justificación Semántica
Estas reglas aseguran que el modelo de disponibilidad sea coherente, evaluable y trazable. Protegen la agenda clínica, evitan conflictos operativos de horarios, garantizan que no se eliminen bloques con citas activas y permiten validar condiciones antes de asignar una cita. El modelo está listo para integrarse en flujos de atención, reportes de ocupación y exhibición internacional.

---

## Reglas Descubiertas (formato estandarizado)

**RN-AVAIL-001**
- Descripción: La hora de inicio debe ser anterior a la hora de fin.
- Condición: Availability.horaInicio >= Availability.horaFin al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_AVAIL_INVALID_TIME_RANGE

**RN-AVAIL-002**
- Descripción: No puede crearse disponibilidad con duración negativa o cero.
- Condición: (horaFin - horaInicio) <= 0 al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_AVAIL_ZERO_DURATION

**RN-AVAIL-003**
- Descripción: No puede modificarse si tiene citas agendadas dentro del bloque.
- Condición: Availability.tieneCitasActivas() == true al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_AVAIL_HAS_ACTIVE_APPOINTMENTS

**RN-AVAIL-004**
- Descripción: No puede haber dos bloques que se solapen para el mismo profesional.
- Condición: Availability.intersectaCon(otraDisponibilidad) == true al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_AVAIL_OVERLAP_CONFLICT

**RN-AVAIL-005**
- Descripción: No puede eliminarse si tiene citas activas asociadas.
- Condición: Availability.tieneCitasActivas() == true al invocar eliminación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_AVAIL_CANNOT_DELETE_WITH_APPOINTMENTS

**RN-AVAIL-006**
- Descripción: Debe estar asociada a un profesional activo.
- Condición: Availability.dentist.status != ACTIVE al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_AVAIL_DENTIST_INACTIVE

**RN-AVAIL-007**
- Descripción: Solo puede editarse si el profesional está activo.
- Condición: Availability.dentist.status != ACTIVE al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_AVAIL_CANNOT_EDIT_INACTIVE_DENTIST

**RN-AVAIL-008**
- Descripción: Desactivación requiere motivo obligatorio.
- Condición: desactivar(motivo) con motivo == null || motivo.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_AVAIL_DEACTIVATION_REQUIRES_REASON

**RN-AVAIL-009**
- Descripción: No puede extenderse sobre otro bloque ya registrado.
- Condición: Nueva extensión intersecta con otra Availability al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_AVAIL_EXTENSION_CONFLICT

---

## Relación con ADRs
- ADR-25 (Dominio): Modelado de Persona en el dominio clínico - relación con Dentist.
- ADR-30 (Dominio): Catálogo de reglas CRUD por rol - permisos para gestionar disponibilidad.
- ADR-32 (Dominio): Implementación sistemática de reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas de negocio - validación de solapamientos.

---

## Eventos de Dominio
- AvailabilityCreated: Al crear nuevo bloque de disponibilidad.
- AvailabilityUpdated: Al modificar horarios de disponibilidad.
- AvailabilityDeactivated: Al desactivar bloque.
- AvailabilityConflictDetected: Al detectar solapamiento con otra disponibilidad.
- AvailabilityFullyBooked: Cuando todas las citas posibles están agendadas.

---

## Tipos de Disponibilidad

**Disponibilidad Recurrente (Semanal)**
- Ejemplo: Lunes 08:00-12:00, Miércoles 14:00-18:00.
- Se aplica todas las semanas.
- Base para generación automática de citas disponibles.

**Disponibilidad Específica (Por Fecha)**
- Ejemplo: 2025-12-25 09:00-11:00 (día festivo con atención especial).
- Sobrescribe disponibilidad recurrente si existe conflicto.
- Útil para excepciones, guardias, atención extraordinaria.

**Disponibilidad Bloqueada (No Disponible)**
- Ejemplo: Vacaciones, capacitación, ausencias.
- Marca períodos donde NO hay disponibilidad.
- Impide agendamiento en esas fechas.

---

## Ejemplo de Uso

```java
// Crear disponibilidad semanal
Availability lunes = Availability.create(
    dentist: drGomez,
    diaSemana: DayOfWeek.MONDAY,
    horaInicio: LocalTime.of(8, 0),
    horaFin: LocalTime.of(12, 0)
);

// Verificar si cubre un horario específico
LocalDateTime citaDeseada = LocalDateTime.of(2025, 12, 15, 10, 30);
boolean cubre = lunes.cubre(citaDeseada); // true si es lunes 10:30

// Detectar conflicto con otra disponibilidad
Availability otraDisp = Availability.create(
    dentist: drGomez,
    diaSemana: DayOfWeek.MONDAY,
    horaInicio: LocalTime.of(11, 0),
    horaFin: LocalTime.of(15, 0)
);

if (lunes.intersectaCon(otraDisp)) {
    throw new InvalidAvailabilityException("Solapamiento detectado");
}
```

---

## Integración con TimeSlot

- **Availability** define bloques amplios (ej: Lunes 08:00-12:00).
- **TimeSlot** divide la disponibilidad en intervalos pequeños (ej: 08:00-08:30, 08:30-09:00).
- TimeSlot permite granularidad fina para agendamiento.
- Cada TimeSlot debe estar contenido dentro de una Availability válida.