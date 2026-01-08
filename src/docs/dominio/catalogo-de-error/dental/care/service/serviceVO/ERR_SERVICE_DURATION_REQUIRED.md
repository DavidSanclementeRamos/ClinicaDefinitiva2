## ERR_SERVICE_DURATION_REQUIRED

- **Código:** ERR_SERVICE_DURATION_REQUIRED
- **Nombre corto:** Duración requerida
- **Mensaje base:** "La duración no puede ser nula"
- **Descripción:**  
  Evita servicios sin duración definida que impiden cálculo de agenda y asignación de recursos.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_DURACION_SERVICIO (createOrUpdateServiceDuration)
- **Regla de negocio:** RN-DURATION-003 — Duración obligatoria (ver ADR-125)
- **Contexto del agregado:** SERVICE_DURATION
- **Tipo semántico:** Validación de entrada
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "duration=null para serviceId=654"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura que el paciente reciba atención con tiempos previstos y evita expectativas erróneas.
- **Ejemplo de uso:**
  ```java
  if (duration.getMinutes() == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DURATION_REQUIRED);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** duration=null → excepción.
    - **Integración:** POST /services/{id}/durations sin duration → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Duration.

---
