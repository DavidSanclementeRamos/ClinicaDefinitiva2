## ERR_PEDIATRIC_AGE_RANGE_TOO_SHORT

- **Código:** ERR_PEDIATRIC_AGE_RANGE_TOO_SHORT
- **Nombre corto:** Formato de rango de edad inválido
- **Mensaje base:** "El rango de edad debe tener formato válido (mínimo 5 caracteres)"
- **Descripción clínica:**  
  Evita rangos mal formateados o demasiado cortos que impidan su interpretación (por ejemplo "0-1" vs "0-12"), garantizando interoperabilidad y claridad en políticas pediátricas.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_RANGO_EDAD_PEDIATRICO (createOrUpdatePediatricAgeRange)
- **Regla de negocio:** RN-PEDIATRIC-002 — Validación de formato de rango (ver ADR-61)
- **Contexto del agregado:** POLITICA_EDAD_PEDIATRICA
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Rango recibido: '0-3' (longitud 3) formato inválido"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita ambigüedad en la aplicación de protocolos y reduce riesgo de errores administrativos o clínicos por datos mal formateados.
- **Ejemplo de uso:**
  ```java
  if (ageRangeString == null || ageRangeString.length() < 5) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PEDIATRIC_AGE_RANGE_TOO_SHORT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** ageRange="0-3" → excepción.
    - **Integración:** POST /pediatric/age-ranges con ageRange="1-2" → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Pediatric.

---