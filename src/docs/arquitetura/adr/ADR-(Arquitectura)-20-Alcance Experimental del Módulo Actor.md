# ADR-20 (Arquitectura) : Alcance Experimental del Módulo Actor

**Estado:** ✅ Aceptado  
**Fecha:** Diciembre 24, 2025  
**Contexto:** Definición del alcance de implementación para exhibición profesional  
**Decisores:** Equipo de Desarrollo  

---

## Contexto y Problema

Durante el proceso de implementación del módulo `Actor` (que incluye los agregados `Dentist`, `Patient`, `Guardian` y `Receptionist`), se realizó un **descubrimiento exhaustivo de reglas de negocio** documentado en archivos 
- [Dentist(odontologo).md](../../dominio/descubrimientos-de-reglas/actores/Dentist%28odontologo%29.md)
- [Guardian(Reponsable).md](../../dominio/descubrimientos-de-reglas/actores/Guardian%28Reponsable%29.md)
- [Patient(Paciente).md](../../dominio/descubrimientos-de-reglas/actores/Patient%28Paciente%29.md)
- [Receptionist(Secretario).md](../../dominio/descubrimientos-de-reglas/actores/Receptionist%28Secretario%29.md)
 
específicos por agregado.

Sin embargo, al enfrentar la implementación real del dominio, surgieron las siguientes realidades:

### 1. **Sobre-especificación inicial**
Los archivos de descubrimiento fueron escritos con **poca experiencia práctica** en DDD, resultando en reglas redundantes, mal ubicadas o técnicamente incorrectas.

### 2. **Madurez arquitectónica evolutiva**
Durante la implementación, se adquirió comprensión sobre:
- Separación de responsabilidades entre agregados y Value Objects
- Principio de responsabilidad única en catálogos de error
- Delegación correcta de validaciones
- Límites de agregados y coordinación entre ellos

### 3. **Catálogos post-implementación**
Una vez consolidados los agregados, se identificaron **validaciones adicionales no cubiertas** en el descubrimiento inicial que requerían catálogos de error específicos.

### 4. **Redundancia de catálogos por operación**
Se introdujeron inicialmente catálogos específicos por operación (ej: `ERR_PATIENT_CREATION_REQUIRES_ACTIVE_USER`, `ERR_PATIENT_INACTIVE`) intentando **personalizar mensajes de error por contexto operacional**, pero esto resultó en:
- **Duplicación semántica**: múltiples catálogos validando el mismo invariante (estado activo de User)
- **Violación de responsabilidad única**: validaciones de estado de User dispersas en cada agregado
- **Mantenimiento complejo**: cambios en lógica de estado requieren modificar múltiples catálogos

**Ejemplo del anti-patrón:**
```java
// ❌ Antes: Catálogos específicos por operación
ERR_PATIENT_CREATION_REQUIRES_ACTIVE_USER  → "No puede crear porque usuario inactivo"
ERR_PATIENT_INACTIVE                       → "No puede editar porque usuario inactivo"
ERR_DENTIST_CREATION_REQUIRES_ACTIVE_USER  → "No puede crear porque usuario inactivo"
ERR_GUARDIAN_INACTIVE                      → "No puede autorizar porque usuario inactivo"

// ✅ Después: Delegación centralizada
UserAccessError.ERR_USER_INACTIVE → "No se puede realizar la operación porque el usuario está inactivo"
```

### 5. **Restricciones de proyecto experimental**
Este es un **proyecto de exhibición profesional**, no un sistema productivo completo, por lo que debe equilibrar:
- Profundidad técnica suficiente para demostrar capacidades
- Alcance manejable para completar en tiempo razonable
- Calidad profesional en lo implementado vs. cobertura exhaustiva

### 6. **Necesidad de documentar decisiones**
Es crítico **justificar y registrar** qué reglas se aplicaron, cuáles se eliminaron y cuáles se pospusieron, estableciendo un **"antes y después"** claro que demuestre evolución técnica.

---

## Decisión

Se establece el **alcance experimental del Módulo Actor** mediante la clasificación de reglas de negocio y catálogos de error en tres categorías:

### 🟢 **APLICADAS** - Implementadas en v1.0 (Exhibición)
Reglas críticas que demuestran comprensión profunda del dominio y arquitectura sólida.

### 🟡 **POSPUESTAS** - Documentadas para v2.0 (Iteración Futura)
Reglas importantes pero que requieren infraestructura adicional o coordinación entre módulos aún no desarrollados.

### 🔴 **ELIMINADAS** - Descartadas con Justificación
Reglas redundantes, mal ubicadas o arquitectónicamente incorrectas según los principios de DDD.

---

## Análisis Detallado por Agregado

---

## 🦷 Agregado: **Dentist** (Odontólogo)

### 🟢 Catálogos APLICADOS (Descubrimiento + Post-Implementación)

| Código | Descripción | Origen | Justificación |
|--------|-------------|--------|---------------|
| **RN-DENTIST-001** | Edad mínima 25 años | Descubrimiento | Requisito legal/profesional crítico |
| **RN-DENTIST-002** | Disponibilidad inicial obligatoria (40h) | Descubrimiento | Garantiza operatividad desde el primer día |
| **RN-DENTIST-003** | No desactivar con citas <24h | Descubrimiento | Protege continuidad del servicio |
| **RN-DENTIST-004** | No citas duplicadas en mismo horario | Descubrimiento | Previene conflictos operacionales |
| **RN-DENTIST-005** | Solo agendar si activo y disponible | Descubrimiento | Regla operacional crítica |
| **RN-DENTIST-007** | Especialidad válida obligatoria | Descubrimiento | Catálogo cerrado de especialidades |
| **RN-DENTIST-010** | Disponibilidad nunca vacía | Descubrimiento | Invariante de agregado |
| **RN-DENTIST-011** | Horario fuera de jornada laboral | **Post-Impl** | Validación de WorkingHours vs solicitud |
| **RN-DENTIST-012** | Rango de vacaciones inválido | **Post-Impl** | Validación temporal de períodos |
| **RN-DENTIST-013** | Reagendación fuera de horario | **Post-Impl** | Coherencia con jornada declarada |
| **RN-DENTIST-014** | Conflicto de vacaciones con citas | **Post-Impl** | Integridad referencial temporal |

### 🔴 Catálogos ELIMINADOS

| Código | Descripción Original | Motivo de Eliminación |
|--------|---------------------|----------------------|
| **RN-DENTIST-006** | Solo puede editarse si está activo | ❌ **DELEGACIÓN A USER**<br>*Razón:* La validación de estado activo es responsabilidad de `UserIdentity`, no del agregado Dentist.<br>*Catálogo original:* `ERR_DENTIST_NOT_EDITABLE`<br>*Reemplazo:* `UserAccessError.ERR_USER_INACTIVE` |
| **RN-DENTIST-008** | No puede crearse con estado INACTIVO | ❌ **REDUNDANCIA**<br>*Razón:* Idéntica a RN-DENTIST-006. User valida estado en constructor.<br>*Catálogo original:* `ERR_DENTIST_INVALID_INITIAL_STATUS`<br>*Reemplazo:* Consolidada en User |
| **RN-DENTIST-009** | Debe tener nombre y documento válidos | ❌ **VALIDACIÓN DE VO**<br>*Razón:* Agrupa validaciones que ocurren en `FullName`, `DocumentId`.<br>*Catálogo original:* `ERR_DENTIST_MISSING_REQUIRED_FIELDS`<br>*Reemplazo:* `ValueObjectError.ERR_FULLNAME_BLANK`, `ERR_DOCUMENT_INVALID_FORMAT` |

---

## 👨‍👩‍👧 Agregado: **Guardian** (Responsable Legal)

### 🟢 Catálogos APLICADOS

| Código | Descripción | Origen | Justificación |
|--------|-------------|--------|---------------|
| **RN-GUARDIAN-003** | No revocar consentimiento si tratamiento iniciado | Descubrimiento → **v1.0** | ⭐ CRÍTICA: Implicación legal fundamental |
| **RN-GUARDIAN-004** | Tipo de relación obligatorio | Descubrimiento | Define vínculo legal |
| **RN-GUARDIAN-010** | Desactivación requiere motivo | Descubrimiento | Auditoría obligatoria |
| **RN-GUARDIAN-011** | Edad válida (22-60 años) | **Post-Impl** | Reemplaza RN-GUARDIAN-008 con rango específico |

### 🟡 Catálogos POSPUESTOS

| Código | Descripción | Motivo | Prioridad |
|--------|-------------|--------|-----------|
| **RN-GUARDIAN-005** | No desactivar con autorizaciones vigentes | Requiere Domain Service coordinando con Treatment | 🟡 MEDIA |
| **RN-GUARDIAN-009** | No modificar vínculo con tratamientos previos | Requiere historial de autorizaciones | 🟡 MEDIA |

### 🔴 Catálogos ELIMINADOS

| Código | Descripción Original | Motivo de Eliminación |
|--------|---------------------|----------------------|
| **RN-GUARDIAN-001** | No puede crearse sin vínculo legal con un paciente | ❌ **PROBLEMA ARQUITECTURAL**<br>*Razón:* Crea huevo-gallina: menor necesita responsable antes de existir como Patient.<br>*Catálogo original:* `ERR_GUARDIAN_MISSING_PATIENT`<br>*Solución:* Guardian es agregado independiente, vinculación posterior |
| **RN-GUARDIAN-002** | No puede autorizar tratamientos si está inactivo | ❌ **DELEGACIÓN A USER**<br>*Catálogo original:* `ERR_GUARDIAN_INACTIVE`<br>*Reemplazo:* `UserAccessError.ERR_USER_INACTIVE` |
| **RN-GUARDIAN-006** | Solo puede editarse si está activo | ❌ **DELEGACIÓN A USER**<br>*Catálogo original:* `ERR_GUARDIAN_NOT_EDITABLE`<br>*Reemplazo:* `UserAccessError.ERR_USER_INACTIVE` |
| **RN-GUARDIAN-007** | Debe tener al menos un medio de contacto válido | ❌ **VALIDACIÓN DE VO**<br>*Catálogo original:* `ERR_GUARDIAN_MISSING_CONTACT`<br>*Reemplazo:* `ValueObjectError.ERR_PHONE_INVALID_FORMAT`, `ERR_EMAIL_INVALID_FORMAT` |
| **RN-GUARDIAN-008** | Debe ser mayor de edad (≥ 18 años) | ❌ **REEMPLAZADO**<br>*Razón:* Regla genérica reemplazada por RN-GUARDIAN-011 con rango específico (22-60).<br>*Catálogo original:* `ERR_GUARDIAN_UNDERAGE`<br>*Nuevo:* `ERR_GUARDIAN_INVALID_AGE` |

---

## 👤 Agregado: **Patient** (Paciente)

### 🟢 Catálogos APLICADOS

| Código | Descripción | Origen | Justificación |
|--------|-------------|--------|---------------|
| **RN-PATIENT-002** | No desactivar con citas/tratamientos activos | Descubrimiento | Protege continuidad clínica |
| **RN-PATIENT-003** | No citas duplicadas en mismo horario | Descubrimiento | Previene conflictos |
| **RN-PATIENT-006** | Edad en rango válido (0-120 años) | Descubrimiento | Integridad de datos |
| **RN-PATIENT-008** | Menor requiere responsable vinculado | Descubrimiento | Cumplimiento legal |
| **RN-PATIENT-009** | No modificar fecha nacimiento con citas | Descubrimiento → **v1.0** | ⭐ CRÍTICA: Consistencia histórica |
| **RN-PATIENT-010** | Desactivación requiere motivo | Descubrimiento | Auditoría obligatoria |
| **RN-PATIENT-013** | Paciente sin turno asignado | **Post-Impl** | Validación de Shift obligatorio |
| **RN-PATIENT-014** | Turno no disponible en rango | **Post-Impl** | Coherencia temporal de Shift |

### 🔴 Catálogos ELIMINADOS

| Código | Descripción Original | Motivo de Eliminación |
|--------|---------------------|----------------------|
| **RN-PATIENT-001** | Debe tener nombre, documento y fecha válida | ❌ **VALIDACIÓN DE VO**<br>*Catálogo original:* `ERR_PATIENT_MISSING_REQUIRED_FIELDS`<br>*Reemplazo:* Catálogos específicos de ValueObject |
| **RN-PATIENT-004** | Solo puede editarse si está activo | ❌ **DELEGACIÓN A USER**<br>*Catálogo original:* `ERR_PATIENT_NOT_EDITABLE`<br>*Reemplazo:* `UserAccessError.ERR_USER_INACTIVE` |
| **RN-PATIENT-005** | Debe registrar contacto válido | ❌ **VALIDACIÓN DE VO**<br>*Catálogo original:* `ERR_PATIENT_MISSING_CONTACT` |
| **RN-PATIENT-007** | Fecha nacimiento no futura | ❌ **VALIDACIÓN DE VO**<br>*Catálogo original:* `ERR_PATIENT_FUTURE_BIRTHDATE`<br>*Reemplazo:* `ValueObjectError.ERR_BIRTHDATE_FUTURE` |
| **RN-PATIENT-011** | Creación requiere usuario activo | ❌ **REDUNDANCIA POR OPERACIÓN**<br>*Razón:* Intento de personalizar error por operación "crear" cuando ya existe validación genérica de estado.<br>*Catálogo original:* `ERR_PATIENT_CREATION_REQUIRES_ACTIVE_USER`<br>*Reemplazo:* `UserAccessError.ERR_USER_INACTIVE` |
| **RN-PATIENT-012** | Paciente debe estar activo para operación | ❌ **REDUNDANCIA POR OPERACIÓN**<br>*Razón:* Duplica validación de estado de User. Intento fallido de personalizar mensaje por contexto.<br>*Catálogo original:* `ERR_PATIENT_INACTIVE`<br>*Reemplazo:* `UserAccessError.ERR_USER_INACTIVE` |

---

## 📋 Agregado: **Receptionist** (Recepcionista)

### 🟢 Catálogos APLICADOS

| Código | Descripción | Origen | Justificación |
|--------|-------------|--------|---------------|
| **RN-RECEPTIONIST-002** | No agendar citas duplicadas | Descubrimiento | Previene errores administrativos |
| **RN-RECEPTIONIST-003** | No cancelar citas <24h sin autorización | Descubrimiento | Política comercial |
| **RN-RECEPTIONIST-010** | Desactivación requiere motivo | **Post-Impl** | Auditoría administrativa |

### 🟡 Catálogos POSPUESTOS

| Código | Descripción | Motivo | Prioridad |
|--------|-------------|--------|-----------|
| **RN-RECEPTIONIST-004** | Asociación a sede válida | Proyecto experimental sin sedes múltiples | 🟢 BAJA |
| **RN-RECEPTIONIST-006** | No desactivar con tareas pendientes | Requiere módulo de gestión de tareas | 🟡 MEDIA |
| **RN-RECEPTIONIST-007** | No modificar sede con citas asignadas | Depende de RN-004 | 🟢 BAJA |

### 🔴 Catálogos ELIMINADOS

| Código | Descripción Original | Motivo de Eliminación |
|--------|---------------------|----------------------|
| **RN-RECEPTIONIST-001** | No confirmar citas para odontólogos inactivos | ❌ **REDUNDANCIA POR OPERACIÓN**<br>*Razón:* La validación de estado del Dentist debe hacerse en el agregado Dentist, no en Receptionist.<br>*Catálogo original:* `ERR_RECEPTIONIST_DENTIST_INACTIVE`<br>*Reemplazo:* Validación delegada: `Dentist.ensureEditable()` lanza `DentistError.ERR_DENTIST_NOT_AVAILABLE` |
| **RN-RECEPTIONIST-005** | Solo puede editarse si está activo | ❌ **DELEGACIÓN A USER**<br>*Catálogo original:* `ERR_RECEPTIONIST_NOT_EDITABLE`<br>*Reemplazo:* `UserAccessError.ERR_USER_INACTIVE` |
| **RN-RECEPTIONIST-009** | Debe tener contacto válido | ❌ **VALIDACIÓN DE VO**<br>*Catálogo original:* `ERR_RECEPTIONIST_MISSING_CONTACT` |

---

## 📦 Value Objects del Módulo Actor

### Catálogos Aplicados (ValueObjectError)

Los siguientes catálogos fueron **extraídos de los agregados** y **centralizados en ValueObjectError** siguiendo el principio de responsabilidad única:

#### **Persona (Person)**

| Código | Value Object | Descripción |
|--------|--------------|-------------|
| `ERR_FULLNAME_NULL` | FullName | Nombre completo nulo |
| `ERR_FULLNAME_BLANK` | FullName | Nombre/apellido vacíos |
| `ERR_BIRTHDATE_NULL` | DateOfBirth | Fecha nacimiento nula |
| `ERR_BIRTHDATE_FUTURE` | DateOfBirth | Fecha nacimiento futura |
| `ERR_BIRTHDATE_INVALID_RANGE` | DateOfBirth | Edad > 130 años |
| `ERR_AGE_OUT_OF_RANGE` | Age | Edad fuera de rango 0-130 |
| `ERR_PHONE_NULL` | PhoneNumber | Teléfono nulo |
| `ERR_PHONE_BLANK` | PhoneNumber | Teléfono vacío |
| `ERR_PHONE_INVALID_FORMAT` | PhoneNumber | Formato telefónico inválido |
| `ERR_ADDRESS_NULL` | Address | Campos dirección nulos |
| `ERR_ADDRESS_BLANK` | Address | Campos dirección vacíos |
| `ERR_DOCUMENT_NULL` | DocumentId | Documento nulo |
| `ERR_DOCUMENT_BLANK` | DocumentId | Documento vacío |
| `ERR_DOCUMENT_INVALID_FORMAT` | DocumentId | Formato documento inválido |
| `ERR_BLOODTYPE_INVALID` | BloodType | Tipo sangre inválido |

#### **Actor Específicos**

| Código | Value Object | Descripción |
|--------|--------------|-------------|
| `ERR_ID_NULL` | DentistId, PatientId, GuardianId, ReceptionId | ID nulo |
| `ERR_ID_BLANK` | (todos los IDs) | ID vacío |
| `ERR_ID_INVALID_FORMAT` | (todos los IDs) | Formato UUID inválido |
| `ERR_SECTOR_NULL` | Sector | Sector nulo |
| `ERR_SECTOR_BLANK` | Sector | Sector vacío |
| `ERR_SECTOR_NOT_ALLOWED` | Sector | Sector no permitido |
| `ERR_TYPE_GUARDIAN_CODE_NULL` | TypeGuardian | Código tipo nulo |
| `ERR_TYPE_GUARDIAN_CODE_BLANK` | TypeGuardian | Código vacío |
| `ERR_TYPE_GUARDIAN_CODE_INVALID` | TypeGuardian | Código no válido |
| `ERR_TYPE_GUARDIAN_DESCRIPTION_BLANK` | TypeGuardian | Descripción vacía |
| `ERR_WORKING_HOURS_NULL` | WorkingHours | Componentes horario nulos |
| `ERR_WORKING_HOURS_INVALID_RANGE` | WorkingHours | Hora inicio > fin |
| `ERR_WORKING_HOURS_INVALID_DECLARED` | WorkingHours | Horas declaradas ≤ 0 |
| `ERR_WORKING_HOURS_EXCEEDS_LEGAL_LIMIT` | WorkingHours | Horas > 48 semanales |
| `ERR_AVAILABILITY_STATUS_NULL` | DentistAvailabilityStatus | Estado nulo |
| `ERR_AVAILABILITY_STATUS_TRANSITION_NULL` | DentistAvailabilityStatus | Transición nula |
| `ERR_AVAILABILITY_STATUS_INVALID_TRANSITION` | DentistAvailabilityStatus | Transición inválida |

---

## 📊 Estadísticas Finales

### Resumen por Categoría

| Agregado | Aplicadas (Desc + Post) | Pospuestas | Eliminadas | Total |
|----------|------------------------|------------|------------|-------|
| Dentist | 11 (7+4) | 0 | 3 | 14 |
| Guardian | 4 (3+1) | 2 | 5 | 11 |
| Patient | 8 (6+2) | 0 | 6 | 14 |
| Receptionist | 3 (2+1) | 3 | 3 | 9 |
| **ValueObjects** | **30** | **0** | **0** | **30** |
| **TOTAL** | **56** | **5** | **17** | **78** |

### Distribución de Catálogos

```
Aplicadas:    72% ████████████████████░░░░░░
Pospuestas:    6% ████░░░░░░░░░░░░░░░░░░░░░░
Eliminadas:   22% ███████████░░░░░░░░░░░░░░░
```

---

## 🎯 Principios Arquitectónicos Consolidados

### 1. **Separación de Validaciones por Capa**

```
┌─────────────────────────────────────────┐
│ Value Object Layer                      │
│ ✓ Validaciones de formato              │
│ ✓ Invariantes de valor único           │
│ ✓ Catálogo: ValueObjectError (30)      │
└─────────────────────────────────────────┘
              ↓
┌─────────────────────────────────────────┐
│ Aggregate Layer                         │
│ ✓ Reglas de negocio del agregado       │
│ ✓ Invariantes del agregado             │
│ ✓ Catálogos: DentistError (11),        │
│   PatientError (8), etc.                │
└─────────────────────────────────────────┘
              ↓
┌─────────────────────────────────────────┐
│ Cross-Aggregate Layer                   │
│ ✓ Domain Services                      │
│ ✓ Coordinación entre agregados         │
│ ✓ Catálogo: UserAccessError (5)        │
└─────────────────────────────────────────┘
```

### 2. **Delegación de Responsabilidades Transversales**

| Responsabilidad | Propietario | Anti-patrón Detectado | Solución |
|----------------|-------------|----------------------|----------|
| Estado activo/inactivo | `UserIdentity` | ❌ Catálogo por agregado y operación | ✅ `UserAccessError.ERR_USER_INACTIVE` |
| Validación de formato | Value Objects | ❌ Validación en agregados | ✅ `ValueObjectError.*` |
| Auditoría de cambios | `AuditContext` | ❌ Logs manuales dispersos | ✅ `AuditError.*` |

### 3. **Eliminación de Redundancia por Operación**

**Antes (Anti-patrón):**
```java
// ❌ Catálogo específico por cada operación CRUD
ERR_PATIENT_CREATION_REQUIRES_ACTIVE_USER
ERR_PATIENT_UPDATE_REQUIRES_ACTIVE_USER
ERR_PATIENT_DELETE_REQUIRES_ACTIVE_USER
ERR_DENTIST_CREATION_REQUIRES_ACTIVE_USER
... (12+ catálogos para misma validación)
```

**Después (Correcto):**
```java
// ✅ Único catálogo delegado a responsable correcto
UserAccessError.ERR_USER_INACTIVE
  → "No se puede realizar la operación porque el usuario está inactivo"

// El contexto operacional se maneja en el mensaje de excepción:
throw new BusinessRuleViolationException(
    UserAccessError.ERR_USER_INACTIVE,
    ContextoEntidad.PATIENT,
    "No se puede crear el paciente" // ← Contexto adicional
);
```

---

## 🚀 Reglas Críticas Implementadas en v1.0

Las siguientes reglas fueron **priorizadas post-ADR inicial** para v1.0 por su importancia técnica y legal:

### 1. **RN-GUARDIAN-003: No revocar consentimiento si tratamiento iniciado** ⭐⭐⭐⭐⭐

**Implementación:**
- Nuevo VO: `TreatmentAuthorization` con estados
- Método: `Guardian.revokeConsentForService()`
- Validación: `TreatmentAuthorization.hasStarted()`

**Justificación:** Regla legal y ética fundamental en sistemas médicos.

### 2. **RN-PATIENT-009: No modificar fecha nacimiento con historial** ⭐⭐⭐⭐

**Implementación:**
- Consulta: `Schedule.getAppointments().isEmpty()`
- Método modificado: `Patient.updateSensitiveData()`

**Justificación:** Protege consistencia temporal e integridad histórica.

---

## 📝 Consecuencias

### Positivas ✅

1. **Eliminación de redundancia:** De 12+ catálogos para estado activo a 1 centralizado
2. **Responsabilidad única:** Value Objects manejan sus propias validaciones (30 catálogos)
3. **Trazabilidad mejorada:** Numeración preservada documenta evolución histórica
4. **Arquitectura limpia:** Separación clara por capas (VO → Aggregate → Cross-Aggregate)
5. **Profesionalismo:** Decisiones documentadas con justificación técnica sólida

### Negativas / Riesgos ⚠️

1. **Deuda técnica documentada:** 5 reglas pospuestas requieren v2.0
2. **Coordinación pendiente:** Reglas críticas implementadas requieren Domain Services
3. **Catálogos históricos:** 17 eliminados deben mantenerse documentados

### Mitigaciones 🛡️

1. **ADR-037:** Estrategia de numeración para preservar trazabilidad
2. **ADR-038:** Histórico de catálogos eliminados con justificaciones
3. **Plantilla de documentación:** Cada catálogo aplicado tiene ficha técnica completa

---

## 🔗 Referencias

### Archivos de Descubrimiento Original
- [Dentist(odontologo).md](../../dominio/descubrimientos-de-reglas/actores/Dentist%28odontologo%29.md)
- [Guardian(Reponsable).md](../../dominio/descubrimientos-de-reglas/actores/Guardian%28Reponsable%29.md)
- [Patient(Paciente).md](../../dominio/descubrimientos-de-reglas/actores/Patient%28Paciente%29.md)
- [Receptionist(Secretario).md](../../dominio/descubrimientos-de-reglas/actores/Receptionist%28Secretario%29.md)


### ADRs Relacionados
- [ADR-(Arquitectura)-22-Estrategia de Numeración de Catálogos de Error.md](ADR-%28Arquitectura%29-22-Estrategia%20de%20Numeraci%C3%B3n%20de%20Cat%C3%A1logos%20de%20Error.md)
- [ADR-(Arquitectura)-23-Catálogos Eliminados - Histórico del Módulo Actor.md](ADR-%28Arquitectura%29-23-Cat%C3%A1logos%20Eliminados%20-%20Hist%C3%B3rico%20del%20M%C3%B3dulo%20Actor.md)
- [ADR-(Dominio)-01-Implementación de Value-Objects.md](../../dominio/decisions/ADR-%28Dominio%29-01-Implementaci%C3%B3n%20de%20Value-Objects.md)
- [ADR-(Dominio)-02-Implementación de reglas de negocio.md](../../dominio/decisions/ADR-%28Dominio%29-02-Implementaci%C3%B3n%20de%20reglas%20de%20negocio.md)
- [ADR-(Dominio)-03-Uso meticuloso de excepciones.md](../../dominio/decisions/ADR-%28Dominio%29-03-Uso%20meticuloso%20de%20excepciones.md)
- [ADR-(Dominio)-04-Inquietud sobre el rol de los Servicios.md](../../dominio/decisions/ADR-%28Dominio%29-04-Inquietud%20sobre%20el%20rol%20de%20los%20Servicios.md)
- [ADR-(Dominio)-05-Identificador único como-Vo.md](../../dominio/decisions/ADR-%28Dominio%29-05-Identificador%20%C3%BAnico%20como-Vo.md)
- [ADR-(actores)-01-Consolidación semántica de horarios clínicos.md](../../dominio/decisions/actor/ADR-%28actores%29-01-Consolidaci%C3%B3n%20sem%C3%A1ntica%20de%20horarios%20cl%C3%ADnicos.md)
- [ADR-(Actores)-02-Delegación de lógica Dentist DomainService.md](../../dominio/decisions/actor/ADR-%28Actores%29-02-Delegaci%C3%B3n%20de%20l%C3%B3gica%20Dentist%20DomainService.md)
- [ADR-(Actores)-03-Mantener la mutación local en Dentist.md](../../dominio/decisions/actor/ADR-%28Actores%29-03-Mantener%20la%20mutaci%C3%B3n%20local%20en%20Dentist.md)
- [ADR-(Actores)-04-Separación de edición de datos de paciente y gobernanza.md](../../dominio/decisions/actor/ADR-%28Actores%29-04-Separaci%C3%B3n%20de%20edici%C3%B3n%20de%20datos%20de%20paciente%20y%20gobernanza.md)
- [ADR-(Actores)-05-Representación TypeGuardian Vo híbrido.md](../../dominio/decisions/actor/ADR-%28Actores%29-05-Representaci%C3%B3n%20TypeGuardian%20Vo%20h%C3%ADbrido.md)
- [ADR-(Actores)-06-Validar responsable en paciente.md](../../dominio/decisions/actor/ADR-%28Actores%29-06-Validar%20responsable%20en%20paciente.md)
- [ADR-(actores)-07-Ubicación del patrón Builder en la entidad Dentist.md](../../dominio/decisions/actor/ADR-%28actores%29-07-Ubicaci%C3%B3n%20del%20patr%C3%B3n%20Builder%20en%20la%20entidad%20Dentist.md)
- [ADR-(Actores)-08-Delegación semántica para validar agendamiento.md](../../dominio/decisions/actor/ADR-%28Actores%29-08-Delegaci%C3%B3n%20sem%C3%A1ntica%20para%20validar%20agendamiento.md)
- [ADR-(Actores)-09-Refactorización semántica canScheduleAt(...) .md](../../dominio/decisions/actor/ADR-%28Actores%29-09-Refactorizaci%C3%B3n%20sem%C3%A1ntica%20canScheduleAt%28...%29%20.md)
- [ADR-(Actores)-10-Modelado de Persona.md](../../dominio/decisions/actor/ADR-%28Actores%29-10-Modelado%20de%20Persona.md)
- [ADR-(Actores)-11-Separación de estado entre User y Dentist.md](../../dominio/decisions/actor/ADR-%28Actores%29-11-Separaci%C3%B3n%20de%20estado%20entre%20User%20y%20Dentist.md)
- [ADR-(Actores)-12-Separación de AvailabilityStatus entre Dentist y Availability.md](../../dominio/decisions/actor/ADR-%28Actores%29-12-Separaci%C3%B3n%20de%20AvailabilityStatus%20entre%20Dentist%20y%20Availability.md)
- [ADR-(Actores)-13-Eliminación del patrón Builder en Receptionist.md](../../dominio/decisions/actor/ADR-%28Actores%29-13-Eliminaci%C3%B3n%20del%20patr%C3%B3n%20Builder%20en%20Receptionist.md)
- [ADR-(Actores)-14-Eliminación de Domain Services en agregados del módulo Actor.md](../../dominio/decisions/actor/ADR-%28Actores%29-14-Eliminaci%C3%B3n%20de%20Domain%20Services%20en%20agregados%20del%20m%C3%B3dulo%20Actor.md)
- [ADR-(Atores)-15-Extrategia de desactivación de Usuarios y Actores.md](../../dominio/decisions/actor/ADR-%28Atores%29-15-Extrategia%20de%20desactivaci%C3%B3n%20de%20Usuarios%20y%20Actores.md)

### Implementación
- Package: `com.example.ClinicaDefinitiva.domain.actor`
- Catálogos: `DentistError`, `PatientError`, `GuardianError`, `ReceptionistError`
- Catálogos transversales: `UserAccessError`, `ValueObjectError`, `AuditError`

---



**Nota final:** Este ADR documenta la **evolución completa** del Módulo Actor: desde el descubrimiento inicial ingenuo hasta la implementación madura con separación correcta de responsabilidades, eliminación de redundancias y aplicación de principios DDD profesionales.