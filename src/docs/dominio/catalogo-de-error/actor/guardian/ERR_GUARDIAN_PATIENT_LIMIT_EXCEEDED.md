## ERR_GUARDIAN_PATIENT_LIMIT_EXCEEDED

- **Código:** ERR_GUARDIAN_PATIENT_LIMIT_EXCEEDED
- **Nombre corto:** Límite de pacientes excedido
- **Mensaje base:** "El responsable ha alcanzado el límite de pacientes a cargo"
- **Descripción clínica:**  
  Impide que un responsable tenga más pacientes asignados de los permitidos por la normativa clínica. Protege la calidad de la atención y evita sobrecarga de responsabilidades legales.
- **Operación / Caso de uso:** ASIGNAR_RESPONSABLE (assignGuardianToPatient)
- **Regla de negocio:** RN-GUARDIAN-012 — Límite máximo de pacientes por responsable (ver ADR-21)
- **Contexto del agregado:** RESPONSABLE (GUARDIAN)
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Guardian ID 102 intentó asignarse a paciente ID 45 superando límite de 5 pacientes"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que cada paciente reciba atención adecuada y que los responsables no asuman más casos de los que pueden manejar.
- **Ejemplo de uso:**
  ```java
  if (guardian.assignedPatients.size() >= MAX_PATIENTS) {
      throw new DomainAggregateException(ErrorCatalog.ERR_GUARDIAN_PATIENT_LIMIT_EXCEEDED);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** guardian con pacientes > límite → excepción.
    - **Integración:** POST /guardians/{id}/assignPatient superando límite → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Guardian.

---