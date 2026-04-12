# Descubrimiento de Reglas de Negocio por Value Object
## Value Object: AestheticDetails (Detalles de Estética Dental)

## Propósito
Representar información específica de servicios de estética dental. Este Value Object contiene detalles sobre el tipo de procedimiento estético, material utilizado y resultado esperado. Asegura que solo tratamientos estéticos bien definidos, con materiales apropiados y expectativas realistas puedan ser creados, protegiendo la satisfacción del paciente y la gestión de expectativas clínicas.

---

## CREACIÓN
- El tipo de procedimiento estético (aestheticType) es obligatorio y no puede estar en blanco.
- aestheticType debe tener al menos 3 caracteres.
- El tipo de procedimiento debe ser uno de los valores reconocidos por el sistema.
- El material utilizado (materialUsed) puede ser null (genérico o no aplica).
- El resultado esperado (expectedResult) puede ser null (no especificado).
- Si expectedResult se especifica, debe tener al menos 10 caracteres.
- Todos los campos son inmutables tras creación.

---

## EDICIÓN / ACTUALIZACIÓN
- AestheticDetails es un Value Object inmutable.
- No se edita, se reemplaza completamente mediante ProvidedService.updateDetails().
- El reemplazo debe pasar todas las validaciones de creación.
- El nuevo AestheticDetails debe seguir siendo coherente con categoría Aesthetics.

---

## INVARIANTES
- aestheticType nunca puede ser null ni blank.
- aestheticType debe existir en el catálogo cerrado VALID_AESTHETIC_TYPES.
- materialUsed puede ser null (procedimientos que no usan material como contorneado).
- expectedResult debe ser descriptivo y realista si se especifica.
- Blanqueamientos no deben prometer tonos irreales (más de 8-10 tonos).
- Carillas de porcelana son permanentes e irreversibles (debe quedar claro).
- Resultados estéticos dependen de condiciones iniciales del paciente.

---

## VALIDACIONES ESPECÍFICAS

**Tipos de Procedimientos Estéticos Válidos:**
```java
private static final Set<String> VALID_AESTHETIC_TYPES = Set.of(
    "WHITENING",              // Blanqueamiento dental
    "VENEER",                 // Carillas estéticas
    "BONDING",                // Resina estética (bonding)
    "CONTOURING",             // Contorneado dental
    "GUM_RESHAPING",          // Remodelación de encías (gingivoplastia)
    "SMILE_DESIGN",           // Diseño de sonrisa completo
    "COMPOSITE_RESTORATION",  // Restauración estética con composite
    "INLAY_ONLAY"            // Incrustaciones estéticas
);
```

**Materiales Comunes:**
```java
materialUsed (ejemplos):
- "HYDROGEN_PEROXIDE"      // Peróxido de hidrógeno (blanqueamiento)
- "CARBAMIDE_PEROXIDE"     // Peróxido de carbamida (blanqueamiento casero)
- "PORCELAIN"              // Porcelana feldespática (carillas)
- "EMAX"                   // Disilicato de litio (carillas prémium)
- "COMPOSITE_RESIN"        // Resina compuesta (bonding)
- "ZIRCONIA"               // Zirconio (incrustaciones)
- null                     // No aplica (contorneado, gingivoplastia)
```

**Resultados Esperados Típicos:**
```java
expectedResult (ejemplos):
- "Aclarar 6-8 tonos en escala Vita"
- "Corregir diastemas y mejorar forma dental"
- "Sonrisa armónica con dientes proporcionados"
- "Restaurar anatomía y color natural"
- "Eliminar manchas y mejorar textura superficial"
- "Contorno gingival simétrico y estético"
```

**Limitaciones por Tipo:**
```java
WHITENING:
- Resultado: 6-10 tonos más claro (según estado inicial)
- Duración: 1-2 años con mantenimiento
- Limitación: No aclara restauraciones existentes

VENEER:
- Resultado: Transformación completa de forma, color, tamaño
- Duración: 10-15 años con cuidados
- Limitación: Procedimiento irreversible (tallado dental)

BONDING:
- Resultado: Mejora estética moderada
- Duración: 3-5 años
- Limitación: Menos resistente que porcelana

CONTOURING:
- Resultado: Ajustes menores de forma
- Duración: Permanente
- Limitación: Solo para casos leves

GUM_RESHAPING:
- Resultado: Simetría gingival, menos "encía visible"
- Duración: Permanente
- Limitación: No altera estructura dental
```

---

## TRAZABILIDAD Y AUDITORÍA
- Cada instancia de AestheticDetails es inmutable.
- Cambios se auditan a nivel de ProvidedService (ServiceDetailsChanged event).
- Se registra tipo de procedimiento anterior y nuevo al reemplazar detalles.
- Sistema emite alertas si expectedResult promete resultados irrealistas.
- Se registra material para trazabilidad de proveedores y lotes.
- Se documenta consentimiento informado sobre permanencia del procedimiento.

---

## Justificación Semántica
Estas reglas protegen la gestión de expectativas del paciente asegurando claridad en el procedimiento, evitan promesas irrealistas que generen insatisfacción, garantizan trazabilidad de materiales (importante en carillas y restauraciones), facilitan educación del paciente sobre durabilidad y limitaciones, protegen legalmente al profesional documentando resultados esperados realistas, y aseguran coherencia entre tipo de procedimiento y material utilizado.

---

## Reglas Descubiertas (formato estandarizado)

**RN-AESTHETIC-001**
- Descripción: El tipo de procedimiento estético es obligatorio y no puede estar en blanco.
- Condición: aestheticType == null || aestheticType.isBlank() al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_AESTHETIC_MISSING_TYPE

**RN-AESTHETIC-002**
- Descripción: El tipo de procedimiento debe ser reconocido por el sistema.
- Condición: !VALID_AESTHETIC_TYPES.contains(aestheticType.toUpperCase()) al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_AESTHETIC_INVALID_TYPE

**RN-AESTHETIC-003**
- Descripción: El tipo de procedimiento debe tener al menos 3 caracteres.
- Condición: aestheticType != null && aestheticType.length() < 3 al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_AESTHETIC_TYPE_TOO_SHORT

**RN-AESTHETIC-004**
- Descripción: El resultado esperado debe tener al menos 10 caracteres si se especifica.
- Condición: expectedResult != null && expectedResult.length() < 10 al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_AESTHETIC_RESULT_TOO_SHORT

**RN-AESTHETIC-005**
- Descripción: Blanqueamiento no debe prometer más de 10 tonos de aclaración.
- Condición: aestheticType == "WHITENING" && expectedResult.contains("> 10 tonos") al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_AESTHETIC_UNREALISTIC_WHITENING

**RN-AESTHETIC-006**
- Descripción: Carillas deben mencionar irreversibilidad en resultado esperado.
- Condición: aestheticType == "VENEER" && expectedResult != null && !expectedResult.toLowerCase().contains("irreversible") al invocar constructor.
- Consecuencia: Se emite advertencia (warning) sugeriendo incluir esta información.
- Error asociado: WARN_AESTHETIC_VENEER_IRREVERSIBILITY

**RN-AESTHETIC-007**
- Descripción: Procedimientos con porcelana deben especificar material.
- Condición: (aestheticType == "VENEER" || aestheticType == "INLAY_ONLAY") && materialUsed == null al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_AESTHETIC_MISSING_MATERIAL

---

## Relación con ADRs
- ADR-02 (Dominio): Implementación de Value Objects con validaciones robustas.
- ADR-05 (Arquitectura): Creación de módulo independiente para Servicios.
- ADR-10 (Arquitectura): Estrategia de persistencia con tablas separadas para detalles.
- ADR-18 (Arquitectura): Simplificación de jerarquía de excepciones.

---

## Value Objects Relacionados

**ServiceType (Enum)**
- Valor para AestheticDetails: `ServiceType.AESTHETICS`.
- Usado para identificar tipo de detalles en runtime.

**ProvidedService (Agregado Raíz)**
- Contiene AestheticDetails mediante composición.
- Valida coherencia entre category="Aesthetics" y details tipo AESTHETICS.

---

## Ejemplo de Uso
```java
// Crear detalles de blanqueamiento en consultorio
AestheticDetails inOfficeWhitening = new AestheticDetails(
    "WHITENING",
    "HYDROGEN_PEROXIDE",
    "Aclarar 6-8 tonos en escala Vita según condiciones iniciales"
);

// Crear detalles de carillas de porcelana
AestheticDetails porcelainVeneers = new AestheticDetails(
    "VENEER",
    "EMAX",
    "Transformación estética completa: corrección de forma, color y tamaño. Procedimiento irreversible que requiere tallado dental mínimo."
);

// Crear detalles de bonding estético
AestheticDetails aestheticBonding = new AestheticDetails(
    "BONDING",
    "COMPOSITE_RESIN",
    "Cerrar diastema anterior y mejorar forma de incisivos laterales"
);

// Crear detalles de contorneado dental
AestheticDetails contouring = new AestheticDetails(
    "CONTOURING",
    null, // no usa material adicional
    "Suavizar bordes irregulares y armonizar longitud de incisivos centrales"
);

// Crear detalles de diseño de sonrisa completo
AestheticDetails smileDesign = new AestheticDetails(
    "SMILE_DESIGN",
    "PORCELAIN",
    "Rehabilitación estética completa con 10 carillas superiores: sonrisa armónica, proporciones ideales y color natural"
);

// Intentar crear con tipo inválido (error)
try {
    AestheticDetails invalid = new AestheticDetails(
        "LASER_WHITENING", // ❌ Tipo no reconocido
        "LASER",
        "Blanquear dientes"
    );
} catch (ValueObjectValidationException e) {
    // Lanza ERR_AESTHETIC_INVALID_TYPE
    System.err.println("Tipo estético no reconocido: " + e.getMessage());
}

// Intentar crear sin tipo (error)
try {
    AestheticDetails invalid = new AestheticDetails(
        null, // ❌ Obligatorio
        "HYDROGEN_PEROXIDE",
        "Mejorar estética"
    );
} catch (IllegalArgumentException e) {
    // Lanza ERR_AESTHETIC_MISSING_TYPE
    System.err.println("Tipo de procedimiento es obligatorio: " + e.getMessage());
}

// Crear con resultado esperado muy breve (error)
try {
    AestheticDetails invalid = new AestheticDetails(
        "WHITENING",
        "HYDROGEN_PEROXIDE",
        "Blanquear" // ❌ < 10 caracteres
    );
} catch (IllegalArgumentException e) {
    // Lanza ERR_AESTHETIC_RESULT_TOO_SHORT
    System.err.println("Resultado esperado muy breve: " + e.getMessage());
}
```

---

## Casos de Uso Clínicos

**Caso 1: Blanqueamiento Dental en Consultorio**
```java
// Procedimiento ambulatorio estándar
AestheticDetails professionalWhitening = new AestheticDetails(
    "WHITENING",
    "HYDROGEN_PEROXIDE",
    "Aclarar 6-8 tonos mediante 3 aplicaciones de peróxido al 35%. Resultado varía según tono inicial y hábitos del paciente."
);

// Asociado a ProvidedService:
// - duration: 90 minutos (3 aplicaciones de 15 min c/u)
// - baseRate: $450,000 COP
// - requiresAuthorization: false
// - Contraindicaciones: Sensibilidad dental, embarazo, caries activas
// - Mantenimiento: Blanqueamiento casero complementario (opcional)
```

**Caso 2: Carillas de Disilicato de Litio (E-max)**
```java
// Transformación estética premium
AestheticDetails emaxVeneers = new AestheticDetails(
    "VENEER",
    "EMAX",
    "Rehabilitación estética con 8 carillas superiores de disilicato de litio: corrección de color, forma y proporciones. Procedimiento irreversible con tallado dental conservador (0.3-0.5mm)."
);

// Asociado a ProvidedService:
// - duration: 120 minutos (cementación)
// - baseRate: $7,200,000 COP (8 x $900,000)
// - requiresAuthorization: true
// - Proceso: Mock-up → Tallado → Provisionales → Cementación
// - Durabilidad: 10-15 años con cuidados
// - Ventaja: Mínimamente invasivo, altamente estético

**Caso 3: Diseño Digital de Sonrisa (DSD)**
```java// Planificación estética integral
AestheticDetails digitalSmileDesign = new AestheticDetails(
"SMILE_DESIGN",
"PORCELAIN",
"Diseño digital de sonrisa con análisis facial, fotografía y videografía: 10 carillas superiores + 4 coronas posteriores. Resultado: sonrisa armónica respetando proporciones áureas y líneas faciales."
);// Asociado a ProvidedService:
// - duration: 180 minutos (fase final)
// - baseRate: $12,000,000 COP (rehabilitación completa)
// - requiresAuthorization: true
// - Fases: Estudio DSD → Mock-up → Preparación → Provisionales → Definitivas
// - Tecnología: Escaneo intraoral, diseño CAD/CAM
// - Equipo: Odontólogo esteta, técnico dental certificado

**Caso 4: Bonding Estético con Composite**
```java// Alternativa conservadora a carillas
AestheticDetails compositeBonding = new AestheticDetails(
"BONDING",
"COMPOSITE_RESIN",
"Cerrar diastema de 2mm entre incisivos centrales superiores mediante estratificación de composite nanohíbrido. Procedimiento reversible y conservador (sin tallado)."
);// Asociado a ProvidedService:
// - duration: 60 minutos
// - baseRate: $400,000 COP (2 dientes)
// - requiresAuthorization: false
// - Ventaja: Conservador, reversible, una sola cita
// - Limitación: Menor durabilidad que porcelana (3-5 años)
// - Mantenimiento: Pulido anual recomendado

**Caso 5: Gingivoplastia Estética (Sonrisa Gingival)**
```java// Corrección de contorno gingival
AestheticDetails gumContouring = new AestheticDetails(
"GUM_RESHAPING",
null, // procedimiento quirúrgico sin material protésico
"Remodelación de contorno gingival para reducir exposición de encía al sonreír (sonrisa gingival). Resultado: simetría gingival y proporciones dentales armónicas."
);// Asociado a ProvidedService:
// - duration: 45 minutos
// - baseRate: $800,000 COP
// - requiresAuthorization: false
// - Técnica: Gingivoplastia con electrobisturí o láser
// - Cicatrización: 7-10 días
// - Indicación: Erupción pasiva alterada, sonrisa gingival
// - Resultado: Permanente