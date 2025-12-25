## ERR_DENTIST_INVALID_VACATION_RANGE

- **Código:** ERR_DENTIST_INVALID_VACATION_RANGE
- **Nombre corto:** Rango de vacaciones inválido
- **Mensaje base:** "El rango de vacaciones solicitado es inválido"
- **Descripción clínica:**  
  Valida que el período de vacaciones ingresado por el odontólogo tenga un rango coherente (fecha inicio < fecha fin, duración dentro de límites permitidos). Evita inconsistencias en la planificación de la clínica.
- **Operación / Caso de uso:** REGISTRAR_VACACIONES (registerVacation)
- **Regla de negocio:** RN-DENTIST-012 — Validación de rango de vacaciones (ver ADR-20)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Vacaciones inválidas: inicio 2025-12-30, fin 2025-12-25 para odontólogo ID 91"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes no sufran cancelaciones por rangos de vacaciones incoherentes y protege la planificación clínica.
- **Ejemplo de uso:**
  ```java
  if (vacation.startDate.isAfter(vacation.endDate)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_INVALID_VACATION_RANGE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** fecha inicio > fecha fin → excepción.
    - **Integración:** POST /dentists/{id}/vacations con rango inválido → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---

