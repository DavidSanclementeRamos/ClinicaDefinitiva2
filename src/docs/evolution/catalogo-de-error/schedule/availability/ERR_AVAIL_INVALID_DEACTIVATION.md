## ERR_AVAIL_INVALID_DEACTIVATION

- **Código:** ERR_AVAIL_INVALID_DEACTIVATION
- **Nombre corto:** Desactivación inválida
- **Mensaje base:** "No puede desactivarse la disponibilidad en el estado actual"
- **Descripción clínica:**  
  Impide desactivar disponibilidades que no cumplen condiciones de estado (ej. ya desactivadas o bloqueadas por citas). Protege la coherencia operativa y evita inconsistencias.
- **Operación / Caso de uso:** DESACTIVAR_DISPONIBILIDAD (deactivateAvailability)
- **Regla de negocio:** RN-AVAIL-013 — Restricción de desactivación según estado (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Disponibilidad ID 321 no puede desactivarse en estado ACTIVO_CON_CITAS"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita que se eliminen disponibilidades que aún afectan la agenda clínica.
- **Ejemplo de uso:**
  ```java
  if (!availability.canDeactivate()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AVAIL_INVALID_DEACTIVATION);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** disponibilidad en estado inválido → excepción.
    - **Integración:** PUT /availability/{id}/deactivate → 409 si estado no permite.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Availability.

---