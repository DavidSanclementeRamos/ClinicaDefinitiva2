## ERR_SERVICE_PRICE_AMOUNT_REQUIRED

- **Código:** ERR_SERVICE_PRICE_AMOUNT_REQUIRED
- **Nombre corto:** Monto requerido
- **Mensaje base:** "El monto no puede ser nulo"
- **Descripción:**  
  Evita precios sin monto que impidan facturación, cotización y comparabilidad entre servicios.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_PRECIO (createOrUpdatePrice)
- **Regla de negocio:** RN-PRICE-001 — Monto obligatorio (ver ADR-122)
- **Contexto del agregado:** PRICE
- **Tipo semántico:** Validación de integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "price.amount=null para serviceId=123"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita prácticas comerciales opacas y protege al paciente frente a cobros inesperados.
- **Ejemplo de uso:**
  ```java
  if (price.getAmount() == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_PRICE_AMOUNT_REQUIRED);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** amount=null → excepción.
    - **Integración:** POST /services/{id}/prices sin amount → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Price.

---