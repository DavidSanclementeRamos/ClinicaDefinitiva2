## ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH

- **Código:** ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH
- **Nombre corto:** Cicatrización insuficiente con injerto óseo
- **Mensaje base:** "Con injerto óseo, el tiempo de cicatrización mínimo es 4 meses"
- **Descripción clínica:**  
  Asegura que los procedimientos con injerto óseo respeten tiempos mínimos de cicatrización para seguridad clínica.
- **Operación / Caso de uso:** REGISTRAR_IMPLANTE (registerImplant)
- **Regla de negocio:** RN-IMPLANTOLOGY-002 — Validación de cicatrización con injerto (ver ADR-40)
- **Contexto del agregado:** IMPLANTE
- **Tipo semántico:** Integridad clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Injerto óseo con healingTime=3 meses"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege al paciente de riesgos asociados a tiempos insuficientes de cicatrización tras injerto.
- **Ejemplo de uso:**
  ```java
  if (implant.hasBoneGraft() && healingTime < 4) {
      throw new DomainAggregateException(ErrorCatalog.ERR_IMPLANTOLOGY_BONE_GRAFT_HEALING_MISMATCH);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** injerto con healingTime=3 → excepción.
    - **Integración:** POST /implants con injerto y healingTime=2 → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Implantology.

---
