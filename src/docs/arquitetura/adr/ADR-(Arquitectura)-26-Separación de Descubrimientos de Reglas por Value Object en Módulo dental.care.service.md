# ADR-26 (Arquitectura): Separación de Descubrimientos de Reglas por Value Object en Módulo Servicios

**Estado:** ✅ Aceptado  
**Fecha:** Enero 01, 2026  
**Contexto:** Documentación de reglas de negocio en módulo Services  
**Autor:** David Stiven Sanclemente

---

## Contexto y Problema

El módulo **dental.care.services** presenta una singularidad arquitectónica respecto al módulo **Actor**:

### **Módulo Actor (Patrón Estándar)**
```
📂 actor/
├── Dentist.java          ← Agregado (entidad persistida)
├── Patient.java          ← Agregado (entidad persistida)
├── Guardian.java         ← Agregado (entidad persistida)
└── Receptionist.java     ← Agregado (entidad persistida)

📂 docs/descubrimientos/actor/
├── Dentist(odontologo).md
├── Patient(Paciente).md
├── Guardian(Responsable).md
└── Receptionist(Secretario).md
```
**1 archivo de descubrimiento = 1 agregado persistido**

---

### **Módulo dental.care.services (Caso Excepcional)**
```
📂 dental.care.services/
├── ProvidedService.java       ← Agregado raíz (persistido)
├── OrthodonticDetails.java    ← Value Object (persistido en tabla separada)
├── SurgicalDetails.java       ← Value Object (persistido en tabla separada)
├── ProstheticDetails.java     ← Value Object (persistido en tabla separada)
├── ImplantologyDetails.java   ← Value Object (persistido en tabla separada)
├── AestheticDetails.java      ← Value Object (persistido en tabla separada)
└── PediatricDetails.java      ← Value Object (persistido en tabla separada)
```

**Pregunta crítica:**
> ¿Debe haber 1 archivo de descubrimiento (solo ProvidedService) o 7 archivos (1 por cada tipo de detalle)?

---

## Decisión

**Se crearán 7 archivos separados de descubrimiento de reglas**, uno por cada clase persistida, incluyendo los Value Objects complejos:

```
📂 docs/descubrimientos/servicios/
├── ProvidedService(ServicioPrestado).md         ← Agregado raíz (15 reglas generales)
├── OrthodonticDetails(DetallesOrtodoncia).md    ← VO persistido (7 reglas)
├── SurgicalDetails(DetallesCirugia).md          ← VO persistido (7 reglas)
├── ProstheticDetails(DetallesProtesis).md       ← VO persistido (7 reglas)
├── ImplantologyDetails(DetallesImplantes).md    ← VO persistido (7 reglas)
├── AestheticDetails(DetallesEstetica).md        ← VO persistido (7 reglas)
└── PediatricDetails(DetallesPediatria).md       ← VO persistido (7 reglas)
```

---

## Justificación Técnica

### **1. Persistencia Independiente (ADR-10)**

Según ADR-10, cada tipo de detalle se persiste en **tabla separada** con estrategia **one-to-one**:

```sql
-- Tabla principal
CREATE TABLE provided_service (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    base_rate DECIMAL,
    -- ... campos comunes
);

-- Tablas de detalles (one-to-one)
CREATE TABLE provided_service_orthodontic (
    id UUID PRIMARY KEY,
    appliance_type VARCHAR(100),
    treatment_duration_months INT,
    requires_followup BOOLEAN,
    FOREIGN KEY (id) REFERENCES provided_service(id)
);

CREATE TABLE provided_service_surgical (
    id UUID PRIMARY KEY,
    surgery_type VARCHAR(255),
    complexity_level VARCHAR(50),
    requires_anesthesia BOOLEAN,
    operating_room_needed BOOLEAN,
    FOREIGN KEY (id) REFERENCES provided_service(id)
);

-- ... 5 tablas más
```

**Consecuencia:**
- Cada tabla puede ser **consultada independientemente**
- Cada tabla tiene **índices específicos** para queries optimizadas
- Cada tabla evoluciona **independientemente**

**Por lo tanto:** Cada tabla merece su propio archivo de descubrimiento.

---

### **2. Value Objects con Identidad Persistida**

Aunque técnicamente son Value Objects (inmutables, comparación por valor), estos objetos:

✅ **Tienen identidad persistida** (PK en BD)  
✅ **Tienen ciclo de vida independiente** (se crean/actualizan separadamente)  
✅ **Tienen reglas de negocio complejas** (no son simples wrappers)  
✅ **Tienen validaciones cruzadas** (ej: anestesia vs complejidad)

**Comparación:**

| Aspecto | VO Simple (Age, Email) | VO Complejo (OrthodonticDetails) |
|---------|------------------------|----------------------------------|
| **Persistencia** | Embebido en tabla padre | Tabla separada (one-to-one) |
| **Reglas** | 2-3 validaciones básicas | 7+ reglas de negocio |
| **Tamaño** | 1-50 líneas código | 100-200 líneas código |
| **Consultas** | Solo via agregado | Queries directas posibles |
| **Evolución** | Cambios simples | Requiere migraciones DDL |

**Conclusión:** Los `*Details` son **Value Objects estratégicos** que merecen documentación separada.

---

### **3. Complejidad de Reglas por Tipo**

**Estadísticas de reglas descubiertas:**

| Clase | Reglas Generales | Reglas Específicas | Total |
|-------|------------------|-------------------|-------|
| ProvidedService | 15 | 0 | 15 |
| OrthodonticDetails | 0 | 7 | 7 |
| SurgicalDetails | 0 | 7 | 7 |
| ProstheticDetails | 0 | 7 | 7 |
| ImplantologyDetails | 0 | 7 | 7 |
| AestheticDetails | 0 | 7 | 7 |
| PediatricDetails | 0 | 7 | 7 |
| **TOTAL** | **15** | **42** | **57** |

**Si hubiera 1 solo archivo:**
- ❌ 57 reglas en un único documento (inmanejable)
- ❌ Navegación confusa entre tipos
- ❌ Conflictos en Git al editar (todos tocan mismo archivo)
- ❌ Dificultad para encontrar reglas específicas

**Con 7 archivos separados:**
- ✅ Cada archivo tiene 7-15 reglas (tamaño manejable)
- ✅ Búsqueda directa: "Reglas de ortodoncia" → archivo específico
- ✅ Ediciones paralelas sin conflictos
- ✅ Consistencia con patrón del proyecto

---

### **4. Consistencia Arquitectónica**

**Principio del proyecto:**
> "1 archivo de descubrimiento = 1 unidad de persistencia con reglas propias"

**Aplicación:**
```
Actor Module:
- Dentist (persistido) → Dentist.md ✅
- Patient (persistido) → Patient.md ✅

Services Module:
- ProvidedService (persistido) → ProvidedService.md ✅
- OrthodonticDetails (persistido) → OrthodonticDetails.md ✅
- SurgicalDetails (persistido) → SurgicalDetails.md ✅
```

**Alternativa rechazada:**
```
❌ Services Module:
   - ProvidedService (persistido) → ProvidedService.md
   - OrthodonticDetails (persistido) → (sin archivo)
   - SurgicalDetails (persistido) → (sin archivo)
```
**Problema:** Viola principio de documentación exhaustiva.

---

### **5. Escalabilidad y Mantenimiento**

**Escenario futuro:** Agregar "Endodoncia" como nueva especialidad.

**Con 7 archivos (decisión actual):**
```diff
📂 docs/descubrimientos/servicios/
  ├── ProvidedService.md
  ├── OrthodonticDetails.md
  ├── SurgicalDetails.md
+ └── EndodonticDetails.md        ← Nuevo archivo
```
**Proceso:**
1. Crear `EndodonticDetails.java`
2. Crear tabla `provided_service_endodontic`
3. Crear `EndodonticDetails.md` con reglas
4. **Total cambios:** 1 archivo nuevo (bajo impacto)

---

**Con 1 archivo único (alternativa rechazada):**
```diff
📂 docs/descubrimientos/servicios/
- └── ProvidedService.md         ← Modificar archivo existente
```
**Proceso:**
1. Crear `EndodonticDetails.java`
2. Crear tabla `provided_service_endodontic`
3. Editar `ProvidedService.md` agregando sección "Endodoncia"
4. **Total cambios:** Archivo con 57 reglas ahora tiene 64 (complejidad creciente)
5. **Riesgo:** Conflictos Git si otro developer está editando ortodoncia

---

### **6. Exhibición Profesional**

Este proyecto se presenta para **entrevistas técnicas**. Los evaluadores buscarán:

✅ **Organización clara**
```
Evaluador: "Muéstrame las reglas de cirugía oral"
Candidato: "Aquí está SurgicalDetails.md"
Tiempo: 5 segundos
```

❌ **Documentación monolítica**
```
Evaluador: "Muéstrame las reglas de cirugía oral"
Candidato: "Están en ProvidedService.md... déjame buscar... línea 347-389"
Tiempo: 45 segundos (mala impresión)
```

---

## Alternativas Consideradas

### **Alternativa A: 1 Archivo Único** ❌

**Estructura:**
```markdown
# ProvidedService.md
## Reglas Generales (15)
## Reglas de Ortodoncia (7)
## Reglas de Cirugía (7)
## Reglas de Prótesis (7)
## Reglas de Implantología (7)
## Reglas de Estética (7)
## Reglas de Pediatría (7)
```

**Problemas:**
- 57 reglas en 1 archivo → Difícil navegación
- Sección "Ortodoncia" en línea 200 → Búsqueda lenta
- Conflictos Git frecuentes
- No escala (agregar endodoncia = archivo más grande)

---

### **Alternativa B: Agrupación por Complejidad** ❌

**Estructura:**
```
📂 docs/descubrimientos/servicios/
├── ProvidedService.md              ← Agregado raíz
└── ServiceDetails-AllTypes.md      ← Todos los VOs juntos
```

**Problemas:**
- Mezcla 6 tipos diferentes en 1 archivo → Confuso
- "AllTypes.md" con 42 reglas → Tamaño subóptimo
- No respeta principio de separación

---

### **Alternativa C: Separación Total (7 archivos)** ✅ **ELEGIDA**

**Estructura:**
```
📂 docs/descubrimientos/servicios/
├── ProvidedService.md              (15 reglas)
├── OrthodonticDetails.md           (7 reglas)
├── SurgicalDetails.md              (7 reglas)
├── ProstheticDetails.md            (7 reglas)
├── ImplantologyDetails.md          (7 reglas)
├── AestheticDetails.md             (7 reglas)
└── PediatricDetails.md             (7 reglas)
```

**Ventajas:**
- ✅ Navegación directa por tipo
- ✅ Archivos manejables (7-15 reglas cada uno)
- ✅ Ediciones paralelas sin conflictos
- ✅ Escalabilidad probada
- ✅ Consistencia con Actor module

---

## Consecuencias

### **Positivas** ✅

1. **Navegación optimizada**
    - Búsqueda directa: "Ortodoncia" → 1 archivo específico
    - Evaluadores en entrevistas encuentran información rápido

2. **Mantenimiento aislado**
    - Cambio en reglas de cirugía no afecta ortodoncia
    - Menor superficie de conflictos Git

3. **Escalabilidad comprobada**
    - Agregar nueva especialidad = 1 archivo nuevo
    - Sin crecimiento descontrolado de archivos existentes

4. **Profesionalismo arquitectónico**
    - Demuestra comprensión de separación de concerns
    - Consistencia con estándares del proyecto

5. **Trazabilidad clara**
    - Git blame por archivo específico
    - Historial de cambios aislado por tipo

---

### **Negativas / Trade-offs** ⚠️

1. **Mayor número de archivos**
    - 7 archivos vs 1 archivo
    - **Mitigación:** Estructura de carpetas clara

2. **Posible duplicación de contexto**
    - Cada archivo repite "Propósito", "Relación con ADRs"
    - **Mitigación:** Templates estandarizados

3. **Requiere disciplina**
    - Developer debe saber en qué archivo editar
    - **Mitigación:** Naming convention claro (tipo en nombre archivo)

---

## Decisión de Diseño: Excepción Justificada

**Pregunta:** ¿Rompe este módulo el principio "1 archivo = 1 agregado"?

**Respuesta:** No, lo **extiende correctamente**:

**Principio original:**
```
1 archivo de descubrimiento = 1 agregado persistido con reglas propias
```

**Extensión para Services:**
```
1 archivo de descubrimiento = 1 unidad de persistencia con reglas propias

Donde "unidad de persistencia" puede ser:
- Agregado raíz (ProvidedService)
- Value Object estratégico persistido independientemente (OrthodonticDetails)
```

**Justificación:**
- Los `*Details` **NO son VOs triviales** (Age, Email)
- Los `*Details` **SÍ tienen persistencia independiente** (tabla propia)
- Los `*Details` **SÍ tienen reglas complejas** (7+ reglas cada uno)

---

## Implementación

### **Estructura Final**
```
📂 com.example.ClinicaDefinitiva.domain.dental.care.services/
├── model/
│   ├── ProvidedService.java           ← Agregado raíz
│   ├── AestheticDetails.java          ← VO estratégico
│   ├── ImplantologyDetails.java       ← VO estratégico
│   ├── OrthodonticDetails.java        ← VO estratégico
│   ├── PediatricDetails.java          ← VO estratégico
│   ├── ProstheticDetails.java         ← VO estratégico
│   └── SurgicalDetails.java           ← VO estratégico
└── valueObject/
    ├── ServiceId.java
    ├── ServiceCode.java
    ├── ServiceCatalog.java
    ├── ServiceDuration.java
    └── ServiceStatus.java

📂 docs/dominio/descubrimientos-de-reglas/servicios/
├── ProvidedService(ServicioPrestado).md
├── OrthodonticDetails(DetallesOrtodoncia).md
├── SurgicalDetails(DetallesCirugia).md
├── ProstheticDetails(DetallesProtesis).md
├── ImplantologyDetails(DetallesImplantes).md
├── AestheticDetails(DetallesEstetica).md
└── PediatricDetails(DetallesPediatria).md
```

---

## Referencias

- **ADR-02 (Dominio):** Implementación sistemática de reglas de negocio por agregado
- **ADR-10 (Arquitectura):** Estrategia de persistencia con tablas separadas para detalles
- **ADR-20 (Arquitectura):** Alcance Experimental del Módulo Actor (patrón de referencia)
- **Patrón del proyecto:** 1 archivo descubrimiento = 1 agregado persistido

---

## Lecciones Aprendidas

### **Para Futuros Módulos**

**Si un módulo tiene Value Objects estratégicos que:**
1. Se persisten en tabla separada
2. Tienen 5+ reglas de negocio complejas
3. Evolucionan independientemente

**Entonces:** Crear archivo de descubrimiento separado.

**Ejemplo futuro:**
```
📂 Module: Treatment (Tratamiento)
├── Treatment.java                    → Agregado raíz
├── TreatmentPlanDetails.java         → VO estratégico (persistido)
└── FollowUpProtocol.java             → VO estratégico (persistido)

📂 docs/descubrimientos/treatment/
├── Treatment.md
├── TreatmentPlanDetails.md           ← Archivo separado justificado
└── FollowUpProtocol.md               ← Archivo separado justificado
```

---

**Resumen Ejecutivo:**  
Los Value Objects estratégicos con persistencia independiente y reglas complejas merecen archivos de descubrimiento separados. Esta decisión mantiene consistencia con el principio "1 archivo = 1 unidad persistida con reglas propias", facilita navegación y mantenimiento, y demuestra madurez arquitectónica apropiada para exhibición profesional.