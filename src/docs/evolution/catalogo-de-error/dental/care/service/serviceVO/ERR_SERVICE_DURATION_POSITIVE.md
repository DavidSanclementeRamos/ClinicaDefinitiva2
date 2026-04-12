## ERR_SERVICE_DURATION_POSITIVE

- **Código:** ERR_SERVICE_DURATION_POSITIVE
- **Nombre corto:** Duración no positiva
- **Mensaje base:** "La duración debe ser positiva"
- **Descripción:**  
  Evita duraciones cero o negativas que invalidan la programación y la facturación.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_DURACION_SERVICIO (createOrUpdateServiceDuration)
- **Regla de negocio:** RN-DURATION-004 — Duración positiva (ver ADR-125)
- **Contexto del agregado:** SERVICE_DURATION
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "durationMinutes=0 para serviceId=987"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita inconsistencias operativas y protege la integridad de la agenda.
- **Ejemplo de uso:**
  ```java
  if (duration.getMinutes() <= 0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DURATION_POSITIVE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** duration=0 → excepción.
    - **Integración:** POST /services/{id}/durations con duration=-15 → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Duration.

---