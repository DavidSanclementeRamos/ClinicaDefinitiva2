## ERR_SERVICE_CATEGORY_MISMATCH

- **Código:** ERR_SERVICE_CATEGORY_MISMATCH
- **Nombre corto:** Categoría incompatible
- **Mensaje base:** "La categoría del servicio no coincide con el tipo de detalles"
- **Descripción:**  
  Valida coherencia entre la categoría declarada del servicio y los detalles asociados (por ejemplo, materiales, duración, requerimientos clínicos) para evitar inconsistencias en catálogo y cobertura.
- **Operación / Caso de uso:** VALIDAR_CATALOGO_SERVICIO (validateServiceCatalogEntry)
- **Regla de negocio:** RN-SERVICE-004 — Coherencia categoría-detalle (ver ADR-111)
- **Contexto del agregado:** SERVICE_CATALOG
- **Tipo semántico:** Integridad de catálogo
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "category=IMPLANTOLOGY pero details.type=CONSULT"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita asignación incorrecta de recursos y problemas de cobertura o consentimiento por información contradictoria.
- **Ejemplo de uso:**
  ```java
  if (!categoryService.matchesDetails(entry.getCategory(), entry.getDetails())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_CATEGORY_MISMATCH);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** category IMPLANTOLOGY con details CONSULT → excepción.
    - **Integración:** POST /service-catalog con categoría incompatible → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---