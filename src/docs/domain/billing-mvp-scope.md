# Alcance del módulo de Facturación — MVP de portafolio

**Estado:** Aprobado (retroactivo)  
**Fecha:** 2026-01-09  
**Categoría:** Dominio — Decisión de alcance  
**Autor:** David Stiven Sanclemente

---

## Por qué existe este documento

El módulo de facturación en salud en Colombia tiene una complejidad normativa real que haría del proyecto completo un trabajo de 6 a 12 meses: facturación electrónica obligatoria (Decreto 358/2020), CUFE con SHA-384, XML en formato UBL 2.1, firma digital con certificado autorizado, integración con DIAN en menos de 24 horas, y APIs propias de cada EPS con protocolos distintos.

Este proyecto no está destinado a producción. Está construido para demostrar capacidad de modelado de dominio complejo. Implementar toda la normativa no habría aportado valor proporcional a ese objetivo: la integración real con DIAN es burocracia técnica, no modelado de dominio.

La decisión fue implementar un MVP que demuestre las habilidades core y simular o excluir lo que depende de infraestructura externa. Este documento registra qué quedó en cada categoría.

> El razonamiento detrás de esta decisión y las lecciones que dejó se narran en [ADR-29 (lección aprendida)](../../../../evolution/lessons-learned/ADR-%28Arquitectura%29-29-alcance%20experimental%20del%20m%C3%B3dulo%20dental.care.services.md).

---

## Qué está implementado

- Agregado `Invoice` con ciclo de vida completo: `DRAFT → ISSUED → PAID / CANCELLED`
- Invariantes de negocio: subtotal + impuesto = total, factura emitida es inmutable, mínimo un ítem para emitir
- Cálculos financieros con `BigDecimal` y `RoundingMode.HALF_UP`
- Snapshot inmutable de precios en `InvoiceItem`: el precio queda congelado al emitir la factura, independientemente de cambios posteriores en la tarifa
- Diferenciación de pagador: `PRIVATE` (pago inmediato) vs `EPS` (pago a 30 días)
- Numeración consecutiva básica thread-safe (`AtomicLong`)
- Eventos de dominio: `InvoiceCreated`, `InvoiceIssued`, `InvoicePaid`, `InvoiceCancelled`

---

## Qué está simulado

| Componente | Qué hace el mock | Qué haría en producción |
|------------|------------------|------------------------|
| Integración DIAN | Genera UUID como identificador | CUFE real con SHA-384, XML UBL 2.1, firma digital, transmisión HTTP |
| Autorización EPS | Aprueba/rechaza con respuesta predefinida | API REST o SOAP específica de cada EPS, OAuth 2.0 |
| Catálogo CUPS | ~20 códigos representativos hardcodeados | 5.000+ procedimientos sincronizados con el Ministerio de Salud |

---

## Qué está fuera de alcance

- Generación de CUFE real (requiere certificado digital — $300k–500k COP/año)
- Flujo completo de glosas y reclamaciones ante EPS
- Validación de autorización previa contra sistema externo
- Consecutivo persistido en base de datos con `SELECT FOR UPDATE`
- Numeración autorizada por resolución DIAN

---

## Para quien quiera extender este módulo

Las áreas de mayor impacto para una contribución real son:

1. **Numeración consecutiva persistida** — reemplazar el `AtomicLong` en memoria por una secuencia en base de datos con manejo de concurrencia real
2. **Integración DIAN** — implementar el adaptador real en la capa de infraestructura sin tocar el dominio
3. **Flujo de glosas** — el modelo de dominio está preparado para incorporar este ciclo de vida

La arquitectura hexagonal garantiza que ninguna de estas extensiones requiere modificar el dominio existente.

---

**Última actualización:** 2026-04-11