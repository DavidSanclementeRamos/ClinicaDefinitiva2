## ERR_SERVICE_STATUS_NULL

- **Código:** ERR_SERVICE_STATUS_NULL
- **Nombre corto:** Estado del servicio nulo
- **Mensaje base:** "El estado del servicio no puede ser nulo"
- **Descripción:**  
  Evita servicios sin estado (activo, inactivo, borrador) que dificulten reglas de visibilidad y facturación.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_ESTADO_SERVICIO (createOrUpdateServiceStatus)
- **Regla de negocio:** RN-SERVICESTATUS-001 — Estado obligatorio (ver ADR-131)
- **Contexto del agregado:** SERVICE_STATUS
- **Tipo semántico:** Validación de integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "status=null para serviceId=555"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura que los pacientes y sistemas vean el estado correcto del servicio y evita cobros indebidos.
- **Ejemplo de uso:**
  ```java
  if (service.getStatus() == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_STATUS_NULL);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** status=null → excepción.
    - **Integración:** PUT /services/{id}/status con status null → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo ServiceStatus.

---