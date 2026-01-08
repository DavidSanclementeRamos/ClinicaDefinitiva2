## ERR_ORTHODONTIC_MISSING_APPLIANCE

- **Código:** ERR_ORTHODONTIC_MISSING_APPLIANCE
- **Nombre corto:** Aparato obligatorio
- **Mensaje base:** "El tipo de aparato es obligatorio y no puede estar en blanco"
- **Descripción clínica:**  
  Evita registrar tratamientos de ortodoncia sin especificar el tipo de aparato, lo que garantiza planificación adecuada, consentimiento informado y trazabilidad del tratamiento.
- **Operación / Caso de uso:** CREAR_TRATAMIENTO_ORTODONCIA (createOrthodonticTreatment)
- **Regla de negocio:** RN-ORTHODONTIC-001 — Obligatorio especificar tipo de aparato (ver ADR-50)
- **Contexto del agregado:** TRATAMIENTO_ORTODONCIA
- **Tipo semántico:** Validación de entrada
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Campo appliance vacío en solicitud de tratamiento"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita decisiones clínicas basadas en datos incompletos y protege el derecho del paciente a información clara sobre el tratamiento.
- **Ejemplo de uso:**
  ```java
  if (treatment.getApplianceType() == null || treatment.getApplianceType().isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ORTHODONTIC_MISSING_APPLIANCE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** tratamiento sin appliance → excepción.
    - **Integración:** POST /orthodontic-treatments sin appliance → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Orthodontic.

---