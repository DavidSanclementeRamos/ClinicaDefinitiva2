
## ERR_DENTIST_EMPTY_AVAILABILITY

- **Código:** ERR_DENTIST_EMPTY_AVAILABILITY
- **Nombre corto:** Disponibilidad vacía odontólogo
- **Mensaje base:** "La disponibilidad del odontólogo no puede quedar vacía"
- **Descripción clínica:**  
  Garantiza que un odontólogo mantenga al menos un bloque de disponibilidad en su agenda. Evita que profesionales registrados queden sin horarios asignables, lo que afectaría la atención de pacientes.
- **Operación / Caso de uso:** ACTUALIZAR_DISPONIBILIDAD (updateAvailability)
- **Regla de negocio:** RN-DENTIST-010 — Disponibilidad no vacía (ver ADR-20)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Odontólogo ID 34 con disponibilidad vacía"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes siempre tengan acceso a horarios válidos y evita registros inconsistentes.
- **Ejemplo de uso:**
  ```java
  if (dentist.availability.isEmpty()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_EMPTY_AVAILABILITY);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** disponibilidad vacía → excepción.
    - **Integración:** PUT /dentists/{id}/availability con lista vacía → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---