## ERR_COMPANY_INVALID_INCORPORATION_DATE

- **Código:** ERR_COMPANY_INVALID_INCORPORATION_DATE
- **Nombre corto:** Fecha de constitución inferior a 1800
- **Mensaje base:** "La fecha de constitución no es válida (no puede ser anterior a 1800)"
- **Descripción clínica:**  
  Rechaza fechas históricamente inválidas (antes de 1800), evitando registros anacrónicos que rompen controles de auditoría y consistencia documental.
- **Operación / Caso de uso:** VALIDAR_FECHA_CONSTITUCIÓN (validateIncorporationDate)
- **Regla de negocio:** RN-COMPANY-009 — Umbral mínimo histórico de fecha de constitución
- **Contexto del agregado:** COMPANY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Fecha de constitución 1799-12-31 inferior al umbral permitido"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Mantiene coherencia histórica y evita falsificación de antigüedad institucional.
- **Ejemplo de uso:**
  ```java
  if (date.isBefore(LocalDate.of(1800, 1, 1))) {
      throw new DomainAggregateException(ErrorCatalog.ERR_COMPANY_INVALID_INCORPORATION_DATE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** fecha < 1800-01-01 → excepción.
    - **Integración:** validación al crear/actualizar con fecha 1700 → 400.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Company.






