## ERR_IMPLANTOLOGY_INVALID_HEALING_TIME

- **Código:** ERR_IMPLANTOLOGY_INVALID_HEALING_TIME
- **Nombre corto:** Tiempo de cicatrización inválido
- **Mensaje base:** "El tiempo de cicatrización debe estar entre 2 y 12 meses"
- **Descripción clínica:**  
  Garantiza que los tiempos de cicatrización registrados estén dentro de un rango clínicamente aceptado.
- **Operación / Caso de uso:** REGISTRAR_IMPLANTE (registerImplant)
- **Regla de negocio:** RN-IMPLANTOLOGY-001 — Validación de rango de cicatrización (ver ADR-40)
- **Contexto del agregado:** IMPLANTE
- **Tipo semántico:** Validación de entrada
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Tiempo recibido: 18 meses fuera de rango"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita registros clínicos que puedan inducir a tratamientos inseguros o expectativas erróneas.
- **Ejemplo de uso:**
  ```java
  if (healingTime < 2 || healingTime > 12) {
      throw new DomainAggregateException(ErrorCatalog.ERR_IMPLANTOLOGY_INVALID_HEALING_TIME);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** tiempo = 1 → excepción.
    - **Integración:** POST /implants con healingTime=18 → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Implantology.

---