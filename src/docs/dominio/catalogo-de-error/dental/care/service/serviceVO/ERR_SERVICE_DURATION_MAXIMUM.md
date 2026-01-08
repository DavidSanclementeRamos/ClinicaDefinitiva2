## ERR_SERVICE_DURATION_MAXIMUM

- **Código:** ERR_SERVICE_DURATION_MAXIMUM
- **Nombre corto:** Duración por encima del máximo
- **Mensaje base:** "La duración máxima es {max} minutos ({hours} horas)"
- **Descripción:**  
  Evita planificaciones excesivamente largas que puedan afectar disponibilidad y logística clínica.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_DURACION_SERVICIO (createOrUpdateServiceDuration)
- **Regla de negocio:** RN-DURATION-006 — Duración máxima configurable (ver ADR-126)
- **Contexto del agregado:** SERVICE_DURATION
- **Tipo semántico:** Validación de negocio
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "duration=600 min, max=480 min (8 horas)"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita bloqueos de agenda y garantiza uso responsable de recursos clínicos.
- **Ejemplo de uso:**
  ```java
  if (duration.getMinutes() > service.getMaxDurationMinutes()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DURATION_MAXIMUM);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** duration=600 min, max=480 → excepción.
    - **Integración:** POST /services/{id}/durations con duration mayor al máximo → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Duration.

---