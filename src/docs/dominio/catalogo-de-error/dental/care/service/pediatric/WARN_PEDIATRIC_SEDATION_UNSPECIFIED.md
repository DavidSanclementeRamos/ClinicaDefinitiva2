## WARN_PEDIATRIC_SEDATION_UNSPECIFIED

- **Código:** WARN_PEDIATRIC_SEDATION_UNSPECIFIED
- **Nombre corto:** Sedación sin tipo especificado
- **Mensaje base:** "Técnicas de sedación deben especificar tipo (consciente/profunda)"
- **Descripción clínica:**  
  Señala la ausencia de especificación del tipo de sedación en procedimientos pediátricos, requisito crítico para planificación anestésica, consentimiento y logística de recuperación.
- **Operación / Caso de uso:** REGISTRAR_PLAN_SEDACION_PEDIATRICA (registerPediatricSedationPlan)
- **Regla de negocio:** RN-PEDIATRIC-004 — Requerimiento de tipo de sedación (ver ADR-63)
- **Contexto del agregado:** PLAN_SEDACION_PEDIATRICA
- **Tipo semántico:** Advertencia de seguridad clínica
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Sedación sin tipo especificado para procedimiento X"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura que los padres/tutores y el equipo clínico conozcan el nivel de sedación, protegiendo la seguridad y el consentimiento informado.
- **Ejemplo de uso:**
  ```java
  if (procedure.requiresSedation() && procedure.getSedationType() == null) {
      log.warn(ErrorCatalog.WARN_PEDIATRIC_SEDATION_UNSPECIFIED);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** requiresSedation=true y sedationType=null → warning.
    - **Integración:** POST /pediatric/sedation-plans sin sedationType → log warning.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Pediatric.

---