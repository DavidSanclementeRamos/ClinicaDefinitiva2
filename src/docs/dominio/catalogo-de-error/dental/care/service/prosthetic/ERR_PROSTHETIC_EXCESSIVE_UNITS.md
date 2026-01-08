## ERR_PROSTHETIC_EXCESSIVE_UNITS

- **Código:** ERR_PROSTHETIC_EXCESSIVE_UNITS
- **Nombre corto:** Exceso de unidades en removible
- **Mensaje base:** "Prótesis removibles no pueden tener más de 14 unidades por arcada"
- **Descripción clínica:**  
  Restringe el número de unidades en prótesis removibles por arcada para evitar diseños no realistas o incompatibles con la indicación de prótesis total; protege la calidad funcional y estética.
- **Operación / Caso de uso:** CREAR_PRÓTESIS_REMOVIBLE (createRemovableProsthesis)
- **Regla de negocio:** RN-PROSTHETIC-003 — Límite de unidades para prótesis removible (ver ADR-72)
- **Contexto del agregado:** PRÓTESIS_PROSTHÉTICA
- **Tipo semántico:** Integridad de negocio
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 422
- **Detalle dinámico sugerido:** "Removible con unitsPerArch=18 para paciente ID 789"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Evita promesas o planes de tratamiento inviables que puedan perjudicar la función masticatoria y la expectativa del paciente.
- **Ejemplo de uso:**
  ```java
  if (prosthesis.isRemovable() && prosthesis.getUnitsPerArch() > 14) {
      throw new DomainAggregateException(ErrorCatalog.ERR_PROSTHETIC_EXCESSIVE_UNITS);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** isRemovable=true y unitsPerArch=15 → excepción.
    - **Integración:** POST /prosthetics con tipo=REMOVABLE y unitsPerArch=16 → 422.
- **Changelog / versión:** 2026-01-07, David — Alta inicial catálogo Prosthetic.

---