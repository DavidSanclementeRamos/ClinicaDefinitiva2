# ERR_ACCOUNT_REQUIRES_DOCUMENT
- **Código:** ERR_ACCOUNT_REQUIRES_DOCUMENT
- **Nombre corto:** Requiere documento
- **Mensaje base:** `error.ledgerAccount.requiresDocument` — "La cuenta requiere un documento para registrar el movimiento"
- **Descripción clínica:** Se lanza cuando la cuenta está marcada como `requiresDocument` pero el movimiento no incluye documento asociado.
- **Operación / Caso de uso:** REGISTRAR_MOVIMIENTO
- **Regla de negocio:** RN-LEDGERACCOUNT-008
- **Contexto del agregado:** LEDGERACCOUNT
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** `"Cuenta 2201 - Proveedores requiere documento y no fue proporcionado"`
- **Mapa a código existente:** Sustituye `DomainAggregateException("La cuenta requiere un documento")`
- **Justificación ética:** Garantiza trazabilidad documental en movimientos contables.
- **Ejemplo de uso:**
  ```java
  if (this.requiresDocument && !hasDocument) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ACCOUNT_REQUIRES_DOCUMENT);
  }
  ```  
- **Pruebas mínimas requeridas:** Unitario (movimiento sin documento), integración (HTTP 400).
- **Changelog / versión:** 2025-12-08 — Autor: David — Alta inicial catálogo LedgerAccount.

---