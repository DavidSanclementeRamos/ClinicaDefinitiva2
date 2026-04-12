## ERR_SERVICE_CATEGORY_NULL_OR_BLANK

- **Código:** ERR_SERVICE_CATEGORY_NULL_OR_BLANK
- **Nombre corto:** Categoría de servicio nula o vacía
- **Mensaje base:** "La categoría del servicio no puede ser nula ni estar vacía"
- **Descripción:**  
  Evita entradas de catálogo sin categoría, lo que dificulta filtrado, navegación y políticas de cobertura.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_CATALOGO_SERVICIO (createOrUpdateServiceCatalog)
- **Regla de negocio:** RN-SERVICECATALOG-002 — Categoría obligatoria en catálogo (ver ADR-132)
- **Contexto del agregado:** SERVICE_CATALOG
- **Tipo semántico:** Validación de entrada
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "catalogEntry.category=null para entryId=102"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Mejora la accesibilidad de la oferta de servicios y evita errores en la asignación de coberturas.
- **Ejemplo de uso:**
  ```java
  if (catalogEntry.getCategory() == null || catalogEntry.getCategory().isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_CATEGORY_NULL_OR_BLANK);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** category=null → excepción.
    - **Integración:** POST /service-catalog entries sin category → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo ServiceCatalog.

---

