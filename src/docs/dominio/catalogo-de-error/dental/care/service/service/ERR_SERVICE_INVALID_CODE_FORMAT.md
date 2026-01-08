## ERR_SERVICE_INVALID_CODE_FORMAT

- **Código:** ERR_SERVICE_INVALID_CODE_FORMAT
- **Nombre corto:** Formato de código inválido
- **Mensaje base:** "El código de servicio debe tener entre 4 y 15 caracteres alfanuméricos"
- **Descripción:**  
  Valida que el código cumpla patrón y longitud esperada para interoperabilidad y legibilidad.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_CODIGO_SERVICIO (createOrUpdateServiceCode)
- **Regla de negocio:** RN-SERVICE-013 — Formato y longitud de código (ver ADR-118)
- **Contexto del agregado:** SERVICE_CODE
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "code='A!' longitud=2"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Mejora integridad de datos y evita problemas en integraciones y búsquedas.
- **Ejemplo de uso:**
  ```java
  if (!code.matches("^[A-Za-z0-9]{4,15}$")) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_INVALID_CODE_FORMAT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** code="A!" → excepción.
    - **Integración:** POST /services con code inválido → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---
