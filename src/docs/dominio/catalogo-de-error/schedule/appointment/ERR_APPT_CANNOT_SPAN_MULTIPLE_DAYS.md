## ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS

- **Código:** ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS
- **Nombre corto:** Cita cruza días
- **Mensaje base:** "La cita no puede cruzar días"
- **Descripción clínica:**  
  Impide definir citas cuyo rango temporal atraviesa medianoche o días diferentes. Protege la coherencia operativa, evita errores de disponibilidad y simplifica la contabilidad y notificaciones.
- **Operación / Caso de uso:** CREAR_CITA (createAppointment)
- **Regla de negocio:** RN-APPT-013 — La cita debe ocurrir en un único día calendario (ver ADR-25)
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Cita solicitada 2025-01-10 23:30 → 2025-01-11 00:30 (cruza días)"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita ambigüedades de cobertura, disponibilidad y facturación; mantiene reglas claras para programación clínica.
- **Ejemplo de uso:**
  ```java
  if (!DateRange.of(start, end).isSameDay()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_APPT_CANNOT_SPAN_MULTIPLE_DAYS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** rango start/end que cruza días → excepción.
    - **Integración:** POST /appointments → 400 si la cita atraviesa medianoche.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Appointment.