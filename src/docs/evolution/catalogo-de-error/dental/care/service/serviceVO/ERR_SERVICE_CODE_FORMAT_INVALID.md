
## ERR_SERVICE_CODE_FORMAT_INVALID

- **Código:** ERR_SERVICE_CODE_FORMAT_INVALID
- **Nombre corto:** Formato de código inválido
- **Mensaje base:** "El código de servicio solo puede contener letras mayúsculas, números y guiones"
- **Descripción:**  
  Establece un formato estándar para códigos que facilita búsquedas, integraciones y evita caracteres problemáticos en sistemas externos.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_CODIGO_SERVICIO (createOrUpdateServiceCode)
- **Regla de negocio:** RN-SERVICECODE-002 — Formato permitido para código (ver ADR-128)
- **Contexto del agregado:** SERVICE_CODE
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "code recibido: 'svc_01' contiene '_' no permitido"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Mejora interoperabilidad y reduce errores en integraciones con terceros.
- **Ejemplo de uso:**
  ```java
  if (!service.getCode().matches("^[A-Z0-9\\-]+$")) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_CODE_FORMAT_INVALID);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** code="svc_01" → excepción.
    - **Integración:** POST /services con code inválido → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo ServiceCode.

---
