## ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP

- **Código:** ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP
- **Nombre corto:** Relación legal inmutable tras autorización
- **Mensaje base:** "No puede modificarse el vínculo legal si ha autorizado tratamientos previamente"
- **Descripción clínica:**  
  Impide que un responsable cambie el tipo de relación legal con el paciente una vez que ya ha autorizado tratamientos. Protege la validez jurídica de las autorizaciones y evita inconsistencias en la trazabilidad clínica.
- **Operación / Caso de uso:** MODIFICAR_RELACION_RESPONSABLE (modifyGuardianRelationship)
- **Regla de negocio:** RN-GUARDIAN-009 — Relación inmutable tras autorización (ver ADR-21)
- **Contexto del agregado:** RESPONSABLE (GUARDIAN)
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Guardian ID 88 intentó modificar relación con paciente ID 12 tras autorización activa"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que las autorizaciones médicas mantengan validez legal y evita fraude o manipulación de vínculos.
- **Ejemplo de uso:**
  ```java
  if (guardian.hasAuthorizedTreatments()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** guardian con autorizaciones previas → excepción.
    - **Integración:** PUT /guardians/{id}/relationship con autorizaciones activas → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Guardian.

---