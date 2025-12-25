## ERR_DENTIST_INVALID_SPECIALTY

- **Código:** ERR_DENTIST_INVALID_SPECIALTY
- **Nombre corto:** Especialidad inválida odontólogo
- **Mensaje base:** "La especialidad proporcionada no es reconocida"
- **Descripción clínica:**  
  Valida que la especialidad registrada para un odontólogo esté dentro de las reconocidas por la clínica. Evita inconsistencias y asegura que el profesional tenga competencias verificadas.
- **Operación / Caso de uso:** REGISTRAR_ODONTOLOGO (registerDentist)
- **Regla de negocio:** RN-DENTIST-007 — Validación de especialidad (ver ADR-20)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Especialidad 'Odontología Espacial' no reconocida para odontólogo ID 72"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes sean atendidos por profesionales con especialidades válidas y certificadas.
- **Ejemplo de uso:**
  ```java
  if (!SpecialtyRegistry.isValid(dentist.specialty)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_INVALID_SPECIALTY);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** especialidad inválida → excepción.
    - **Integración:** POST /dentists con especialidad no reconocida → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---