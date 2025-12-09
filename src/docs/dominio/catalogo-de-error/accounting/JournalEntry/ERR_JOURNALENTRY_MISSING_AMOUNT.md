# Entrada: ERR_JOURNALENTRY_MISSING_AMOUNT

- **Código:** ERR_JOURNALENTRY_MISSING_AMOUNT
- **Nombre corto:** Monto obligatorio
- **Mensaje base:** `error.journalEntry.missingAmount` — "El monto es obligatorio"
- **Descripción clínica:**  
  Este error ocurre cuando se intenta registrar un asiento contable sin especificar el monto.  
  La ausencia de valor numérico invalida la operación, ya que impide la correcta contabilización y balance de débitos y créditos.
- **Operación / Caso de uso:** CREAR_ASIENTO_CONTABLE
- **Regla de negocio:** RN-JOURNALENTRY-023 — "Todo asiento debe tener un monto definido" (ver ADR-23-ERR_JOURNALENTRY_MISSING_AMOUNT.md)
- **Contexto del agregado:** JOURNALENTRY
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400 (Bad Request)
- **Detalle dinámico sugerido:** `"Asiento con ID 456 sin monto definido"`
- **Mapa a código existente:** Sustituye validación previa en `InvalidJournalEntryException("El monto es obligatorio")`
- **Justificación ética:**  
  Este error protege la integridad contable y evita registros incompletos que podrían afectar la trazabilidad financiera.
- **Ejemplo de uso:**
  ```java
  if (amount == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_JOURNALENTRY_MISSING_AMOUNT);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - Unitario: Verificar que se lanza la excepción al crear un asiento con monto `null`.
    - Integración: Validar que la API devuelve HTTP 400 con el código de error correspondiente.
- **Changelog / versión:**
    - 2025-12-08 — Autor: David — Alta inicial catálogo JournalEntry.

---