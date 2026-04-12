## ERR_PATIENT_INVALID_AGE

- **Código:** ERR_PATIENT_INVALID_AGE
- **Nombre corto:** Edad inválida paciente
- **Mensaje base:** "La edad del paciente debe estar en el rango válido (0-120 años)"
- **Descripción clínica:**  
  Valida que la edad registrada para un paciente esté dentro de un rango clínicamente aceptable. Evita errores de captura y asegura coherencia en la información demográfica utilizada para diagnósticos y tratamientos.
- **Operación / Caso de uso:** REGISTRAR_PACIENTE (registerPatient)
- **Regla de negocio:** RN-PATIENT-006 — Validación de edad paciente (ver ADR-22)
- **Contexto del agregado:** PACIENTE
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Paciente con edad 135 fuera de rango permitido"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los datos clínicos sean veraces y evita registros fraudulentos o inconsistentes.
- **Ejemplo de uso:**
  ```java
  if (patient.age < 0 || patient.age > 120) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PATIENT_INVALID_AGE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** edad fuera de rango → excepción.
    - **Integración:** POST /patients con edad 135 → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Patient.

---