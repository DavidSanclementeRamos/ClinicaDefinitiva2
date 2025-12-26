

# ADR-24 (Arquitectura): Alcance Experimental del Módulo Schedule

**Estado:** ✅ Aceptado  
**Fecha:** Diciembre 25, 2024  
**Contexto:** Definición del alcance de implementación para exhibición profesional  
**Autor:** David stiven Sanclemente

---

## Contexto y Problema

Durante el proceso de implementación del módulo `Schedule` (que incluye los agregados `Appointment`, `Availability`, `Shift` y el derivado `TimeSlot`), se realizó un **descubrimiento exhaustivo de reglas de negocio** documentado en archivos:
- [Appointment(Cita).md](../../dominio/descubrimientos-de-reglas/schedule/Appointment(Cita).md)
- [Availability(Disponibilidad).md](../../dominio/descubrimientos-de-reglas/schedule/Availability(Disponibilidad).md)
- [Shift(Turno-Operativo).md](../../dominio/descubrimientos-de-reglas/schedule/Shift(Turno-Operativo).md)
- [TimeSlot(Bloque-Horario).md](../../dominio/descubrimientos-de-reglas/schedule/TimeSlot(Bloque-Horario).md)

específicos por agregado.

Sin embargo, al enfrentar la implementación real del dominio, surgieron las siguientes realidades:

### 1. **Sobre-especificación inicial**
Los archivos de descubrimiento fueron escritos con **poca experiencia práctica** en DDD, resultando en:
- Reglas redundantes entre agregados
- Validaciones mal ubicadas arquitectónicamente
- Confusión entre agregados, entidades y value objects
- Catálogos duplicados para validaciones similares

### 2. **Madurez arquitectónica evolutiva**
Durante la implementación, se adquirió comprensión sobre:
- Modelado correcto: ¿Availability como VO o Agregado?
- Diferencia semántica entre Shift (presencia física) y Availability (horario de atención)
- TimeSlot como concepto derivado vs entidad persistida
- Separación de responsabilidades: Appointment vs Schedule vs Domain Services

### 3. **Decisión crítica sobre concurrencia**
El sistema de agendamiento es inherentemente concurrente. Surgió la pregunta:
> ¿Implementar manejo de concurrencia en MVP experimental o postergar para fase productiva?

**Impacto:** Race conditions en agendamiento simultáneo pueden causar doble-booking (dos usuarios agendando el mismo slot).

### 4. **Catálogos redundantes entre agregados**
Se detectaron validaciones duplicadas:

**Ejemplo del anti-patrón:**
```java
// ❌ Validaciones repetidas en múltiples agregados
ERR_APPT_DENTIST_INACTIVE        → "Dentista inactivo"
ERR_AVAIL_DENTIST_INACTIVE       → "Dentista inactivo"
ERR_SHIFT_PROFESSIONAL_INACTIVE  → "Profesional inactivo"
ERR_TIMESLOT_PROFESSIONAL_INACTIVE → "Profesional inactivo"

// ✅ Solución: Delegar a agregado responsable
Dentist.ensureActive() → lanza DentistError.ERR_DENTIST_NOT_AVAILABLE
```

### 5. **Restricciones de proyecto experimental**
Este es un **proyecto de exhibición profesional**, no un sistema productivo completo, por lo que debe equilibrar:
- Profundidad técnica suficiente para demostrar capacidades
- Implementación de concurrencia (crítica para nivel senior)
- Alcance manejable para completar en tiempo razonable
- Calidad profesional en lo implementado vs. cobertura exhaustiva

### 6. **Necesidad de documentar decisiones**
Es crítico **justificar y registrar** qué reglas se aplicaron, cuáles se eliminaron y cuáles se pospusieron, estableciendo un **"antes y después"** claro que demuestre evolución técnica.

---

## Decisión

Se establece el **alcance experimental del Módulo Schedule** mediante la clasificación de reglas de negocio y catálogos de error en tres categorías:

### 🟢 **APLICADAS** - Implementadas en v1.0 (Exhibición)
Reglas críticas que demuestran comprensión profunda del dominio, arquitectura sólida y manejo de concurrencia.

### 🟡 **POSPUESTAS** - Documentadas para v2.0 (Iteración Futura)
Reglas importantes pero que requieren infraestructura adicional, eventos de dominio o coordinación entre módulos aún no desarrollados.

### 🔴 **ELIMINADAS** - Descartadas con Justificación
Reglas redundantes, mal ubicadas o arquitectónicamente incorrectas según los principios de DDD.

---

## Decisiones Arquitectónicas Fundamentales

### 1. ✅ **Availability como Agregado con Identidad**

**Decisión:** Modelar Availability como agregado completo, NO como Value Object.

**Justificación:**
- **Reglas que requieren identidad:**
    - RN-AVAIL-003: "No puede modificarse si tiene citas agendadas" → Requiere consultar citas asociadas
    - RN-AVAIL-005: "No puede eliminarse con citas activas" → Requiere estado persistido
    - RN-AVAIL-009: "No puede extenderse sobre otro bloque" → Requiere comparación con otros agregados

- **Value Objects no pueden:**
    - Tener relaciones bidireccionales
    - Consultar repositorios
    - Tener ciclo de vida independiente

**Implementación:**
```java

public class Availability {
    
    private AvailabilityId id;  // ✅ Identidad propia
    private DentistId dentistId;
    private Long version;  // ✅ Concurrencia
    
    public boolean overlapsWith(Availability other) { ... }
    public void extend(LocalTime newEnd, Availability... existing) { ... }
}
```

**Catálogos implementados:** RN-AVAIL-001, 002, 004, 008, 009

---

### 2. ✅ **Shift como Agregado Operativo Independiente**

**Decisión:** Implementar Shift como agregado separado de Availability.

**Justificación semántica:**

| Concepto | Shift | Availability |
|----------|-------|-------------|
| **Representa** | Presencia física en clínica | Horario de atención clínica |
| **Ejemplo** | Lunes 08:00-16:00 (incluye almuerzo, prep) | Lunes 09:00-12:00 (solo citas) |
| **Uso** | Validar que dentista esté en sede | Validar que puede atender pacientes |
| **Relación** | Availability DEBE estar dentro de Shift | - |

**Valor agregado real:**
- ✅ Validación: "No agendar cita si dentista no tiene turno ese día"
- ✅ Métricas: Tiempo en turno vs tiempo con citas (eficiencia)
- ✅ Gestión: Guardias, capacitaciones, turnos administrativos
- ✅ Futuro: Base para rotaciones y gestión de recursos humanos

**Implementación:**
```java

public class Shift {
    private ShiftId id;
    private LocalDate date;  // ✅ Fecha específica
    private LocalTime startTime;
    private LocalTime endTime;
    private ShiftType type;  // CLINICAL, ADMINISTRATIVE, ON_CALL, TRAINING
    
    public boolean coversInterval(LocalDateTime start, LocalDateTime end) { ... }
}
```

**Catálogos implementados:** RN-SHIFT-001, 003, 007, 008, 009

---

### 3. ✅ **TimeSlot como Derivado Dinámico (NO Persistido)**

**Decisión:** TimeSlot se genera on-demand desde Availability, NO se persiste en BD.

**Justificación:**
- **TimeSlot es un concepto de consulta**, no de estado
- Generación dinámica evita problemas de sincronización
- Reduce complejidad: 1 tabla (Availability) vs 2 tablas (Availability + TimeSlot)
- Escalable: cambiar duración de slots no requiere migración de datos

**Implementación:**
```java
public final class TimeSlot {  // ✅ Inmutable, sin @Entity
    private final AvailabilityId availabilityId;  // Referencia al padre
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    
    // Factory method
    public static List<TimeSlot> generateFrom(
        Availability availability, 
        LocalDate targetDate,
        int slotDurationMinutes
    ) {
        // Genera slots dinámicamente
    }
}
```

**Consecuencia:** Todos los catálogos RN-TIMESLOT-* (9 reglas) fueron **eliminados** ya que las validaciones ocurren en Availability o Appointment.

---

### 4. ✅ **Concurrencia con Pessimistic Locking**

**Decisión:** Implementar pessimistic locking en operaciones críticas de agendamiento.

**Escenario crítico:**
```
T1: Usuario A intenta agendar    09:00-10:00
T2: Usuario B intenta agendar    09:30-10:30  (simultáneo)

Sin locking: ❌ Ambos ven slot libre → doble-booking
Con locking: ✅ B espera a que A termine → conflicto detectado
```

**Justificación de PESSIMISTIC vs OPTIMISTIC:**

| Criterio | Optimistic (@Version) | Pessimistic (WRITE LOCK) |
|----------|----------------------|-------------------------|
| **Latencia** | ✅ Baja (sin locks) | ⚠️ Media (locks de corta duración) |
| **Conflictos** | ⚠️ Lanza exception post-commit | ✅ Previene antes de commit |
| **UX** | ❌ Usuario ve "conflicto después" | ✅ Usuario ve "slot ocupado" en tiempo real |
| **Complejidad** | ✅ Simple (1 anotación) | ⚠️ Requiere diseño de queries |
| **Caso de uso** | Baja concurrencia | **Alta concurrencia** ← Agendamiento |

**Implementación:**
```java
public interface AppointmentRepository extends JpaRepository<Appointment, AppointmentId> {
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)  // ✅ Bloquea registros
    @Query("SELECT a FROM Appointment a WHERE a.dentist.value = :dentistId " +
           "AND a.status.value IN ('SCHEDULED', 'CONFIRMED') " +
           "AND ((a.start < :end AND a.end > :start))")
    List<Appointment> findConflictingForDentistWithLock(
        @Param("dentistId") String dentistId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end
    );
}
```

**Catálogos afectados:** RN-APPT-004, RN-APPT-009 ahora son thread-safe.

---

### 5. ✅ **Máquina de Estados con Transiciones Validadas**

**Decisión:** Implementar patrón de transiciones explícitas en Value Objects de estado.

**Problema original:**
```java
// ❌ Appointment.java (antes)
public void confirm() {
    this.status.isConfirmed(); // ⚠️ Getter booleano, NO transición
}
```

**Solución:**
```java
// ✅ AppointmentStatus.java
public final class AppointmentStatus {
    private Status value;
    
    private static final EnumMap<Status, Set<Status>> VALID_TRANSITIONS = ...;
    
    public boolean canTransitionTo(Status next) {
        return VALID_TRANSITIONS
            .getOrDefault(value, EnumSet.noneOf(Status.class))
            .contains(next);
    }
    
    public AppointmentStatus transitionTo(Status next) {
        if (!canTransitionTo(next)) {
            throw new InvalidStateTransitionException(value, next);
        }
        return new AppointmentStatus(next);
    }
}

// ✅ Appointment.java (después)
public void confirm() {
    this.status = this.status.transitionTo(Status.CONFIRMED);
    this.lastUpdated = LocalDateTime.now();
}
```

**Valor agregado:**
- ✅ Transiciones explícitas y validadas
- ✅ Estados finales (COMPLETED, CANCELLED) no cambian
- ✅ Trazabilidad: ¿de qué estados puedo llegar a CONFIRMED?

---

## Análisis Detallado por Agregado

---

## 📅 Agregado: **Appointment** (Cita Clínica)

### 🟢 Catálogos APLICADOS (Descubrimiento)

| Código | Descripción | Justificación |
|--------|-------------|---------------|
| **RN-APPT-002** | No agendar fuera de disponibilidad | ⭐ CRÍTICA: Protege agenda del dentista |
| **RN-APPT-003** | Actores válidos (separado en 003-DENT + 003-PAT) | Semántica mejorada por tipo de actor |
| **RN-APPT-004** | Sin conflictos con citas de dentista | Thread-safe con pessimistic lock |
| **RN-APPT-006** | Solo editar en SCHEDULED/CONFIRMED | Máquina de estados |
| **RN-APPT-007** | No cancelar <24h | Política comercial crítica |
| **RN-APPT-008** | Cancelación requiere motivo | Auditoría obligatoria |
| **RN-APPT-009** | Sin conflictos con citas de paciente | Thread-safe con pessimistic lock |
| **RN-APPT-010** | No agendar en el pasado | Validación temporal básica |
| **RN-APPT-011** | Motivo clínico obligatorio | Trazabilidad médica |

### 🟡 Catálogos POSPUESTOS

| Código | Descripción | Motivo | Prioridad |
|--------|-------------|--------|-----------|
| **RN-APPT-005** | Solo finalizar con duración y notas | Requiere flujo completo de atención | 🟡 MEDIA |

### 🔴 Catálogos ELIMINADOS

| Código | Descripción Original | Motivo de Eliminación |
|--------|---------------------|----------------------|
| **RN-APPT-001** | No agendar si dentista inactivo | ❌ **DELEGACIÓN A DENTIST**<br>*Razón:* Dentist.ensureActive() valida su propio estado.<br>*Catálogo original:* `ERR_APPT_DENTIST_INACTIVE`<br>*Reemplazo:* `DentistError.ERR_DENTIST_NOT_AVAILABLE` |

---

## 🕐 Agregado: **Availability** (Disponibilidad)

### 🟢 Catálogos APLICADOS

| Código | Descripción | Origen | Justificación |
|--------|-------------|--------|---------------|
| **RN-AVAIL-001** | Hora inicio < hora fin | Descubrimiento | Invariante estructural |
| **RN-AVAIL-002** | Duración > 0 | Descubrimiento | Validación de rango |
| **RN-AVAIL-004** | Sin solapamiento para mismo profesional | Descubrimiento | Thread-safe con pessimistic lock |
| **RN-AVAIL-008** | Desactivación requiere motivo | Descubrimiento | Auditoría obligatoria |
| **RN-AVAIL-009** | No extender sobre otro bloque | Descubrimiento | Previene conflictos |

### 🔴 Catálogos ELIMINADOS

| Código | Descripción Original | Motivo de Eliminación |
|--------|---------------------|----------------------|
| **RN-AVAIL-003** | No modificar si tiene citas agendadas | ❌ **RESPONSABILIDAD DE DOMAIN SERVICE**<br>*Razón:* Aggregate no debe consultar otros agregados directamente.<br>*Solución:* `AvailabilityManagementService.updateAvailability()` valida esto |
| **RN-AVAIL-005** | No eliminar con citas activas | ❌ **RESPONSABILIDAD DE DOMAIN SERVICE**<br>*Razón:* Similar a RN-AVAIL-003 |
| **RN-AVAIL-006** | Dentista debe estar activo | ❌ **DELEGACIÓN A DENTIST** |
| **RN-AVAIL-007** | Solo editar si profesional activo | ❌ **DELEGACIÓN A DENTIST** |

---

## 👔 Agregado: **Shift** (Turno Operativo)

### 🟢 Catálogos APLICADOS

| Código | Descripción | Justificación |
|--------|-------------|---------------|
| **RN-SHIFT-001** | Hora inicio < hora fin | Invariante estructural |
| **RN-SHIFT-003** | Sin solapamiento para mismo profesional | Thread-safe con pessimistic lock |
| **RN-SHIFT-007** | Cancelación requiere motivo | Auditoría administrativa |
| **RN-SHIFT-008** | Duración > 0 | Validación de rango |
| **RN-SHIFT-009** | No modificar <24h sin autorización | Política operativa con flag especial |

### 🟡 Catálogos POSPUESTOS

| Código | Descripción | Motivo | Prioridad |
|--------|-------------|--------|-----------|
| **RN-SHIFT-006** | Asociación a sede válida | Proyecto experimental sin múltiples sedes | 🟢 BAJA |

### 🔴 Catálogos ELIMINADOS

| Código | Descripción Original | Motivo de Eliminación |
|--------|---------------------|----------------------|
| **RN-SHIFT-002** | No crear si profesional inactivo | ❌ **DELEGACIÓN A DENTIST** |
| **RN-SHIFT-004** | No editar si tiene tareas asignadas | ❌ **REDUNDANCIA CON APPOINTMENT**<br>*Razón:* Las "tareas" son citas, validadas por RN-AVAIL-003 |
| **RN-SHIFT-005** | No cancelar con tareas activas | ❌ **REDUNDANCIA CON APPOINTMENT** |

---

## 🕒 Derivado: **TimeSlot** (Bloque Horario)

### 🔴 **TODOS LOS CATÁLOGOS ELIMINADOS** (9 reglas)

**Decisión arquitectónica:** TimeSlot NO se persiste, se genera dinámicamente desde Availability.

| Códigos eliminados | Razón |
|-------------------|-------|
| **RN-TIMESLOT-001 a 009** | ❌ **ARQUITECTURA INCORRECTA**<br>*Justificación:* TimeSlot es un concepto derivado de lectura. Todas sus validaciones ocurren en:<br>• **Availability** (cobertura, duración)<br>• **Appointment** (asignación, conflictos)<br>• **Domain Service** (coordinación) |

---

## 📊 Estadísticas Finales

### Resumen por Categoría

| Agregado | Aplicadas | Pospuestas | Eliminadas | Total |
|----------|-----------|------------|------------|-------|
| Appointment | 9 | 1 | 1 | 11 |
| Availability | 5 | 0 | 4 | 9 |
| Shift | 5 | 1 | 3 | 9 |
| TimeSlot | 0 | 0 | 9 | 9 |
| **TOTAL** | **19** | **2** | **17** | **38** |

### Distribución de Catálogos

```
Aplicadas:    50% ████████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒
Pospuestas:    5% ██▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
Eliminadas:   45% ███████████▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
```

---

## 🎯 Principios Arquitectónicos Consolidados

### 1. **Separación de Validaciones por Capa**

```
┌─────────────────────────────────────────┐
│ Value Object Layer                      │
│ ✓ Validaciones de formato              │
│ ✓ Invariantes de valor único           │
│ ✓ Transiciones de estado               │
└─────────────────────────────────────────┘
              ↓
┌─────────────────────────────────────────┐
│ Aggregate Layer                         │
│ ✓ Reglas de negocio del agregado       │
│ ✓ Invariantes internos                 │
│ ✓ Validaciones que NO cruzan agregados │
└─────────────────────────────────────────┘
              ↓
┌─────────────────────────────────────────┐
│ Domain Service Layer                    │
│ ✓ Coordinación entre agregados         │
│ ✓ Validaciones que requieren consultas │
│ ✓ Orquestación transaccional           │
└─────────────────────────────────────────┘
```

### 2. **Delegación de Responsabilidades Transversales**

| Responsabilidad | Propietario | Anti-patrón Detectado | Solución |
|----------------|-------------|----------------------|----------|
| Estado activo dentista | `Dentist` | ❌ Validado en 4 agregados | ✅ `Dentist.ensureActive()` |
| Conflictos de horario | `Repository + Service` | ❌ Lógica en agregado | ✅ Pessimistic lock en repo |
| Validación de slots | `Availability` | ❌ TimeSlot como entidad | ✅ TimeSlot.generateFrom() |

### 3. **Patrón de Concurrencia Implementado**

```java
public class AppointmentSchedulingService {
    
    public Appointment scheduleAppointment(...) {
        
        // 1. Validaciones de negocio (sin DB)
        validateBusinessRules(...);
        
        // 2. ✅ PESSIMISTIC LOCK: Consultar conflictos con bloqueo
        List<Appointment> conflicts = repository
            .findConflictingForDentistWithLock(dentistId, start, end);
        
        if (!conflicts.isEmpty()) {
            throw new BusinessRuleViolationException(
                "RN-APPT-004", "Dentista ya tiene cita en ese horario"
            );
        }
        
        // 3. Crear y persistir
        return repository.save(newAppointment);
    }
}
```

---

## 🚀 Elementos Pospuestos (Fuera de Alcance Experimental)

### 1. Auditoría Completa
- Tracking de createdBy, modifiedBy
- Historial de cambios de estado por cita
- **Razón:** Requiere integración con sistema de usuarios y sesiones
- **Preparación:** Campos timestamp (`creationDate`, `lastUpdated`) ya implementados

### 2. Eventos de Dominio
- `AppointmentScheduled`, `AppointmentCancelled`, `AvailabilityBlocked`, etc.
- **Razón:** Requiere infraestructura de mensajería (RabbitMQ/Kafka)
- **Preparación:** Clases de evento diseñadas pero no publicadas

### 3. Notificaciones y Recordatorios
- Email/SMS 24h antes de cita
- Confirmación automática 48h previas
- **Razón:** Fuera del dominio core, pertenece a bounded context de Comunicación

### 4. Métricas y Reportes Avanzados
- Tasa de ocupación por dentista
- Slots desperdiciados
- **Razón:** Requiere CQRS o vistas materializadas

---

## ✅ Métricas de Éxito del MVP

### Cobertura de Reglas Críticas:
- ✅ Appointment: 9/11 reglas (82%)
- ✅ Availability: 5/9 reglas (56%, resto delegado)
- ✅ Shift: 5/9 reglas (56%, resto redundante)
- ✅ TimeSlot: Rediseñado como concepto derivado

### Calidad Técnica Implementada:
- ✅ Pessimistic locking en operaciones críticas
- ✅ Máquinas de estado con transiciones validadas
- ✅ Separación clara de agregados (Availability != VO)
- ✅ Repositorios con queries optimizadas
- ✅ Domain Services para coordinación
- ✅ Value Objects inmutables con validaciones

### Complejidad Manejada:
- ✅ Concurrencia (escenario crítico para nivel senior)
- ✅ Transacciones distribuidas (Schedule + Dentist + Patient)
- ✅ Validaciones temporales complejas (solapamientos, rangos)

---

## 📈 Plan de Evolución

### Fase 2 (Cuando exista cliente real):
1. Implementar auditoría completa con AuditContext
2. Publicar eventos de dominio
3. Agregar validaciones de recursos (salas, equipos)
4. Sistema de notificaciones

### Fase 3 (Escala):
5. Migrar a distributed locks (Redis Redlock)
6. Implementar CQRS para reportes complejos
7. Event sourcing para trazabilidad total
8. Sharding por sede/región geográfica

---

## 🔍 Consecuencias

### Positivas ✅

1. **Concurrencia resuelta:** Pessimistic locking previene double-booking
2. **Arquitectura clara:** Availability como agregado, TimeSlot como derivado
3. **Separación semántica:** Shift (presencia) vs Availability (atención)
4. **Eliminación de redundancia:** 17 catálogos duplicados consolidados
5. **Profesionalismo demostrable:** Manejo de concurrencia crítico para nivel senior
6. **Escalabilidad:** Domain Services permiten agregar validaciones sin tocar agregados

### Negativas / Riesgos ⚠️

1. **Latencia de locks:** Pessimistic locking añade ~50-200ms por operación
2. **Deuda técnica documentada:** 2 reglas críticas pospuestas (RN-APPT-005, RN-SHIFT-006)
3**Testing complejo:** Pruebas de concurrencia requieren setup multi-thread

### Mitigaciones 🛡️

1. **Performance:**
  - Locks de corta duración (queries indexadas, transacciones rápidas)
  - Índices en columnas críticas: `(dentist_id, start_time)`, `(patient_id, start_time)`
  - Connection pool configurado: `hikari.maximum-pool-size=20`

2. **Escalabilidad futura:**
  - Arquitectura preparada para migrar a distributed locks (Redis Redlock)
  - Flag `withLock` en puerto permite cambiar estrategia sin modificar dominio
  - Posible implementación de CQRS manteniendo modelo de escritura actual

3. **Testing:**
  - Domain tests puros sin frameworks (JUnit + Mockito básico)
  - Integration tests con Testcontainers para concurrencia real
  - Guía interna: `docs/testing/concurrency-testing-guide.md` (a crear)

4. **Documentación:**
  - Cada agregado tiene diagrama de estados
  - Flujos de operaciones documentados en ADRs específicos
  - README técnico explica separación de capas

---

## 📚 Referencias

### Archivos de Descubrimiento Original
- [Appointment(Cita).md](../../dominio/descubrimientos-de-reglas/schedule/Appointment(Cita).md)
- [Availability(Disponibilidad).md](../../dominio/descubrimientos-de-reglas/schedule/Availability(Disponibilidad).md)
- [Shift(Turno-Operativo).md](../../dominio/descubrimientos-de-reglas/schedule/Shift(Turno-Operativo).md)
- [TimeSlot(Bloque-Horario).md](../../dominio/descubrimientos-de-reglas/schedule/TimeSlot(Bloque-Horario).md)

### ADRs Relacionados del Módulo Schedule
- ADR-(Schedule)-01: Motivos creación clase Schedule
- ADR-(Schedule)-02: Sistema de transiciones de estado
- ADR-(Schedule)-03: Diferenciación semántica TimeSlot y WorkingHours
- ADR-(Schedule)-04: Validaciones de Reagendamiento de Citas
- ADR-(Schedule)-05: Diferenciación semántica WorkingHours y WeeklyAvailability
- ADR-(Schedule)-06: Revisión de uso de queries
- ADR-(Schedule)-07: Alcance experimental (este documento)

### ADRs de Arquitectura Global
- ADR-(Arquitectura)-20: Alcance Experimental del Módulo Actor (referencia)
- ADR-(Arquitectura)-22: Estrategia de Numeración de Catálogos de Error
- ADR-(Arquitectura)-23: Catálogos Eliminados - Histórico del Módulo Schedule

### Implementación
- Package: `com.example.ClinicaDefinitiva.domain.schedule`
- Agregados: `Appointment`, `Availability`, `Shift`, 'Schedule'
- Derivados: `TimeSlot` (no persistido)
- Catálogos: `AppointmentError`, `AvailabilityError`, `ShiftError`
- Domain Services: `AppointmentDomainService`

---

**Nota final:** Este ADR documenta la **evolución completa** del Módulo Schedule: desde el descubrimiento inicial ingenuo hasta la implementación madura con manejo de concurrencia, separación correcta de agregados, eliminación de redundancias y aplicación de principios DDD profesionales. **La implementación de pessimistic locking demuestra competencia en sistemas concurrentes críticos, habilidad esencial para posiciones senior.**

---
