# Descubrimiento de Reglas de Negocio por Value Object
## Value Object: OrthodonticDetails (Detalles de Ortodoncia)

## Propósito
Representar información específica de servicios ortodónticos. Este Value Object contiene detalles técnicos sobre el tipo de aparato, duración estimada del tratamiento y requisitos de seguimiento. Asegura que solo tratamientos ortodónticos bien definidos, con duraciones realistas y tipos de aparato válidos puedan ser creados, protegiendo la coherencia clínica del sistema.

---

## CREACIÓN
- El tipo de aparato (applianceType) es obligatorio y no puede estar en blanco.
- El tipo de aparato debe ser uno de los valores reconocidos por el sistema.
- La duración del tratamiento (treatmentDurationMonths) debe estar entre 6 y 48 meses.
- La duración del tratamiento no puede ser negativa.
- Debe indicar explícitamente si requiere seguimiento periódico (requiresFollowup).
- Si requiresFollowup es true, debe haber claridad sobre frecuencia (implícito: mensual).
- Todos los campos son inmutables tras creación.

---

## EDICIÓN / ACTUALIZACIÓN
- OrthodonticDetails es un Value Object inmutable.
- No se edita, se reemplaza completamente mediante ProvidedService.updateDetails().
- El reemplazo debe pasar todas las validaciones de creación.
- El nuevo OrthodonticDetails debe seguir siendo coherente con categoría Orthodontics.

---

## INVARIANTES
- applianceType nunca puede ser null ni blank.
- treatmentDurationMonths debe estar en rango 6-48 meses si se especifica.
- requiresFollowup nunca puede ser null (usa Boolean.TRUE.equals() para normalizar).
- El tipo de aparato debe existir en el catálogo cerrado VALID_APPLIANCE_TYPES.
- Tratamientos con alineadores (CLEAR_ALIGNERS) típicamente son más cortos (12-24 meses).
- Tratamientos con brackets metálicos (METAL_BRACKETS) pueden ser más largos (18-36 meses).

---

## VALIDACIONES ESPECÍFICAS

**Tipo de Aparato Válidos:**
```java
private static final Set<String> VALID_APPLIANCE_TYPES = Set.of(
    "METAL_BRACKETS",        // Brackets metálicos convencionales
    "CERAMIC_BRACKETS",      // Brackets cerámicos estéticos
    "LINGUAL_BRACKETS",      // Brackets linguales (cara interna)
    "CLEAR_ALIGNERS",        // Alineadores transparentes (ej. Invisalign)
    "REMOVABLE_APPLIANCES",  // Aparatos removibles
    "FUNCTIONAL_APPLIANCES"  // Aparatos funcionales ortopédicos
);
```

**Rangos Típicos por Tipo:**
```java
METAL_BRACKETS        → 18-36 meses (tratamientos complejos)
CERAMIC_BRACKETS      → 18-30 meses (similar a metálicos)
LINGUAL_BRACKETS      → 24-36 meses (más complejos)
CLEAR_ALIGNERS        → 12-24 meses (casos leves a moderados)
REMOVABLE_APPLIANCES  → 6-18 meses (tratamientos menores)
FUNCTIONAL_APPLIANCES → 12-24 meses (crecimiento)
```

---

## TRAZABILIDAD Y AUDITORÍA
- Cada instancia de OrthodonticDetails es inmutable.
- Cambios se auditan a nivel de ProvidedService (ServiceDetailsChanged event).
- Se registra tipo de aparato anterior y nuevo al reemplazar detalles.
- Sistema emite alertas si duración es atípica para el tipo de aparato.

---

## Justificación Semántica
Estas reglas aseguran coherencia clínica en tratamientos ortodónticos, evitan duraciones irreales (< 6 meses o > 48 meses), garantizan que solo tipos de aparato reconocidos sean utilizados, protegen la calidad de información para reportes de gestión clínica, y facilitan cálculos de agendamiento y control de citas de seguimiento.

---

## Reglas Descubiertas (formato estandarizado)

**RN-ORTHODONTIC-001**
- Descripción: El tipo de aparato es obligatorio y no puede estar en blanco.
- Condición: applianceType == null || applianceType.isBlank() al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_ORTHODONTIC_MISSING_APPLIANCE

**RN-ORTHODONTIC-002**
- Descripción: La duración del tratamiento debe estar entre 6 y 48 meses.
- Condición: treatmentDurationMonths != null && (treatmentDurationMonths < 6 || treatmentDurationMonths > 48) al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_ORTHODONTIC_INVALID_DURATION

**RN-ORTHODONTIC-003**
- Descripción: El tipo de aparato debe ser reconocido por el sistema.
- Condición: !VALID_APPLIANCE_TYPES.contains(applianceType.toUpperCase()) al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_ORTHODONTIC_INVALID_APPLIANCE

**RN-ORTHODONTIC-004**
- Descripción: La duración debe ser positiva si se especifica.
- Condición: treatmentDurationMonths != null && treatmentDurationMonths <= 0 al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_ORTHODONTIC_NEGATIVE_DURATION

**RN-ORTHODONTIC-005**
- Descripción: Alineadores transparentes deben tener duración típica de 12-24 meses.
- Condición: applianceType == "CLEAR_ALIGNERS" && (treatmentDurationMonths < 12 || treatmentDurationMonths > 24) al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_ORTHODONTIC_ATYPICAL_ALIGNER_DURATION

**RN-ORTHODONTIC-006**
- Descripción: Brackets linguales deben tener duración mínima de 18 meses.
- Condición: applianceType == "LINGUAL_BRACKETS" && treatmentDurationMonths < 18 al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_ORTHODONTIC_ATYPICAL_LINGUAL_DURATION

---

## Relación con ADRs
- ADR-02 (Dominio): Implementación de Value Objects con validaciones robustas.
- ADR-05 (Arquitectura): Creación de módulo independiente para Servicios.
- ADR-10 (Arquitectura): Estrategia de persistencia con tablas separadas para detalles.
- ADR-18 (Arquitectura): Simplificación de jerarquía de excepciones.

---

## Value Objects Relacionados

**ServiceType (Enum)**
- Valor para OrthodonticDetails: `ServiceType.ORTHODONTIC`.
- Usado para identificar tipo de detalles en runtime.

**ProvidedService (Agregado Raíz)**
- Contiene OrthodonticDetails mediante composición.
- Valida coherencia entre category="Orthodontics" y details tipo ORTHODONTIC.

---

## Ejemplo de Uso
```java
// Crear detalles de ortodoncia con brackets metálicos
OrthodonticDetails metalBrackets = new OrthodonticDetails(
    "METAL_BRACKETS",
    24, // 24 meses de tratamiento
    true // requiere controles mensuales
);

// Crear detalles con alineadores transparentes
OrthodonticDetails clearAligners = new OrthodonticDetails(
    "CLEAR_ALIGNERS",
    18, // 18 meses de tratamiento
    true // requiere controles cada 2 semanas
);

// Intentar crear con duración inválida (error)
try {
    OrthodonticDetails invalid = new OrthodonticDetails(
        "METAL_BRACKETS",
        3, // ❌ < 6 meses
        true
    );
} catch (IllegalArgumentException e) {
    // Lanza ERR_ORTHODONTIC_INVALID_DURATION
    System.err.println("Duración inválida: " + e.getMessage());
}

// Intentar crear con tipo inválido (error)
try {
    OrthodonticDetails invalid = new OrthodonticDetails(
        "INVISIBLE_BRACES", // ❌ Tipo no reconocido
        24,
        true
    );
} catch (ValueObjectValidationException e) {
    // Lanza ERR_ORTHODONTIC_INVALID_APPLIANCE
System.err.println("Tipo de aparato no reconocido: " + e.getMessage());
}
// Crear con valores opcionales null
OrthodonticDetails minimal = new OrthodonticDetails(
"CERAMIC_BRACKETS",
null, // duración no especificada aún
false // no requiere seguimiento especial
);
// Comparación por igualdad (Value Object)
OrthodonticDetails details1 = new OrthodonticDetails("METAL_BRACKETS", 24, true);
OrthodonticDetails details2 = new OrthodonticDetails("METAL_BRACKETS", 24, true);
assert details1.equals(details2); // true - igualdad por valor
// Reemplazo completo (inmutabilidad)
ProvidedService service = ... // servicio existente
OrthodonticDetails newDetails = new OrthodonticDetails(
"CERAMIC_BRACKETS", // cambio de metálicos a cerámicos
24,
true
);
service.updateDetails(newDetails); // reemplaza completamente

---

## Casos de Uso Clínicos

**Caso 1: Tratamiento Ortodóntico Estándar**
```java
// Paciente adolescente con apiñamiento moderado
OrthodonticDetails standard = new OrthodonticDetails(
    "METAL_BRACKETS",
    24, // 2 años
    true // controles mensuales
);
// Servicio: "Ortodoncia Integral con Brackets Metálicos"
// Citas: 24 controles + visitas emergencias
```

**Caso 2: Tratamiento Estético para Adulto**
```java
// Profesional que requiere estética
OrthodonticDetails aesthetic = new OrthodonticDetails(
    "CLEAR_ALIGNERS",
    18, // 1.5 años
    true // controles cada 2 semanas
);
// Servicio: "Ortodoncia Invisible con Alineadores"
// Citas: ~36 controles (cada 15 días)
```

**Caso 3: Tratamiento Ortopédico Infantil**
```java
// Niño en crecimiento (8-12 años)
OrthodonticDetails orthopedic = new OrthodonticDetails(
    "FUNCTIONAL_APPLIANCES",
    15, // 1.25 años
    true // controles cada 3 semanas
);
// Servicio: "Ortopedia Maxilar con Aparatos Funcionales"
// Objetivo: Guiar crecimiento óseo
```