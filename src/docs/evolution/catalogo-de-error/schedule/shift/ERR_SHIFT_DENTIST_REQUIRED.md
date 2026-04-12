# ERR_SHIFT_DENTIST_REQUIRED

- **Código:** ERR_SHIFT_DENTIST_REQUIRED
- **Nombre corto:** DentistId requerido
- **Mensaje base:** "Debe especificarse un DentistId válido para crear un turno"
- **Descripción clínica:**  
  Obliga a asociar cada turno a un odontólogo válido. Evita registros huérfanos y asegura que la agenda operativa represente responsabilidades reales en la clínica.
- **Operación / Caso de uso:** CREAR_TURNO (createShift)
- **Regla de negocio:** RN-SHIFT-010 — DentistId obligatorio
- **Contexto del agregado:** TURNO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Turno creado sin DentistId válido (valor recibido: '')"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza trazabilidad y responsabilidad clínica, evitando programación sobre actores inexistentes o inválidos.
- **Ejemplo de uso:**
  ```java
  if (dentistId == null || !DentistId.isValid(dentistId)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_SHIFT_DENTIST_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** dentistId = null → excepción con ERR_SHIFT_DENTIST_REQUIRED.
    - **Unitario:** dentistId con formato inválido → excepción.
    - **Integración:** POST /shifts sin dentistId → 400 y catálogo en body.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Shift.

---