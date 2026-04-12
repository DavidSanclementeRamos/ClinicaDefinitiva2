# Descubrimiento de Reglas de Negocio por Value Object
## Value Object: ImplantologyDetails (Detalles de Implantología Dental)

## Propósito
Representar información específica de servicios de implantología dental. Este Value Object contiene detalles críticos sobre el tiempo de cicatrización (osteointegración), tipo de implante, sitio de colocación y necesidad de injerto óseo. Asegura que solo procedimientos implantológicos bien definidos, con tiempos de cicatrización realistas y requisitos de injerto apropiados puedan ser creados, protegiendo el éxito del tratamiento y la planificación del caso.

---

## CREACIÓN
- El tiempo de cicatrización (healingTimeMonths) puede ser null (no determinado aún).
- Si se especifica, debe estar entre 2 y 12 meses.
- El tiempo de cicatrización no puede ser negativo.
- El tipo de implante (implantType) puede ser null (genérico).
- El sitio de colocación (placementSite) puede ser null (genérico).
- Debe indicar explícitamente si requiere injerto óseo (requiresBoneGraft).
- requiresBoneGraft nunca puede ser null (se normaliza a false por defecto).
- Todos los campos son inmutables tras creación.

---

## EDICIÓN / ACTUALIZACIÓN
- ImplantologyDetails es un Value Object inmutable.
- No se edita, se reemplaza completamente mediante ProvidedService.updateDetails().
- El reemplazo debe pasar todas las validaciones de creación.
- El nuevo ImplantologyDetails debe seguir siendo coherente con categoría Implantology.

---

## INVARIANTES
- Si healingTimeMonths se especifica, debe ser positivo y <= 12 meses.
- Si requiresBoneGraft es true, healingTimeMonths mínimo debe ser 4 meses.
- requiresBoneGraft se normaliza usando Boolean.TRUE.equals().
- Implantes en maxilar superior típicamente requieren 4-6 meses de cicatrización.
- Implantes en mandíbula típicamente requieren 3-4 meses de cicatrización.
- Implantes con injerto óseo requieren tiempo adicional de cicatrización.
- El tiempo de cicatrización incluye fase de osteointegración completa.

---

## VALIDACIONES ESPECÍFICAS

**Tiempos de Cicatrización Típicos:**
```java
Sin injerto óseo:
- Mandíbula (hueso denso): 3-4 meses
- Maxilar anterior: 4-5 meses
- Maxilar posterior: 5-6 meses

Con injerto óseo:
- Injerto menor (preservación alveolar): 4-6 meses
- Injerto moderado (bloque autólogo): 6-8 meses
- Injerto mayor (elevación seno maxilar): 6-9 meses
- Injerto complejo (reconstrucción): 9-12 meses
```

**Tipos de Implante Comunes:**
```java
implantType (ejemplos):
- "STANDARD_TITANIUM"      // Implante titanio estándar
- "NARROW_DIAMETER"        // Diámetro reducido (espacios estrechos)
- "WIDE_DIAMETER"          // Diámetro ancho (molares)
- "SHORT_IMPLANT"          // Implante corto (hueso limitado)
- "ZIRCONIA_IMPLANT"       // Implante cerámico (estética)
- "MINI_IMPLANT"           // Mini-implante (prótesis removible)
- "ZYGOMATIC_IMPLANT"      // Implante cigomático (maxilar atrófico)
```

**Sitios de Colocación:**
```java
placementSite (notación FDI):
- "11" // Incisivo central superior derecho
- "21" // Incisivo central superior izquierdo
- "36" // Primer molar inferior izquierdo
- "46" // Primer molar inferior derecho
- "ANTERIOR_MAXILLA" // Región anterior superior
- "POSTERIOR_MANDIBLE" // Región posterior inferior
```

**Tipos de Injerto Óseo:**
```java
Cuando requiresBoneGraft = true:
- Injerto autólogo (del propio paciente)
- Injerto alógeno (banco de huesos)
- Injerto xenoinjerto (origen animal, ej. hueso bovino)
- Injerto sintético (hidroxiapatita, β-TCP)
- Elevación de seno maxilar (lateral o crestal)
- Regeneración ósea guiada (GBR/ROG)
```

---

## TRAZABILIDAD Y AUDITORÍA
- Cada instancia de ImplantologyDetails es inmutable.
- Cambios se auditan a nivel de ProvidedService (ServiceDetailsChanged event).
- Se registra tiempo de cicatrización anterior y nuevo al reemplazar detalles.
- Sistema emite alertas si tiempo de cicatrización es atípico.
- Se registra sitio de colocación para planificación quirúrgica.
- Se audita necesidad de injerto óseo para coordinación con banco de tejidos.

---

## Justificación Semántica
Estas reglas protegen el éxito del tratamiento implantológico asegurando tiempos de cicatrización adecuados, evitan planificación incorrecta de carga protésica prematura, garantizan coherencia entre injerto óseo y tiempo de espera, facilitan coordinación con laboratorio para prótesis sobre implantes, protegen contra fracasos por osteointegración insuficiente, y aseguran información completa para consentimiento informado del paciente.

---

## Reglas Descubiertas (formato estandarizado)

**RN-IMPLANTOLOGY-001**
- Descripción: El tiempo de cicatrización debe estar entre 2 y 12 meses si se especifica.
- Condición: healingTimeMonths != null && (healingTimeMonths < 2 || healingTimeMonths > 12) al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_IMPLANTOLOGY_INVALID_HEALING_TIME

**RN-IMPLANTOLOGY-002**
- Descripción: Si requiere injerto óseo, el tiempo de cicatrización mínimo es 4 meses.
- Condición: requiresBoneGraft == true && healingTimeMonths != null && healingTimeMonths < 4 al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH

**RN-IMPLANTOLOGY-003**
- Descripción: El tiempo de cicatrización no puede ser negativo.
- Condición: healingTimeMonths != null && healingTimeMonths < 0 al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME

**RN-IMPLANTOLOGY-004**
- Descripción: Tiempos de cicatrización muy cortos (< 3 meses) sin injerto son atípicos.
- Condición: requiresBoneGraft == false && healingTimeMonths != null && healingTimeMonths < 3 al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_IMPLANTOLOGY_SHORT_HEALING_TIME

**RN-IMPLANTOLOGY-005**
- Descripción: Tiempos de cicatrización muy largos (> 9 meses) sin injerto complejo son atípicos.
- Condición: requiresBoneGraft == false && healingTimeMonths != null && healingTimeMonths > 9 al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_IMPLANTOLOGY_LONG_HEALING_TIME

**RN-IMPLANTOLOGY-006**
- Descripción: Implantes zigomáticos requieren tiempo de cicatrización extendido.
- Condición: implantType == "ZYGOMATIC_IMPLANT" && healingTimeMonths < 6 al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_IMPLANTOLOGY_ZYGOMATIC_SHORT_HEALING

**RN-IMPLANTOLOGY-007**
- Descripción: El sitio de colocación debe tener formato válido si se especifica.
- Condición: placementSite != null && placementSite.length() < 2 al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE

---

## Relación con ADRs
- ADR-02 (Dominio): Implementación de Value Objects con validaciones robustas.
- ADR-05 (Arquitectura): Creación de módulo independiente para Servicios.
- ADR-10 (Arquitectura): Estrategia de persistencia con tablas separadas para detalles.
- ADR-18 (Arquitectura): Simplificación de jerarquía de excepciones.

---

## Value Objects Relacionados

**ServiceType (Enum)**
- Valor para ImplantologyDetails: `ServiceType.IMPLANTOLOGY`.
- Usado para identificar tipo de detalles en runtime.

**ProvidedService (Agregado Raíz)**
- Contiene ImplantologyDetails mediante composición.
- Valida coherencia entre category="Implantology" y details tipo IMPLANTOLOGY.

---

## Ejemplo de Uso
```java
// Crear detalles de implante estándar sin injerto
ImplantologyDetails standardImplant = new ImplantologyDetails(
    4, // 4 meses de cicatrización
    "STANDARD_TITANIUM",
    "36", // Primer molar inferior izquierdo (FDI)
    false // sin injerto óseo
);

// Crear detalles de implante con elevación de seno maxilar
ImplantologyDetails sinusLift = new ImplantologyDetails(
    6, // 6 meses por injerto óseo
    "STANDARD_TITANIUM",
    "16", // Primer molar superior derecho (FDI)
    true // requiere elevación de seno (injerto)
);

// Crear detalles de implante zigomático (caso complejo)
ImplantologyDetails zygomaticImplant = new ImplantologyDetails(
    8, // 8 meses (caso complejo)
    "ZYGOMATIC_IMPLANT",
    "POSTERIOR_MAXILLA",
    true // con injerto óseo complementario
);

// Crear detalles de mini-implante para prótesis removible
ImplantologyDetails miniImplant = new ImplantologyDetails(
    3, // 3 meses (carga rápida)
    "MINI_IMPLANT",
    "ANTERIOR_MANDIBLE",
    false // sin injerto
);

// Intentar crear con tiempo inválido (error)
try {
    ImplantologyDetails invalid = new ImplantologyDetails(
        1, // ❌ < 2 meses
        "STANDARD_TITANIUM",
        "46",
        false
    );
} catch (ValueObjectValidationException e) {
    // Lanza ERR_IMPLANTOLOGY_INVALID_HEALING_TIME
    System.err.println("Tiempo de cicatrización muy corto: " + e.getMessage());
}

// Intentar crear con injerto pero tiempo insuficiente (error)
try {
    ImplantologyDetails invalid = new ImplantologyDetails(
        3, // ❌ < 4 meses con injerto
        "STANDARD_TITANIUM",
        "21",
        true // requiere injerto
    );
} catch (ValueObjectValidationException e) {
    // Lanza ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH
    System.err.println("Injerto óseo requiere más tiempo: " + e.getMessage());
}

// Crear sin especificar tiempo (planificación pendiente)
ImplantologyDetails pending = new ImplantologyDetails(
    null, // tiempo a determinar según evaluación radiográfica
    "STANDARD_TITANIUM",
    "11",
    false
);
```

---

## Casos de Uso Clínicos

**Caso 1: Implante Unitario Mandibular (Sin Injerto)**
```java
// Implante en zona posterior mandibular con hueso denso
ImplantologyDetails posteriorImplant = new ImplantologyDetails(
    3, // 3 meses (hueso denso tipo I-II)
    "STANDARD_TITANIUM",
    "46", // Primer molar inferior derecho
    false
);

// Asociado a ProvidedService:
// - duration: 60 minutos (cirugía)
// - baseRate: $1,200,000 COP
// - requiresAuthorization: true
// - Proceso: Cirugía → Espera 3 meses → Corona definitiva
// - Éxito esperado: 95-98%
```

**Caso 2: Implante Anterior con Preservación Alveolar**
```java
// Implante en zona estética con injerto menor
ImplantologyDetails anteriorImplant = new ImplantologyDetails(
    5, // 5 meses (injerto + zona estética)
    "STANDARD_TITANIUM",
    "21", // Incisivo central superior izquierdo
    true // injerto de preservación alveolar
);

// Asociado a ProvidedService:
// - duration: 90 minutos (cirugía + injerto)
// - baseRate: $1,800,000 COP (implante + injerto)
// - requiresAuthorization: true
// - Proceso: Extracción + injerto → Espera 5 meses → Implante + provisional → Corona definitiva
// - Consideraciones: Estética crítica, provisional inmediato
```

**Caso 3: Elevación de Seno Maxilar con Implante Simultáneo**
```java
// Procedimiento complejo en maxilar posterior atrófico
ImplantologyDetails sinusLiftImplant = new ImplantologyDetails(
    6, // 6 meses (elevación seno lateral)
    "STANDARD_TITANIUM",
    "26", // Primer molar superior izquierdo
    true // elevación de seno maxilar
);

// Asociado a ProvidedService:
// - duration: 120 minutos (cirugía compleja)
// - baseRate: $2,500,000 COP (implante + elevación seno)
// - requiresAuthorization: true
// - Equipo: Cirujano especializado, material injerto (xenoinjerto)
// - Proceso: Elevación seno + implante → Espera 6 meses → Corona definitiva
// - Riesgo: Perforación membrana Schneider
```

**Caso 4: Implantes Zigomáticos (Maxilar Atrófico Severo)**
```java
// Alternativa a injertos extensos en maxilar atrófico
ImplantologyDetails zygomaticCase = new ImplantologyDetails(
    8, // 8 meses (caso complejo)
    "ZYGOMATIC_IMPLANT",
    "POSTERIOR_MAXILLA", // Región posterior bilateral
    true // injertos complementarios
);

// Asociado a ProvidedService:
// - duration: 240 minutos (cirugía mayor)
// - baseRate: $8,000,000 COP (par de implantes zigomáticos)
// - requiresAuthorization: true
// - Equipo: Cirujano maxilofacial especializado
// - Proceso: Cirugía zigomáticos → Espera 8 meses → Prótesis fija atornillada
// - Indicación: Maxilar superior edéntulo con atrofia severa
// - Ventaja: Evita injertos óseos extensos
```

**Caso 5: Mini-Implantes para Prótesis Removible**
```java
// Retención de prótesis total inferior
ImplantologyDetails miniImplantsOverdenture = new ImplantologyDetails(
    3, // 3 meses (carga relativamente rápida)
    "MINI_IMPLANT",
    "ANTERIOR_MANDIBLE", // 4 mini-implantes región anterior
    false // sin injerto (hueso remanente suficiente)
);

// Asociado a ProvidedService:
// - duration: 60 minutos (4 mini-implantes)
// - baseRate: $2,000,000 COP (4 mini-implantes)
// - requiresAuthorization: true
// - Proceso: Cirugía → Espera 3 meses → Adaptación prótesis con aditamentos
// - Ventaja: Procedimiento menos invasivo, costo menor
// - Indicación: Pacientes con limitaciones para implantes estándar
```