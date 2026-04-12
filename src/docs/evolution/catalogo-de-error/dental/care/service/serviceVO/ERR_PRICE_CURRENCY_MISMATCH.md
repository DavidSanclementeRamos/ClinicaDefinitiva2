## ERR_PRICE_CURRENCY_MISMATCH

- **Código:** ERR_PRICE_CURRENCY_MISMATCH
- **Nombre corto:** Monedas incompatibles
- **Mensaje base:** "No se pueden operar precios con monedas distintas"
- **Descripción:**  
  Evita operaciones aritméticas o comparaciones entre precios expresados en monedas diferentes sin conversión explícita.
- **Operación / Caso de uso:** CALCULAR_TOTAL_PRECIO (calculatePriceTotal)
- **Regla de negocio:** RN-PRICE-004 — Consistencia de moneda en operaciones (ver ADR-123)
- **Contexto del agregado:** PRICE
- **Tipo semántico:** Integridad de negocio
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "priceA.currency=USD priceB.currency=EUR"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita resultados engañosos y protege la exactitud financiera en cotizaciones y facturación.
- **Ejemplo de uso:**
  ```java
  if (!priceA.getCurrency().equals(priceB.getCurrency())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PRICE_CURRENCY_MISMATCH);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** USD vs EUR → excepción.
    - **Integración:** POST /orders con precios en distintas monedas → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Price.

---