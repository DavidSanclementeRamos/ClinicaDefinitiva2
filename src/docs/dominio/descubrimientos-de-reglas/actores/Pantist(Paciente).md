# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Patient (Paciente)

## Propósito
Representar al paciente dentro del sistema clínico, asegurando que su información sea válida, trazable y que sus interacciones (citas, tratamientos, historial) respeten la semántica del dominio. Este agregado protege la integridad de los datos clínicos y permite gestión completa del ciclo de atención del paciente.

---

## CREACIÓN
- Debe tener nombre completo (FullName), documento único (DocumentId) y fecha de nacimiento válida.
- No puede crearse con estado INACTIVO.
- Debe registrar al menos un medio de contacto (email o teléfono).
- La edad calculada no puede ser negativa ni mayor a 120 años.
- Fecha de nacimiento no puede ser futura.
- Si es menor de edad, debe estar vinculado a un responsable (Guardian).
- Debe especificarse canal de reserva (web, presencial, telefónico, referido).

---

## EDICIÓN / ACTUALIZACIÓN
- No puede modificarse la fecha de nacimiento si ya tiene citas registradas.
- No puede eliminarse el documento ni el nombre.
- Solo puede editarse si está activo.
- Cambios sensibles deben registrar fecha, responsable y motivo.
- No puede modificarse el documento de identidad (inmutable).
- Cambios en responsable legal requieren validación y auditoría.

---

## DESACTIVACIÓN / ELIMINACIÓN
- No puede desactivarse si tiene citas activas o tratamientos en curso.
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar motivo obligatorio de desactivación.
- Debe registrar fecha de desactivación.
- Debe notificar a responsable legal si el paciente es menor.

---

## OPERACIONES DE DOMINIO
- puedeAgendar() → Verifica si está activo y no tiene bloqueos clínicos.
- tieneCitaEn(fechaHora) → Verifica conflicto de horario con cita existente.
- historialCompleto() → Devuelve todas las citas y tratamientos ordenados cronológicamente.
- esMenorDeEdad() → Útil para validar reglas de consentimiento (< 18 años).
- vincularResponsable(guardian) → Asocia responsable legal.
- desvincularResponsable(motivo) → Remueve vínculo con validaciones.
- agregarBloqueoClinico(motivo) → Impide agendamiento temporal.

---

## INVARIANTES GLOBALES
- Un paciente activo debe tener al menos un medio de contacto válido.
- No puede tener dos citas en el mismo horario.
- No puede tener tratamientos abiertos sin citas asociadas.
- Si es menor de edad, debe tener responsable legal vinculado.
- La edad debe estar en rango válido (0-120 años).
- No puede agendar nuevas citas si está inactivo o bloqueado clínicamente.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada cambio en datos sensibles (documento, contacto, estado).
- Se puede emitir un Outcome al intentar agendar si está inactivo o bloqueado.
- Se registra el motivo y fecha de desactivación.
- Se registra cada vinculación/desvinculación de responsable legal.
- Sistema emite alertas al detectar intentos de operaciones sobre paciente inactivo.

---

## Justificación Semántica
Estas reglas aseguran que el modelo de paciente sea coherente, evaluable y trazable. Protegen la continuidad clínica, evitan estados inválidos como pacientes sin contacto o con citas duplicadas, garantizan protección de menores mediante vínculo obligatorio con responsable legal y permiten auditar cada decisión relevante en el ciclo de vida del paciente.

---

## Reglas Descubiertas (formato estandarizado)

**RN-PATIENT-001**
- Descripción: Un paciente debe tener nombre, documento y fecha de nacimiento válida.
- Condición: Patient.fullName == null || Patient.documentId == null || Patient.birthDate == null al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_PATIENT_MISSING_REQUIRED_FIELDS

**RN-PATIENT-002**
- Descripción: No puede desactivarse si tiene citas activas o tratamientos en curso.
- Condición: Patient.citasActivas > 0 || Patient.tratamientosEnCurso > 0 al invocar desactivación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_PATIENT_ACTIVE_SERVICES

**RN-PATIENT-003**
- Descripción: No puede tener dos citas en el mismo horario.
- Condición: Patient.tieneCitaEn(fechaHora) == true al intentar agendar nueva cita.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_PATIENT_TIME_CONFLICT

**RN-PATIENT-004**
- Descripción: Solo puede editarse si está activo.
- Condición: Patient.status != ACTIVE al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_PATIENT_NOT_EDITABLE

**RN-PATIENT-005**
- Descripción: Debe registrar al menos un medio de contacto válido.
- Condición: Patient.email == null && Patient.phoneNumber == null al invocar creación/edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_PATIENT_MISSING_CONTACT

**RN-PATIENT-006**
- Descripción: La edad calculada debe estar en rango válido (0-120 años).
- Condición: Patient.age < 0 || Patient.age > 120 al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_PATIENT_INVALID_AGE

**RN-PATIENT-007**
- Descripción: Fecha de nacimiento no puede ser futura.
- Condición: Patient.birthDate > LocalDate.now() al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_PATIENT_FUTURE_BIRTHDATE

**RN-PATIENT-008**
- Descripción: Si es menor de edad, debe tener responsable legal vinculado.
- Condición: Patient.esMenorDeEdad() == true && Patient.guardian == null al invocar operaciones clínicas.
- Consecuencia: Se rechaza operación (ej: agendamiento, tratamiento).
- Error asociado: ERR_PATIENT_MINOR_REQUIRES_GUARDIAN

**RN-PATIENT-009**
- Descripción: No puede modificarse fecha de nacimiento si tiene citas registradas.
- Condición: Patient.citas.size() > 0 al intentar modificar birthDate.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_PATIENT_CANNOT_MODIFY_BIRTHDATE

**RN-PATIENT-010**
- Descripción: Desactivación requiere motivo obligatorio.
- Condición: desactivar(motivo) con motivo == null || motivo.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_PATIENT_DEACTIVATION_REQUIRES_REASON

---

## Relación con ADRs
- ADR-25 (Dominio): Modelado de Persona en el dominio clínico - estructura base.
- ADR-30 (Dominio): Catálogo de reglas CRUD por rol - permisos para gestionar pacientes.
- ADR-27 (Dominio): Formas de construcción de objetos (Builder vs Setters).
- ADR-32 (Dominio): Implementación sistemática de reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas de negocio - validación de operaciones clínicas.

---

## Eventos de Dominio
- PatientRegistered: Al crear nuevo paciente.
- PatientActivated: Al activar paciente inactivo.
- PatientDeactivated: Al desactivar paciente.
- PatientProfileUpdated: Al actualizar información personal.
- PatientGuardianLinked: Al vincular responsable legal.
- PatientGuardianUnlinked: Al desvincular responsable legal.
- PatientClinicalBlockApplied: Al aplicar bloqueo clínico.
- PatientClinicalBlockRemoved: Al remover bloqueo clínico.

---

## Value Objects Involucrados

**DateOfBirth (Fecha de Nacimiento)**
- Valida rango de edad (0-120 años).
- Calcula edad actual automáticamente.
- No puede ser futura.
- Inmutable.

**DocumentId (Documento de Identidad)**
- Valida formato según tipo (CC, TI, CE, Pasaporte).
- Longitud específica por tipo de documento.
- Único por paciente.
- Inmutable.

**ReservationChannel (Canal de Reserva)**
- Valores permitidos: WEB, PRESENCIAL, TELEFONICO, REFERIDO.
- Permite trazabilidad del origen del paciente.
- Útil para métricas de marketing.

**UserStatus (Estado de Usuario)**
- Posibles estados: ACTIVE, INACTIVE, SUSPENDED, BLOCKED.
- Controla transiciones válidas.
- Afecta capacidad operativa.

**PhoneNumber (Teléfono)**
- Valida formato colombiano.
- Normaliza entrada.
- Longitud: 7-15 dígitos.

**Email (Correo Electrónico)**
- Valida formato RFC 5322.
- Normaliza a minúsculas.
- Único recomendado pero no obligatorio.

---

## Ejemplo de Uso

```java
// Crear paciente adulto
Patient mariaLopez = Patient.register(
    fullName: FullName.of("María", "López"),
    documentId: DocumentId.of("CC", "987654321"),
    dateOfBirth: DateOfBirth.of(LocalDate.of(1990, 3, 15)),
    phoneNumber: PhoneNumber.of("+57", "3109876543"),
    email: Email.of("maria.lopez@email.com"),
    reservationChannel: ReservationChannel.WEB
);

// Verificar si puede agendar
if (mariaLopez.puedeAgendar()) {
    // Proceder con agendamiento
}

// Crear paciente menor (requiere responsable)
Patient juanPequeño = Patient.register(
    fullName: FullName.of("Juan", "Pequeño"),
    documentId: DocumentId.of("TI", "123456789"),
    dateOfBirth: DateOfBirth.of(LocalDate.of(2015, 8, 10)),
    phoneNumber: PhoneNumber.of("+57", "3001112233"),
    email: null,
    reservationChannel: ReservationChannel.PRESENCIAL
);

// Vincular responsable obligatorio
if (juanPequeño.esMenorDeEdad()) {
    Guardian padre = Guardian.find("CC", "111222333");
    juanPequeño.vincularResponsable(padre);
}

// Historial completo
List<Appointment> historial = mariaLopez.historialCompleto();
```

---

## Bloqueos Clínicos

**Tipos de Bloqueo:**

- **Morosidad**: Deudas pendientes superiores a X días.
- **Incumplimiento**: No asistencia reiterada sin cancelar.
- **Comportamiento**: Conductas inapropiadas registradas.
- **Médico**: Restricción por condición de salud (requiere autorización).
- **Administrativo**: Documentación incompleta o irregular.

**Gestión de Bloqueos:**
```java
// Aplicar bloqueo
mariaLopez.agregarBloqueoClinico(
    tipo: TipoBloqueo.MOROSIDAD,
    motivo: "Deuda pendiente > 60 días",
    autorizado: usuarioAdmin
);

// Verificar bloqueos
if (mariaLopez.tieneBloqueosActivos()) {
    List<BloqueoClinico> bloqueos = mariaLopez.getBloqueosActivos();
    // Mostrar advertencia al usuario
}

// Remover bloqueo
mariaLopez.removerBloqueoClinico(
    bloqueoId: "BLQ-001",
    motivo: "Deuda saldada",
    autorizado: usuarioAdmin
);
```

---

## Consentimiento Informado (Menores)

Para pacientes menores de edad:

1. **Registro inicial**: Debe vincular responsable legal.
2. **Agendamiento**: Sistema valida presencia de responsable.
3. **Tratamientos**: Requiere autorización explícita del responsable.
4. **Documentación**: Firma del responsable en consentimientos.
5. **Comunicación**: Notificaciones enviadas al responsable, no al menor.

```java
// Validación en agendamiento
if (paciente.esMenorDeEdad()) {
    if (paciente.getGuardian() == null) {
        throw new InvalidPatientException(
            "Paciente menor de edad requiere responsable legal vinculado"
        );
    }
    
    // Notificar al responsable
    notificationService.sendAppointmentNotification(
        recipient: paciente.getGuardian(),
        appointment: nuevaCita
    );
}
```

---

## Métricas de Gestión

**Pacientes Activos vs. Inactivos**
```
Tasa Retención = (Pacientes Activos / Total Pacientes) * 100
```

**Adherencia al Tratamiento**
- Citas completadas vs. citas programadas.
- Promedio de inasistencias por paciente.
- Tiempo promedio entre citas.

**Origen de Pacientes**
- Distribución por canal de reserva.
- Efectividad de canales de adquisición.
- Tasa de conversión por canal.

**Demografía**
- Distribución por edad y género.
- Pacientes menores vs. adultos.
- Localización geográfica.