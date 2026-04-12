## ERR_SERVICE_CODE_LENGTH_INVALID

- **Código:** ERR_SERVICE_CODE_LENGTH_INVALID
- **Nombre corto:** Longitud de código inválida
- **Mensaje base:** "El código de servicio debe tener entre {min} y {max} caracteres"
- **Descripción:**  
  Asegura que los códigos cumplan límites de longitud para legibilidad y compatibilidad con sistemas externos.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_CODIGO_SERVICIO (createOrUpdateServiceCode)
- **Regla de negocio:** RN-SERVICECODE-003 — Longitud válida para código (ver ADR-128)
- **Contexto del agregado:** SERVICE_CODE
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "code recibido: 'AB' longitud 2, min=3 max=10"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita códigos demasiado cortos o largos que dificulten uso y mantenimiento.
- **Ejemplo de uso:**
  ```java
  if (service.getCode().length() < min || service.getCode().length() > max) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_CODE_LENGTH_INVALID);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** code length 2 with min=3 → excepción.
    - **Integración:** POST /services con code demasiado largo → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo ServiceCode.

---