## ERR_AVAIL_ID_BLANK

- **Código:** ERR_AVAIL_ID_BLANK
- **Nombre corto:** AvailabilityId vacío
- **Mensaje base:** "El valor de AvailabilityId no puede estar vacío"
- **Descripción clínica:**  
  Impide que el identificador de disponibilidad sea una cadena vacía. Protege la integridad de datos y evita registros inválidos.
- **Operación / Caso de uso:** CREAR_DISPONIBILIDAD (createAvailability)
- **Regla de negocio:** RN-AVAIL-002 — Identificador no vacío (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "AvailabilityId vacío en creación de disponibilidad"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita inconsistencias en la trazabilidad clínica.
- **Ejemplo de uso:**
  ```java
  if (availabilityId.trim().isEmpty()) {
      throw new ValueObjectValidationException(ErrorCatalog.ERR_AVAIL_ID_BLANK);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** availabilityId = "" → excepción.
    - **Integración:** POST /availability → 400 si ID vacío.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo VO Availability.

---
