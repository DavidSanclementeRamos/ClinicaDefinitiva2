## ERR_AVAIL_ID_REQUIRED

- **Código:** ERR_AVAIL_ID_REQUIRED
- **Nombre corto:** AvailabilityId requerido
- **Mensaje base:** "El valor de AvailabilityId no puede ser nulo"
- **Descripción clínica:**  
  Impide crear disponibilidades sin identificador válido. Protege la trazabilidad y evita registros huérfanos.
- **Operación / Caso de uso:** CREAR_DISPONIBILIDAD (createAvailability)
- **Regla de negocio:** RN-AVAIL-001 — Identificador obligatorio (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "AvailabilityId nulo en creación de disponibilidad"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que cada disponibilidad sea única y rastreable.
- **Ejemplo de uso:**
  ```java
  if (availabilityId == null) {
      throw new ValueObjectValidationException(ErrorCatalog.ERR_AVAIL_ID_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** availabilityId = null → excepción.
    - **Integración:** POST /availability → 400 si falta ID.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo VO Availability.

---
