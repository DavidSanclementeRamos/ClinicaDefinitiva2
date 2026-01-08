## ERR_SERVICE_TYPE_IMMUTABLE

- **Código:** ERR_SERVICE_TYPE_IMMUTABLE
- **Nombre corto:** Tipo inmutable
- **Mensaje base:** "No puede cambiar el tipo de detalles una vez establecido"
- **Descripción:**  
  Evita cambios en la clasificación estructural del servicio (p. ej., de PROCEDURE a CONSULT) que podrían invalidar historiales, contratos y reglas de facturación.
- **Operación / Caso de uso:** ACTUALIZAR_SERVICIO (updateService)
- **Regla de negocio:** RN-SERVICE-006 — Inmutabilidad de tipo de servicio (ver ADR-113)
- **Contexto del agregado:** SERVICE
- **Tipo semántico:** Integridad de dominio
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "serviceId=333 oldType=PROCEDURE newType=CONSULT"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Mantiene trazabilidad clínica y evita alteraciones que puedan afectar decisiones pasadas o cobertura.
- **Ejemplo de uso:**
  ```java
  if (!existing.getType().equals(updated.getType())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_TYPE_IMMUTABLE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** intento de cambio de tipo → excepción.
    - **Integración:** PUT /services/{id} con type distinto → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---