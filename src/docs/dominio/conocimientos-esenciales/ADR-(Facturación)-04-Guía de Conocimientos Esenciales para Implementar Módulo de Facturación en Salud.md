# ADR-04 (Facturación): Guía de Conocimientos Esenciales para Implementar Módulo de Facturación en Salud

## Estado
- Accepted (retroactivo)

## Fecha
- 2026-01-09

## Contexto

El módulo de facturación para servicios de salud odontológica en Colombia representa uno de los componentes más complejos del sistema. Durante la planificación inicial, se identificó que este módulo requiere conocimientos especializados en múltiples dominios que van más allá del desarrollo de software tradicional.

### Desafíos Identificados

Al analizar los requisitos del módulo de facturación, se identificaron las siguientes áreas de complejidad:

1. **Dominio de negocio especializado**: El sistema de salud colombiano tiene actores, procesos y terminología específica (EPSs, IPSs, CUPS, ISS 2001)
2. **Marco normativo estricto**: La DIAN exige cumplimiento riguroso de facturación electrónica con especificaciones técnicas detalladas
3. **Cálculos financieros críticos**: Los errores de redondeo o cálculo pueden generar inconsistencias contables y pérdidas económicas
4. **Integraciones complejas**: Cada EPS tiene protocolos diferentes de radicación y autorización
5. **Problemas de concurrencia**: La numeración consecutiva debe ser thread-safe para evitar duplicados

### Riesgos de Implementar sin Preparación Adecuada

- **Rechazo de facturas (glosas)**: Facturas con errores generan rechazo por parte de EPSs, afectando el flujo de caja
- **Incumplimiento normativo**: Violaciones a normas DIAN pueden resultar en sanciones económicas
- **Inconsistencias financieras**: Errores de cálculo impiden cuadre contable y auditorías
- **Pérdida de trazabilidad**: Sin snapshot inmutable, las facturas cambian retroactivamente
- **Problemas de concurrencia**: Numeración duplicada invalida la resolución DIAN

## Decisión

Se establece como **requisito previo** a la implementación del módulo de facturación, el estudio y comprensión de los siguientes dominios de conocimiento. Este documento sirve como guía de aprendizaje para cualquier desarrollador que aborde este módulo.

---

## 1. DOMINIO DE NEGOCIO: FACTURACIÓN EN SALUD COLOMBIA

### 1.1 Sistema de Salud Colombiano

**Temas a estudiar:**
- Estructura del sistema de salud (EPSs, IPSs, Régimen Contributivo vs Subsidiado)
- Tipos de pagadores: EPS, Medicina Prepagada, ARL, SOAT, Particulares
- Diferencias operativas entre cada tipo de pagador
- Flujo de autorización previa para procedimientos

**Recursos recomendados:**
- Ley 100 de 1993 (Sistema General de Seguridad Social en Salud)
- Página web del Ministerio de Salud: https://www.minsalud.gov.co

**Conceptos críticos a dominar:**
```
Actores del Sistema:
├── EPS (Entidades Promotoras de Salud)
│   ├── Contributivo: Empleados formales
│   └── Subsidiado: Población vulnerable
├── IPS (Instituciones Prestadoras de Salud)
│   └── Clínica odontológica = IPS
├── Medicina Prepagada (Coomeva, Colsanitas)
├── ARL (Administradoras de Riesgos Laborales)
├── SOAT (Seguro Obligatorio Accidentes de Tránsito)
└── Particulares (Pago directo)
```

**Problema común a evitar:**
- Tratar todos los pagadores de la misma forma
- No validar autorización previa cuando es obligatoria
- Aplicar mismas tarifas a todos los pagadores

### 1.2 Manual Tarifario ISS 2001

**Temas a estudiar:**
- Qué es el Manual Tarifario ISS 2001 y su vigencia legal
- Cálculo de tarifas: UVR × Factor × Cantidad
- Cómo se negocian los factores en contratos con EPSs
- Actualización anual de UVR

**Fórmula básica:**
```
Tarifa = UVR × Factor Contrato × Cantidad

Ejemplo:
Código CUPS: 890101 (Consulta odontológica)
UVR: $45,000
Factor Contrato EPS Sura: 1.3
Tarifa Final = $45,000 × 1.3 = $58,500
```

**Recursos recomendados:**
- Resolución 5261 de 1994 (Manual Tarifario ISS)
- Documentación de contratos con EPSs (ejemplos reales si es posible)

**Problema común a evitar:**
- Asumir que todas las EPSs pagan igual
- No validar vigencia de contrato al momento de facturar
- No mantener histórico de cambios de factor

### 1.3 Código CUPS (Clasificación Única de Procedimientos en Salud)

**Temas a estudiar:**
- Estructura y jerarquía del código CUPS
- Cómo buscar códigos en el catálogo oficial
- Actualización periódica del catálogo (Ministerio de Salud)
- Relación entre CUPS y tarifas ISS

**Ejemplos de códigos:**
```
890101 → Consulta odontológica general
890201 → Radiografía periapical
890301 → Profilaxis y limpieza dental
890401 → Calzas en amalgama
890501 → Exodoncia simple
```

**Recursos recomendados:**
- Página oficial CUPS: https://www.minsalud.gov.co/cups
- Resolución 3100 de 2019 (actualización CUPS)

**Problema común a evitar:**
- Usar códigos inventados o desactualizados
- No validar existencia de código antes de facturar
- Mezclar códigos de diferentes versiones del catálogo

### 1.4 Flujos de Facturación por Tipo de Pagador

**Temas a estudiar:**
- Flujo completo de facturación a paciente particular
- Flujo completo de facturación a EPS (con autorización previa)
- Concepto de radicación ante EPS
- Qué son las glosas y cómo impactan el flujo de caja
- Términos de pago por tipo de pagador (NET_30, NET_60)

**Flujo EPS (caso más complejo):**
```
1. Paciente solicita cita
2. IPS solicita autorización a EPS
3. EPS aprueba/rechaza (genera número de autorización)
4. Si aprobado: Servicio se presta
5. IPS emite factura con # autorización
6. IPS radica factura ante EPS (presentación formal)
7. EPS valida factura (puede generar glosas)
8. Si hay glosas: IPS tiene 10 días para responder
9. Si no hay glosas: Pago a 30-60 días
```

**Recursos recomendados:**
- Circular 006 de 2019 del Ministerio de Salud (glosas)
- Manuales de radicación de EPSs principales (Sura, Salud Total)

**Problema común a evitar:**
- Facturar sin validar autorización previa
- No rastrear glosas en el sistema
- No gestionar cuentas por cobrar (factura emitida ≠ factura pagada)

### 1.5 Términos de Pago y Flujo de Caja

**Temas a estudiar:**
- Diferencia entre factura emitida y factura pagada
- Plazos de pago según tipo de pagador
- Cómo proyectar flujo de caja con facturas a crédito
- Gestión de cartera vencida

**Plazos típicos en Colombia:**
```
PARTICULAR: Inmediato (IMMEDIATE)
EPS: 30-60 días (NET_30, NET_60)
PREPAGADA: 30-45 días (NET_30)
ARL: 30 días (NET_30)
SOAT: 30-90 días (NET_30, NET_90)
```

**Problema común a evitar:**
- No diferenciar entre estados PENDING y PAID
- No hacer seguimiento a facturas vencidas
- No proyectar impacto en flujo de caja

---

## 2. NORMATIVA LEGAL: DIAN Y FACTURACIÓN ELECTRÓNICA

### 2.1 Marco Legal de Facturación Electrónica

**Temas a estudiar:**
- Decreto 358 de 2020 (facturación electrónica obligatoria)
- Resolución 000042 de 2020 (especificaciones técnicas)
- Proceso de solicitud de resolución de facturación ante DIAN
- Requisitos mínimos de una factura válida

**Recursos recomendados:**
- Portal DIAN: https://www.dian.gov.co/facturacion-electronica
- Guías técnicas de facturación electrónica (documentos PDF DIAN)

**Conceptos críticos:**
```
Factura electrónica válida debe incluir:
✅ Numeración consecutiva autorizada
✅ NIT del prestador
✅ Identificación del paciente (CC, TI, CE)
✅ Código CUPS del servicio
✅ Descripción del servicio
✅ Valor unitario y total
✅ Discriminación de impuestos (IVA si aplica)
✅ Fecha de emisión y vencimiento
✅ CUFE (Código Único de Factura Electrónica)
✅ QR con enlace a validación DIAN
```

### 2.2 Numeración Consecutiva

**Temas a estudiar:**
- Qué es una resolución de facturación
- Cómo solicitar rango numérico ante DIAN
- Vigencia de la resolución (típicamente 1-2 años)
- Consecuencias de saltos en numeración

**Conceptos críticos:**
- La numeración debe ser estrictamente consecutiva (sin saltos)
- Ejemplo válido: FECO-0001, FECO-0002, FECO-0003
- Ejemplo inválido: FECO-0001, FECO-0003 (falta FECO-0002)

**Problema común a evitar:**
- Permitir duplicados por problemas de concurrencia
- Usar UUID como número de factura
- No monitorear agotamiento de rango

### 2.3 CUFE (Código Único de Factura Electrónica)

**Temas a estudiar:**
- Qué es el CUFE y para qué sirve
- Algoritmo de generación (SHA-384)
- Componentes que se incluyen en el hash
- Validación en portal DIAN

**Algoritmo simplificado:**
```
CUFE = SHA-384(
    número_factura +
    fecha_emisión +
    hora_emisión +
    valor_total +
    valor_impuestos +
    valor_subtotal +
    NIT_prestador +
    documento_paciente
)
```

**Recursos recomendados:**
- Anexo técnico Resolución 000042 de 2020
- Ejemplos de generación de CUFE (documentos DIAN)

**Problema común a evitar:**
- Calcular CUFE con formato de fecha incorrecto
- Redondear decimales de forma incorrecta
- No usar codificación UTF-8

### 2.4 XML UBL 2.1 (Universal Business Language)

**Temas a estudiar:**
- Qué es UBL y por qué se usa internacionalmente
- Estructura básica de un XML UBL de factura
- Validación contra esquema XSD oficial
- Namespaces y prefijos requeridos

**Recursos recomendados:**
- Documentación oficial UBL 2.1: http://docs.oasis-open.org/ubl/
- Ejemplos de XML de factura (repositorio DIAN)
- Herramientas de validación XSD

**Problema común a evitar:**
- Generar XML manualmente con concatenación de strings
- No validar contra XSD antes de enviar
- Usar librerías desactualizadas

### 2.5 Firma Digital

**Temas a estudiar:**
- Qué es un certificado digital y cómo funciona
- Proveedores autorizados en Colombia (Certicámara, GSE)
- Proceso de firma de XML
- Vigencia y renovación de certificados

**Recursos recomendados:**
- Sitio web Certicámara: https://www.certicamara.com
- Guías de implementación de firma digital (DIAN)

**Problema común a evitar:**
- No monitorear vigencia del certificado
- Dejar vencer certificado y bloquear facturación
- No tener backup de certificado

### 2.6 IVA (Impuesto al Valor Agregado)

**Temas a estudiar:**
- Cuándo aplica IVA en servicios odontológicos
- Diferencia entre servicios de salud (exentos) y estéticos (IVA 19%)
- Cómo discriminar IVA en la factura

**Regla básica:**
```
Servicios de salud: EXENTOS (0%)
Procedimientos estéticos: IVA 19%

Ejemplos:
✅ Limpieza dental: Exento
✅ Ortodoncia correctiva: Exento
❌ Blanqueamiento dental: IVA 19%
❌ Carillas estéticas: IVA 19%
```

**Problema común a evitar:**
- Aplicar IVA a todos los servicios
- No diferenciar entre salud y estética

---


## 3. CÁLCULOS FINANCIEROS

### 3.1 Aritmética con BigDecimal

**Temas a estudiar:**
- Por qué no usar double/float para dinero
- Cómo usar BigDecimal correctamente
- Modos de redondeo (RoundingMode)
- Escala (número de decimales)

**Recursos recomendados:**
- Documentación oficial Java BigDecimal
- Artículos sobre "never use float for money"

**Reglas básicas:**
```java
✅ BigDecimal subtotal = new BigDecimal("100000.00");
❌ BigDecimal subtotal = new BigDecimal(100000.0); // Pierde precisión

✅ result.setScale(2, RoundingMode.HALF_UP);
❌ result sin definir escala
```

**Problema común a evitar:**
- Crear BigDecimal desde double
- No definir escala y modo de redondeo
- Usar comparaciones con ==

### 3.2 Distribución de Totales

**Temas a estudiar:**
- Problema de centavos faltantes al dividir
- Cómo ajustar último ítem para compensar
- Validar que suma de ítems = total factura

**Escenario típico:**
```
Total a distribuir: $100,000.00 en 3 ítems
División: $100,000 / 3 = $33,333.33...

Distribución incorrecta:
Ítem 1: $33,333.33
Ítem 2: $33,333.33
Ítem 3: $33,333.33
Total: $99,999.99 ❌ (falta $0.01)

Distribución correcta:
Ítem 1: $33,333.33
Ítem 2: $33,333.33
Ítem 3: $33,333.34 (ajustado)
Total: $100,000.00 ✅
```

---

## 4. CONCURRENCIA Y TRANSACCIONES

### 4.1 Numeración Consecutiva Thread-Safe

**Temas a estudiar:**
- Problemas de race conditions
- AtomicLong para contadores
- Secuencias de base de datos
- Bloqueos pesimistas

**Problema crítico:**
```
Thread 1: Lee counter = 100 → Genera FECO-0101
Thread 2: Lee counter = 100 → Genera FECO-0101 ❌ DUPLICADO
```

**Soluciones a estudiar:**
1. AtomicLong (si todo en memoria)
2. Secuencia de BD (nextval)
3. SELECT FOR UPDATE (bloqueo pesimista)

### 4.2 Transacciones y Consistencia

**Temas a estudiar:**
- Propiedades ACID
- Cuándo usar @Transactional
- Qué operaciones deben estar en una transacción
- Rollback en caso de error

**Problema común a evitar:**
- Crear Invoice sin ítems en transacciones separadas
- No hacer rollback cuando falla validación

---

## 5. INTEGRACIÓN Y APIs

### 5.1 Integración con DIAN

**Temas a estudiar:**
- Protocolo de comunicación (REST/SOAP)
- Timeouts y reintentos
- Manejo de errores
- Códigos de respuesta DIAN

**Estrategia de reintentos:**
```
Intento 1: Inmediato
Intento 2: Espera 2 segundos
Intento 3: Espera 4 segundos
Intento 4: Espera 8 segundos
```

### 5.2 Integración con EPSs

**Temas a estudiar:**
- Cada EPS tiene API diferente
- Patrón Adapter para múltiples integraciones
- Autenticación OAuth 2.0
- SOAP vs REST

**Problema común a evitar:**
- Acoplar lógica de negocio a API específica
- No abstraer diferencias entre EPSs

---

## 6. TESTING

### 6.1 Tests Críticos a Implementar

**Temas a estudiar:**
- Testing de agregados (unit tests)
- Testing de invariantes
- Testing de transiciones de estado
- Testing de concurrencia

**Tests obligatorios:**
```java
1. Invariante: subtotal + tax = total
2. No permite facturar con tarifa vencida
3. CUFE se genera correctamente (determinístico)
4. Numeración consecutiva sin duplicados (concurrency test)
5. Transiciones de estado válidas
6. Cálculos de IVA correctos
```

---

## 7. RENDIMIENTO Y OPTIMIZACIÓN

### 7.1 Consultas N+1

**Temas a estudiar:**
- Qué es el problema N+1
- Uso de JOIN FETCH
- Lazy vs Eager loading
- Proyecciones para queries

### 7.2 Índices de Base de Datos

**Temas a estudiar:**
- Cuándo crear índices
- Índices simples vs compuestos
- Impacto en performance de escritura

**Índices críticos:**
```sql
- invoice_number (búsqueda frecuente)
- status + due_date (cuentas por cobrar)
- patient_id (facturas por paciente)
```

---

## Consecuencias

### Positivas
- **Preparación adecuada**: Entender el contexto antes de codificar
- **Reducción de bugs**: Conocimiento del dominio previene errores conceptuales
- **Código mantenible**: Modelado correcto facilita evolución del sistema
- **Cumplimiento normativo**: Entender DIAN desde el inicio evita refactoring posterior
- **Base de conocimiento**: Documento sirve como referencia permanente

### Negativas
- **Curva de aprendizaje**: Requiere inversión de tiempo antes de codificar
- **Complejidad inicial**: Puede parecer abrumador para colaboradores nuevos
- **Necesidad de actualización**: Normativa DIAN y catálogo CUPS cambian periódicamente

### Riesgos Mitigados
- ❌ Implementar sin entender glosas → Pérdidas financieras
- ❌ Ignorar numeración consecutiva → Resolución DIAN invalidada
- ❌ No usar BigDecimal → Errores de redondeo
- ❌ Snapshot mutable → Facturas cambian retroactivamente
- ❌ Sin eventos de dominio → Acoplamiento alto


---

## Checklist Pre-Implementación

Antes de iniciar la codificación del módulo, asegurar comprensión de:

**Dominio de Negocio:**
- [ ] ¿Qué es una glosa y por qué ocurre?
- [ ] ¿Cuál es la diferencia entre EPS contributivo y subsidiado?
- [ ] ¿Cómo se calcula una tarifa ISS 2001?
- [ ] ¿Qué es un código CUPS y dónde consultarlo?
- [ ] ¿Cómo funciona el ciclo de radicación ante una EPS?

**Normativa DIAN:**
- [ ] ¿Qué es el CUFE y cómo se genera?
- [ ] ¿Por qué la numeración debe ser consecutiva?
- [ ] ¿Qué componentes tiene un XML UBL 2.1?
- [ ] ¿Cuándo aplica IVA en servicios odontológicos?

**Modelado Técnico:**
- [ ] ¿Qué es un snapshot inmutable y por qué es crítico?
- [ ] ¿Por qué usar BigDecimal en lugar de double?
- [ ] ¿Cómo garantizar numeración consecutiva en concurrencia?

---

## Recursos de Aprendizaje

### Documentación Oficial
- DIAN Facturación Electrónica: https://www.dian.gov.co/facturacion-electronica
- Ministerio de Salud CUPS: https://www.minsalud.gov.co/cups
- Resolución 5261 de 1994 (Tarifas ISS)
- Decreto 358 de 2020 (Facturación electrónica)

### Comunidades
- Foro DIAN (consultas técnicas)
- Stack Overflow (tag: domain-driven-design)
- DDD Community Slack

---

## Notas

## Notas

- Este ADR documenta los conocimientos necesarios para comprender el dominio
  completo de facturación en salud en Colombia
- **NO implica que todo deba implementarse** en el proyecto experimental
- Ver **[ADR-(Facturación)-05-Alcance de Implementación del Módulo de Facturación (MVP Portfolio).md](../glosario-semantico/ADR-(Facturación)-05-Alcance%20de%20Implementación%20del%20Módulo%20de%20Facturación%20(MVP%20Portfolio).md)** para el alcance específico de implementación del MVP
- El objetivo es tener visión completa del problema real, aunque se implemente
  una versión simplificada para fines de portfolio
- Los conocimientos adquiridos permitirán:
    - Tomar decisiones de diseño informadas
    - Documentar limitaciones conscientemente


## Resumen de la estructura final:
```
[ADR-(Facturación)-04-Guía de Conocimientos Esenciales para Implementar Módulo de Facturación en Salud .md](ADR-%28Facturaci%C3%B3n%29-04-Gu%C3%ADa%20de%20Conocimientos%20Esenciales%20para%20Implementar%20M%C3%B3dulo%20de%20Facturaci%C3%B3n%20en%20Salud%20.md): "Qué necesito SABER" (conocimiento del dominio)
└─> [ADR-(Facturación)-05-Alcance de Implementación del Módulo de Facturación (MVP Portfolio).md](ADR-%28Facturaci%C3%B3n%29-05-Alcance%20de%20Implementaci%C3%B3n%20del%20M%C3%B3dulo%20de%20Facturaci%C3%B3n%20%28MVP%20Portfolio%29.md): "Qué voy a IMPLEMENTAR" (alcance MVP)
└─> Implementar: Core técnico
└─> Simular: Integraciones
└─> Documentar: Complejidad real
```