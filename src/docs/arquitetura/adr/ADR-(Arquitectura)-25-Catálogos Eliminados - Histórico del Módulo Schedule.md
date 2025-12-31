# ADR-25 (Arquitectura): Catálogos Eliminados - Histórico del Módulo Schedule

**Estado:** 📚 Registro Histórico  
**Fecha:** Diciembre 28, 2025  
**Propósito:** Documentar catálogos de error eliminados con justificación técnica

---

## Propósito de este Documento

Este ADR mantiene el **registro histórico oficial** de todos los catálogos de error que fueron eliminados del Módulo Schedule, incluyendo:
- Código y descripción original
- Fecha de eliminación
- Motivo técnico detallado
- Catálogo de reemplazo (si aplica)
- Referencias a decisiones arquitectónicas

**Nota:** Según ADR-22, los códigos eliminados **NUNCA se reutilizan**. Este documento sirve como referencia para:
- Auditorías de cumplimiento
- Debugging de logs históricos
- Migración de sistemas legacy
- Trazabilidad de evolución arquitectónica

---

## 📅 Agregado: **Appointment** (Cita Clínica) - Eliminados

### ERR_APPT_DENTIST_INACTIVE
- **Código:** RN-APPT-001
- **Descripción original:** "No puede crearse si el odontólogo está inactivo"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** DELEGACIÓN A DENTIST
- **Justificación técnica:**  
  La validación de estado activo del odontólogo es responsabilidad del agregado `Dentist`, no de `Appointment`. El agregado Dentist ya expone el método `canScheduleBetween(user, start, end)` que incluye esta validación mediante `UserStatus.from(user).mustBeActive()` y `ensureEditable()`. Mantener esta validación en Appointment viola el principio de responsabilidad única y genera duplicación de lógica de negocio.
- **Reemplazo:** Delegación a `Dentist.canScheduleBetween(user, start, end)`
- **Ejemplo uso (antes):**
  ```java
  // En AppointmentSchedulingService
  if (!dentist.isActive()) {
      throw new BusinessRuleViolationException(
          "RN-APPT-001",
          "No puede crearse si el odontólogo está inactivo"
      );
  }
  ```
- **Ejemplo uso (después):**
  ```java
  // En AppointmentSchedulingService
  dentist.canScheduleBetween(user, start, end);  // ✅ Delega validación al agregado
  ```
- **Referencia:** ADR-22, Sección "División de Responsabilidades"

---

## 🕐 Agregado: **Availability** (Disponibilidad) - Eliminados

### ERR_AVAIL_HAS_ACTIVE_APPOINTMENTS
- **Código:** RN-AVAIL-003
- **Descripción original:** "No puede modificarse si tiene citas agendadas dentro del bloque"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** RESPONSABILIDAD DE DOMAIN SERVICE
- **Justificación técnica:**  
  Esta validación requiere consultar citas asociadas desde el repositorio, lo cual NO debe hacerse dentro del agregado `Availability`. Los agregados no deben tener dependencias a repositorios de otros agregados. La validación correcta debe ocurrir en un Domain Service que tenga acceso al `AppointmentRepository` y pueda consultar si existen citas en el rango de la disponibilidad.
- **Reemplazo:** `AvailabilityManagementService.updateAvailability()`
- **Arquitectura correcta:**
  ```java
  // ❌ ANTES: En Availability (agregado)
  public void update(...) {
      if (this.hasActiveAppointments()) {  // ← Consulta a otro agregado
          throw new BusinessRuleViolationException("RN-AVAIL-003");
      }
  }
  
  // ✅ DESPUÉS: En Domain Service
  public void updateAvailability(Availability availability, ...) {
      List<Appointment> conflicts = appointmentRepository
          .findByAvailability(availability.getId());
      
      if (!conflicts.isEmpty()) {
          throw new BusinessRuleViolationException("Tiene citas activas");
      }
      
      availabilityRepository.save(availability);
  }
  ```
- **Referencia:** ADR-22, Principio "Agregados no consultan otros agregados"

---

### ERR_AVAIL_CANNOT_DELETE_WITH_APPOINTMENTS
- **Código:** RN-AVAIL-005
- **Descripción original:** "No puede eliminarse si tiene citas activas asociadas"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** RESPONSABILIDAD DE DOMAIN SERVICE (idéntico a RN-AVAIL-003)
- **Justificación técnica:**  
  Misma razón que RN-AVAIL-003. La validación de citas asociadas debe hacerse en el Domain Service, no en el agregado.
- **Reemplazo:** `AvailabilityManagementService.deleteAvailability()`
- **Referencia:** ADR-22

---

### ERR_AVAIL_DENTIST_INACTIVE
- **Código:** RN-AVAIL-006
- **Descripción original:** "Debe estar asociada a un profesional activo"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** DELEGACIÓN A DENTIST
- **Justificación técnica:**  
  La validación de estado del dentista es responsabilidad del agregado `Dentist`, no de `Availability`. Al crear o modificar una disponibilidad, el Domain Service debe primero validar que el dentista esté activo mediante `dentist.canScheduleBetween()` o `dentist.ensureEditable()`.
- **Reemplazo:** Delegación a `Dentist.ensureEditable()`
- **Referencia:** ADR-22

---

### ERR_AVAIL_CANNOT_EDIT_INACTIVE_DENTIST
- **Código:** RN-AVAIL-007
- **Descripción original:** "Solo puede editarse si el profesional está activo"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** DELEGACIÓN A DENTIST (idéntico a RN-AVAIL-006)
- **Reemplazo:** Delegación a `Dentist.ensureEditable()`
- **Referencia:** ADR-22

---

### ERR_AVAIL_DEACTIVATION_REQUIRES_REASON
- **Código:** RN-AVAIL-008
- **Descripción original:** "La desactivación requiere motivo obligatorio"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** ELIMINACIÓN DE AGREGADO (Availability quedó como VO en Schedule)
- **Justificación técnica:**  
  Aunque inicialmente se consideró modelar `Availability` como agregado completo, en la implementación final se optó por mantenerlo como Value Object dentro de `WeeklyAvailability` y `Schedule`. Por lo tanto, las operaciones de desactivación con motivo no aplican en este contexto. Si en el futuro se decide convertir Availability en agregado, este catálogo podría reintroducirse con un nuevo código.
- **Reemplazo:** Ninguno (regla no aplica en diseño actual)
- **Referencia:** ADR-22, Sección "Availability como VO vs Agregado"

---

## 👔 Agregado: **Shift** (Turno Operativo) - Eliminados

### ERR_SHIFT_PROFESSIONAL_INACTIVE
- **Código:** RN-SHIFT-002
- **Descripción original:** "No puede crearse si el profesional está inactivo"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** DELEGACIÓN A DENTIST
- **Justificación técnica:**  
  Idéntico a RN-APPT-001 y RN-AVAIL-006. La validación de estado activo del profesional debe delegarse al agregado `Dentist`.
- **Reemplazo:** Delegación a `Dentist.ensureEditable()`
- **Referencia:** ADR-22

---

### ERR_SHIFT_CANNOT_EDIT
- **Código:** RN-SHIFT-004
- **Descripción original:** "No puede editarse si tiene tareas asignadas o está dentro de 24h"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** REDUNDANCIA CON APPOINTMENT
- **Justificación técnica:**  
  Las "tareas asignadas" mencionadas son en realidad citas (`Appointment`). Esta validación duplica la lógica que ya existe en `RN-AVAIL-003` (validar citas asociadas). La coordinación entre Shift y Appointments debe hacerse en el Domain Service, no mediante catálogos duplicados.
- **Reemplazo:** Validación en `AppointmentSchedulingService`
- **Nota:** La restricción de 24h se mantiene en `Shift.reschedule()` mediante el parámetro `hasAuthorization`
- **Referencia:** ADR-22

---

### ERR_SHIFT_HAS_ACTIVE_TASKS
- **Código:** RN-SHIFT-005
- **Descripción original:** "No puede cancelarse si tiene tareas activas"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** REDUNDANCIA CON APPOINTMENT (idéntico a RN-SHIFT-004)
- **Justificación técnica:**  
  Las "tareas activas" son citas. Esta validación es idéntica a RN-SHIFT-004 pero en contexto de cancelación. La validación debe hacerse en Domain Service consultando `AppointmentRepository`.
- **Reemplazo:** Validación en Domain Service
- **Referencia:** ADR-22

---

## 🕒 Derivado: **TimeSlot** (Bloque Horario) - TODOS ELIMINADOS

### Decisión Arquitectónica: TimeSlot como Concepto Derivado

**Fecha eliminación:** 2025-12-28  
**Motivo:** ARQUITECTURA INCORRECTA - TimeSlot no debe ser agregado persistido

**Justificación técnica:**  
Durante el diseño inicial, se consideró `TimeSlot` como agregado independiente con su propio repositorio y ciclo de vida. Sin embargo, esto generaba:
- Complejidad de sincronización entre Availability y TimeSlot
- Redundancia de validaciones (las mismas reglas aplicaban a Availability y TimeSlot)
- Problema de consistencia: ¿Qué pasa si se modifica Availability pero no se regeneran los TimeSlots?

**Decisión correcta:**  
TimeSlot es un **concepto derivado de lectura**, generado dinámicamente desde `Availability`:

```java
// ✅ TimeSlot se genera on-demand, NO se persiste
List<TimeSlot> slots = TimeSlot.generateFrom(
    availability, 
    targetDate, 
    slotDurationMinutes
);
```

**Consecuencia:** Todos los catálogos RN-TIMESLOT-* (9 reglas) fueron eliminados porque:
- Las validaciones estructurales (RN-TIMESLOT-001, 002) ocurren en `Availability`
- Las validaciones de asignación (RN-TIMESLOT-005) ocurren en `Appointment`
- Las validaciones de cobertura (RN-TIMESLOT-006, 009) ocurren en Domain Service

---

### ERR_TIMESLOT_INVALID_DURATION
- **Código:** RN-TIMESLOT-001
- **Descripción original:** "La duración debe ser positiva y dentro de límites permitidos"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** VALIDACIÓN OCURRE EN AVAILABILITY
- **Reemplazo:** `Availability` valida su duración completa
- **Referencia:** ADR-22, "TimeSlot como Derivado Ligero"

---

### ERR_TIMESLOT_PROFESSIONAL_INACTIVE
- **Código:** RN-TIMESLOT-002
- **Descripción original:** "No puede crearse si el profesional está inactivo"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** DELEGACIÓN A DENTIST
- **Reemplazo:** `Dentist.ensureEditable()`

---

### ERR_TIMESLOT_OVERLAP_CONFLICT
- **Código:** RN-TIMESLOT-003
- **Descripción original:** "No puede solaparse con otro TimeSlot ya asignado"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** VALIDACIÓN OCURRE EN AVAILABILITY
- **Reemplazo:** `Availability.overlapsWith()`

---

### ERR_TIMESLOT_CANNOT_EDIT
- **Código:** RN-TIMESLOT-004
- **Descripción original:** "No puede editarse si tiene cita asignada o está dentro de 24h previas"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** VALIDACIÓN OCURRE EN APPOINTMENT/AVAILABILITY
- **Reemplazo:** Validación en Domain Service

---

### ERR_TIMESLOT_ALREADY_BOOKED
- **Código:** RN-TIMESLOT-005
- **Descripción original:** "No puede tener más de una cita asignada"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** VALIDACIÓN OCURRE EN DOMAIN SERVICE CON LOCK
- **Reemplazo:** `AppointmentSchedulingService.ensureNoConflicts()` con pessimistic lock

---

### ERR_TIMESLOT_OUTSIDE_AVAILABILITY
- **Código:** RN-TIMESLOT-006
- **Descripción original:** "Debe estar contenido dentro de una disponibilidad válida"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** VALIDACIÓN OCURRE EN DOMAIN SERVICE
- **Reemplazo:** `AppointmentSchedulingService.ensureAvailabilityCoverage()`

---

### ERR_TIMESLOT_CANCELLATION_REQUIRES_REASON
- **Código:** RN-TIMESLOT-007
- **Descripción original:** "Cancelación requiere motivo obligatorio"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** VALIDACIÓN OCURRE EN APPOINTMENT
- **Reemplazo:** `Appointment.cancel(reason)` valida motivo obligatorio

---

### ERR_TIMESLOT_HAS_ACTIVE_APPOINTMENT
- **Código:** RN-TIMESLOT-008
- **Descripción original:** "No puede cancelarse si tiene cita activa"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** VALIDACIÓN OCURRE EN DOMAIN SERVICE
- **Reemplazo:** Consulta a `AppointmentRepository` en Domain Service

---

### ERR_TIMESLOT_EXCEEDS_AVAILABILITY
- **Código:** RN-TIMESLOT-009
- **Descripción original:** "No puede extenderse fuera de la disponibilidad original"
- **Fecha eliminación:** 2025-12-28
- **Motivo:** VALIDACIÓN OCURRE EN AVAILABILITY
- **Reemplazo:** `Availability.extend()` valida límites

---

## 📊 Estadísticas de Eliminación

| Agregado/Concepto | Eliminados | Motivo Principal |
|-------------------|-----------|------------------|
| Appointment | 1 | Delegación a Dentist |
| Availability | 4 | Delegación (3), Decisión de diseño (1) |
| Shift | 3 | Delegación (1), Redundancia (2) |
| TimeSlot | 9 | Arquitectura incorrecta (concepto derivado) |
| **TOTAL** | **17** | |

### Distribución por Motivo

```
ARQUITECTURA INCORRECTA:     53% ████████████████░░░░░░░░░░
DELEGACIÓN A DENTIST:        24% ███████░░░░░░░░░░░░░░░░░░░
RESPONSABILIDAD DOMAIN SVC:  12% ███░░░░░░░░░░░░░░░░░░░░░░░
REDUNDANCIA:                 11% ███░░░░░░░░░░░░░░░░░░░░░░░
```

---

## Lecciones Aprendidas

### 1. **TimeSlot: El Peligro del Sobre-Modelado**

**Problema inicial:**
- Se modeló TimeSlot como agregado independiente con identidad propia
- Se crearon 9 catálogos de error específicos
- Se diseñó persistencia y repositorio dedicado

**Realidad:**
- TimeSlot es un **concepto de consulta**, no de estado persistente
- Todas sus validaciones ya existen en Availability o Appointment
- La generación dinámica evita problemas de sincronización

**Lección:** No todo concepto del dominio necesita ser un agregado persistido. Algunos conceptos son "proyecciones" o "vistas" derivadas de otros agregados.

---

### 2. **Delegación vs Duplicación de Validaciones**

**Anti-patrón detectado:**
```java
// ❌ Validación duplicada en múltiples agregados
if (!dentist.isActive()) {  // En Appointment
    throw new Exception();
}
if (!dentist.isActive()) {  // En Availability
    throw new Exception();
}
if (!dentist.isActive()) {  // En Shift
    throw new Exception();
}
```

**Solución correcta:**
```java
// ✅ Delegación al agregado responsable
dentist.ensureEditable();  // En Domain Service
```

**Lección:** Si múltiples agregados necesitan validar el estado de otro, la validación debe **delegarse**, no **duplicarse**.

---

### 3. **Domain Services para Coordinación Entre Agregados**

**Problema inicial:**
- Availability intentaba validar si tenía citas asociadas
- Shift intentaba validar si tenía tareas asignadas
- Esto requería que los agregados consultaran repositorios ajenos

**Solución correcta:**
```java
// ✅ Domain Service coordina múltiples agregados
public class AvailabilityManagementService {
    public void updateAvailability(Availability av, ...) {
        // Consulta repositorio de otro agregado
        List<Appointment> conflicts = appointmentRepo.findByAvailability(av.getId());
        
        if (!conflicts.isEmpty()) {
            throw new BusinessRuleViolationException("Tiene citas activas");
        }
        
        availabilityRepo.save(av);
    }
}
```

**Lección:** La coordinación entre agregados NO ocurre dentro de los agregados, sino en **Domain Services** que tienen acceso a múltiples repositorios.

---

### 4. **Validaciones en la Capa Correcta**

| Tipo de Validación | Responsable | Ejemplo |
|-------------------|-------------|---------|
| Estado del agregado | Agregado mismo | `Dentist.ensureEditable()` |
| Formato de datos | Value Object | `ServiceDuration.of(30)` valida rango |
| Coordinación entre agregados | Domain Service | Validar conflictos con lock |
| Datos de entrada simples | Domain Service | `start < end`, `reason not blank` |

---

## Comparación con Módulo Actor

### Similitudes en Patrones de Eliminación

| Patrón | Actor | Schedule |
|--------|-------|----------|
| Delegación a User | 41% (7/17) | 24% (4/17) |
| Validación de VO | 35% (6/17) | 0% (Schedule no tuvo este problema) |
| Redundancia por operación | 18% (3/17) | 11% (2/17) |
| Arquitectura incorrecta | 6% (1/17) | 53% (9/17) TimeSlot |

### Diferencias Clave

**Módulo Actor:**
- Problema principal: Delegación a User y validaciones de VO mal ubicadas
- Causa: Falta de comprensión de separación de capas

**Módulo Schedule:**
- Problema principal: TimeSlot sobre-modelado como agregado
- Causa: Sobre-ingeniería y complejidad innecesaria

**Aprendizaje:** Los errores evolucionaron de "no entender dónde van las validaciones" (Actor) a "sobre-complicar el diseño" (Schedule), lo cual demuestra maduración arquitectónica.

---

## Referencias Cruzadas

- **ADR-25:** Alcance Experimental del Módulo Schedule
- **ADR-22 (Arquitectura):** Estrategia de Numeración de Catálogos de Error
- **ADR-23 (Arquitectura):** Catálogos Eliminados - Histórico del Módulo Actor
- **Código fuente:** `com.example.ClinicaDefinitiva.domain.schedule.errors`

---

## Mantenimiento de este Documento

### Cuándo Actualizar
- Al eliminar un nuevo catálogo de error del módulo Schedule
- Al identificar catálogo obsoleto en código legacy
- Al migrar sistema que referencia catálogo eliminado

### Template de Nueva Entrada
```markdown
### ERR_<AGREGADO>_<DESCRIPCION>
- **Código:** RN-<AGREGADO>-<NNN>
- **Descripción original:** "<MENSAJE_ORIGINAL>"
- **Fecha eliminación:** YYYY-MM-DD
- **Motivo:** <CATEGORIA>
- **Justificación técnica:** <EXPLICACION_DETALLADA>
- **Reemplazo:** <NUEVO_CATALOGO> (si aplica)
- **Referencia:** ADR-XXX
```

---

## Relacionado con:
- [ADR-(Arquitectura)-18-Simplificación general de jerarquía de excepciones en el dominio.md](ADR-%28Arquitectura%29-18-Simplificaci%C3%B3n%20general%20de%20jerarqu%C3%ADa%20de%20excepciones%20en%20el%20dominio.md)
- [ADR-(Arquitectura)-19-Catálogo único de errores con contextos diferenciados (Entidad vs VO).md](ADR-%28Arquitectura%29-19-Cat%C3%A1logo%20%C3%BAnico%20de%20errores%20con%20contextos%20diferenciados%20%28Entidad%20vs%20VO%29.md)
- [ADR-(Arquitectura)-21-Catálogos de errores por agregado con interfaz común.md](ADR-%28Arquitectura%29-21-Cat%C3%A1logos%20de%20errores%20por%20agregado%20con%20interfaz%20com%C3%BAn.md)
- [ADR-(Arquitectura)-22-Estrategia de Numeración de Catálogos de Error.md](ADR-%28Arquitectura%29-22-Estrategia%20de%20Numeraci%C3%B3n%20de%20Cat%C3%A1logos%20de%20Error.md)
- [ADR-(Arquitectura)-24-Alcance Experimental del Módulo Schedule.md](ADR-%28Arquitectura%29-24-Alcance%20Experimental%20del%20M%C3%B3dulo%20Schedule.md)

---

## Aprobación

**Autor:** David Stiven Sanclemente  
**Fecha:** Diciembre 28, 2025  
**Estado:** Registro Histórico Oficial  
**Próxima revisión:** Cada eliminación de catálogo

---

**Nota final:** Este documento es **inmutable histórico**. Nuevas entradas se agregan cronológicamente. Entradas existentes **NUNCA** se modifican (solo se corrigen errores tipográficos menores). Los 17 catálogos eliminados documentan la evolución del diseño arquitectónico del módulo Schedule, mostrando el journey desde un diseño sobre-ingenierizado (TimeSlot como agregado) hacia una arquitectura más simple y efectiva (TimeSlot como concepto derivado).