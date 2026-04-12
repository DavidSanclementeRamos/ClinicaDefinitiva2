## ERR_PEDIATRIC_INVALID_AGE_RANGE

- **Código:** ERR_PEDIATRIC_INVALID_AGE_RANGE
- **Nombre corto:** Rango de edad pediátrico inválido
- **Mensaje base:** "El rango de edad debe especificar edades pediátricas válidas (0-18 años)"
- **Descripción clínica:**  
  Asegura que los rangos de edad definidos para protocolos pediátricos correspondan al intervalo aceptado de 0 a 18 años, evitando aplicar guías o tratamientos no apropiados para la edad del paciente.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_RANGO_EDAD_PEDIATRICO (createOrUpdatePediatricAgeRange)
- **Regla de negocio:** RN-PEDIATRIC-001 — Validación de rango de edad pediátrica (ver ADR-60)
- **Contexto del agregado:** POLITICA_EDAD_PEDIATRICA
- **Tipo semántico:** Validación de dominio
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Rango recibido: 19-25 años fuera de rango pediátrico"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Protege a menores evitando la aplicación de protocolos diseñados para adultos y garantiza la seguridad y adecuación del tratamiento.
- **Ejemplo de uso:**
  ```java
  if (ageRange.getMin() < 0 || ageRange.getMax() > 18) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PEDIATRIC_INVALID_AGE_RANGE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** min=0 max=19 → excepción.
    - **Integración:** POST /pediatric/age-ranges con max=20 → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Pediatric.

---