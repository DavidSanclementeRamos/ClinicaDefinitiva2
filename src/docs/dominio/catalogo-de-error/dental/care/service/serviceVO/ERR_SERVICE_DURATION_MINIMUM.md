## ERR_SERVICE_DURATION_MINIMUM

- **Código:** ERR_SERVICE_DURATION_MINIMUM
- **Nombre corto:** Duración por debajo del mínimo
- **Mensaje base:** "La duración mínima es {min} minutos"
- **Descripción:**  
  Valida que la duración no sea inferior al mínimo operativo definido para el servicio, garantizando tiempo suficiente para atención segura.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_DURACION_SERVICIO (createOrUpdateServiceDuration)
- **Regla de negocio:** RN-DURATION-005 — Duración mínima configurable (ver ADR-126)
- **Contexto del agregado:** SERVICE_DURATION
- **Tipo semántico:** Validación de negocio
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "duration=10 minutos, min=15"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege la calidad asistencial evitando tiempos insuficientes para procedimientos seguros.
- **Ejemplo de uso:**
  ```java
  if (duration.getMinutes() < service.getMinDurationMinutes()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DURATION_MINIMUM);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** duration=10 min, min=15 → excepción.
    - **Integración:** POST /services/{id}/durations con duration menor al mínimo → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Duration.

---