## ERR_PEDIATRIC_MATERIALS_TOO_SHORT

- **Código:** ERR_PEDIATRIC_MATERIALS_TOO_SHORT
- **Nombre corto:** Descripción de materiales insuficiente
- **Mensaje base:** "Materiales pediátricos deben describirse adecuadamente (mínimo 5 caracteres)"
- **Descripción clínica:**  
  Evita descripciones de materiales demasiado breves que impidan identificar componentes, alergias o compatibilidades específicas para pacientes pediátricos.
- **Operación / Caso de uso:** REGISTRAR_MATERIALES_PEDIATRICOS (registerPediatricMaterials)
- **Regla de negocio:** RN-PEDIATRIC-006 — Requerimiento de descripción mínima de materiales (ver ADR-65)
- **Contexto del agregado:** MATERIAL_PEDIATRICO
- **Tipo semántico:** Validación de completitud
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Material recibido: 'Res' longitud 3"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Asegura trazabilidad y reduce riesgo de reacciones adversas por falta de información sobre materiales usados en menores.
- **Ejemplo de uso:**
  ```java
  if (materialsDescription == null || materialsDescription.length() < 5) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PEDIATRIC_MATERIALS_TOO_SHORT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** materialsDescription="Res" → excepción.
    - **Integración:** POST /pediatric/materials con descripción corta → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Pediatric.

---