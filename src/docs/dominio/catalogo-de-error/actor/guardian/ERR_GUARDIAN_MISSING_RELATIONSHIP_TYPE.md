## ERR_GUARDIAN_MISSING_RELATIONSHIP_TYPE

- **Código:** ERR_GUARDIAN_MISSING_RELATIONSHIP_TYPE
- **Nombre corto:** Relación con paciente obligatoria
- **Mensaje base:** "Debe registrarse el tipo de relación con el paciente"
- **Descripción clínica:**  
  Obliga a que todo responsable declare explícitamente el tipo de relación legal con el paciente (ej. padre, tutor, representante). Esto asegura trazabilidad y validez jurídica de las autorizaciones clínicas.
- **Operación / Caso de uso:** REGISTRAR_RESPONSABLE (registerGuardian)
- **Regla de negocio:** RN-GUARDIAN-004 — Relación obligatoria con paciente (ver ADR-20)
- **Contexto del agregado:** RESPONSABLE (GUARDIAN)
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Guardian ID 45 sin tipo de relación registrado con paciente ID 12"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege la validez legal de las autorizaciones médicas y evita vínculos ambiguos o fraudulentos.
- **Ejemplo de uso:**
  ```java
  if (guardian.relationshipType == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_GUARDIAN_MISSING_RELATIONSHIP_TYPE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** guardian sin relación → excepción.
    - **Integración:** POST /guardians sin relación → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Guardian.

---