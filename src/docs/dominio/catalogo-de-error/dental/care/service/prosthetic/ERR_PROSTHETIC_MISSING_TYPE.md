## ERR_PROSTHETIC_MISSING_TYPE

- **Código:** ERR_PROSTHETIC_MISSING_TYPE
- **Nombre corto:** Tipo de prótesis obligatorio
- **Mensaje base:** "Debe especificar si la prótesis es fija o removible"
- **Descripción clínica:**  
  Evita registrar prótesis sin indicar su modalidad (fija/removible), lo que es esencial para planificación, consentimiento, logística de laboratorio y facturación.
- **Operación / Caso de uso:** CREAR_PRÓTESIS (createProsthesis)
- **Regla de negocio:** RN-PROSTHETIC-001 — Obligatorio especificar tipo de prótesis (ver ADR-70)
- **Contexto del agregado:** PRÓTESIS_PROSTHÉTICA
- **Tipo semántico:** Validación de entrada
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Campo type ausente en solicitud de prótesis para paciente ID 123"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que el paciente reciba información clara sobre el tratamiento propuesto y evita decisiones clínicas basadas en datos incompletos.
- **Ejemplo de uso:**
  ```java
  if (prosthesis.getType() == null) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PROSTHETIC_MISSING_TYPE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** solicitud sin campo type → excepción.
    - **Integración:** POST /prosthetics sin type → 400.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Prosthetic.

---