## ERR_SERVICE_HAS_PENDING_INVOICES

- **Código:** ERR_SERVICE_HAS_PENDING_INVOICES
- **Nombre corto:** Facturas pendientes
- **Mensaje base:** "No puede desactivarse porque tiene facturas pendientes"
- **Descripción:**  
  Impide desactivar servicios que están asociados a facturas no saldadas para preservar integridad contable y evitar pérdida de trazabilidad financiera.
- **Operación / Caso de uso:** DESACTIVAR_SERVICIO (deactivateService)
- **Regla de negocio:** RN-SERVICE-012 — Bloqueo por facturas pendientes (ver ADR-117)
- **Contexto del agregado:** SERVICE / INVOICE
- **Tipo semántico:** Integridad financiera
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "serviceId=555 pendingInvoices=2"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege derechos contractuales y evita inconsistencias en la contabilidad del centro.
- **Ejemplo de uso:**
  ```java
  if (invoiceRepository.countPendingByService(serviceId) > 0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_HAS_PENDING_INVOICES);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** pendingInvoices=1 → excepción al desactivar.
    - **Integración:** DELETE /services/{id} con facturas pendientes → 409.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---
