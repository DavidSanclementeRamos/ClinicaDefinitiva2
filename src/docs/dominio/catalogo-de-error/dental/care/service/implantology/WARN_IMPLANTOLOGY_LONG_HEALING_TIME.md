## WARN_IMPLANTOLOGY_LONG_HEALING_TIME

- **Código:** WARN_IMPLANTOLOGY_LONG_HEALING_TIME
- **Nombre corto:** Cicatrización demasiado larga
- **Mensaje base:** "Tiempos mayores a 9 meses sin injerto complejo son atípicos"
- **Descripción clínica:**  
  Advierte sobre tiempos de cicatrización excesivos en implantes sin injertos complejos.
- **Operación / Caso de uso:** REGISTRAR_IMPLANTE (registerImplant)
- **Regla de negocio:** RN-IMPLANTOLOGY-005 — Advertencia de cicatrización larga (ver ADR-42)
- **Contexto del agregado:** IMPLANTE
- **Tipo semántico:** Advertencia clínica
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Implante sin injerto con healingTime=11 meses"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Permite identificar casos fuera de lo común para análisis clínico adicional.
- **Ejemplo de uso:**
  ```java
  if (!implant.hasComplexGraft() && healingTime > 9) {
      log.warn(ErrorCatalog.WARN_IMPLANTOLOGY_LONG_HEALING_TIME);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** healingTime=10 sin injerto complejo → warning.
    - **Integración:** POST /implants con healingTime=12 → log warning.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Implantology.

---