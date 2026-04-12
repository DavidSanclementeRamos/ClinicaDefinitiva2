## ERR_PROSTHETIC_INVALID_UNITS

- **Código:** ERR_PROSTHETIC_INVALID_UNITS
- **Nombre corto:** Unidades inválidas
- **Mensaje base:** "El número de unidades debe ser mayor o igual a 0"
- **Descripción clínica:**  
  Evita valores negativos o no numéricos en el conteo de unidades protésicas, preservando la coherencia de planificación, fabricación y facturación.
- **Operación / Caso de uso:** CREAR_O_ACTUALIZAR_PRÓTESIS (createOrUpdateProsthesis)
- **Regla de negocio:** RN-PROSTHETIC-002 — Validación de unidades no negativas (ver ADR-71)
- **Contexto del agregado:** PRÓTESIS_PROSTHÉTICA
- **Tipo semántico:** Validación de formato
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Units recibido: -2 para prótesis ID 456"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita errores administrativos que podrían derivar en tratamientos incorrectos o facturación indebida.
- **Ejemplo de uso:**
  ```java
  if (prosthesis.getUnits() < 0) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PROSTHETIC_INVALID_UNITS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** units=-1 → excepción.
    - **Integración:** POST /prosthetics con units=-5 → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Prosthetic.

---