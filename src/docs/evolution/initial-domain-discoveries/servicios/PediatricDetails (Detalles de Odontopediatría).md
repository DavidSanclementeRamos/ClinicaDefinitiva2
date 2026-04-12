# Descubrimiento de Reglas de Negocio por Value Object
## Value Object: PediatricDetails (Detalles de Odontopediatría)

## Propósito
Representar información específica de servicios de odontopediatría. Este Value Object contiene detalles sobre el rango de edad apropiado, técnicas de manejo conductual y materiales específicos para pacientes pediátricos. Asegura que solo tratamientos odontopediátricos bien definidos, con rangos de edad apropiados y técnicas de manejo validadas puedan ser creados, protegiendo la experiencia del paciente infantil y la efectividad del tratamiento.

---

## CREACIÓN
- El rango de edad (ageRange) puede ser null (aplicable a cualquier edad pediátrica).
- Si se especifica, debe indicar edades entre 0 y 18 años.
- El rango debe seguir formato "X-Y años" o descriptivo válido.
- La técnica de manejo conductual (behaviorManagement) puede ser null (estándar).
- Los materiales pediátricos (pediatricMaterials) pueden ser null (materiales estándar).
- Todos los campos son inmutables tras creación.

---

## EDICIÓN / ACTUALIZACIÓN
- PediatricDetails es un Value Object inmutable.
- No se edita, se reemplaza completamente mediante ProvidedService.updateDetails().
- El reemplazo debe pasar todas las validaciones de creación.
- El nuevo PediatricDetails debe seguir siendo coherente con categoría Pediatrics.

---

## INVARIANTES
- ageRange debe especificar edades pediátricas (0-18 años) si se define.
- ageRange puede usar descriptores: "Bebés", "Preescolares", "Escolares", "Adolescentes".
- behaviorManagement debe ser técnica reconocida o null.
- pediatricMaterials debe listar materiales apropiados para niños o null.
- Sellantes se aplican típicamente en niños de 6-14 años (primeros molares permanentes).
- Fluorización es preventiva y aplicable desde erupción dental (6 meses-18 años).
- Materiales deben ser biocompatibles y aprobados para uso pediátrico.

---

## VALIDACIONES ESPECÍFICAS

**Rangos de Edad Pediátricos:**
```java
ageRange (ejemplos válidos):
- "0-3 años"     // Bebés y primera infancia
- "3-6 años"     // Preescolares
- "6-12 años"    // Escolares
- "12-18 años"   // Adolescentes
- "6-14 años"    // Rango típico para sellantes
- "Todas las edades pediátricas" // 0-18 años
- null           // Aplicable a cualquier edad infantil

Descriptores válidos:
- "Bebés"        // 0-3 años
- "Preescolares" // 3-6 años
- "Escolares"    // 6-12 años
- "Adolescentes" // 12-18 años
```

**Técnicas de Manejo Conductual:**
```java
behaviorManagement (ejemplos):
- "Decir-Mostrar-Hacer"           // Tell-Show-Do (estándar)
- "Refuerzo positivo"             // Elogios y recompensas
- "Distracción"                   // Juegos, música, videos
- "Control de voz"                // Modulación tono y volumen
- "Modelado"                      // Observar otro niño
- "Desensibilización gradual"     // Exposición progresiva
- "Sedación consciente"           // Óxido nitroso (casos complejos)
- "Contención física benevolente" // Solo emergencias (último recurso)
- null                            // Técnicas estándar
```

**Materiales Pediátricos:**
```java
pediatricMaterials (ejemplos):
- "Ionómero de vidrio"            // Cementación temporal
- "Resina fotopolimerizable"      // Restauraciones estéticas
- "Sellantes de fosas y fisuras"  // Prevención caries
- "Flúor barniz"                  // Fluorización tópica
- "Pasta profiláctica sabor"      // Limpieza con sabor agradable
- "Anestesia tópica sabor"        // Reducir molestia infiltración
- "Coronas de acero cromado"      // Restauraciones extensas
- "Pulpotomía con MTA"           // Material biocompatible
- null                            // Materiales estándar
```

**Procedimientos Típicos por Edad:**
```java
0-3 años (Bebés):
- Primera visita dental (habituación)
- Fluorización preventiva
- Educación higiene a padres

3-6 años (Preescolares):
- Limpieza y fluorización
- Restauraciones dentición temporal
- Manejo de caries temprana
- Aplicación de sellantes (molares temporales)

6-12 años (Escolares):
- Sellantes de fosas y fisuras (molares permanentes)
- Ortodoncia interceptiva
- Restauraciones dentición mixta
- Educación higiene personal

12-18 años (Adolescentes):
- Ortodoncia correctiva
- Restauraciones estéticas
- Manejo trauma dental (deportes)
- Extracción terceros molares (cordales)
```

---

## TRAZABILIDAD Y AUDITORÍA
- Cada instancia de PediatricDetails es inmutable.
- Cambios se auditan a nivel de ProvidedService (ServiceDetailsChanged event).
- Se registra rango de edad anterior y nuevo al reemplazar detalles.
- Sistema emite alertas si ageRange especifica edades > 18 años.
- Se registra técnica de manejo conductual para evaluación de efectividad.
- Se documenta consentimiento de padres/tutores para procedimientos.

---

## Justificación Semántica
Estas reglas protegen la experiencia del paciente pediátrico asegurando técnicas de manejo apropiadas, evitan aplicación de procedimientos en edades inadecuadas (ej. sellantes antes de erupción dental), garantizan uso de materiales biocompatibles y aprobados para niños, facilitan planificación de citas con duración apropiada para atención infantil, protegen legalmente documentando consentimiento de padres, y aseguran trazabilidad de técnicas conductuales efectivas.

---

## Reglas Descubiertas (formato estandarizado)

**RN-PEDIATRIC-001**
- Descripción: El rango de edad debe especificar edades pediátricas válidas (0-18 años) si se define.
- Condición: ageRange especifica edades > 18 o formato inválido al invocar constructor.
- Consecuencia: Se rechaza creación y se registra Outcome.
- Error asociado: ERR_PEDIATRIC_INVALID_AGE_RANGE

**RN-PEDIATRIC-002**
- Descripción: El rango de edad debe tener formato válido si se especifica.
- Condición: ageRange != null && ageRange.length() < 5 al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_PEDIATRIC_AGE_RANGE_TOO_SHORT

**RN-PEDIATRIC-003**
- Descripción: Sellantes deben indicar rango de edad apropiado (6-14 años típicamente).
- Condición: pediatricMaterials.contains("sellante") && ageRange especifica < 6 años al invocar constructor.
- Consecuencia: Se emite advertencia (warning) pero no se rechaza creación.
- Error asociado: WARN_PEDIATRIC_SEALANT_AGE_MISMATCH

**RN-PEDIATRIC-004**
- Descripción: Técnicas de sedación consciente deben documentarse explícitamente.
- Condición: behaviorManagement.contains("sedación") && !behaviorManagement.contains("consciente") al invocar constructor.
- Consecuencia: Se emite advertencia (warning) sugeriendo especificar tipo.
- Error asociado: WARN_PEDIATRIC_SEDATION_UNSPECIFIED

**RN-PEDIATRIC-005**
- Descripción: Contención física solo debe usarse en emergencias.
- Condición: behaviorManagement.contains("contención física") al invocar constructor.
- Consecuencia: Se emite advertencia crítica (warning) recordando restricciones éticas.
- Error asociado: WARN_PEDIATRIC_PHYSICAL_RESTRAINT

**RN-PEDIATRIC-006**
- Descripción: Materiales deben ser apropiados para edad si se especifican.
- Condición: pediatricMaterials != null && pediatricMaterials.length() < 5 al invocar constructor.
- Consecuencia: Se rechaza creación y se lanza IllegalArgumentException.
- Error asociado: ERR_PEDIATRIC_MATERIALS_TOO_SHORT

**RN-PEDIATRIC-007**
- Descripción: Bebés (0-3 años) requieren técnicas de manejo específicas.
- Condición: ageRange.contains("0-3") && behaviorManagement == null al invocar constructor.
- Consecuencia: Se emite advertencia (warning) sugeriendo especificar técnica.
- Error asociado: WARN_PEDIATRIC_INFANT_MANAGEMENT_MISSING

---

## Relación con ADRs
- ADR-02 (Dominio): Implementación de Value Objects con validaciones robustas.
- ADR-05 (Arquitectura): Creación de módulo independiente para Servicios.
- ADR-10 (Arquitectura): Estrategia de persistencia con tablas separadas para detalles.
- ADR-18 (Arquitectura): Simplificación de jerarquía de excepciones.

---

## Value Objects Relacionados

**ServiceType (Enum)**
- Valor para PediatricDetails: `ServiceType.PEDIATRICS`.
- Usado para identificar tipo de detalles en runtime.

**ProvidedService (Agregado Raíz)**
- Contiene PediatricDetails mediante composición.
- Valida coherencia entre category="Pediatrics" y details tipo PEDIATRICS.

---

## Ejemplo de Uso
```java
// Crear detalles de sellantes de fosas y fisuras
PediatricDetails sealants = new PediatricDetails(
    "6-14 años",
    "Decir-Mostrar-Hacer",
    "Sellantes de resina fotopolimerizable en molares permanentes"
);

// Crear detalles de fluorización preventiva
PediatricDetails fluoride = new PediatricDetails(
    "Todas las edades pediátricas",
    "Refuerzo positivo",
    "Flúor barniz de aplicación tópica con sabor agradable"
);

// Crear detalles de tratamiento con sedación consciente
PediatricDetails sedation = new PediatricDetails(
    "3-10 años",
    "Sedación consciente con óxido nitroso",
    "Anestesia tópica sabor, óxido nitroso 30%, monitoreo continuo"
);

// Crear detalles de primera visita dental (bebé)
PediatricDetails firstVisit = new PediatricDetails(
    "0-3 años",
    "Técnica de vuelta en rodilla (knee-to-knee)",
    "Examen visual, educación higiene oral a padres"
);

// Crear detalles de restauración en preescolar
PediatricDetails restoration = new PediatricDetails(
    "3-6 años",
    "Distracción con videos y música",
    "Ionómero de vidrio para restauración temporal en molares deciduos"
);

// Intentar crear con edad inválida (error)
try {
    PediatricDetails invalid = new PediatricDetails(
        "18-25 años", // ❌ Fuera de rango pediátrico
        "Estándar",
        "Materiales estándar"
    );
} catch (ValueObjectValidationException e) {
    // Lanza ERR_PEDIATRIC_INVALID_AGE_RANGE
    System.err.println("Rango de edad no pediátrico: " + e.getMessage());
}

// Crear con valores genéricos (null)
PediatricDetails generic = new PediatricDetails(
    null, // aplicable a cualquier edad pediátrica
    null, // técnicas estándar de manejo
    null  // materiales estándar
);
```

---

## Casos de Uso Clínicos

**Caso 1: Sellantes de Fosas y Fisuras (Prevención)**
```java
// Procedimiento preventivo estándar en escolares
PediatricDetails preventiveSealants = new PediatricDetails(
    "6-14 años",
    "Decir-Mostrar-Hacer con refuerzo positivo",
    "Sellantes de resina fotopolimerizable en primeros y segundos molares permanentes. Aplicación con técnica de aislamiento absoluto."
);

// Asociado a ProvidedService:
// - duration: 30 minutos (4 molares)
// - baseRate: $200,000 COP
// - requiresAuthorization: false
// - Indicación: Molares permanentes recién erupcionados con surcos profundos
// - Efectividad: Reducción 80% caries en primeros 2 años
// - Mantenimiento: Revisión anual, reaplicación si necesario
```

**Caso 2: Fluorización Tópica (Bebés y Preescolares)**
```java
// Aplicación preventiva desde erupción dental
PediatricDetails topicalFluoride = new PediatricDetails(
    "6 meses - 6 años",
    "Técnica de vuelta en rodilla para bebés, Decir-Mostrar-Hacer para preescolares",
    "Flúor barniz 5% NaF con sabor agradable (fresa, uva). Aplicación cada 3-6 meses según riesgo de caries."
);

// Asociado a ProvidedService:
// - duration: 15 minutos
// - baseRate: $80,000 COP
// - requiresAuthorization: false
// - Beneficio: Fortalecimiento esmalte, prevención caries temprana
// - Instrucción: No comer/beber 30 minutos post-aplicación
// - Frecuencia: Cada 3 meses (alto riesgo), cada 6 meses (bajo riesgo)
```

**Caso 3: Pulpotomía en Molar Temporal (Preescolar)**
```java
// Tratamiento endodóntico conservador
PediatricDetails pulpotomy = new PediatricDetails(
    "3-8 años",
    "Sedación consciente con óxido nitroso + Decir-Mostrar-Hacer",
    "Pulpotomía con MTA (agregado de trióxido mineral), corona de acero cromado en molar temporal con caries profunda"
);

// Asociado a ProvidedService:
// - duration: 60 minutos
// - baseRate: $350,000 COP
// - requiresAuthorization: true
// - Indicación: Caries profunda en molar temporal sin afectación periapical
// - Material: MTA (biocompatible, induce formación dentina)
// - Pronóstico: 90% éxito hasta exfoliación natural
// - Alternativa: Extracción + mantenedor de espacio
```

**Caso 4: Manejo de Ansiedad Dental (Primera Visita Negativa)**
```java
// Desensibilización para niño con experiencia traumática previa
PediatricDetails anxietyManagement = new PediatricDetails(
    "4-10 años",
    "Desensibilización gradual con modelado y refuerzo positivo. Visitas cortas progresivas sin procedimientos invasivos inicialmente.",
    "Pasta profiláctica sabor agradable, cepillo eléctrico pediátrico, premios no alimentarios"
);

// Asociado a ProvidedService:
// - duration: 20 minutos (visita habituación)
// - baseRate: $0 COP (cortesía) o incluido en plan
// - requiresAuthorization: false
// - Objetivo: Crear experiencia positiva, reducir ansiedad
// - Protocolo: 2-3 visitas de habituación antes de tratamiento
// - Involucrar: Padres como apoyo, nunca como coacción
```

**Caso 5: Tratamiento Restaurador con Sedación Consciente**
```java
// Múltiples caries en niño ansioso no cooperador
PediatricDetails sedationTreatment = new PediatricDetails(
    "3-7 años",
    "Sedación consciente con óxido nitroso 30-50% + técnicas de distracción (tablet con videos)",
    "Restauraciones múltiples con resina fotopolimerizable y coronas de acero cromado. Monitoreo continuo de signos vitales."
);

// Asociado a ProvidedService:
// - duration: 90-120 minutos
// - baseRate: $800,000 COP (sedación + restauraciones)
// - requiresAuthorization: true
// - Requisitos: Ayuno previo 4 horas, consentimiento padres
// - Equipo: Odontopediatra certificado en sedación, asistente
// - Monitoreo: Oxímetro de pulso, presión arterial
// - Indicaciones: Niño muy ansioso, múltiples tratamientos necesarios
// - Contraindicaciones: Infección respiratoria activa, alergias
```