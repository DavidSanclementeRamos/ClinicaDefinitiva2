## ERR_COMPANY_FUTURE_INCORPORATION_DATE

- **Código:** ERR_COMPANY_FUTURE_INCORPORATION_DATE
- **Nombre corto:** Fecha de constitución futura inválida
- **Mensaje base:** "La fecha de constitución no puede ser futura"
- **Descripción clínica:**  
  Se impide registrar o actualizar una empresa con fecha de constitución posterior a la fecha actual. Esto asegura coherencia temporal y legal del acto constitutivo.
- **Operación / Caso de uso:** VALIDAR_FECHA_CONSTITUCIÓN (validateIncorporationDate)
- **Regla de negocio:** RN-COMPANY-002 — Fecha de constitución no puede ser futura
- **Contexto del agregado:** COMPANY
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Fecha de constitución 2026-01-01 es futura"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita falsedad documental y garantiza integridad temporal del registro.
- **Ejemplo de uso:**
  ```java
  if (company.getIncorporationDate().isAfter(LocalDate.now())) {
      throw new DomainAggregateException(ErrorCatalog.ERR_COMPANY_FUTURE_INCORPORATION_DATE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** fecha > hoy → excepción.
    - **Integración:** PUT /companies/{id} con fecha futura → 400.
- **Changelog / versión:** 2025-12-08, David — Alta inicial catálogo Company.

---