
## ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE

- **Código:** ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE
- **Nombre corto:** Sitio de colocación inválido
- **Mensaje base:** "El sitio de colocación debe tener formato válido si se especifica"
- **Descripción clínica:**  
  Garantiza que, al registrar el sitio anatómico de colocación (p. ej., cuadrante, pieza, región), el valor siga un formato y catálogo validado para trazabilidad y planificación quirúrgica. Si no se especifica, no aplica; si se especifica incorrecto, se rechaza.
- **Operación / Caso de uso:** REGISTRAR_IMPLANTE (registerImplant)
- **Regla de negocio:** RN-IMPLANTOLOGY-007 — Validación de sitio anatómico (ver ADR-44)
- **Contexto del agregado:** IMPLANTE
- **Tipo semántico:** Integridad de catálogo
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Sitio 'UL-M3' no reconocido por el catálogo"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita ambigüedad anatómica y errores de planificación que puedan comprometer la seguridad del paciente.
- **Ejemplo de uso:**
  ```java
  String site = implant.getPlacementSite();
  if (site != null && !placementCatalog.isValid(site)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_IMPLANTOLOGY_INVALID_PLACEMENT_SITE);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** site="X-INVALID" → excepción.
    - **Integración:** POST /implants con placementSite no conforme al catálogo → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Implantology.

---
