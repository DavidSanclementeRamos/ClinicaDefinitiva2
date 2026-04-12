## ERR_GUARDIAN_CANNOT_REVOKE_STARTED_TREATMENT

- **Código:** ERR_GUARDIAN_CANNOT_REVOKE_STARTED_TREATMENT
- **Nombre corto:** Revocación inválida de consentimiento iniciado
- **Mensaje base:** "No se puede revocar el consentimiento de un tratamiento que ya ha iniciado"
- **Descripción clínica:**  
  Impide que un responsable retire su consentimiento una vez que el tratamiento clínico ya ha comenzado. Protege la continuidad terapéutica y evita riesgos para el paciente por interrupciones abruptas.
- **Operación / Caso de uso:** REVOCAR_CONSENTIMIENTO (revokeConsent)
- **Regla de negocio:** RN-GUARDIAN-003 — Restricción de revocación de consentimiento iniciado (ver ADR-20)
- **Contexto del agregado:** RESPONSABLE (GUARDIAN)
- **Tipo semántico:** Autorización
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Intento de revocar consentimiento de tratamiento ID 321 ya iniciado"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza la seguridad clínica del paciente y evita que se interrumpan tratamientos en curso sin protocolo médico.
- **Ejemplo de uso:**
  ```java
  if (treatment.isStarted()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_GUARDIAN_CANNOT_REVOKE_STARTED_TREATMENT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** tratamiento iniciado → excepción.
    - **Integración:** POST /guardians/{id}/revokeConsent con tratamiento activo → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Guardian.

---