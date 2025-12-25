## ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS

- **Código:** ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS
- **Nombre corto:** Desactivación con autorizaciones vigentes
- **Mensaje base:** "No puede desactivarse si tiene autorizaciones clínicas vigentes"
- **Descripción clínica:**  
  Impide que un responsable sea desactivado mientras mantiene autorizaciones clínicas activas. Protege la continuidad de los tratamientos y asegura que los pacientes no queden sin respaldo legal durante procesos médicos en curso.
- **Operación / Caso de uso:** DESACTIVAR_RESPONSABLE (deactivateGuardian)
- **Regla de negocio:** RN-GUARDIAN-005 — Restricción de desactivación con autorizaciones vigentes (ver ADR-20)
- **Contexto del agregado:** RESPONSABLE (GUARDIAN)
- **Tipo semántico:** Autorización
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 409
- **Detalle dinámico sugerido:** "Guardian ID 77 con autorizaciones clínicas activas"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes tengan siempre un responsable legal activo durante tratamientos médicos.
- **Ejemplo de uso:**
  ```java
  if (guardian.hasActiveAuthorizations()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** guardian con autorizaciones activas → excepción.
    - **Integración:** PUT /guardians/{id}/deactivate con autorizaciones vigentes → 409.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Guardian.

---

