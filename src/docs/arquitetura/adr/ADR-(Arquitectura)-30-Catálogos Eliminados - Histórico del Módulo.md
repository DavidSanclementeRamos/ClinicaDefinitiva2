

# ADR-31 (Arquitectura): Catálogos Eliminados - Histórico del Módulo `dental.care.services`

**Estado:** 📚 Registro Histórico  
**Fecha:** Enero 06, 2026  
**Propósito:** Documentar catálogos de error eliminados con justificación técnica

---

## Propósito de este Documento

Este ADR mantiene el **registro histórico oficial** de todos los catálogos de error que fueron eliminados del Módulo `dental.care.services`, incluyendo:
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

## 🏥 ProvidedService (Servicio Prestado) - Eliminados

### ERR_SERVICE_PRICE_NEGATIVE
- **Código:** RN-SERVICE-001
- **Descripción original:** "La tarifa base no puede ser negativa"
- **Fecha eliminación:** 2026-01-06
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
  - **Justificación técnica:**  
    La validación pertenece al VO `Price`. Mantenerla en el agregado era redundante y violaba responsabilidad única.
- **Reemplazo:** `ValueObjectError.ERR_SERVICE_PRICE_NEGATIVE`
- **Referencia:** ADR-29, Sección "Delegación Sistemática a Value Objects"

---

### ERR_SERVICE_INVALID_DURATION
- **Código:** RN-SERVICE-002
- **Descripción original:** "Duración inválida para el servicio"
- **Fecha eliminación:** 2026-01-06
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
- **Justificación técnica:**  
  La validación de duración mínima y máxima fue trasladada a `ServiceDuration`.
- **Reemplazo:**
    - `ValueObjectError.ERR_SERVICE_DURATION_MINIMUM`
    - `ValueObjectError.ERR_SERVICE_DURATION_MAXIMUM`
- **Referencia:** ADR-29, Sección "ServiceDuration Refactorizado"

---

### ERR_SERVICE_MISSING_REQUIRED_FIELDS
- **Código:** RN-SERVICE-009
- **Descripción original:** "Debe tener nombre y descripción válidos"
- **Fecha eliminación:** 2026-01-06
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
- **Justificación técnica:**  
  Agrupaba validaciones de `ServiceName` y `ServiceDescription`. La responsabilidad fue trasladada a los respectivos VOs.
- **Reemplazo:**
    - `ValueObjectError.ERR_SERVICE_NAME_CUSTOM_INVALID`
    - `ValueObjectError.ERR_SERVICE_DESCRIPTION_INVALID`
- **Referencia:** ADR-29, Sección "Creación de Value Objects Dedicados"

---

### ERR_SERVICE_CODE_DUPLICATE / ERR_SERVICE_INVALID_CODE_FORMAT
- **Código:** RN-SERVICE-007 / RN-SERVICE-013
- **Descripción original:** "Código duplicado" / "Formato inválido de código"
- **Fecha eliminación:** 2026-01-06
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
- **Justificación técnica:**  
  La validación de unicidad y formato de código fue trasladada a `ServiceCode`.
- **Reemplazo:**
    - `ValueObjectError.ERR_SERVICE_CODE_DUPLICATE`
    - `ValueObjectError.ERR_SERVICE_CODE_LENGTH_INVALID`
- **Referencia:** ADR-29, Sección "ServiceCode Refactorizado"

---

### ERR_SERVICE_NOT_BILLABLE
- **Código:** RN-SERVICE-010
- **Descripción original:** "Servicios inactivos no facturables"
- **Fecha eliminación:** 2026-01-06
- **Motivo:** REUBICACIÓN DE RESPONSABILIDAD
- **Justificación técnica:**  
  La regla pertenece al módulo `Billing`, no a `ProvidedService`.
- **Reemplazo:** `BillingError.ERR_CANNOT_BILL_INACTIVE_SERVICE`
- **Referencia:** ADR-29, Sección "Separación de Responsabilidades"

---

### ERR_SERVICE_DESCRIPTION_TOO_SHORT
- **Código:** RN-SERVICE-014
- **Descripción original:** "La descripción debe tener al menos 20 caracteres"
- **Fecha eliminación:** 2026-01-06
- **Motivo:** VALIDACIÓN DE VALUE OBJECT
- **Justificación técnica:**  
  La validación fue trasladada a `ServiceDescription`.
- **Reemplazo:** `ValueObjectError.ERR_SERVICE_DESCRIPTION_INVALID`
- **Referencia:** ADR-29, Sección "ServiceDescription (Nuevo)"

---

## 📊 Estadísticas de Eliminación

| Agregado        | Eliminados | Motivo Principal                  |
|-----------------|------------|-----------------------------------|
| ProvidedService | 6          | Delegación a VO (5), Reubicación (1) |
| **TOTAL**       | **6**      |                                   |

---

### Distribución por Motivo

```
DELEGACIÓN A VALUE OBJECT:   83% ██████████████████████████░░░░
REUBICACIÓN DE RESPONSABILIDAD: 17% ████░░░░░░░░░░░░░░░░░░░░░░
```

---

## Lecciones Aprendidas

### 1. **Validaciones en la Capa Correcta**
- **Value Objects:** Validaciones de formato, rangos, consistencia interna
- **Agregados:** Invariantes de negocio, coordinación de VOs
- **Domain Services:** Validaciones cross-agregado

### 2. **Evitar Catálogos Genéricos**
❌ **Anti-patrón:**
```java
ERR_SERVICE_MISSING_REQUIRED_FIELDS
```

✅ **Correcto:**
```java
ValueObjectError.ERR_SERVICE_NAME_CUSTOM_INVALID
ValueObjectError.ERR_SERVICE_DESCRIPTION_INVALID
```

### 3. **Separación de Responsabilidades**
Reglas de facturación deben residir en el módulo `Billing`, no en `Services`.

---

## Referencias Cruzadas

- **ADR-29:** Alcance Experimental del Módulo Services
- **ADR-22:** Estrategia de Numeración de Catálogos de Error
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

## Aprobación

**Autor:** David Stiven Sanclemente  
**Fecha:** Enero 06, 2026  
**Estado:** Registro Histórico Oficial  
**Próxima revisión:** Cada eliminación de catálogo

---

**Nota final:** Este documento es **inmutable histórico**. Nuevas entradas se agregan cronológicamente. Entradas existentes **NUNCA** se modifican (solo se corrigen errores tipográficos menores).

---