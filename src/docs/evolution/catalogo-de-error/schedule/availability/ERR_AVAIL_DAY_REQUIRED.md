## ERR_AVAIL_DAY_REQUIRED

- **Código:** ERR_AVAIL_DAY_REQUIRED
- **Nombre corto:** Día requerido
- **Mensaje base:** "Debe especificarse un día de la semana válido para crear disponibilidad"
- **Descripción clínica:**  
  Obliga a registrar el día de la semana en el que aplica la disponibilidad. Protege la coherencia de la agenda y evita bloques sin contexto temporal.
- **Operación / Caso de uso:** CREAR_DISPONIBILIDAD (createAvailability)
- **Regla de negocio:** RN-AVAIL-011 — Día obligatorio (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Disponibilidad creada sin día válido (valor recibido: null)"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que las disponibilidades sean comprensibles y útiles para pacientes y personal administrativo.
- **Ejemplo de uso:**
  ```java
  if (dayOfWeek == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AVAIL_DAY_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** disponibilidad sin día → excepción.
    - **Integración:** POST /availability → 400 si falta día.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Availability.

---