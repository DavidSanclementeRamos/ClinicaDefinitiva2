# Descubrimiento de Reglas de Negocio por Value Object
## Value Object: ProstheticDetails (Detalles de Prótesis Dental)

## Propósito
Representar información específica de servicios protésicos. Este Value Object contiene detalles técnicos sobre si la prótesis es fija o removible, el material utilizado, el tipo específico de prótesis y el número de unidades. Asegura que solo prótesis bien definidas, con materiales apropiados y número de unidades coherente puedan ser creadas, protegiendo la calidad del tratamiento y la facturación correcta.

---

## CREACIÓN
- Debe especificar si es fija o removible (fixedOrRemovable) - campo obligatorio.
- fixedOrRemovable no puede estar en blanco.
- El material (material) puede ser null (genérico).
- El tipo de prótesis (prostheticType) puede ser null (genérico).
- El número de unidades (units) debe ser mayor o igual a 0.
- Si units es null, se normaliza a 0 por defecto.
- Todos los campos son inmutables tras creación.

---

## EDICIÓN / ACTUALIZACIÓN
- ProstheticDetails es un Value Object inmutable.
- No se edita, se reemplaza completamente mediante ProvidedService.updateDetails().
- El reemplazo debe pasar todas las validaciones de creación.
- El nuevo ProstheticDetails debe seguir siendo coherente con categoría Prosthetics.

---

## INVARIANTES
- fixedOrRemovable nunca puede ser null ni blank.
- fixedOrRemovable debe ser "FIXED" o "REMOVABLE" (valores normalizados).
- units debe ser >= 0.
- Prótesis removibles no pueden tener más de 14 unidades (arcada completa superior o inferior).
- Prótesis totales removibles típicamente tienen 14 unidades por arcada.
- Prótesis fijas (coronas individuales) típicamente tienen 1 unidad.
- Prótesis fijas (puentes) tienen 3+ unidades.
- units = 0 indica que el número no está definido aún (presupuesto pendiente).

---

## VALIDACIONES ESPECÍFICAS

**Tipos de Prótesis Fijas:**
```java
FIXED prostheticTypes:
- "PORCELAIN_CROWN"      // Corona de porcelana unitaria
- "METAL_CROWN"          // Corona metálica
- "ZIRCONIA_CROWN"       // Corona de zirconio
- "FIXED_BRIDGE"         // Puente fijo (3+ unidades)
- "MARYLAND_BRIDGE"      // Puente Maryland (adhesivo)
- "IMPLANT_CROWN"        // Corona sobre implante
- "VENEER"               // Carilla estética
```

**Tipos de Prótesis Removibles:**
```java
REMOVABLE prostheticTypes:
- "FULL_DENTURE"         // Prótesis total (14 unidades)
- "PARTIAL_DENTURE"      // Prótesis parcial removible
- "FLEXIBLE_DENTURE"     // Prótesis flexible (nylon)
- "CAST_METAL_PARTIAL"   // Prótesis parcial colada
- "IMMEDIATE_DENTURE"    // Prótesis inmediata post-extracción
```

**Materiales Comunes:**
```java
Materials:
- "PORCELAIN"            // Porcelana feldespática
- "ZIRCONIA"             // Dióxido de zirconio
- "METAL_CERAMIC"        // Metal-porcelana (PFM)
- "ACRYLIC"              // Acrílico (resina)
- "CHROME_COBALT"        // Cromo-cobalto (colados)
- "FLEXIBLE_NYLON"       // Nylon flexible
- "COMPOSITE"            // Composite estético
```

**Rangos de Unidades por Tipo:**
```
FIXED:
- Corona individual: 1 unidad
- Puente 3 piezas: 3 unidades
- Puente 4 piezas: 4 unidades
- Carilla: 1 unidad
- Rehabilitación completa: 14-28 unidades

REMOVABLE:
- Prótesis total superior: 14 unidades
- Prótesis total inferior: 14 unidades
- Prótesis parcial: 1-13 unidades
- Prótesis completa bimaxilar: 28 unidades
```

---

## TRAZABILIDAD Y AUDITORÍA
- Cada instancia de ProstheticDetails es inmutable.
- Cambios se auditan a nivel de ProvidedService (ServiceDetailsChanged event).
- Se registra tipo anterior y nuevo, número de unidades al reemplazar detalles.
- Sistema emite alertas si units es inconsistente con prostheticType.
- Se registra material para trazabilidad de proveedores.

---

## Justificación Semántica
Estas reglas aseguran coherencia en especificación de prótesis, evitan errores de facturación por unidades incorrectas, garantizan claridad en tipo de prótesis (fija vs removible), facilitan cálculo de materiales y costos de laboratorio, protegen contra valores atípicos (ej. 20 unidades removibles), y aseguran información completa para órdenes a laboratorios dentales.

---

## Reglas Descubiertas (formato estandarizado)

**RN-PROSTHETIC-001**
- Descripción: Debe especificar si es fija o removible.
- Condición: fixedOrRemovable == null || fixedOrRemovable.isBlank() al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_PROSTHETIC_MISSING_TYPE

**RN-PROSTHETIC-002**
- Descripción: El número de unidades debe ser mayor o igual a 0.
- Condición: units < 0 al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_PROSTHETIC_INVALID_UNITS

**RN-PROSTHETIC-003**
- Descripción: Prótesis removibles no pueden tener más de 14 unidades por arcada.
- Condición: fixedOrRemovable == "REMOVABLE" && units > 14 al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_PROSTHETIC_EXCESSIVE_UNITS

**RN-PROSTHETIC-004**
- Descripción: fixedOrRemovable debe ser "FIXED" o "REMOVABLE".
- Condición: fixedOrRemovable.toUpperCase() not in ["FIXED", "REMOVABLE"] al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_PROSTHETIC_INVALID_TYPE_VALUE

**RN-PROSTHETIC-005**
- Descripción: Corona individual debe tener 1 unidad.
- Condición: prostheticType.contains("CROWN") && !prostheticType.contains("BRIDGE") && units > 1 al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_PROSTHETIC_CROWN_MULTIPLE_UNITS

**RN-PROSTHETIC-006**
- Descripción: Prótesis total debe tener 14 unidades.
- Condición: prostheticType == "FULL_DENTURE" && units != 14 && units != 0 al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_PROSTHETIC_FULL_DENTURE_UNITS

**RN-PROSTHETIC-007**
- Descripción: Puente fijo debe tener al menos 3 unidades.
- Condición: prostheticType.contains("BRIDGE") && units > 0 && units < 3 al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_PROSTHETIC_BRIDGE_INSUFFICIENT_UNITS

---

## Relación con ADRs
- ADR-02 (Dominio): Implementación de Value Objects con validaciones robustas.
- ADR-05 (Arquitectura): Creación de módulo independiente para Servicios.
- ADR-10 (Arquitectura): Estrategia de persistencia con tablas separadas para detalles.
- ADR-18 (Arquitectura): Simplificación de jerarquía de excepciones.

---

## Value Objects Relacionados

**ServiceType (Enum)**
- Valor para ProstheticDetails: `ServiceType.PROSTHETICS`.
- Usado para identificar tipo de detalles en runtime.

**ProvidedService (Agregado Raíz)**
- Contiene ProstheticDetails mediante composición.
- Valida coherencia entre category="Prosthetics" y details tipo PROSTHETICS.

---

## Ejemplo de Uso
```java
// Crear detalles de corona de porcelana individual
ProstheticDetails porcelainCrown = new ProstheticDetails(
    "FIXED",
    "PORCELAIN",
    "PORCELAIN_CROWN",
    1 // 1 unidad (corona individual)
);

// Crear detalles de puente fijo 3 piezas
ProstheticDetails fixedBridge = new ProstheticDetails(
    "FIXED",
    "METAL_CERAMIC",
    "FIXED_BRIDGE",
    3 // 3 unidades (2 pilares + 1 póntico)
);

// Crear detalles de prótesis total superior
ProstheticDetails fullDenture = new ProstheticDetails(
    "REMOVABLE",
    "ACRYLIC",
    "FULL_DENTURE",
    14 // 14 piezas dentales (arcada completa)
);

// Crear detalles de prótesis parcial removible
ProstheticDetails partialDenture = new ProstheticDetails(
    "REMOVABLE",
    "CHROME_COBALT",
    "CAST_METAL_PARTIAL",
    5 // 5 piezas a reemplazar
);

// Intentar crear con unidades negativas (error)
try {
    ProstheticDetails invalid = new ProstheticDetails(
        "FIXED",
        "ZIRCONIA",
        "ZIRCONIA_CROWN",
        -1 // ❌ Negativo
    );
} catch (IllegalArgumentException e) {
    // Lanza ERR_PROSTHETIC_INVALID_UNITS
    System.err.println("Unidades inválidas: " + e.getMessage());
}

// Intentar crear removible con exceso de unidades (error)
try {
    ProstheticDetails invalid = new ProstheticDetails(
        "REMOVABLE",
        "ACRYLIC",
        "PARTIAL_DENTURE",
        20 // ❌ > 14 unidades por arcada
    );
} catch (ValueObjectValidationException e) {
    // Lanza ERR_PROSTHETIC_EXCESSIVE_UNITS
    System.err.println("Demasiadas unidades para removible: " + e.getMessage());
}

// Crear sin especificar unidades (presupuesto pendiente)
ProstheticDetails pending = new ProstheticDetails(
    "FIXED",
    "ZIRCONIA",
    "FIXED_BRIDGE",
    null // se normaliza a 0
);
```

---

## Casos de Uso Clínicos

**Caso 1: Corona de Zirconio sobre Implante**
```java
// Rehabilitación estética unitaria
ProstheticDetails implantCrown = new ProstheticDetails(
    "FIXED",
    "ZIRCONIA",
    "IMPLANT_CROWN",
    1
);

// Asociado a ProvidedService:
// - duration: 60 minutos (cementación)
// - baseRate: $900,000 COP
// - requiresAuthorization: true
// - Prerequisito: Implante osteointegrado (3-6 meses)
```

**Caso 2: Puente Metal-Porcelana 4 Unidades**
```java
// Reemplazo de 2 piezas faltantes con 2 pilares
ProstheticDetails bridge = new ProstheticDetails(
    "FIXED",
    "METAL_CERAMIC",
    "FIXED_BRIDGE",
    4 // 2 pilares + 2 pónticos
);

// Asociado a ProvidedService:
// - duration: 90 minutos (cementación)
// - baseRate: $1,600,000 COP (4 x $400,000)
// - requiresAuthorization: true
// - Prerequisito: Tallado de pilares, provisionales
```

**Caso 3: Prótesis Total Acrílica Bimaxilar**
```java
// Rehabilitación completa desdentado total
ProstheticDetails completeDenture = new ProstheticDetails(
    "REMOVABLE",
    "ACRYLIC",
    "FULL_DENTURE",
    28 // 14 superior + 14 inferior
);

// Asociado a ProvidedService:
// - duration: 120 minutos (instalación y ajustes)
// - baseRate: $2,500,000 COP (par completo)
// - requiresAuthorization: true
// - Proceso: Impresiones, registro, prueba, instalación
```

**Caso 4: Prótesis Parcial Flexible (Estética)**
```java
// Alternativa estética a prótesis metálicas
ProstheticDetails flexiblePartial = new ProstheticDetails(
    "REMOVABLE",
    "FLEXIBLE_NYLON",
    "FLEXIBLE_DENTURE",
    6 // 6 piezas anteriores
);

// Asociado a ProvidedService:
// - duration: 45 minutos (instalación)
// - baseRate: $1,200,000 COP
// - requiresAuthorization: false
// - Ventaja: Sin ganchos metálicos visibles
```