# ADR-23 (Arquitectura): Catálogos Eliminados - Histórico del Módulo Actor

**Estado:** 📚 Registro Histórico  
**Fecha:** Diciembre 24, 2024  
**Propósito:** Documentar catálogos de error eliminados con justificación técnica  
---

## Propósito de este Documento

Este ADR mantiene el **registro histórico oficial** de todos los catálogos de error que fueron eliminados del Módulo Actor, incluyendo:
- Código y descripción original
- Fecha de eliminación
- Motivo técnico detallado
- Catálogo de reemplazo (si aplica)
- Referencias a decisiones arquitectónicas

**Nota:** Según ADR-037, los códigos eliminados **NUNCA se reutilizan**. Este documento sirve como referencia para:
- Auditorías de cumplimiento
- Debugging de logs históricos
- Migración de sistemas legacy
- Trazabilidad de evolución arquitectónica

---

## 🦷 Dentist (Odontólogo) - Eliminados

### ERR_DENTIST_NOT_EDITABLE
- **Código:** RN-DENTIST-006
- **Descripción original:** "Solo puede editarse si está activo"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** DELEGACIÓN A USER
- **Justificación técnica:**  
  La validación de estado activo/inactivo es responsabilidad del agregado `UserIdentity`, no de Dentist. Tener un catálogo específico en cada agregado para esta validación viola el principio de responsabilidad única y genera redundancia.
- **Reemplazo:** `UserAccessError.ERR_USER_INACTIVE`
- **Ejemplo uso (antes):**
  ```java
  if (dentist.status != ACTIVE) {
      throw new BusinessRuleViolationException(ERR_DENTIST_NOT_EDITABLE);
  }
  ```
- **Ejemplo uso (después):**
  ```java
  UserStatus.from(user).mustBeActive(ERR_USER_INACTIVE, ContextoEntidad.DENTIST);
  ```
- **Referencia:** ADR-036, Sección "Delegación de Responsabilidades Transversales"

---

### ERR_DENTIST_INVALID_INITIAL_STATUS
- **Código:** RN-DENTIST-008
- **Descripción original:** "No puede crearse con estado INACTIVO"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** REDUNDANCIA
- **Justificación técnica:**  
  Es idéntica a RN-DENTIST-006. El agregado `User` ya valida el estado en su constructor. No tiene sentido tener dos catálogos para la misma invariante.
- **Reemplazo:** Consolidada en `UserAccessError.ERR_USER_INACTIVE`
- **Referencia:** ADR-036

---

### ERR_DENTIST_MISSING_REQUIRED_FIELDS
- **Código:** RN-DENTIST-009
- **Descripción original:** "Debe tener nombre y documento válidos"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
- **Justificación técnica:**  
  Este catálogo agrupaba dos validaciones diferentes (`FullName` y `DocumentId`) que ocurren en sus respectivos Value Objects, no en el agregado. Un catálogo genérico para múltiples VOs viola responsabilidad única y dificulta debugging.
- **Reemplazo:**
    - `ValueObjectError.ERR_FULLNAME_BLANK`
    - `ValueObjectError.ERR_DOCUMENT_INVALID_FORMAT`
- **Referencia:** ADR-036, Principio "Separación de Validaciones por Capa"

---

## 👨‍👩‍👧 Guardian (Responsable) - Eliminados

### ERR_GUARDIAN_MISSING_PATIENT
- **Código:** RN-GUARDIAN-001
- **Descripción original:** "No puede crearse sin vínculo legal con un paciente"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** PROBLEMA ARQUITECTURAL (Huevo-Gallina)
- **Justificación técnica:**  
  Crea un problema de dependencia circular: un paciente menor necesita un responsable antes de ser creado, pero esta regla requiere que el paciente ya exista. La solución correcta es que Guardian sea un agregado independiente y la vinculación ocurra posteriormente mediante `patient.vincularResponsable(guardian)`.
- **Reemplazo:** Ninguno (regla arquitectónica modificada)
- **Nuevo flujo:**
  ```java
  // 1. Crear Guardian independiente
  Guardian guardian = Guardian.register(...);
  
  // 2. Crear Patient
  Patient patient = Patient.register(...);
  
  // 3. Vincular
  patient.vincularResponsable(guardian);
  ```
- **Referencia:** ADR-036, Sección "Guardian - Reglas Eliminadas"

---

### ERR_GUARDIAN_INACTIVE
- **Código:** RN-GUARDIAN-002
- **Descripción original:** "No puede autorizar tratamientos si está inactivo"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** DELEGACIÓN A USER
- **Reemplazo:** `UserAccessError.ERR_USER_INACTIVE`
- **Referencia:** ADR-036

---

### ERR_GUARDIAN_NOT_EDITABLE
- **Código:** RN-GUARDIAN-006
- **Descripción original:** "Solo puede editarse si está activo"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** DELEGACIÓN A USER (idéntico a RN-GUARDIAN-002)
- **Reemplazo:** `UserAccessError.ERR_USER_INACTIVE`
- **Referencia:** ADR-036

---

### ERR_GUARDIAN_MISSING_CONTACT
- **Código:** RN-GUARDIAN-007
- **Descripción original:** "Debe tener al menos un medio de contacto válido"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
- **Justificación técnica:**  
  La regla es correcta, pero la validación ocurre en los Value Objects `PhoneNumber` y `Email`, no en el agregado Guardian.
- **Reemplazo:**
    - `ValueObjectError.ERR_PHONE_INVALID_FORMAT`
    - `ValueObjectError.ERR_EMAIL_INVALID_FORMAT`
- **Referencia:** ADR-036

---

### ERR_GUARDIAN_UNDERAGE
- **Código:** RN-GUARDIAN-008 (original)
- **Descripción original:** "Debe ser mayor de edad (≥ 18 años)"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** REEMPLAZO POR REGLA MÁS ESPECÍFICA
- **Justificación técnica:**  
  La regla genérica (≥18) fue reemplazada por una regla más precisa del dominio clínico (22-60 años) que refleja mejor los requisitos reales de un responsable legal en contexto médico.
- **Reemplazo:** `GuardianError.ERR_GUARDIAN_INVALID_AGE` (RN-GUARDIAN-011)
- **Nota:** El código RN-GUARDIAN-008 fue **reutilizado** porque el catálogo original nunca llegó a producción.
- **Referencia:** ADR-036

---

## 👤 Patient (Paciente) - Eliminados

### ERR_PATIENT_MISSING_REQUIRED_FIELDS
- **Código:** RN-PATIENT-001
- **Descripción original:** "Debe tener nombre, documento y fecha de nacimiento válida"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
- **Justificación técnica:**  
  Agrupa tres validaciones diferentes (`FullName`, `DocumentId`, `DateOfBirth`) que ocurren en distintos Value Objects.
- **Reemplazo:**
    - `ValueObjectError.ERR_FULLNAME_BLANK`
    - `ValueObjectError.ERR_DOCUMENT_INVALID_FORMAT`
    - `ValueObjectError.ERR_BIRTHDATE_FUTURE`
- **Referencia:** ADR-036

---

### ERR_PATIENT_NOT_EDITABLE
- **Código:** RN-PATIENT-004
- **Descripción original:** "Solo puede editarse si está activo"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** DELEGACIÓN A USER
- **Reemplazo:** `UserAccessError.ERR_USER_INACTIVE`
- **Referencia:** ADR-036

---

### ERR_PATIENT_MISSING_CONTACT
- **Código:** RN-PATIENT-005
- **Descripción original:** "Debe registrar al menos un medio de contacto válido"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
- **Reemplazo:** Validaciones en `PhoneNumber`, `Email`
- **Referencia:** ADR-036

---

### ERR_PATIENT_FUTURE_BIRTHDATE
- **Código:** RN-PATIENT-007
- **Descripción original:** "Fecha de nacimiento no puede ser futura"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
- **Justificación técnica:**  
  Esta validación ocurre en el constructor de `DateOfBirth`, no en el agregado Patient.
- **Reemplazo:** `ValueObjectError.ERR_BIRTHDATE_FUTURE`
- **Referencia:** ADR-036

---

### ERR_PATIENT_CREATION_REQUIRES_ACTIVE_USER
- **Código:** RN-PATIENT-011
- **Descripción original:** "El paciente debe tener estado ACTIVO para poder ser creado"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** REDUNDANCIA POR OPERACIÓN
- **Justificación técnica:**  
  Este catálogo fue un intento de **personalizar mensajes de error por operación** ("crear" vs "editar" vs "eliminar"). Esto resultó en proliferación de catálogos que validaban la misma invariante (estado activo de User). La solución correcta es un único catálogo centralizado con contexto adicional en el mensaje de excepción.
- **Anti-patrón detectado:**
  ```java
  // ❌ Antes: 3+ catálogos para misma validación
  ERR_PATIENT_CREATION_REQUIRES_ACTIVE_USER  → "No puede crear..."
  ERR_PATIENT_INACTIVE                       → "No puede editar..."
  ERR_PATIENT_DELETE_REQUIRES_ACTIVE_USER    → "No puede eliminar..."
  ```
- **Solución:**
  ```java
  // ✅ Después: 1 catálogo + contexto
  throw new BusinessRuleViolationException(
      UserAccessError.ERR_USER_INACTIVE,
      ContextoEntidad.PATIENT,
      "No se puede crear el paciente" // ← Contexto específico
  );
  ```
- **Reemplazo:** `UserAccessError.ERR_USER_INACTIVE`
- **Referencia:** ADR-036, Sección "Eliminación de Redundancia por Operación"

---

### ERR_PATIENT_INACTIVE
- **Código:** RN-PATIENT-012
- **Descripción original:** "El paciente debe estar ACTIVO para realizar esta operación"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** REDUNDANCIA POR OPERACIÓN (idéntico a RN-PATIENT-011)
- **Reemplazo:** `UserAccessError.ERR_USER_INACTIVE`
- **Referencia:** ADR-036

---

## 📋 Receptionist (Recepcionista) - Eliminados

### ERR_RECEPTIONIST_DENTIST_INACTIVE
- **Código:** RN-RECEPTIONIST-001
- **Descripción original:** "El dentista asociado se encuentra inactivo"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** REDUNDANCIA POR OPERACIÓN
- **Justificación técnica:**  
  La validación de estado del Dentist debe hacerse en el agregado Dentist, no en Receptionist. Esta validación ya existe como `Dentist.ensureEditable()` que lanza `DentistError.ERR_DENTIST_NOT_AVAILABLE`.
- **Reemplazo:** Delegación a `Dentist.ensureEditable()`
- **Referencia:** ADR-036

---

### ERR_RECEPTIONIST_NOT_EDITABLE
- **Código:** RN-RECEPTIONIST-005
- **Descripción original:** "Solo puede editarse si está activo"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** DELEGACIÓN A USER
- **Reemplazo:** `UserAccessError.ERR_USER_INACTIVE`
- **Referencia:** ADR-036

---

### ERR_RECEPTIONIST_MISSING_CONTACT
- **Código:** RN-RECEPTIONIST-009 (catálogo eliminado, regla mantenida)
- **Descripción original:** "Debe tener al menos un medio de contacto válido"
- **Fecha eliminación:** 2024-12-24
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
- **Nota:** La **regla se mantiene**, solo se actualizó el catálogo para reflejar que la validación ocurre en VOs.
- **Reemplazo:** Validaciones en `PhoneNumber`, `Email`
- **Referencia:** ADR-036

---

## 📊 Estadísticas de Eliminación

| Agregado | Eliminados | Motivo Principal |
|----------|-----------|------------------|
| Dentist | 3 | Delegación a User (2), Validación VO (1) |
| Guardian | 5 | Delegación a User (3), Validación VO (1), Arquitectura (1) |
| Patient | 6 | Redundancia por operación (2), Delegación User (2), Validación VO (2) |
| Receptionist | 3 | Delegación User (2), Validación VO (1) |
| **TOTAL** | **17** | |

### Distribución por Motivo

```
DELEGACIÓN A USER:           41% ███████████░░░░░░░░░░░░░░░
VALIDACIÓN DE VO:            35% █████████░░░░░░░░░░░░░░░░░
REDUNDANCIA POR OPERACIÓN:   18% █████░░░░░░░░░░░░░░░░░░░░░
PROBLEMA ARQUITECTURAL:       6% ██░░░░░░░░░░░░░░░░░░░░░░░░
```

---

## Lecciones Aprendidas

### 1. **Evitar Catálogos por Operación**
❌ **Anti-patrón:**
```java
ERR_PATIENT_CREATION_REQUIRES_X
ERR_PATIENT_UPDATE_REQUIRES_X
ERR_PATIENT_DELETE_REQUIRES_X
```

✅ **Correcto:**
```java
ERR_PATIENT_REQUIRES_X
// Contexto operacional en mensaje de excepción
```

### 2. **Validaciones en la Capa Correcta**
- **Value Objects:** Validaciones de formato, rangos, consistencia interna
- **Agregados:** Invariantes de negocio, coordinación de VOs
- **Domain Services:** Validaciones cross-agregado

### 3. **Delegación de Responsabilidades Transversales**
Estados como "activo/inactivo" que aplican a múltiples agregados deben manejarse en un componente transversal (ej: `UserAccessError`), no duplicarse en cada agregado.

---

## Referencias Cruzadas

- **ADR-036:** Alcance Experimental del Módulo Actor
- **ADR-037:** Estrategia de Numeración de Catálogos de Error
- **Código fuente:** `com.example.ClinicaDefinitiva.domain.errors`

---

## Mantenimiento de este Documento

### Cuándo Actualizar
- Al eliminar un nuevo catálogo de error
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
- [ADR-(Arquitectura)-23-Catálogos Eliminados - Histórico del Módulo Actor.md](ADR-%28Arquitectura%29-23-Cat%C3%A1logos%20Eliminados%20-%20Hist%C3%B3rico%20del%20M%C3%B3dulo%20Actor.md)
- [ADR-(Arquitectura)-20-Alcance Experimental del Módulo Actor.md](ADR-%28Arquitectura%29-20-Alcance%20Experimental%20del%20M%C3%B3dulo%20Actor.md)
- [ADR-(Arquitectura)-21-Catálogos de errores por agregado con interfaz común.md](ADR-%28Arquitectura%29-21-Cat%C3%A1logos%20de%20errores%20por%20agregado%20con%20interfaz%20com%C3%BAn.md)
- [ADR-(Arquitectura)-19-Catálogo único de errores con contextos diferenciados (Entidad vs VO).md](ADR-%28Arquitectura%29-19-Cat%C3%A1logo%20%C3%BAnico%20de%20errores%20con%20contextos%20diferenciados%20%28Entidad%20vs%20VO%29.md)

---
## Aprobación

**Autor:** David Stiven Sanclemente 
**Fecha:** Diciembre 24, 2024  
**Estado:** Registro Histórico Oficial  
**Próxima revisión:** Cada eliminación de catálogo

---

**Nota final:** Este documento es **inmutable histórico**. Nuevas entradas se agregan cronológicamente. Entradas existentes **NUNCA** se modifican (solo se corrigen errores tipográficos menores).