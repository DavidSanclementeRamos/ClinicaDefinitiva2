# ADR 05 (Facturación): Alcance de Implementación del Módulo de Facturación (MVP Portfolio)

## Estado
- Accepted (retroactivo)

## Fecha
- 2026-01-09


## Contexto

### Naturaleza del Proyecto

Este es un proyecto **experimental y educativo**, construido con los siguientes objetivos:

1. **Generar experiencia técnica** en modelado de dominio complejo
2. **Exhibir capacidades** como desarrollador para búsqueda de empleo
3. **Demostrar comprensión** de problemas reales del mundo empresarial
4. **Construir portfolio** que refleje calidad profesional

El proyecto **NO** está destinado a:
- Ser usado en producción inmediata
- Competir con software comercial
- Cumplir con todos los requisitos legales de Colombia
- Integrarse con sistemas reales (DIAN, EPSs)

### Complejidad del Dominio Real

Según **[ADR-(Facturación)-04-Guía de Conocimientos Esenciales para Implementar Módulo de Facturación en Salud .md](ADR-%28Facturaci%C3%B3n%29-04-Gu%C3%ADa%20de%20Conocimientos%20Esenciales%20para%20Implementar%20M%C3%B3dulo%20de%20Facturaci%C3%B3n%20en%20Salud%20.md)**, el módulo de facturación en salud en Colombia presenta complejidad significativa:

**Normativa Legal:**
- Facturación electrónica obligatoria (Decreto 358/2020)
- Generación de CUFE con SHA-384
- XML en formato UBL 2.1
- Firma digital con certificado autorizado
- Numeración consecutiva autorizada por DIAN
- Conservación legal por 5 años

**Integraciones Externas:**
- Transmisión a DIAN (< 24 horas tras emisión)
- APIs específicas de cada EPS (Sura, Salud Total, Nueva EPS, etc.)
- Protocolos diversos (REST, SOAP, portales web)
- Autenticación OAuth 2.0, tokens, certificados

**Lógica de Negocio:**
- Manual Tarifario ISS 2001
- Códigos CUPS (5000+ procedimientos)
- Glosas y reclamaciones
- Autorizaciones previas
- Términos de pago diferenciados
- Conciliación de cuentas por cobrar

### Estimación de Esfuerzo

Implementar la complejidad completa requeriría:
- **Tiempo**: 6-12 meses de desarrollo
- **Recursos**: Certificado digital ($300k-500k COP/año), acceso a sandbox EPSs
- **Validación**: Contador, experto en facturación de salud
- **Mantenimiento**: Actualizaciones continuas (catálogo CUPS, normativa DIAN)

### Problema Identificado

El esfuerzo de implementar **todo** el dominio real presenta desventajas para un proyecto de portfolio:

1. **ROI bajo**: Los reclutadores revisarán el código 10-15 minutos
2. **Tiempo excesivo**: 6-12 meses vs objetivo de 2-3 meses
3. **Detalles irrelevantes**: Integración DIAN real no demuestra habilidad de modelado
4. **Riesgo de no terminar**: Proyecto inconcluso es peor que proyecto limitado pero funcional

## Decisión

Se implementará un **MVP estratégico** que demuestre capacidades técnicas fundamentales sin perderse en detalles de integración que no aportan valor proporcional al objetivo del proyecto.

### Principios Rectores del Alcance

```
1. IMPLEMENTAR: Complejidad técnica que demuestra habilidades core
   → Modelado de dominio, invariantes, eventos, concurrencia

2. SIMULAR: Integraciones externas con mocks inteligentes
   → DIAN, EPSs, autorización previa

3. DOCUMENTAR: Cómo se implementaría en escenario real
   → ADRs, diagramas, roadmap hacia producción

4. UNA FEATURE COMPLEJA: Showcase de nivel técnico avanzado
   → Elegir 1-2 features para profundizar
```

---

## Alcance Definido: MVP Facturación

### ✅ IMPLEMENTAR (Core Técnico)

#### 1. Agregado Invoice
```java
Componentes:
├── Invoice (Aggregate Root)
│   ├── InvoiceId (Value Object)
│   ├── InvoiceStatus (Value Object con transiciones)
│   ├── Money (Value Object - BigDecimal)
│   └── List<InvoiceItem> (Entidades del agregado)
├── InvoiceItem (Entity)
│   ├── Código de servicio
│   ├── Descripción
│   ├── Cantidad
│   ├── Precio unitario (BigDecimal)
│   └── Total línea
└── InvoiceNumber (Value Object - consecutivo simple)

Estados implementados:
- DRAFT: Factura en construcción
- ISSUED: Factura emitida (inmutable)
- PAID: Factura pagada
- CANCELLED: Factura anulada

Transiciones válidas:
- DRAFT → ISSUED
- DRAFT → CANCELLED
- ISSUED → PAID
- ISSUED → CANCELLED (con restricciones)
```

**Justificación**: Demuestra modelado DDD sólido, manejo de agregados, invariantes y máquina de estados.

#### 2. Invariantes Garantizados

```java
Invariantes obligatorios:
1. Subtotal + Tax = Total (aritmética exacta)
2. Total > 0 (no facturas vacías)
3. Todos los ítems tienen la misma moneda
4. Estado PAID es inmutable (no puede cambiar)
5. Factura ISSUED no puede modificar ítems
6. Al menos 1 ítem para emitir
```

**Justificación**: Demuestra comprensión de reglas de negocio y validaciones de dominio.

#### 3. Cálculos Financieros con BigDecimal

```java
Operaciones implementadas:
- Cálculo de subtotal (suma de líneas)
- Cálculo de impuestos (IVA 0% o 19%)
- Cálculo de total
- Redondeo correcto (RoundingMode.HALF_UP)
- Distribución de totales (ajuste de centavos)
- Comparaciones de montos

Tests específicos:
- Suma de ítems = total factura
- Precisión de decimales (no pérdida)
- Distribución sin centavos faltantes
```

**Justificación**: Demuestra conocimiento crítico de manejo de dinero en software.

#### 4. Eventos de Dominio

```java
Eventos implementados:
- InvoiceCreated
- InvoiceIssued (DRAFT → ISSUED)
- InvoicePaid (ISSUED → PAID)
- InvoiceCancelled
- InvoiceItemAdded

Consumidores de ejemplo:
- EmailNotificationHandler (mock)
- InvoiceIssuedLogger
- AuditEventHandler
```

**Justificación**: Demuestra desacoplamiento y arquitectura orientada a eventos.

#### 5. Tipos de Pagador (Simplificados)

```java
PayerType (enum):
- PRIVATE: Paciente particular
- EPS: Entidad Promotora de Salud (simplificado)

Diferencias implementadas:
- PRIVATE: Pago inmediato, tarifas libres
- EPS: Pago a 30 días, requiere "número de autorización" (String simple)

NO implementado:
- Integración real con EPSs
- Validación de autorización contra sistema externo
- Múltiples EPSs con APIs diferentes
```

**Justificación**: Demuestra comprensión de lógica de negocio diferenciada sin complejidad de integración.

#### 6. Numeración Consecutiva (Thread-Safe Simplificado)

```java
Implementación:
- AtomicLong para contador en memoria
- Formato: INV-0001, INV-0002, INV-0003
- Thread-safe para concurrencia básica

Tests:
- Test de concurrencia (10 threads, 100 facturas cada uno)
- Validar que no hay duplicados
- Validar que no hay saltos

NO implementado:
- Resolución DIAN real
- Secuencia persistida en BD
- Rango autorizado por entidad externa
```

**Justificación**: Demuestra manejo de concurrencia sin complejidad de integración legal.

#### 7. Tests Unitarios Críticos

```java
Suite de tests obligatoria:
1. Invariante: subtotal + tax = total
2. No permite emitir sin ítems
3. No permite modificar factura ISSUED
4. Transiciones de estado válidas/inválidas
5. Cálculos BigDecimal precisos
6. Numeración consecutiva sin duplicados (concurrencia)
7. Eventos se emiten correctamente
8. Snapshot de precios no cambia (si se implementa)

Cobertura mínima: 80%
```

**Justificación**: Demuestra rigor en testing y calidad de código.

---

### ⚠️ SIMULAR (Integraciones Externas)

#### 1. DIAN (Simulado)

```java
Implementación:
- DIANServiceMock que simula transmisión
- Genera UUID como "CUFE" (en lugar de SHA-384 real)
- Responde con éxito/error configurable
- Latencia simulada (500ms-2s)

NO implementado:
- CUFE real con SHA-384
- XML UBL 2.1
- Firma digital con certificado
- Transmisión HTTP real a DIAN
- Validación en portal DIAN
```

**Justificación**: La integración DIAN real no aporta valor técnico al portfolio; es configuración y burocracia.

#### 2. EPSs (Simulados)

```java
Implementación:
- EPSServiceMock con respuestas predefinidas
- Simulación de autorización previa (aprueba/rechaza)
- Simulación de radicación (acepta/glosa)
- Códigos de respuesta hardcodeados

NO implementado:
- APIs reales de EPSs (Sura, Salud Total, etc.)
- OAuth 2.0
- SOAP Web Services
- Manejo de glosas real
```

**Justificación**: Cada EPS tiene API diferente; simular demuestra el concepto sin complejidad de integración.

#### 3. Catálogo CUPS (Limitado)

```java
Implementación:
- 15-20 códigos CUPS representativos
- Estructura de datos básica (code, description, category)
- Validación de código contra catálogo local

Códigos de ejemplo:
- 890101: Consulta odontológica
- 890301: Profilaxis dental
- 890401: Calza en amalgama
- 890501: Extracción simple
- ... (10-15 más)

NO implementado:
- Catálogo completo (5000+ códigos)
- Sincronización con Ministerio de Salud
- Versionado de catálogo
```

**Justificación**: 15 códigos son suficientes para demostrar validación; 5000 no aportan valor adicional.

---

### 📝 DOCUMENTAR (Complejidad Real)

#### 1. ADRs de Conocimiento

Ya creados:
- **[ADR-(Facturación)-01-Validación de Tarifas Vigentes al Momento de Facturar.md](../decisions/billing/ADR-(Facturación)-01-Validación%20de%20Tarifas%20Vigentes%20al%20Momento%20de%20Facturar.md)**: Validación de tarifas vigentes (cómo se haría en producción)
- **[ADR-(Facturación)-02-Snapshot Inmutable de Precios en Facturación.md](../decisions/billing/ADR-(Facturación)-02-Snapshot%20Inmutable%20de%20Precios%20en%20Facturación.md)**: Snapshot inmutable de precios (por qué es crítico)
- **[ADR-(Facturación)-03-Cumplimiento Normativo DIAN Colombia.md](../decisions/billing/ADR-(Facturación)-03-Cumplimiento%20Normativo%20DIAN%20Colombia.md)**: Cumplimiento normativo DIAN (requisitos completos)
- **[ADR-(Facturación)-04-Guía de Conocimientos Esenciales para Implementar Módulo de Facturación en Salud .md](ADR-%28Facturaci%C3%B3n%29-04-Gu%C3%ADa%20de%20Conocimientos%20Esenciales%20para%20Implementar%20M%C3%B3dulo%20de%20Facturaci%C3%B3n%20en%20Salud%20.md)**: Guía de conocimientos esenciales (qué estudiar)
- **[ADR-(Facturación)-05-Alcance de Implementación del Módulo de Facturación (MVP Portfolio).md](ADR-(Facturación)-05-Alcance de Implementación del Módulo de Facturación (MVP Portfolio).md)**: Este documento (qué implementar)

**Objetivo**: Demostrar que se comprende la complejidad real aunque no se implemente completa.

#### 2. README con Sección "Limitaciones Conocidas"

```markdown
## Limitaciones Conocidas

Este es un MVP educativo. Las siguientes features están simuladas:

### Integraciones Externas
- **DIAN**: Se usa UUID en lugar de CUFE real (SHA-384)
- **EPSs**: Mock con respuestas predefinidas
- **Firma Digital**: No implementada (requiere certificado $300k/año)

### Lógica de Negocio Simplificada
- **Glosas**: No hay flujo completo de respuesta
- **Tarifas**: No se valida vigencia contra contrato real
- **Autorizaciones**: String simple en lugar de validación externa

### Roadmap hacia Producción
Ver ADR-BILLING-03 para especificaciones de implementación real.
```

**Objetivo**: Transparencia sobre el alcance; demuestra madurez técnica.

#### 3. Diagramas de Arquitectura

```
Diagramas a incluir:
- Diagrama de agregados (Invoice, Rate, Payment)
- Diagrama de estados de Invoice
- Diagrama de flujo de facturación completo (real vs implementado)
- Diagrama de integración con DIAN (cómo sería en producción)
```

**Objetivo**: Comunicar visión arquitectónica sin implementar todo.

---

### 🎯 UNA FEATURE COMPLEJA (Showcase Técnico)

Se elige **UNA** de las siguientes para implementar con profundidad:

#### Opción A: Snapshot Inmutable de Precios ⭐ (RECOMENDADO)

```java
Implementación completa:
- InvoiceItem almacena copia de unitPrice (no referencia a Rate)
- Al emitir factura (DRAFT → ISSUED), precios se congelan
- Rate puede cambiar posteriormente sin afectar facturas emitidas
- Tests que validan inmutabilidad

Valor demostrado:
✅ Comprensión de desnormalización controlada
✅ Diseño para inmutabilidad
✅ Pensamiento en auditoría y trazabilidad

Esfuerzo: 1-2 semanas
Impacto en portfolio: ALTO
```

#### Opción B: Numeración Consecutiva con Persistencia

```java
Implementación completa:
- Secuencia en base de datos (nextval)
- SELECT FOR UPDATE para evitar duplicados
- Tests de concurrencia exhaustivos (100+ threads)
- Manejo de rollback y retry

Valor demostrado:
✅ Manejo avanzado de concurrencia
✅ Transacciones DB complejas
✅ Testing de race conditions

Esfuerzo: 1-2 semanas
Impacto en portfolio: MEDIO-ALTO
```

#### Opción C: Máquina de Estados Robusta

```java
Implementación completa:
- Patrón State para transiciones
- Validaciones exhaustivas por estado
- Eventos de dominio por cada transición
- Tests de todas las transiciones válidas/inválidas

Valor demostrado:
✅ Patrones de diseño avanzados
✅ Modelado de comportamiento complejo
✅ Testing exhaustivo

Esfuerzo: 1-2 semanas
Impacto en portfolio: MEDIO
```

**Decisión**: Implementar **Opción A (Snapshot Inmutable)** por mayor impacto en demostración de madurez técnica.

---

## Consecuencias

### Positivas

1. **✅ Proyecto terminable**
2. **✅ Foco en habilidades core** 
3. **✅ Demuestra madurez**
4. **✅ Portfolio efectivo**
5. **✅ Base sólida**
6. **✅ Transparencia**

### Negativas

1. **⚠️ No es "production-ready"**: No cumple con normativa DIAN real
2. **⚠️ Integraciones simuladas**: No se puede usar con EPSs reales
3. **⚠️ Alcance limitado**: Algunas features complejas quedan fuera

### Riesgos Mitigados

- ❌ Proyecto inconcluso por sobre-alcance → ✅ MVP funcional terminado
- ❌ Código complejo difícil de revisar → ✅ Código limpio enfocado
- ❌ Perderse en detalles irrelevantes → ✅ Foco en habilidades técnicas
- ❌ Tiempo excesivo sin ROI → ✅ 2 meses con alto ROI para portfolio

---

## Roadmap Futuro 

Si se quisiera expandir el proyecto hacia producción:

### Fase 2: Integración DIAN Real 
- Implementar CUFE con SHA-384
- Generar XML UBL 2.1
- Adquirir certificado digital
- Integrar con sandbox DIAN
- Manejo de errores DIAN

### Fase 3: Integración EPSs 
- API Sura (REST)
- API Salud Total (SOAP)
- OAuth 2.0 y tokens
- Manejo de glosas
- Portal web Nueva EPS

### Fase 4: Features Avanzadas 
- Catálogo CUPS completo
- Validación de tarifas vigentes
- Gestión de cuentas por cobrar
- Reportes contables
- Auditoría completa

**Total estimado hacia producción: 9-13 meses adicionales**


---

## Relación con otros ADR


---

## Notas Finales

### Principio Rector
> "El mejor proyecto de portfolio no es el más completo, sino el que demuestra tu nivel técnico de forma clara y rápida."

### Recordatorio
- Este es un **proyecto educativo**, no un producto comercial
- El objetivo es **conseguir empleo**, no construir software de producción
- La **calidad** de lo implementado es más importante que la **cantidad**
- **Terminar** un MVP bien hecho vale más que dejar inconcluso un proyecto ambicioso

