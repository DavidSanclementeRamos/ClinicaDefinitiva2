# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Receptionist (Recepcionista)

## Propósito
Representar al personal administrativo encargado de gestionar la agenda clínica, validar condiciones operativas para el agendamiento y facilitar la interacción entre pacientes y profesionales. Este agregado no atiende clínicamente, pero protege la coherencia operativa del sistema y garantiza la eficiencia del flujo de atención.

---

## CREACIÓN
- Debe tener nombre completo (FullName), documento único (DocumentId) y credenciales de acceso.
- No puede crearse con estado INACTIVO.
- Debe estar asociado a una sede o unidad operativa válida.
- Debe registrar al menos un medio de contacto válido (email o teléfono).
- Debe asignarse rol de usuario con permisos específicos (RECEPTIONIST).
- Se asigna estado inicial ACTIVE por defecto.

---

## EDICIÓN / ACTUALIZACIÓN
- No puede modificarse la sede si tiene citas asignadas en curso.
- No puede modificarse el documento de identidad (inmutable).
- No puede eliminarse el documento ni el nombre.
- Solo puede editarse si está activo.
- Cambios en sede requieren validación de tareas pendientes.
- Cambios sensibles deben registrar fecha, responsable y motivo.

---

## DESACTIVACIÓN / ELIMINACIÓN
- No puede desactivarse si tiene tareas pendientes (citas por confirmar, pacientes en espera).
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar motivo obligatorio de desactivación.
- Debe registrar fecha de desactivación.
- Debe reasignar tareas pendientes a otro recepcionista antes de desactivar.

---

## OPERACIONES DE DOMINIO
- puedeAgendarPara(dentist, patient, fechaHora) → Verifica odontólogo activo/disponible y paciente habilitado.
- confirmarCita(appointment) → Valida condiciones clínicas y operativas antes de confirmar.
- cancelarCita(appointment) → Solo si no está dentro de las 24h previas.
- reasignarCita(appointment, nuevoHorario) → Solo si hay disponibilidad y no hay conflicto.
- tieneTareasPendientes() → Verifica si tiene citas por confirmar o pacientes en espera.
- reasignarTareas(otroRecepcionista) → Transfiere tareas pendientes.

---

## INVARIANTES GLOBALES
- Un recepcionista activo debe estar asociado a una sede válida.
- No puede confirmar citas para odontólogos inactivos.
- No puede agendar citas duplicadas para el mismo paciente en el mismo horario.
- No puede cancelar citas dentro de las 24h previas sin autorización especial.
- Debe tener credenciales de acceso válidas para operar en el sistema.
- No puede modificar información clínica de pacientes.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada acción de agendamiento, confirmación, cancelación y reasignación.
- Se puede emitir un Outcome al intentar agendar en condiciones inválidas.
- Se registra el motivo y fecha de desactivación o bloqueo.
- Se registra cada interacción con pacientes (llamadas, mensajes, visitas).
- Sistema emite alertas al detectar intentos de operación fuera de permisos.

---

## Justificación Semántica
Estas reglas aseguran que el modelo de recepcionista sea operativo, trazable y coherente con las restricciones clínicas. Protegen la agenda, evitan errores administrativos, garantizan que solo personal activo y autorizado gestione citas y permiten auditar cada decisión relevante en el flujo de atención.

---

## Reglas Descubiertas (formato estandarizado)

**RN-RECEPTIONIST-001**
- Descripción: No puede confirmar citas para odontólogos inactivos.
- Condición: confirmarCita() invocado con Dentist.status != ACTIVE.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_RECEPTIONIST_DENTIST_INACTIVE

**RN-RECEPTIONIST-002**
- Descripción: No puede agendar citas duplicadas para el mismo paciente en el mismo horario.
- Condición: puedeAgendarPara() detecta cita existente con mismo patientId y fechaHora.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_RECEPTIONIST_DUPLICATE_APPOINTMENT

**RN-RECEPTIONIST-003**
- Descripción: Solo puede cancelar citas si no están dentro de las 24h previas.
- Condición: cancelarCita() invocado con (fechaCita - now) < 24h.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_RECEPTIONIST_LATE_CANCELLATION

**RN-RECEPTIONIST-004**
- Descripción: Debe estar asociado a una sede válida.
- Condición: Receptionist.sede == null || sede.status != ACTIVE al invocar creación/operaciones.
- Consecuencia: No se permite creación ni operaciones.
- Error asociado: ERR_RECEPTIONIST_INVALID_LOCATION

**RN-RECEPTIONIST-005**
- Descripción: Solo puede editarse si está activo.
- Condición: Receptionist.status != ACTIVE al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_RECEPTIONIST_NOT_EDITABLE

**RN-RECEPTIONIST-006**
- Descripción: No puede desactivarse si tiene tareas pendientes.
- Condición: Receptionist.tieneTareasPendientes() == true al invocar desactivación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_RECEPTIONIST_HAS_PENDING_TASKS

**RN-RECEPTIONIST-007**
- Descripción: No puede modificar sede si tiene citas asignadas en curso.
- Condición: Receptionist.citasAsignadas > 0 al intentar modificar sede.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_RECEPTIONIST_ACTIVE_ASSIGNMENTS

**RN-RECEPTIONIST-008**
- Descripción: Debe tener credenciales de acceso válidas.
- Condición: Receptionist.credentials == null || !credentials.isValid() al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_RECEPTIONIST_MISSING_CREDENTIALS

**RN-RECEPTIONIST-009**
- Descripción: Debe tener al menos un medio de contacto válido.
- Condición: Receptionist.email == null && Receptionist.phoneNumber == null al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_RECEPTIONIST_MISSING_CONTACT

**RN-RECEPTIONIST-010**
- Descripción: Desactivación requiere motivo obligatorio.
- Condición: desactivar(motivo) con motivo == null || motivo.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_RECEPTIONIST_DEACTIVATION_REQUIRES_REASON

---

## Relación con ADRs
- ADR-25 (Dominio): Modelado de Persona en el dominio clínico - estructura base.
- ADR-30 (Dominio): Catálogo de reglas CRUD por rol - permisos específicos de recepción.
- ADR-27 (Dominio): Formas de construcción de objetos (Builder vs Setters).
- ADR-32 (Dominio): Implementación sistemática de reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas de negocio - validación de operaciones administrativas.

---

## Eventos de Dominio
- ReceptionistRegistered: Al crear nuevo recepcionista.
- ReceptionistActivated: Al activar recepcionista inactivo.
- ReceptionistDeactivated: Al desactivar recepcionista.
- ReceptionistLocationUpdated: Al cambiar sede asignada.
- ReceptionistAppointmentScheduled: Al agendar nueva cita.
- ReceptionistAppointmentConfirmed: Al confirmar cita.
- ReceptionistAppointmentCancelled: Al cancelar cita.
- ReceptionistAppointmentRescheduled: Al reprogramar cita.
- ReceptionistTasksReassigned: Al transferir tareas pendientes.

---

## Permisos y Responsabilidades

**Operaciones Permitidas:**

- ✅ Agendar citas (validando disponibilidad).
- ✅ Confirmar citas (validando condiciones).
- ✅ Cancelar citas (fuera de 24h).
- ✅ Reprogramar citas (con validaciones).
- ✅ Consultar disponibilidad de odontólogos.
- ✅ Consultar información de pacientes (solo contacto y citas).
- ✅ Registrar nuevos pacientes.
- ✅ Actualizar contacto de pacientes.
- ✅ Generar reportes de ocupación de agenda.

**Operaciones NO Permitidas:**

- ❌ Modificar información clínica de pacientes.
- ❌ Autorizar tratamientos.
- ❌ Registrar diagnósticos.
- ❌ Acceder a historia clínica completa.
- ❌ Modificar tarifas o precios.
- ❌ Aprobar pagos o facturas.
- ❌ Desactivar odontólogos o pacientes.
- ❌ Modificar configuración del sistema.

---

## Flujo de Trabajo Típico

**1. Llegada de Paciente (Presencial)**
```java
// Recepcionista busca paciente
Patient paciente = patientRepository.findByDocument("CC", "123456789");

// Verifica si tiene cita
Appointment citaHoy = appointmentRepository.findByPatientAndToday(paciente);

if (citaHoy != null) {
    // Confirmar llegada
    recepcionista.confirmarCita(citaHoy);
} else {
    // Paciente sin cita
    recepcionista.notificarPacienteSinCita(paciente);
}
```

**2. Agendamiento Telefónico**
```java
// Recepcionista recibe llamada
Patient paciente = patientRepository.findByPhone("+573001234567");

// Consultar disponibilidad
LocalDateTime fechaDeseada = LocalDateTime.of(2025, 12, 20, 10, 0);
List<Dentist> disponibles = recepcionista.consultarDisponibilidad(fechaDeseada);

// Agendar con odontólogo seleccionado
if (recepcionista.puedeAgendarPara(drGomez, paciente, fechaDeseada)) {
    Appointment nuevaCita = Appointment.create(
        patient: paciente,
        dentist: drGomez,
        dateTime: fechaDeseada,
        type: AppointmentType.CONTROL,
        reason: "Control periódico"
    );
    
    recepcionista.registrarCita(nuevaCita);
    recepcionista.enviarConfirmacion(paciente, nuevaCita);
}
```

**3. Cancelación de Cita**
```java
// Paciente llama para cancelar
Appointment cita = appointmentRepository.findById(citaId);

// Validar plazo de 24h
if (recepcionista.puedeCancel(cita)) {
    recepcionista.cancelarCita(
        appointment: cita,
        motivo: "Motivo del paciente",
        notificarOdontologo: true
    );
} else {
    // Requiere autorización especial
    recepcionista.solicitarAutorizacionCancelacion(cita);
}
```

**4. Reprogramación**
```java
// Paciente solicita cambio de fecha
Appointment citaOriginal = appointmentRepository.findById(citaId);
LocalDateTime nuevaFecha = LocalDateTime.of(2025, 12, 25, 14, 0);

if (recepcionista.puedeReasignar(citaOriginal, nuevaFecha)) {
    recepcionista.reasignarCita(
        appointment: citaOriginal,
        nuevoHorario: nuevaFecha,
        motivo: "Solicitud del paciente"
    );
    
    recepcionista.notificarCambio(citaOriginal, nuevaFecha);
}
```

---

## Gestión de Tareas Pendientes

**Tipos de Tareas:**

- **Citas por Confirmar**: Citas agendadas que requieren confirmación telefónica 24-48h antes.
- **Pacientes en Espera**: Lista de espera para cancelaciones de último momento.
- **Seguimientos**: Llamadas de seguimiento post-tratamiento.
- **Recordatorios**: Envío de recordatorios de cita vía SMS/email.

```java
// Obtener tareas pendientes
List<Task> tareas = recepcionista.getTareasPendientes();

// Procesar tareas
for (Task tarea : tareas) {
    switch (tarea.getType()) {
        case CONFIRMAR_CITA:
            recepcionista.confirmarCitaTelefonica(tarea.getAppointment());
            break;
        case LISTA_ESPERA:
            recepcionista.procesarListaEspera(tarea);
            break;
        case SEGUIMIENTO:
            recepcionista.realizarSeguimiento(tarea.getPatient());
            break;
    }
}
```

---

## Transferencia de Tareas

Cuando un recepcionista se desactiva o cambia de sede:

```java
// Reasignar tareas pendientes
Receptionist otroRecepcionista = receptionistRepository.findBySede(sede);

if (recepcionista.tieneTareasPendientes()) {
    recepcionista.reasignarTareas(
        nuevoRecepcionista: otroRecepcionista,
        motivo: "Cambio de turno"
    );
}

// El sistema:
// - Transfiere todas las tareas pendientes
// - Notifica al nuevo recepcionista
// - Registra auditoría completa
// - Permite desactivación del recepcionista original
```

---

## Ejemplo de Uso Completo

```java
// Registrar recepcionista
Receptionist laura = Receptionist.register(
    fullName: FullName.of("Laura", "Martínez"),
    documentId: DocumentId.of("CC", "777888999"),
    sede: sedeNorte,
    phoneNumber: PhoneNumber.of("+57", "3205556677"),
    email: Email.of("laura.martinez@clinica.com"),
    credentials: UserCredentials.create("lmartinez", "password123")
);

// Asignar permisos de recepción
laura.assignRole(Role.RECEPTIONIST);

// Operación diaria: agendar cita
Patient paciente = patientRepository.findByDocument("CC", "111222333");
Dentist odontologo = dentistRepository.findById(dentistId);
LocalDateTime fecha = LocalDateTime.of(2025, 12, 20, 10, 0);

if (laura.puedeAgendarPara(odontologo, paciente, fecha)) {
    Appointment cita = Appointment.create(
        patient: paciente,
        dentist: odontologo,
        dateTime: fecha,
        type: AppointmentType.PRIMERA_VEZ,
        reason: "Dolor molar inferior derecho"
    );
    
    laura.registrarCita(cita);
    laura.enviarConfirmacion(paciente, cita);
}
```

---

## Métricas de Gestión

**Productividad por Recepcionista**
```
Citas Agendadas / Hora de Trabajo
```

**Tasa de Confirmación**
```
Confirmación = (Citas Confirmadas / Citas Programadas) * 100
```

**Tiempo Promedio de Atención**
- Tiempo desde llegada del paciente hasta inicio de consulta.
- Identifica cuellos de botella en recepción.

**Cancelaciones Gestionadas**
- Cancelaciones dentro de plazo (> 24h).
- Cancelaciones de último momento (< 24h).
- Impacto en ocupación de agenda.

**Efectividad de Seguimiento**
- Tasa de respuesta a llamadas de confirmación.
- Pacientes contactados vs. pendientes.
- Recordatorios enviados vs. citas asistidas.