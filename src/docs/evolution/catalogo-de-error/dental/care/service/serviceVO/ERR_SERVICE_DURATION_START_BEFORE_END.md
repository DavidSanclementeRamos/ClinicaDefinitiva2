## ERR_SERVICE_DURATION_START_BEFORE_END

- **Código:** ERR_SERVICE_DURATION_START_BEFORE_END
- **Nombre corto:** Inicio posterior al fin
- **Mensaje base:** "El tiempo de inicio debe ser anterior al tiempo de fin"
- **Descripción:**  
  Evita duraciones con orden temporal invertido que invalidan agendamiento y cálculos de disponibilidad.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_DURACION_SERVICIO (createOrUpdateServiceDuration)
- **Regla de negocio:** RN-DURATION-002 — Inicio antes de fin (ver ADR-124)
- **Contexto del agregado:** SERVICE_DURATION
- **Tipo semántico:** Validación temporal
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "start=2026-02-01T12:00 end=2026-02-01T10:00"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita errores de programación que podrían causar solapamientos o citas imposibles.
- **Ejemplo de uso:**
  ```java
  if (!duration.getStart().isBefore(duration.getEnd())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DURATION_START_BEFORE_END);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** start after end → excepción.
    - **Integración:** POST /services/{id}/durations con start >= end → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Duration.

---