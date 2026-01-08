## ERR_SERVICE_CODE_REQUIRED

- **Código:** ERR_SERVICE_CODE_REQUIRED
- **Nombre corto:** Código de servicio obligatorio
- **Mensaje base:** "El código de servicio no puede ser nulo ni estar en blanco"
- **Descripción:**  
  Garantiza que cada servicio tenga un identificador único legible que facilite referencia, facturación y trazabilidad.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_CODIGO_SERVICIO (createOrUpdateServiceCode)
- **Regla de negocio:** RN-SERVICECODE-001 — Código obligatorio (ver ADR-128)
- **Contexto del agregado:** SERVICE_CODE
- **Tipo semántico:** Validación de integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "serviceCode=null para serviceId=333"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita ambigüedad administrativa y facilita auditoría.
- **Ejemplo de uso:**
  ```java
  if (service.getCode() == null || service.getCode().isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_CODE_REQUIRED);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** code="" → excepción.
    - **Integración:** POST /services sin code → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo ServiceCode.

---