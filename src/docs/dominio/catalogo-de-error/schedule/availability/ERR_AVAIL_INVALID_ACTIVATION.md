## ERR_AVAIL_INVALID_ACTIVATION

- **Código:** ERR_AVAIL_INVALID_ACTIVATION
- **Nombre corto:** Activación inválida
- **Mensaje base:** "No puede activarse la disponibilidad en el estado actual"
- **Descripción clínica:**  
  Impide activar disponibilidades que no cumplen condiciones de estado (ej. ya activadas o bloqueadas). Protege la coherencia operativa y evita duplicaciones.
- **Operación / Caso de uso:** ACTIVAR_DISPONIBILIDAD (activateAvailability)
- **Regla de negocio:** RN-AVAIL-014 — Restricción de activación según estado (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Disponibilidad ID 654 no puede activarse en estado BLOQUEADO"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita inconsistencias en la agenda y protege la confianza de pacientes y profesionales.
- **Ejemplo de uso:**
  ```java
  if (!availability.canActivate()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AVAIL_INVALID_ACTIVATION);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** disponibilidad en estado inválido → excepción.
    - **Integración:** PUT /availability/{id}/activate → 409 si estado no permite.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Availability.

---