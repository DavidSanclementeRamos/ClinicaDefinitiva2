## ERR_SERVICE_NAME_NULL_OR_BLANK

- **Código:** ERR_SERVICE_NAME_NULL_OR_BLANK
- **Nombre corto:** Nombre de servicio nulo o vacío
- **Mensaje base:** "El nombre del servicio no puede ser nulo ni estar vacío"
- **Descripción:**  
  Evita servicios sin nombre que impidan identificación por pacientes y personal clínico.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_CATALOGO_SERVICIO (createOrUpdateServiceCatalog)
- **Regla de negocio:** RN-SERVICECATALOG-001 — Nombre obligatorio en catálogo (ver ADR-132)
- **Contexto del agregado:** SERVICE_CATALOG
- **Tipo semántico:** Validación de entrada
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "catalogEntry.name=null para entryId=101"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Facilita la comunicación con el paciente y la correcta presentación de servicios.
- **Ejemplo de uso:**
  ```java
  if (catalogEntry.getName() == null || catalogEntry.getName().isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_NAME_NULL_OR_BLANK);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** name="" → excepción.
    - **Integración:** POST /service-catalog entries sin name → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo ServiceCatalog.

---
