## ERR_AVAIL_EXTENSION_CONFLICT

- **Código:** ERR_AVAIL_EXTENSION_CONFLICT
- **Nombre corto:** Conflicto de extensión
- **Mensaje base:** "No puede extenderse sobre otro bloque ya registrado"
- **Descripción clínica:**  
  Impide que una disponibilidad se extienda sobre otra ya existente. Protege la coherencia de la agenda y evita duplicaciones.
- **Operación / Caso de uso:** EXTENDER_DISPONIBILIDAD (extendAvailability)
- **Regla de negocio:** RN-AVAIL-009 — Restricción de extensión (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Disponibilidad ID 456 intenta extenderse sobre bloque existente"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita inconsistencias en la agenda y protege la confianza de pacientes y profesionales.
- **Ejemplo de uso:**
  ```java
  if (availability.overlapsWith(existingBlock)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AVAIL_EXTENSION_CONFLICT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** extensión sobre bloque existente → excepción.
    - **Integración:** PUT /availability/{id}/extend → 409 si conflicto.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Availability.

---