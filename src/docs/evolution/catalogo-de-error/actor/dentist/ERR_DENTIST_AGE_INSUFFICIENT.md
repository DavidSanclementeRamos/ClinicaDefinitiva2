## ERR_DENTIST_AGE_INSUFFICIENT

- **Código:** ERR_DENTIST_AGE_INSUFFICIENT
- **Nombre corto:** Edad mínima odontólogo
- **Mensaje base:** "El odontólogo debe tener al menos 25 años"
- **Descripción clínica:**  
  Garantiza que los odontólogos registrados tengan la edad mínima requerida para ejercer en la clínica. Esta regla protege la idoneidad profesional y asegura cumplimiento con normativas de salud.
- **Operación / Caso de uso:** CREAR_ODONTOLOGO (registerDentist)
- **Regla de negocio:** RN-DENTIST-001 — Edad mínima odontólogo (ver ADR-20 Alcance Experimental)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Intento de registrar odontólogo con edad 23"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que solo profesionales con edad suficiente ejerzan, protegiendo la seguridad clínica de los pacientes.
- **Ejemplo de uso:**
  ```java
  if (dentist.age < 25) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_AGE_INSUFFICIENT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** odontólogo con edad <25 → excepción.
    - **Integración:** POST /dentists con edad 23 → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---
