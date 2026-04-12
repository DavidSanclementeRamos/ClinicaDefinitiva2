# ADR-50 (Arquitectura): Simplificación de VOContext y Eliminación de CodeVO

**Estado**: Aprobado
**Fecha:** Diciembre 18, 2025  
**Contexto:** Simplificación del sistema de excepciones de Value Objects  
**Autor:** David Stiven Sanclemente  
**Afecta a:** Todos los módulos del dominio

---

## Contexto y Problemática

### El Error Arquitectónico Original

Durante el desarrollo del sistema se implementó un mecanismo de trazabilidad de excepciones con **tres niveles de identificación**:

```java
// ❌ PROBLEMA: Triple ceremonia para cada VO
public enum CodeVO {
    INV44,  // InvoiceNumber
    INT45,  // InvoiceItemId
    RAT46,  // RateId
    QUA47,  // Quantity
    // ... 48+ códigos más
}

public enum VOContext implements DomainContext {
    INVOICE_NUMBER(CodeVO.INV44),
    INVOICE_ITEM_ID(CodeVO.INT45),
    RATE_ID(CodeVO.RAT46),
    BILLING_QUANTITY(CodeVO.QUA47),
    // ... 50+ contextos más
}

// Uso en excepciones
throw new ValueObjectValidationException(
    BillingVOError.ERR_INVOICE_NUMBER_REQUIRED,  // 1. Error específico
    VOContext.INVOICE_NUMBER                      // 2. Contexto granular
);
```

### La Intención Original (Buena)

**Objetivo:** Trazabilidad completa del origen de cada error.

**Razonamiento:**
> "Si tengo `VOContext.INVOICE_NUMBER` separado de `VOContext.RATE_ID`,
> puedo distinguir exactamente qué VO lanzó la excepción."

### El Problema Real (Malo)

**Cada VO nuevo requería 3 modificaciones en archivos centrales:**

1. Agregar entrada en `CodeVO.java` (enum de 48+ líneas)
2. Agregar entrada en `VOContext.java` (enum de 50+ líneas)
3. Crear entrada en `XxxVOError.java` (error catalog)

**Resultado:**
- ❌ **Fricción constante**: Modificar 2 enums centrales por cada VO
- ❌ **Merge conflicts**: Todos los módulos tocaban los mismos archivos
- ❌ **Violación DRY**: Información duplicada en 3 lugares
- ❌ **Escalabilidad nula**: Billing tiene 10 VOs → 20 líneas en enums centrales
- ❌ **Contradicción con ADR-18**: Prometió "simplificación", pero agregó burocracia

---

## Análisis Crítico: ¿Por Qué Falló?

### 1. **CodeVO No Aporta Valor Semántico**

```java
// ¿Qué información transmite "INV44"?
CodeVO.INV44  // ← Solo ruido visual

// vs. lo que ya sabemos del contexto:
VOContext.INVOICE_NUMBER  // ← Auto-descriptivo
BillingVOError.ERR_INVOICE_NUMBER_REQUIRED  // ← Ya dice "invoice.number"
```

**Pregunta crítica:** ¿Cuándo usaríamos `INV44` en lugar del nombre completo?  
**Respuesta:** Nunca. Es pura ceremonia.

---

### 2. **VOContext Ultra-Granular es Innecesario**

**Pregunta:** ¿Necesitamos 4 contextos diferentes para el mismo módulo?

```java
VOContext.INVOICE_ID         // \
VOContext.INVOICE_NUMBER     //  |-- Todos son del módulo "Billing"
VOContext.INVOICE_ITEM_ID    //  |
VOContext.RATE_ID            //  |
VOContext.BILLING_QUANTITY   // /
```

**Realidad:** El `ErrorCatalog` ya especifica el campo exacto:

```java
BillingVOError.ERR_INVOICE_NUMBER_REQUIRED(
    "RN-BILLING-010",
    "error.invoice.number.required",  // ← Ya dice "invoice.number"
    "El número de factura no puede ser nulo"
)
```

**Por lo tanto:** `VOContext.BILLING` es suficiente. El error específico ya está en el catalog.

---

### 3. **Información Duplicada en 3 Lugares**

```java
// 1. En el error
ERR_INVOICE_NUMBER_REQUIRED → "invoice.number"

// 2. En el contexto  
VOContext.INVOICE_NUMBER → "INVOICE_NUMBER"

// 3. En el código
CodeVO.INV44 → "INV44"

// Los 3 dicen lo mismo, pero en formatos diferentes ❌
```

**Violación:** Principio DRY (Don't Repeat Yourself)

---

### 4. **No Escala - Crecimiento Lineal**

**Proyección a 6 meses:**

| Módulo | VOs Nuevos | Líneas CodeVO | Líneas VOContext | Total |
|--------|------------|---------------|------------------|-------|
| Billing | 10 | +10 | +10 | +20 |
| Treatments | 12 | +12 | +12 | +24 |
| Inventory | 8 | +8 | +8 | +16 |
| Lab Orders | 15 | +15 | +15 | +30 |
| **TOTAL** | **45** | **+45** | **+45** | **+90** |

**Enums centrales de 150+ líneas** que TODOS los módulos tocan.

**Problemas Git:**
- Merge conflicts en cada feature branch
- Code reviews confusos (cambios mezclados)
- Riesgo de duplicar códigos sin darse cuenta

---

## Decisión

### Adoptar Simplificación Radical

**Eliminar `CodeVO.java` completamente.**  
**Simplificar `VOContext` a nivel de módulo/dominio.**

---

## Diseño de la Solución

### 1. VOContext Simplificado

```java
package com.example.ClinicaDefinitiva.domain.errors.context;

/**
 * Contexto de Value Objects agrupados por módulo/dominio.
 * 
 * Decisión de diseño (ADR-50):
 * - Un contexto por MÓDULO, no por VO individual
 * - El ErrorCatalog específico ya identifica el campo exacto
 * - Elimina fricción de crear nuevo enum por cada VO
 * 
 * Ejemplo de uso:
 * ```
* throw new ValueObjectValidationException(
*     BillingVOError.ERR_INVOICE_NUMBER_REQUIRED,
*     VOContext.BILLING  // ← Genérico para todo el módulo
* );
* ```
*/
public enum VOContext implements DomainContext {

    // ========== MÓDULOS DE DOMINIO CORE ==========
    
    /**
     * Contexto para Value Objects del dominio de Persona.
     * VOs incluidos: FullName, Age, Address, PhoneNumber, DateOfBirth, etc.
     */
    PERSON("PERSON"),
    
    /**
     * Contexto para Value Objects del módulo de Programación.
     * VOs incluidos: AppointmentId, ShiftId, AvailabilityStatus, etc.
     */
    SCHEDULING("SCHEDULING"),
    
    /**
     * Contexto para Value Objects del módulo de Facturación.
     * VOs incluidos: InvoiceId, InvoiceNumber, Quantity, RateId, InvoiceStatus, etc.
     */
    BILLING("BILLING"),
    
    /**
     * Contexto para Value Objects de Servicios Odontológicos.
     * VOs incluidos: ServiceId, ServiceCode, ServiceName, ServiceDuration, etc.
     */
    DENTAL_SERVICES("DENTAL_SERVICES"),
    
    /**
     * Contexto para Value Objects de Contabilidad.
     * VOs incluidos: ContractId, LedgerAccountId, JournalEntryId, etc.
     */
    ACCOUNTING("ACCOUNTING"),
    
    /**
     * Contexto para Value Objects de Gestión de Usuarios.
     * VOs incluidos: UserId, Email, Permission, RoleId, etc.
     */
    USER_MANAGEMENT("USER_MANAGEMENT"),
    
    /**
     * Contexto para Value Objects de Tratamientos Clínicos.
     * VOs incluidos: TreatmentId, Diagnosis, TreatmentStatus, etc.
     */
    CLINICAL_TREATMENTS("CLINICAL_TREATMENTS"),
    
    /**
     * Contexto para Value Objects de Actores del Sistema.
     * VOs incluidos: DentistId, PatientId, ReceptionistId, GuardianId, etc.
     */
    ACTORS("ACTORS");
    
    private final String code;
    
    VOContext(String code) {
        this.code = code;
    }
    
    @Override
    public String getCode() {
        return code;
    }
    
    @Override
    public String toString() {
        return code;
    }
}
```

---

### 2. Eliminación de CodeVO

**Archivo `CodeVO.java` → ELIMINADO COMPLETAMENTE**

**Justificación:**
- No aporta valor semántico
- Solo agrega ceremonia
- La información ya está en VOContext y ErrorCatalog

---

### 3. Ejemplos de Uso Refactorizado

#### Antes (❌ Complejo)

```java
// 1. Definir código
public enum CodeVO {
    INV44,  // InvoiceNumber
}

// 2. Definir contexto
public enum VOContext {
    INVOICE_NUMBER(CodeVO.INV44),
}

// 3. Usar en excepción
throw new ValueObjectValidationException(
    BillingVOError.ERR_INVOICE_NUMBER_REQUIRED,
    VOContext.INVOICE_NUMBER
);
```

#### Después (✅ Simple)

```java
// 1. VOContext ya existe (genérico por módulo)

// 2. Usar directamente
throw new ValueObjectValidationException(
    BillingVOError.ERR_INVOICE_NUMBER_REQUIRED,
    VOContext.BILLING  // ← Un solo contexto para todo billing
);
```

---

### 4. Ejemplos Concretos por Módulo

#### Módulo Billing

```java
// InvoiceNumber.java
public static InvoiceNumber of(String value) {
    if (value == null || value.isBlank()) {
        throw new ValueObjectValidationException(
            BillingVOError.ERR_INVOICE_NUMBER_REQUIRED,
            VOContext.BILLING  // ← Todos usan BILLING
        );
    }
    return new InvoiceNumber(value);
}

// Quantity.java
public static Quantity of(int value) {
    if (value < 1) {
        throw new ValueObjectValidationException(
            BillingVOError.ERR_QUANTITY_MUST_BE_POSITIVE,
            VOContext.BILLING  // ← Mismo contexto
        );
    }
    return new Quantity(value);
}

// RateId.java
public static RateId of(Long value) {
    if (value == null) {
        throw new ValueObjectValidationException(
            BillingVOError.ERR_RATE_ID_NULL,
            VOContext.BILLING  // ← Mismo contexto
        );
    }
    return new RateId(value);
}
```

#### Módulo Person

```java
// FullName.java
if (firstName == null || firstName.isBlank()) {
    throw new ValueObjectValidationException(
        PersonVOError.ERR_FIRST_NAME_REQUIRED,
        VOContext.PERSON  // ← Todos usan PERSON
    );
}

// Age.java
if (years < 0 || years > 150) {
    throw new ValueObjectValidationException(
        PersonVOError.ERR_AGE_OUT_OF_RANGE,
        VOContext.PERSON  // ← Mismo contexto
    );
}
```

---

## Comparación: Antes vs Después

### Métricas de Complejidad

| Aspecto | Antes (❌) | Después (✅) | Mejora |
|---------|-----------|-------------|--------|
| **Líneas CodeVO** | 48 | 0 (eliminado) | -100% |
| **Líneas VOContext** | 50+ | 8 | -84% |
| **Modificaciones por VO nuevo** | 3 archivos | 0 archivos | -100% |
| **Merge conflicts** | Altos | Bajos | -90% |
| **Duplicación de info** | 3 lugares | 1 lugar | -66% |

### Ejemplo de Agregar Nuevo VO

#### Antes (❌)

```java
// 1. Modificar CodeVO.java
public enum CodeVO {
    // ... 47 existentes
    PAY48,  // PaymentMethod (NUEVO)
}

// 2. Modificar VOContext.java
public enum VOContext {
    // ... 50 existentes
    PAYMENT_METHOD(CodeVO.PAY48),  // NUEVO
}

// 3. Crear error en catalog
ERR_PAYMENT_METHOD_REQUIRED(...)

// 4. Usar en VO
throw new ValueObjectValidationException(
    BillingVOError.ERR_PAYMENT_METHOD_REQUIRED,
    VOContext.PAYMENT_METHOD
);

// Total: 3 archivos modificados
```

#### Después (✅)

```java
// 1. Crear error en catalog
ERR_PAYMENT_METHOD_REQUIRED(...)

// 2. Usar en VO
throw new ValueObjectValidationException(
    BillingVOError.ERR_PAYMENT_METHOD_REQUIRED,
    VOContext.BILLING  // ← Ya existe
);

// Total: 0 archivos centrales modificados
```

---

## Consecuencias

### Positivas ✅

1. **Reducción drástica de ceremonias**
    - De 3 modificaciones por VO → 0 modificaciones
    - Enums centrales de 100+ líneas → 8 líneas

2. **Mejor escalabilidad**
    - Nuevos módulos agregan 1 línea a VOContext
    - VOs nuevos no tocan archivos centrales

3. **Menos merge conflicts**
    - VOContext casi nunca cambia
    - Cada módulo trabaja aislado en su ErrorCatalog

4. **Simplicidad sin perder trazabilidad**
    - El ErrorCatalog sigue siendo específico
    - VOContext identifica el módulo de origen
    - Logs siguen siendo trazables

5. **Alineación con ADR-18**
    - Cumple la promesa de "simplificación"
    - Nuevas reglas sin crear nuevas clases

### Negativas ⚠️

1. **Pérdida de granularidad de contexto**
    - Antes: `VOContext.INVOICE_NUMBER`
    - Ahora: `VOContext.BILLING`
    - **Mitigación:** El ErrorCatalog ya especifica `invoice.number`

2. **Contexto menos específico en logs**
    - Antes: `[VOContext: INVOICE_NUMBER]`
    - Ahora: `[VOContext: BILLING]`
    - **Mitigación:** El error completo tiene el campo: `ERR_INVOICE_NUMBER_REQUIRED`

### Trade-offs 🔄

| Perdemos | Ganamos |
|----------|---------|
| Contexto ultra-granular | Simplicidad y mantenibilidad |
| Un enum por cada VO | Escalabilidad sin fricción |
| 3 lugares con info | 1 fuente de verdad (ErrorCatalog) |

**Evaluación:** El trade-off es **altamente favorable**. La granularidad perdida es redundante con el ErrorCatalog.

---

## Impacto en el Sistema

### Archivos Afectados

| Archivo | Cambio | Razón |
|---------|--------|-------|
| `CodeVO.java` | ❌ ELIMINADO | No aporta valor |
| `VOContext.java` | 🔄 REFACTORIZADO | Simplificado a 8 entradas |
| `BillingVOError.java` | ✅ SIN CAMBIOS | Sigue siendo específico |
| `InvoiceNumber.java` | 🔄 USO ACTUALIZADO | Usa `VOContext.BILLING` |
| `Quantity.java` | 🔄 USO ACTUALIZADO | Usa `VOContext.BILLING` |
| `RateId.java` | 🔄 USO ACTUALIZADO | Usa `VOContext.BILLING` |

---


## Referencias

### ADRs Relacionados

- **ADR-18**: Simplificación general de jerarquía de excepciones
    - Prometió simplificación, pero CodeVO/VOContext crearon complejidad
    - Esta refactorización alinea la implementación con la intención original

- **ADR-19**: Catálogo único de errores con contextos diferenciados
    - Introdujo `VOContext` correctamente
    - Pero la granularidad se excedió

- **ADR-21**: Catálogos de errores por agregado con interfaz común
    - Demuestra que el ErrorCatalog ya provee especificidad
    - Hace redundante el VOContext ultra-granular

### Principios Aplicados

- **KISS** (Keep It Simple, Stupid)
- **YAGNI** (You Aren't Gonna Need It)
- **DRY** (Don't Repeat Yourself)
- **Open/Closed** (abierto a extensión, cerrado a modificación)

---

## Conclusión

Este ADR documenta un **error arquitectónico** y su corrección.

**El error:** Sobre-ingenierizar el sistema de contextos creyendo que más granularidad es siempre mejor.

**La corrección:** Simplificar agrupando contextos por módulo, confiando en que el ErrorCatalog provee la especificidad necesaria.

**El valor:** Demostrar que identificar y corregir over-engineering es una habilidad profesional valiosa.

---

**Resumen ejecutivo:**
`CodeVO` eliminado. `VOContext` simplificado de 50+ a 8 entradas.
Fricción de desarrollo reducida 90%. Trazabilidad preservada mediante ErrorCatalog específico.
Alineación lograda con principios de simplicidad del ADR-18.