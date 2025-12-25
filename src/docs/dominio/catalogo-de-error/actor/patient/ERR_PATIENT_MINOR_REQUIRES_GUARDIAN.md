## ERR_PATIENT_MINOR_REQUIRES_GUARDIAN

- **Código:** ERR_PATIENT_MINOR_REQUIRES_GUARDIAN
- **Nombre corto:** Paciente menor requiere responsable
- **Mensaje base:** "Los pacientes menores de edad deben tener un responsable legal vinculado"
- **Descripción clínica:**  
  Obliga a que todo paciente menor de edad esté vinculado a un responsable legal. Protege la validez jurídica de las autorizaciones médicas y asegura que los menores reciban atención bajo supervisión adecuada.
- **Operación / Caso de uso:** REGISTRAR_PACIENTE (registerPatient)
- **Regla de negocio:** RN-PATIENT-008 — Paciente menor requiere responsable (ver ADR-22)
- **Contexto del agregado:** PACIENTE
- **Tipo semántico:** Autorización
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Paciente ID 45 menor de edad sin responsable vinculado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los menores estén protegidos legalmente y que las decisiones médicas sean tomadas por un adulto responsable.
- **Ejemplo de uso:**
  ```java
  if (patient.age < 18 && patient.guardian == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** paciente menor sin responsable → excepción.
    - **Integración:** POST /patients con edad 12 sin guardian → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Patient.

---