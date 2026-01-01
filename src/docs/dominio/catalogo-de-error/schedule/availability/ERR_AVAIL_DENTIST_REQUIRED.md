### ERR_AVAIL_DENTIST_REQUIRED

- **Código:** ERR_AVAIL_DENTIST_REQUIRED
- **Nombre corto:** DentistId requerido
- **Mensaje base:** "Debe especificarse un DentistId válido para crear disponibilidad"
- **Descripción clínica:**  
  Obliga a asociar cada disponibilidad a un odontólogo válido. Protege la trazabilidad clínica, evita registros huérfanos y asegura que la agenda represente actores reales del sistema.
- **Operación / Caso de uso:** CREAR_DISPONIBILIDAD (createAvailability)
- **Regla de negocio:** RN-AVAIL-010 — DentistId obligatorio (ver ADR-24)
- **Contexto del agregado:** HORARIO
- **Tipo semántico:** Integridad
- **Severidad sugerida:** ERROR
- **HTTP sugerido:** 400
- **Detalle dinámico sugerido:** "Disponibilidad sin DentistId o con formato inválido (valor recibido: '')"
- **Mapa a código existente:** Nuevo código
- **Justificación ética:** La disponibilidad sin profesional identificado compromete la programación clínica y la responsabilidad operativa de la atención.
- **Ejemplo de uso:**
  ```java
  if (dentistId == null || !DentistId.isValid(dentistId)) {
      throw new DomainAggregateException(ErrorCatalog.ERR_AVAIL_DENTIST_REQUIRED);
  }
  ```
- **Pruebas mínimas requeridas:**
    - **Unitario:** `createAvailability(null, ...)` → excepción con ERR_AVAIL_DENTIST_REQUIRED.
    - **Unitario:** `createAvailability(invalidUuid, ...)` → excepción con ERR_AVAIL_DENTIST_REQUIRED.
    - **Integración:** POST /availability sin `dentistId` → 400 y código del catálogo en el body.
- **Changelog / versión:** 2025-12-30, David — Alta inicial catálogo Availability.

