## ERR_SERVICE_PRICE_NEGATIVE

- **Código:** ERR_SERVICE_PRICE_NEGATIVE
- **Nombre corto:** Monto negativo
- **Mensaje base:** "El monto no puede ser negativo"
- **Descripción:**  
  Evita registros de precios negativos que comprometan la contabilidad y la lógica de cobro.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_PRECIO (createOrUpdatePrice)
- **Regla de negocio:** RN-PRICE-003 — No negatividad en monto (ver ADR-122)
- **Contexto del agregado:** PRICE
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "price.amount=-150.00 currency=USD"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita errores financieros y prácticas que puedan inducir a cobros indebidos.
- **Ejemplo de uso:**
  ```java
  if (price.getAmount().compareTo(BigDecimal.ZERO) < 0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_PRICE_NEGATIVE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** amount=-1.00 → excepción.
    - **Integración:** POST /services/{id}/prices con amount negativo → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Price.

---