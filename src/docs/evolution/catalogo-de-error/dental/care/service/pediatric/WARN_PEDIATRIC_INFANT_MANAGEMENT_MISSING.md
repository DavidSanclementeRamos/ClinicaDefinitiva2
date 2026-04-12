## WARN_PEDIATRIC_INFANT_MANAGEMENT_MISSING

- **Código:** WARN_PEDIATRIC_INFANT_MANAGEMENT_MISSING
- **Nombre corto:** Manejo de lactantes no especificado
- **Mensaje base:** "Bebés (0-3 años) requieren técnicas de manejo específicas"
- **Descripción clínica:**  
  Advierte cuando un plan para pacientes de 0 a 3 años no incluye técnicas o consideraciones específicas (p. ej., inmovilización suave, comunicación con cuidador, tiempos de alimentación), necesarias para seguridad y confort.
- **Operación / Caso de uso:** PLANIFICAR_ATENCION_INFANTIL (planInfantCare)
- **Regla de negocio:** RN-PEDIATRIC-007 — Requerimiento de manejo específico para lactantes (ver ADR-66)
- **Contexto del agregado:** ATENCION_PEDIATRICA
- **Tipo semántico:** Advertencia de práctica clínica
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Paciente 18 meses sin plan de manejo específico"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege la seguridad y el bienestar de los lactantes al asegurar que se apliquen técnicas adaptadas a su edad y vulnerabilidad.
- **Ejemplo de uso:**
  ```java
  if (patientAge <= 3 && !carePlan.includesInfantManagement()) {
      log.warn(ErrorCatalog.WARN_PEDIATRIC_INFANT_MANAGEMENT_MISSING);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** age=2 y carePlan.missingInfantManagement → warning.
    - **Integración:** POST /pediatric/care-plans para paciente 2 años sin manejo específico → log warning.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Pediatric.

---