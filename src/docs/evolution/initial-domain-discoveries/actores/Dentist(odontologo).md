# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Dentist (Odontólogo)

## Propósito
Representar al profesional clínico encargado de atender citas odontológicas. Este agregado gestiona disponibilidad, estado operativo, especialidad y expone reglas que protegen la continuidad del servicio y la coherencia clínica del sistema. Asegura que solo profesionales calificados y activos puedan brindar atención.

---

## CREACIÓN
- El odontólogo debe tener al menos 25 años (requisito legal/profesional).
- Debe registrar disponibilidad inicial (al menos un bloque horario).
- No puede crearse con estado INACTIVO.
- Debe tener nombre válido (FullName) y documento único (DocumentId).
- Debe tener especialidad reconocida válida (Specialty).
- Debe tener al menos un medio de contacto válido (email o teléfono).
- Dirección completa es obligatoria.
- Estado inicial por defecto: ACTIVE.

---

## EDICIÓN / ACTUALIZACIÓN
- La disponibilidad no puede quedar completamente vacía.
- No puede reducirse la edad registrada (campo inmutable derivado de fecha nacimiento).
- No puede editarse si está inactivo.
- No puede modificarse el documento de identidad.
- Puede actualizarse nombre, contacto, dirección, especialidad.
- Cambios en especialidad requieren justificación y validación.
- Cambios sensibles deben registrar fecha, responsable y motivo.

---

## DESACTIVACIÓN / ELIMINACIÓN
- No puede desactivarse si tiene citas activas en las próximas 24 horas.
- La desactivación se realiza mediante cambio de estado (UserStatus).
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar motivo obligatorio de desactivación.
- Debe notificar a pacientes con citas futuras antes de desactivar.

---

## OPERACIONES DE DOMINIO
- puedeAgendar(fechaHora) → Verifica si está activo, tiene disponibilidad y no tiene otra cita.
- tieneCitaEn(fechaHora) → Verifica conflicto de horario con cita existente.
- citasActivasEnLasProximas24Horas() → Devuelve citas que bloquean desactivación.
- agregarDisponibilidad(availability) → Agrega bloque de disponibilidad.
- removerDisponibilidad(availability) → Remueve disponibilidad si no tiene citas.
- actualizarEspecialidad(nuevaEspecialidad) → Cambia especialidad con validación.

---

## INVARIANTES GLOBALES
- Un odontólogo activo siempre debe tener al menos una disponibilidad registrada.
- No puede tener dos citas en el mismo horario.
- No puede estar activo sin edad válida (≥ 25 años).
- La disponibilidad debe ser coherente (no puede tener bloques solapados).
- Debe tener especialidad válida reconocida por el sistema.
- No puede atender citas si está inactivo.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra el rechazo al desactivar si tiene citas activas.
- Se puede emitir un Outcome al intentar agendar en horario no disponible.
- Se registra cada cambio de especialidad con justificación.
- Se registra cada cambio de estado con fecha y responsable.
- Sistema emite alertas al intentar operaciones con odontólogo inactivo.

---

## Justificación Semántica
Estas reglas protegen la integridad clínica del sistema, evitan estados inconsistentes como odontólogos activos sin disponibilidad o con citas duplicadas, aseguran cumplimiento de requisitos profesionales mínimos y garantizan que el modelo sea evaluable, trazable y listo para exhibición internacional.

---

## Reglas Descubiertas (formato estandarizado)

**RN-DENTIST-001**
- Descripción: Un odontólogo debe tener al menos 25 años al crearse.
- Condición: Dentist.age < 25 al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_DENTIST_AGE_INSUFFICIENT

**RN-DENTIST-002**
- Descripción: Debe registrar disponibilidad inicial.
- Condición: Dentist.availability == null || availability.isEmpty() al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_DENTIST_MISSING_AVAILABILITY

**RN-DENTIST-003**
- Descripción: No puede desactivarse si tiene citas activas en las próximas 24 horas.
- Condición: Dentist.citasActivasEnLasProximas24Horas() > 0 al invocar desactivación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_DENTIST_ACTIVE_APPOINTMENTS

**RN-DENTIST-004**
- Descripción: No puede tener dos citas en el mismo horario.
- Condición: Dentist.tieneCitaEn(fechaHora) == true al intentar agendar nueva cita.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_DENTIST_TIME_CONFLICT

**RN-DENTIST-005**
- Descripción: Solo puede agendar si está activo y tiene disponibilidad.
- Condición: Dentist.status != ACTIVE || availability == null al invocar puedeAgendar().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_DENTIST_NOT_AVAILABLE

**RN-DENTIST-006**
- Descripción: Solo puede editarse si está activo.
- Condición: Dentist.status != ACTIVE al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_DENTIST_NOT_EDITABLE

**RN-DENTIST-007**
- Descripción: Debe tener especialidad reconocida válida.
- Condición: Dentist.specialty == null || !specialty.isRecognized() al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_DENTIST_INVALID_SPECIALTY

**RN-DENTIST-008**
- Descripción: No puede crearse con estado INACTIVO.
- Condición: Dentist.status == INACTIVE al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_DENTIST_INVALID_INITIAL_STATUS

**RN-DENTIST-009**
- Descripción: Debe tener nombre y documento válidos.
- Condición: Dentist.fullName == null || Dentist.documentId == null al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_DENTIST_MISSING_REQUIRED_FIELDS

**RN-DENTIST-010**
- Descripción: La disponibilidad no puede quedar vacía al editar.
- Condición: Dentist.availability.isEmpty() después de remover disponibilidad.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_DENTIST_EMPTY_AVAILABILITY

---

## Relación con ADRs
- ADR-25 (Dominio): Modelado de Persona en el dominio clínico - estructura base.
- ADR-30 (Dominio): Catálogo de reglas CRUD por rol - permisos para gestionar odontólogos.
- ADR-27 (Dominio): Formas de construcción de objetos (Builder vs Setters).
- ADR-32 (Dominio): Implementación sistemática de reglas de negocio por agregado.
- ADR-034 (Dominio): Guardian de reglas de negocio - validación de agendamiento.

---

## Eventos de Dominio
- DentistRegistered: Al crear nuevo odontólogo.
- DentistActivated: Al activar odontólogo inactivo.
- DentistDeactivated: Al desactivar odontólogo.
- DentistSpecialtyUpdated: Al cambiar especialidad.
- DentistAvailabilityAdded: Al agregar nueva disponibilidad.
- DentistAvailabilityRemoved: Al remover disponibilidad.
- DentistProfileUpdated: Al actualizar información de contacto.

---

## Especialidades Odontológicas

**Especialidades Reconocidas en Colombia:**

- **Odontología General**: Atención integral básica.
- **Ortodoncia**: Corrección de malposiciones dentales y maxilares.
- **Endodoncia**: Tratamiento de conductos radiculares.
- **Periodoncia**: Tratamiento de encías y tejidos de soporte.
- **Cirugía Oral y Maxilofacial**: Procedimientos quirúrgicos complejos.
- **Odontopediatría**: Atención especializada en niños.
- **Prostodoncia**: Rehabilitación con prótesis dentales.
- **Implantología**: Colocación de implantes dentales.
- **Estética Dental**: Procedimientos estéticos y cosméticos.
- **Patología Oral**: Diagnóstico de enfermedades bucales.

---

## Value Objects Involucrados

**DateOfBirth (Fecha de Nacimiento)**
- Valida edad mínima de 25 años.
- Calcula edad actual automáticamente.
- Inmutable.

**Specialty (Especialidad)**
- Debe ser una especialidad reconocida.
- Valida contra catálogo del sistema.
- Puede requerir certificación.

**WeeklyAvailability (Disponibilidad Semanal)**
- Define horarios recurrentes por día de semana.
- Valida que no haya solapamientos.
- Permite múltiples bloques por día.

**UserStatus (Estado de Usuario)**
- Posibles estados: ACTIVE, INACTIVE, SUSPENDED, BLOCKED.
- Controla transiciones válidas entre estados.
- Afecta capacidad operativa.

**PhoneNumber (Teléfono)**
- Valida formato colombiano (+57).
- Normaliza entrada (elimina espacios, guiones).
- Longitud: 7-15 dígitos.

**FullName (Nombre Completo)**
- Valida mínimo 2 caracteres por nombre/apellido.
- Solo acepta letras y espacios.
- Normaliza capitalización.

**Address (Dirección)**
- Debe contener calle, ciudad, código postal, país.
- Valida formato colombiano.
- Permite trazabilidad geográfica.

---

## Ejemplo de Uso

```java
// Crear odontólogo
Dentist drGomez = Dentist.register(
    fullName: FullName.of("Carlos", "Gómez"),
    documentId: DocumentId.of("CC", "123456789"),
    dateOfBirth: DateOfBirth.of(LocalDate.of(1985, 5, 20)),
    specialty: Specialty.ORTODONCIA,
    phoneNumber: PhoneNumber.of("+57", "3001234567"),
    email: Email.of("carlos.gomez@clinica.com"),
    address: Address.of("Calle 10 #5-20", "Cali", "760001", "Colombia")
);

// Agregar disponibilidad
WeeklyAvailability lunes = WeeklyAvailability.create(
    dayOfWeek: DayOfWeek.MONDAY,
    startTime: LocalTime.of(9, 0),
    endTime: LocalTime.of(13, 0)
);
drGomez.agregarDisponibilidad(lunes);

// Validar si puede agendar
LocalDateTime citaDeseada = LocalDateTime.of(2025, 12, 15, 10, 30);
if (drGomez.puedeAgendar(citaDeseada)) {
    // Proceder con agendamiento
}

// Desactivar (valida citas futuras)
if (drGomez.citasActivasEnLasProximas24Horas().isEmpty()) {
    drGomez.deactivate("Vacaciones programadas");
}
```

---

## Métricas de Gestión

**Ocupación de Agenda**
```
Ocupación = (Citas Agendadas / Slots Disponibles) * 100
```

**Productividad por Odontólogo**
- Citas completadas vs. citas programadas.
- Promedio de duración por cita.
- Ingresos generados por profesional.

**Disponibilidad Efectiva**
- Horas totales disponibles vs. horas realmente utilizadas.
- Períodos con baja ocupación.
- Oportunidades de optimización de agenda.