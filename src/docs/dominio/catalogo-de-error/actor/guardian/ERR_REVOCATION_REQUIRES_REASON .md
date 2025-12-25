## ERR_REVOCATION_REQUIRES_REASON

- **Código:** ERR_REVOCATION_REQUIRES_REASON
- **Nombre corto:** Revocación requiere motivo obligatorio
- **Mensaje base:** "La revocación de consentimiento requiere motivo obligatorio"
- **Descripción clínica:**  
  Obliga a que toda revocación de consentimiento clínico esté acompañada de un motivo explícito. Esto asegura trazabilidad y permite auditar las razones por las cuales un responsable retira su autorización.
- **Operación / Caso de uso:** REVOCAR_CONSENTIMIENTO (revokeConsent)
- **Regla de negocio:** RN-GUARDIAN-011 — Motivo obligatorio en revocación de consentimiento (ver ADR-21)
- **Contexto del agregado:** RESPONSABLE (GUARDIAN)
- **Tipo semántico:** Autorización
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Guardian ID 65 revocó consentimiento de paciente ID 23 sin motivo"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza transparencia y responsabilidad en la gestión clínica, evitando revocaciones arbitrarias que afecten al paciente.
- **Ejemplo de uso:**
  ```java
  if (revocationReason == null || revocationReason.isBlank()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_REVOCATION_REQUIRES_REASON);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** revocación sin motivo → excepción.
    - **Integración:** POST /guardians/{id}/revokeConsent sin motivo → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Guardian.  
