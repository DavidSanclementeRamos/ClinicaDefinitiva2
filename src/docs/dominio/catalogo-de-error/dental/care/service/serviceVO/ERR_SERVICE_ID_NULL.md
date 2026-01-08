
## ERR_SERVICE_ID_NULL

- **Código:** ERR_SERVICE_ID_NULL
- **Nombre corto:** Identificador nulo
- **Mensaje base:** "El identificador del servicio no puede ser nulo"
- **Descripción:**  
  Evita operaciones sobre servicios sin identificador, lo que impediría localización y trazabilidad.
- **Operación / Caso de uso:** OBTENER_O_ACTUALIZAR_SERVICIO (getOrUpdateService)
- **Regla de negocio:** RN-SERVICEID-001 — ID obligatorio (ver ADR-130)
- **Contexto del agregado:** SERVICE
- **Tipo semántico:** Validación de integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "serviceId=null en solicitud"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita modificaciones accidentales y garantiza responsabilidad en cambios de datos clínicos.
- **Ejemplo de uso:**
  ```java
  if (serviceId == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_ID_NULL);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** serviceId=null → excepción.
    - **Integración:** PUT /services/null → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo ServiceId.

---