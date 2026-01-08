## ERR_SERVICE_CODE_DUPLICATE

- **Código:** ERR_SERVICE_CODE_DUPLICATE
- **Nombre corto:** Código duplicado
- **Mensaje base:** "El código de servicio ya existe y debe ser único"
- **Descripción:**  
  Evita la creación de servicios con códigos duplicados que generan ambigüedad en facturación y registros clínicos.
- **Operación / Caso de uso:** CREAR_CODIGO_SERVICIO (createServiceCode)
- **Regla de negocio:** RN-SERVICECODE-004 — Unicidad de código (ver ADR-129)
- **Contexto del agregado:** SERVICE_CODE
- **Tipo semántico:** Integridad de negocio
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "code='SRV-001' ya existe"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Mantiene integridad de datos y evita errores administrativos que afectan al paciente.
- **Ejemplo de uso:**
  ```java
  if (serviceRepository.existsByCode(service.getCode())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_CODE_DUPLICATE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** code duplicate check → excepción.
    - **Integración:** POST /services con code existente → 409.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo ServiceCode.

---