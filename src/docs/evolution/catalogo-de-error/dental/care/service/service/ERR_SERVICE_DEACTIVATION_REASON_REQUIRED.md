## ERR_SERVICE_DEACTIVATION_REASON_REQUIRED

- **Código:** ERR_SERVICE_DEACTIVATION_REASON_REQUIRED
- **Nombre corto:** Motivo de desactivación requerido
- **Mensaje base:** "Debe registrar motivo de desactivación con mínimo 10 caracteres"
- **Descripción:**  
  Obliga a documentar la razón de desactivación para auditoría, trazabilidad y comunicación con pacientes y equipos.
- **Operación / Caso de uso:** DESACTIVAR_SERVICIO (deactivateService)
- **Regla de negocio:** RN-SERVICE-015 — Registro de motivo de desactivación (ver ADR-120)
- **Contexto del agregado:** SERVICE
- **Tipo semántico:** Integridad documental
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "deactivationReason length=5"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Facilita rendición de cuentas y comunicación responsable con pacientes afectados.
- **Ejemplo de uso:**
  ```java
  if (deactivationReason == null || deactivationReason.trim().length() < 10) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DEACTIVATION_REASON_REQUIRED);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** reason="No" → excepción.
    - **Integración:** POST /services/{id}/deactivate con reason corto → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Service.

---