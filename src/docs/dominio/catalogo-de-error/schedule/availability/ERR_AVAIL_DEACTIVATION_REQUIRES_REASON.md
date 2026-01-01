## ERR_AVAIL_DEACTIVATION_REQUIRES_REASON

- **Código:** ERR_AVAIL_DEACTIVATION_REQUIRES_REASON
- **Nombre corto:** Desactivación sin motivo
- **Mensaje base:** "La desactivación requiere motivo obligatorio"
- **Descripción clínica:**  
  Obliga a registrar un motivo clínico o administrativo al desactivar una disponibilidad. Protege la trazabilidad y permite auditoría.
- **Operación / Caso de uso:** DESACTIVAR_DISPONIBILIDAD (deactivateAvailability)
- **Regla de negocio:** RN-AVAIL-008 — Desactivación requiere motivo (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Disponibilidad ID 123 desactivada sin motivo"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza transparencia en la gestión de horarios y protege la confianza clínica.
- **Ejemplo de uso:**
  ```java
  if (reason == null || reason.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AVAIL_DEACTIVATION_REQUIRES_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** desactivación sin motivo → excepción.
    - **Integración:** PUT /availability/{id}/deactivate → 400 si falta motivo.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Availability.

---