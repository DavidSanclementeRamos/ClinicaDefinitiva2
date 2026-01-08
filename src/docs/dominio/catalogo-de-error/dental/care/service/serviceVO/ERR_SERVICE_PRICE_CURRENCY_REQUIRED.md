
## ERR_SERVICE_PRICE_CURRENCY_REQUIRED

- **Código:** ERR_SERVICE_PRICE_CURRENCY_REQUIRED
- **Nombre corto:** Moneda requerida
- **Mensaje base:** "La moneda no puede ser nula"
- **Descripción:**  
  Evita precios sin moneda que generen ambigüedad en transacciones y conversión de tarifas.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_PRECIO (createOrUpdatePrice)
- **Regla de negocio:** RN-PRICE-002 — Moneda obligatoria (ver ADR-122)
- **Contexto del agregado:** PRICE
- **Tipo semántico:** Validación de integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "price.currency=null para priceId=789"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura transparencia en costos y evita errores en facturación multi-moneda.
- **Ejemplo de uso:**
  ```java
  if (price.getCurrency() == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_PRICE_CURRENCY_REQUIRED);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** currency=null → excepción.
    - **Integración:** POST /services/{id}/prices sin currency → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Price.

---