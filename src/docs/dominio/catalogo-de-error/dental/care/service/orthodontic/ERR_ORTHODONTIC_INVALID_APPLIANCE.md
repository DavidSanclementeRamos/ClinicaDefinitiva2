## ERR_ORTHODONTIC_INVALID_APPLIANCE

- **Código:** ERR_ORTHODONTIC_INVALID_APPLIANCE
- **Nombre corto:** Aparato no reconocido
- **Mensaje base:** "El tipo de aparato debe ser reconocido por el sistema"
- **Descripción clínica:**  
  Garantiza que solo se utilicen tipos de aparatos validados por el sistema (p. ej., brackets metálicos, alineadores, linguales), facilitando estandarización y seguimiento clínico.
- **Operación / Caso de uso:** CREAR_TRATAMIENTO_ORTODONCIA (createOrthodonticTreatment)
- **Regla de negocio:** RN-ORTHODONTIC-003 — Validación contra catálogo de aparatos (ver ADR-51)
- **Contexto del agregado:** TRATAMIENTO_ORTODONCIA
- **Tipo semántico:** Integridad de catálogo
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Aparato recibido: 'X-APPLIANCE' no reconocido"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita confusiones en la ejecución clínica y asegura que los pacientes reciban tratamientos con dispositivos aprobados y documentados.
- **Ejemplo de uso:**
  ```java
  if (!applianceCatalog.isValid(treatment.getApplianceType())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_ORTHODONTIC_INVALID_APPLIANCE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** appliance="UNKNOWN" → excepción.
    - **Integración:** POST /orthodontic-treatments con appliance inválido → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Orthodontic.

---
