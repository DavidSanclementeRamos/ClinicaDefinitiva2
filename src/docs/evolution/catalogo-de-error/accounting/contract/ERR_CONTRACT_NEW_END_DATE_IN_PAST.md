## ERR_CONTRACT_NEW_END_DATE_IN_PAST
- **Código:** ERR_CONTRACT_NEW_END_DATE_IN_PAST
- **Nombre corto:** Nueva fecha en pasado
- **Mensaje base:** "La nueva fecha de fin no puede estar en el pasado"
- **Descripción clínica:** No se puede extender un contrato con una fecha de fin anterior a la fecha actual.
- **Operación / Caso de uso:** EXTENDER_CONTRATO
- **Regla de negocio:** RN-CONTRACT-010
- **Contexto del agregado:** CONTRACT
- **Tipo semántico:** Validación clínica / Integridad temporal
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Extensión inválida: nueva fecha 2024-01-01 anterior a hoy"
- **Mapa a código existente:** Nuevo
- **Justificación ética:** Evita inconsistencias temporales y asegura vigencia válida.
- **Ejemplo de uso:**
  ```java
  if (newEndDate.isBefore(LocalDate.now())) throw new TemporalValidationException(ERR_CONTRACT_NEW_END_DATE_IN_PAST);
  ```  
- **Pruebas mínimas requeridas:** Unitario: extensión con fecha pasada → excepción.
- **Changelog / versión:** 2025-12-0, David — Alta inicial catálogo Contract.


---