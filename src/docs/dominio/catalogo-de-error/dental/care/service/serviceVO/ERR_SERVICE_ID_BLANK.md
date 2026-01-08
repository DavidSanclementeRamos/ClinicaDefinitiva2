## ERR_SERVICE_ID_BLANK

- **Código:** ERR_SERVICE_ID_BLANK
- **Nombre corto:** Identificador vacío
- **Mensaje base:** "El identificador del servicio no puede estar vacío"
- **Descripción:**  
  Evita identificadores vacíos que impiden operaciones REST y trazabilidad.
- **Operación / Caso de uso:** OBTENER_O_ACTUALIZAR_SERVICIO (getOrUpdateService)
- **Regla de negocio:** RN-SERVICEID-002 — ID no vacío (ver ADR-130)
- **Contexto del agregado:** SERVICE
- **Tipo semántico:** Validación de entrada
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "serviceId='' en solicitud"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita ambigüedad y errores en rutas y permisos.
- **Ejemplo de uso:**
  ```java
  if (serviceId.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_ID_BLANK);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** serviceId="" → excepción.
    - **Integración:** GET /services/ → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo ServiceId.

---
