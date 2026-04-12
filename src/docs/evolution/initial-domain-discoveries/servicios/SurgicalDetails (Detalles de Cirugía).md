# Descubrimiento de Reglas de Negocio por Value Object
## Value Object: SurgicalDetails (Detalles de Cirugía Oral y Maxilofacial)

## Propósito
Representar información específica de servicios quirúrgicos odontológicos. Este Value Object contiene detalles críticos sobre el tipo de cirugía, nivel de complejidad, requisitos de anestesia y necesidad de quirófano. Asegura que solo procedimientos quirúrgicos bien definidos, con niveles de complejidad apropiados y requisitos operativos coherentes puedan ser creados, protegiendo la seguridad del paciente y la planificación operativa.

---

## CREACIÓN
- El tipo de cirugía (surgeryType) puede ser null (procedimiento genérico).
- Si se especifica, el tipo de cirugía debe tener al menos 3 caracteres.
- El nivel de complejidad (complexityLevel) puede ser null.
- Si se especifica, debe ser uno de los valores reconocidos: LOW, MEDIUM, HIGH, CRITICAL.
- Debe indicar explícitamente si requiere anestesia (requiresAnesthesia).
- Debe indicar explícitamente si requiere quirófano (operatingRoomNeeded).
- requiresAnesthesia nunca puede ser null (se normaliza a false por defecto).
- operatingRoomNeeded nunca puede ser null (se normaliza a false por defecto).
- Todos los campos son inmutables tras creación.

---

## EDICIÓN / ACTUALIZACIÓN
- SurgicalDetails es un Value Object inmutable.
- No se edita, se reemplaza completamente mediante ProvidedService.updateDetails().
- El reemplazo debe pasar todas las validaciones de creación.
- El nuevo SurgicalDetails debe seguir siendo coherente con categoría Surgery.

---

## INVARIANTES
- Si requiresAnesthesia es true, complexityLevel debe ser al menos MEDIUM.
- Si operatingRoomNeeded es true, la duración del servicio debe ser al menos 60 minutos.
- Si complexityLevel es CRITICAL, tanto requiresAnesthesia como operatingRoomNeeded deben ser true.
- requiresAnesthesia y operatingRoomNeeded se normalizan usando Boolean.TRUE.equals().
- Cirugías de baja complejidad (LOW) no deberían requerir quirófano.
- Cirugías que requieren quirófano implican mayor nivel de complejidad.

---

## VALIDACIONES ESPECÍFICAS

**Niveles de Complejidad Válidos:**
```java
private static final Set<String> VALID_COMPLEXITY_LEVELS = Set.of(
    "LOW",      // Extracciones simples, frenectomías
    "MEDIUM",   // Extracciones quirúrgicas, biopsias
    "HIGH",     // Implantes complejos, injertos óseos
    "CRITICAL"  // Cirugía maxilofacial mayor, reconstrucciones
);
```

**Tipos de Cirugía Comunes:**
```java
Ejemplos de surgeryType:
- "Extracción de cordal incluido"
- "Implante dental unitario"
- "Injerto óseo autólogo"
- "Frenectomía labial"
- "Biopsia de lesión oral"
- "Cirugía ortognática"
- "Reconstrucción maxilar"
```

**Matriz de Coherencia:**
```
Complejidad   | Anestesia  | Quirófano | Duración Típica
--------------|------------|-----------|------------------
LOW           | Opcional   | No        | 15-30 min
MEDIUM        | Requerida  | Opcional  | 30-60 min
HIGH          | Requerida  | Requerida | 60-120 min
CRITICAL      | Requerida  | Requerida | 120-240 min
```

---

## TRAZABILIDAD Y AUDITORÍA
- Cada instancia de SurgicalDetails es inmutable.
- Cambios se auditan a nivel de ProvidedService (ServiceDetailsChanged event).
- Se registra nivel de complejidad anterior y nuevo al reemplazar detalles.
- Sistema emite alertas críticas si cirugía compleja no requiere anestesia.
- Se registra discrepancia entre complejidad y requisitos operativos.

---

## Justificación Semántica
Estas reglas protegen la seguridad del paciente asegurando requisitos operativos coherentes, evitan planificación incorrecta de recursos (quirófano, anestesiólogo), garantizan que cirugías complejas tengan tiempo y recursos adecuados, facilitan cálculo de costos y preparación pre-operatoria, y aseguran cumplimiento de protocolos de seguridad quirúrgica.

---

## Reglas Descubiertas (formato estandarizado)

**RN-SURGICAL-001**
- Descripción: Si requiere anestesia, el nivel de complejidad debe ser al menos MEDIUM.
- Condición: requiresAnesthesia == true && complexityLevel == "LOW" al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH

**RN-SURGICAL-002**
- Descripción: Si requiere quirófano, la duración del servicio debe ser al menos 60 minutos.
- Condición: operatingRoomNeeded == true && service.duration < 60min al validar coherencia.
- Consecuencia: Se rechaza creación de servicio o se sugiere ajuste de duración.
- Error asociado: ERR_SURGICAL_OPERATING_ROOM_DURATION_MISMATCH

**RN-SURGICAL-003**
- Descripción: El nivel de complejidad debe ser reconocido si se especifica.
- Condición: complexityLevel != null && !VALID_COMPLEXITY_LEVELS.contains(complexityLevel) al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_SURGICAL_INVALID_COMPLEXITY

**RN-SURGICAL-004**
- Descripción: Si complejidad es CRITICAL, debe requerir anestesia y quirófano.
- Condición: complexityLevel == "CRITICAL" && (!requiresAnesthesia || !operatingRoomNeeded) al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS

**RN-SURGICAL-005**
- Descripción: Cirugías de baja complejidad no deberían requerir quirófano.
- Condición: complexityLevel == "LOW" && operatingRoomNeeded == true al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_SURGICAL_LOW_COMPLEXITY_OPERATING_ROOM

**RN-SURGICAL-006**
- Descripción: El tipo de cirugía debe tener al menos 3 caracteres si se especifica.
- Condición: surgeryType != null && surgeryType.length() < 3 al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_SURGICAL_TYPE_TOO_SHORT

**RN-SURGICAL-007**
- Descripción: Cirugías que requieren quirófano implican complejidad al menos MEDIUM.
- Condición: operatingRoomNeeded == true && complexityLevel == "LOW" al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_SURGICAL_OPERATING_ROOM_COMPLEXITY_MISMATCH

---

## Relación con ADRs
- ADR-02 (Dominio): Implementación de Value Objects con validaciones robustas.
- ADR-05 (Arquitectura): Creación de módulo independiente para Servicios.
- ADR-10 (Arquitectura): Estrategia de persistencia con tablas separadas para detalles.
- ADR-18 (Arquitectura): Simplificación de jerarquía de excepciones.

---

## Value Objects Relacionados

**ServiceType (Enum)**
- Valor para SurgicalDetails: `ServiceType.SURGERY`.
- Usado para identificar tipo de detalles en runtime.

**ProvidedService (Agregado Raíz)**
- Contiene SurgicalDetails mediante composición.
- Valida coherencia entre category="Surgery" y details tipo SURGERY.
- Valida coherencia entre duration y operatingRoomNeeded.

---

## Ejemplo de Uso
```java
// Crear detalles de extracción simple (baja complejidad)
SurgicalDetails simpleExtraction = new SurgicalDetails(
    "Extracción dental simple",
    "LOW",
    false, // sin anestesia general
    false  // sin quirófano
);

// Crear detalles de extracción de cordal incluido (media complejidad)
SurgicalDetails wisdomTooth = new SurgicalDetails(
    "Extracción de cordal incluido",
    "MEDIUM",
    true,  // requiere anestesia local profunda
    false  // consultorio equipado
);

// Crear detalles de implante complejo (alta complejidad)
SurgicalDetails complexImplant = new SurgicalDetails(
    "Implante dental con elevación de seno maxilar",
    "HIGH",
    true,  // anestesia general o sedación profunda
    true   // quirófano ambulatorio
);

// Crear detalles de cirugía maxilofacial mayor (crítica)
SurgicalDetails orthognathicSurgery = new SurgicalDetails(
    "Cirugía ortognática bimaxilar",
    "CRITICAL",
    true,  // anestesia general obligatoria
    true   // quirófano hospitalario
);

// Intentar crear con inconsistencia (error)
try {
    SurgicalDetails invalid = new SurgicalDetails(
        "Cirugía compleja",
        "LOW", // ❌ Baja complejidad
        true,  // pero requiere anestesia
        false
    );
} catch (ValueObjectValidationException e) {
    // Lanza ERR_SURGICAL_ANESTHESIA_COMPLEXITY_MISMATCH
    System.err.println("Inconsistencia: " + e.getMessage());
}

// Intentar crear crítica sin requisitos (error)
try {
    SurgicalDetails invalid = new SurgicalDetails(
        "Cirugía mayor",
        "CRITICAL",
        false, // ❌ No requiere anestesia
        false  // ❌ No requiere quirófano
    );
} catch (ValueObjectValidationException e) {
    // Lanza ERR_SURGICAL_CRITICAL_MISSING_REQUIREMENTS
    System.err.println("Cirugía crítica debe requerir anestesia y quirófano");
}

// Crear con valores opcionales null (genérico)
SurgicalDetails generic = new SurgicalDetails(
    null,  // tipo no especificado
    null,  // complejidad a determinar
    false,
    false
);
```

---

## Casos de Uso Clínicos

**Caso 1: Extracción de Cordal Incluido**
```java
// Procedimiento quirúrgico ambulatorio común
SurgicalDetails wisdomTooth = new SurgicalDetails(
    "Extracción quirúrgica de tercer molar incluido",
    "MEDIUM",
    true,  // anestesia local + sedación consciente
    false  // consultorio dental equipado
);

// Asociado a ProvidedService:
// - duration: 60 minutos
// - baseRate: $200,000 COP
// - requiresAuthorization: true
// - Preparación: Rx panorámica, consentimiento informado
```

**Caso 2: Colocación de Implante con Injerto Óseo**
```java
// Procedimiento complejo, requiere quirófano ambulatorio
SurgicalDetails implantWithGraft = new SurgicalDetails(
    "Implante dental con injerto óseo autólogo",
    "HIGH",
    true,  // anestesia general o sedación profunda
    true   // quirófano ambulatorio
);

// Asociado a ProvidedService:
// - duration: 120 minutos
// - baseRate: $1,500,000 COP
// - requiresAuthorization: true
// - Equipo: Cirujano, anestesiólogo, instrumentadora
```

**Caso 3: Frenectomía Labial Pediátrica**
```java
// Procedimiento menor, rápido
SurgicalDetails frenectomy = new SurgicalDetails(
    "Frenectomía labial superior",
    "LOW",
    false, // anestesia tópica + infiltración local
    false  // consultorio estándar
);

// Asociado a ProvidedService:
// - duration: 20 minutos
// - baseRate: $150,000 COP
// - requiresAuthorization: false
// - Paciente: Usualmente niños 6-10 años
```

**Caso 4: Cirugía Ortognática Bimaxilar**
```java
// Procedimiento mayor hospitalario
SurgicalDetails orthognathic = new SurgicalDetails(
    "Cirugía ortognática correctiva bimaxilar",
    "CRITICAL",
    true,  // anestesia general obligatoria
    true   // quirófano hospitalario
);

// Asociado a ProvidedService:
// - duration: 240 minutos (4 horas)
// - baseRate: $8,000,000 COP
// - requiresAuthorization: true (EPS + auditoria médica)
// - Equipo: Cirujano maxilofacial, anestesiólogo, equipo enfermería
// - Postoperatorio: Hospitalización 1-2 días
```