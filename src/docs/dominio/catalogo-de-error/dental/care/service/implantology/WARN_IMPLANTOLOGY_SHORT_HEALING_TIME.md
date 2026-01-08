## WARN_IMPLANTOLOGY_SHORT_HEALING_TIME

- **Código:** WARN_IMPLANTOLOGY_SHORT_HEALING_TIME
- **Nombre corto:** Cicatrización demasiado corta
- **Mensaje base:** "Tiempos menores a 3 meses sin injerto son atípicos"
- **Descripción clínica:**  
  Advierte sobre tiempos de cicatrización poco comunes en implantes sin injerto óseo.
- **Operación / Caso de uso:** REGISTRAR_IMPLANTE (registerImplant)
- **Regla de negocio:** RN-IMPLANTOLOGY-004 — Advertencia de cicatrización corta (ver ADR-42)
- **Contexto del agregado:** IMPLANTE
- **Tipo semántico:** Advertencia clínica
- **Severidad sugerida:** WARNING
- **HTTP sugerido:** 200
- **Detalle dinámico sugerido:** "Implante sin injerto con healingTime=2 meses"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Informa al clínico de escenarios atípicos para revisión y confirmación.
- **Ejemplo de uso:**
  ```java
  if (!implant.hasBoneGraft() && healingTime < 3) {
      log.warn(ErrorCatalog.WARN_IMPLANTOLOGY_SHORT_HEALING_TIME);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** healingTime=2 sin injerto → warning.
    - **Integración:** POST /implants con healingTime=1 → log warning.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Implantology.

---