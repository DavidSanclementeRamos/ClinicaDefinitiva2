## ERR_DENTIST_NOT_AVAILABLE

- **Código:** ERR_DENTIST_NOT_AVAILABLE
- **Nombre corto:** Odontólogo no disponible
- **Mensaje base:** "El odontólogo no está disponible para agendar en este momento"
- **Descripción clínica:**  
  Impide que se agenden citas con un odontólogo que no tiene disponibilidad activa. Protege la experiencia del paciente y evita asignaciones imposibles de cumplir.
- **Operación / Caso de uso:** AGENDAR_CITA (scheduleAppointment)
- **Regla de negocio:** RN-DENTIST-005 — Validación de disponibilidad (ver ADR-20)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Intento de agendar cita con odontólogo ID 56 sin disponibilidad activa"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita frustración de pacientes y asegura que solo se agenden citas con profesionales realmente disponibles.
- **Ejemplo de uso:**
  ```java
  if (!dentist.isAvailable()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_NOT_AVAILABLE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** odontólogo sin disponibilidad → excepción.
    - **Integración:** POST /appointments con odontólogo inactivo → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---