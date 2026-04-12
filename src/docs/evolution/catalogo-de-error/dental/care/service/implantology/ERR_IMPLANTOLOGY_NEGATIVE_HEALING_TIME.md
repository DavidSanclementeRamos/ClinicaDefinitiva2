## ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME

- **Código:** ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME
- **Nombre corto:** Tiempo de cicatrización negativo
- **Mensaje base:** "El tiempo de cicatrización no puede ser negativo"
- **Descripción clínica:**  
  Evita registros absurdos o inválidos que comprometan la integridad de la información clínica.
- **Operación / Caso de uso:** REGISTRAR_IMPLANTE (registerImplant)
- **Regla de negocio:** RN-IMPLANTOLOGY-003 — Validación de no negatividad (ver ADR-41)
- **Contexto del agregado:** IMPLANTE
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Tiempo recibido: -5 meses"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza registros clínicos coherentes y evita errores de captura.
- **Ejemplo de uso:**
  ```java
  if (healingTime < 0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_IMPLANTOLOGY_NEGATIVE_HEALING_TIME);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** healingTime=-1 → excepción.
    - **Integración:** POST /implants con healingTime=-5 → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Implantology.

---
