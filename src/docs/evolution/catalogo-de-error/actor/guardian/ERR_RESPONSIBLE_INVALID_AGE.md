## ERR_RESPONSIBLE_INVALID_AGE

- **Código:** ERR_RESPONSIBLE_INVALID_AGE
- **Nombre corto:** Edad inválida responsable
- **Mensaje base:** "El responsable debe tener entre 22 y 60 años"
- **Descripción clínica:**  
  Valida que el responsable legal de un paciente cumpla con el rango de edad clínicamente aceptado (22–60 años). Evita que menores o adultos mayores sin capacidad legal sean asignados como responsables.
- **Operación / Caso de uso:** REGISTRAR_RESPONSABLE (registerGuardian)
- **Regla de negocio:** RN-GUARDIAN-008 — Validación de edad responsable (ver ADR-20)
- **Contexto del agregado:** RESPONSABLE (GUARDIAN)
- **Tipo semántico:** Validación clínica
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Intento de registrar responsable con edad 19"
- **Mapa a código existente:** Reemplaza RN-GUARDIAN-008 (≥18 años)
- **Justificación ética:** Garantiza que los responsables legales tengan capacidad jurídica y madurez clínica para asumir decisiones médicas.
- **Ejemplo de uso:**
  ```java
  if (guardian.age < 22 || guardian.age > 60) {
      throw new DomainAggregateException(ErrorCatalog.ERR_RESPONSIBLE_INVALID_AGE);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** edad fuera de rango → excepción.
    - **Integración:** POST /guardians con edad 19 → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Guardian.

---