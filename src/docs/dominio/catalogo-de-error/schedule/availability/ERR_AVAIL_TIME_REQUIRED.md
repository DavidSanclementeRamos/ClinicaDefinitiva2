## ERR_AVAIL_TIME_REQUIRED

- **Código:** ERR_AVAIL_TIME_REQUIRED
- **Nombre corto:** Horas requeridas
- **Mensaje base:** "Debe especificarse hora de inicio y fin para crear disponibilidad"
- **Descripción clínica:**  
  Obliga a registrar explícitamente las horas de inicio y fin de cada disponibilidad. Protege la coherencia operativa y evita registros incompletos.
- **Operación / Caso de uso:** CREAR_DISPONIBILIDAD (createAvailability)
- **Regla de negocio:** RN-AVAIL-012 — Horas obligatorias (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Disponibilidad creada sin hora de inicio/fin"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita disponibilidades ambiguas que comprometan la programación de citas.
- **Ejemplo de uso:**
  ```java
  if (start == null || end == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AVAIL_TIME_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** disponibilidad sin hora → excepción.
    - **Integración:** POST /availability → 400 si faltan horas.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Availability.

---
