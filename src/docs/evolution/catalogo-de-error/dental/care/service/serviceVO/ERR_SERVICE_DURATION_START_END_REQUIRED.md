## ERR_SERVICE_DURATION_START_END_REQUIRED

- **Código:** ERR_SERVICE_DURATION_START_END_REQUIRED
- **Nombre corto:** Tiempos inicio y fin requeridos
- **Mensaje base:** "Los tiempos de inicio y fin no pueden ser nulos"
- **Descripción:**  
  Evita duraciones incompletas que impidan agendamiento, cálculo de disponibilidad y coordinación de recursos.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_DURACION_SERVICIO (createOrUpdateServiceDuration)
- **Regla de negocio:** RN-DURATION-001 — Inicio y fin obligatorios (ver ADR-124)
- **Contexto del agregado:** SERVICE_DURATION
- **Tipo semántico:** Validación de integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "start=null end=2026-02-01T10:00 serviceId=321"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura planificación segura y evita solapamientos o huecos en la atención al paciente.
- **Ejemplo de uso:**
  ```java
  if (duration.getStart() == null || duration.getEnd() == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SERVICE_DURATION_START_END_REQUIRED);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** start=null → excepción.
    - **Integración:** POST /services/{id}/durations con start missing → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Duration.

---