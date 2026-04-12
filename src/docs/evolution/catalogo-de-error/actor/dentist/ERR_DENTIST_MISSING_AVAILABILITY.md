## ERR_DENTIST_MISSING_AVAILABILITY

- **Código:** ERR_DENTIST_MISSING_AVAILABILITY
- **Nombre corto:** Disponibilidad inicial obligatoria
- **Mensaje base:** "El odontólogo debe registrar disponibilidad inicial (mínimo 40 horas semanales)"
- **Descripción clínica:**  
  Obliga a que todo odontólogo registrado declare su disponibilidad mínima semanal. Esto asegura que el sistema pueda asignar citas de manera confiable y evita profesionales sin agenda definida.
- **Operación / Caso de uso:** CREAR_ODONTOLOGO (registerDentist)
- **Regla de negocio:** RN-DENTIST-002 — Disponibilidad inicial obligatoria (ver ADR-20 Alcance Experimental)
- **Contexto del agregado:** ODONTOLOGO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Odontólogo ID 45 sin disponibilidad inicial"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** Garantiza que los pacientes puedan acceder a profesionales con agenda clara, evitando falsas expectativas de atención.
- **Ejemplo de uso:**
  ```java
  if (dentist.availability.isEmpty()) {
      throw new DomainAggregateException(ErrorCatalog.ERR_DENTIST_MISSING_AVAILABILITY);
  }
  ```  
- **Pruebas mínimas requeridas:**
    - **Unitario:** odontólogo sin disponibilidad → excepción.
    - **Integración:** POST /dentists sin disponibilidad → 400.
- **Changelog / versión:** 2025-12-25, David — Alta inicial catálogo Dentist.

---