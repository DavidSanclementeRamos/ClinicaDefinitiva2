## WARN_PEDIATRIC_PHYSICAL_RESTRAINT

- **Código:** WARN_PEDIATRIC_PHYSICAL_RESTRAINT
- **Nombre corto:** Contención física con restricción ética
- **Mensaje base:** "Contención física solo debe usarse en emergencias - restricciones éticas"
- **Descripción clínica:**  
  Advertencia crítica sobre el uso de contención física en pediatría; su empleo debe limitarse a situaciones de emergencia y documentarse con justificación ética y legal.
- **Operación / Caso de uso:** REGISTRAR_USO_CONTENCION_FISICA (registerPhysicalRestraintUse)
- **Regla de negocio:** RN-PEDIATRIC-005 — Restricciones éticas para contención física (ver ADR-64)
- **Contexto del agregado:** INTERVENCION_PEDIATRICA
- **Tipo semántico:** Advertencia ética crítica
- **Severidad sugerida:** WARNING_CRITICAL
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Contención física registrada sin justificación clínica en procedimiento Y"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege los derechos del menor y exige que medidas coercitivas sean excepcionales, justificadas y supervisadas.
- **Ejemplo de uso:**
  ```java
  if (procedure.usedPhysicalRestraint() && !procedure.hasEmergencyJustification()) {
      log.warn(ErrorCatalog.WARN_PEDIATRIC_PHYSICAL_RESTRAINT);
      // Además: marcar para revisión ética
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** usedPhysicalRestraint=true y no emergencyJustification → warning crítico.
    - **Integración:** POST /pediatric/interventions con contención sin justificación → log warning y flag de revisión.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Pediatric.

---