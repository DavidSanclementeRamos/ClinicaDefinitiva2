## ERR_AVAIL_OVERLAP_CONFLICT

- **Código:** ERR_AVAIL_OVERLAP_CONFLICT
- **Nombre corto:** Conflicto de solapamiento
- **Mensaje base:** "No puede haber dos bloques que se solapen para el mismo profesional"
- **Descripción clínica:**  
  Impide que un odontólogo registre dos disponibilidades que se superpongan. Protege la coherencia de la agenda y evita conflictos operativos.
- **Operación / Caso de uso:** CREAR_DISPONIBILIDAD (createAvailability)
- **Regla de negocio:** RN-AVAIL-004 — Restricción de solapamiento (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Odontólogo ID 87 ya tiene disponibilidad en el rango solicitado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita errores administrativos y garantiza que cada bloque de disponibilidad sea único y válido.
- **Ejemplo de uso:**
  ```java
  if (dentist.hasAvailabilityOverlap(newAvailability)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AVAIL_OVERLAP_CONFLICT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** disponibilidad solapada → excepción.
    - **Integración:** POST /availability → 409 si conflicto.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Availability.

---
