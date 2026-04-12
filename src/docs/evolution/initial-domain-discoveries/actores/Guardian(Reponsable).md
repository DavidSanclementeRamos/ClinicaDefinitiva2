# Descubrimiento de Reglas de Negocio por Agregado
## Agregado: Guardian (Responsable Legal)

## Propósito
Representar al adulto responsable legal o clínico de un paciente. Este agregado gestiona el consentimiento, la autorización de tratamientos, la trazabilidad de decisiones clínicas en nombre del paciente representado y protege los derechos de menores o personas bajo tutela legal.

---

## CREACIÓN
- Debe tener nombre completo (FullName), documento único (DocumentId) y vínculo legal con el paciente.
- Debe registrar al menos un medio de contacto válido (email o teléfono).
- No puede crearse sin asociarse a un paciente válido.
- Debe especificarse tipo de relación (PADRE, MADRE, TUTOR, REPRESENTANTE_LEGAL).
- Debe ser mayor de edad (≥ 18 años).
- Se asigna estado inicial ACTIVE por defecto.

---

## EDICIÓN / ACTUALIZACIÓN
- No puede modificarse el vínculo legal si ya ha autorizado tratamientos.
- No puede modificarse el documento de identidad (inmutable).
- Cambios sensibles deben registrar fecha, responsable y motivo.
- Solo puede editarse si está activo.
- No puede eliminarse el documento ni el nombre.
- Cambios en tipo de relación requieren justificación legal.

---

## DESACTIVACIÓN / ELIMINACIÓN
- No puede desactivarse si tiene autorizaciones clínicas vigentes.
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar motivo obligatorio de desactivación.
- Debe registrar fecha de desactivación.
- Debe reasignar responsabilidad a otro tutor antes de desactivar.

---

## OPERACIONES DE DOMINIO
- puedeAutorizarTratamiento(paciente, tratamiento) → Verifica vínculo, consentimiento y estado activo.
- confirmarConsentimiento(tratamiento) → Registra aceptación explícita con firma digital/física.
- revocarConsentimiento(tratamiento) → Solo si el tratamiento no ha iniciado.
- esResponsableDe(paciente) → Valida relación actual activa.
- tieneAutorizacionesVigentes() → Verifica si tiene tratamientos autorizados activos.
- transferirResponsabilidad(nuevoGuardian, motivo) → Transfiere tutela con validaciones.

---

## INVARIANTES GLOBALES
- Un responsable activo debe estar vinculado a al menos un paciente.
- No puede autorizar tratamientos si está inactivo.
- No puede revocar consentimiento una vez iniciado el tratamiento.
- Debe tener al menos un medio de contacto válido.
- Debe ser mayor de edad para ejercer responsabilidad legal.
- No puede tener múltiples tipos de relación con el mismo paciente.

---

## TRAZABILIDAD Y AUDITORÍA
- Se registra cada autorización, revocación y edición de vínculo.
- Se puede emitir un Outcome al intentar autorizar sin vínculo válido.
- Se registra el motivo y fecha de desactivación o cambio de relación.
- Se registra firma digital/física en cada consentimiento.
- Sistema emite alertas al detectar intentos de operación sin autorización válida.

---

## Justificación Semántica
Estas reglas aseguran que el modelo de responsable sea coherente, trazable y legalmente válido. Protegen la integridad de los tratamientos clínicos, evitan autorizaciones inválidas, garantizan protección legal de menores/tutelados y permiten auditar cada decisión tomada en nombre del paciente.

---

## Reglas Descubiertas (formato estandarizado)

**RN-GUARDIAN-001**
- Descripción: No puede crearse sin vínculo legal con un paciente.
- Condición: Guardian.patient == null al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_GUARDIAN_MISSING_PATIENT

**RN-GUARDIAN-002**
- Descripción: No puede autorizar tratamientos si está inactivo.
- Condición: Guardian.status != ACTIVE al invocar puedeAutorizarTratamiento().
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_GUARDIAN_INACTIVE

**RN-GUARDIAN-003**
- Descripción: No puede revocar consentimiento si el tratamiento ya inició.
- Condición: Treatment.status == INICIADO || Treatment.status == COMPLETADO al invocar revocarConsentimiento().
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_GUARDIAN_TREATMENT_ALREADY_STARTED

**RN-GUARDIAN-004**
- Descripción: Debe registrar tipo de relación al crearse.
- Condición: Guardian.relationshipType == null al invocar creación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_GUARDIAN_MISSING_RELATIONSHIP_TYPE

**RN-GUARDIAN-005**
- Descripción: No puede desactivarse si tiene autorizaciones vigentes.
- Condición: Guardian.tieneAutorizacionesVigentes() == true al invocar desactivación.
- Consecuencia: Se rechaza operación y se registra Outcome.
- Error asociado: ERR_GUARDIAN_ACTIVE_AUTHORIZATIONS

**RN-GUARDIAN-006**
- Descripción: Solo puede editarse si está activo.
- Condición: Guardian.status != ACTIVE al invocar edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_GUARDIAN_NOT_EDITABLE

**RN-GUARDIAN-007**
- Descripción: Debe tener al menos un medio de contacto válido.
- Condición: Guardian.email == null && Guardian.phoneNumber == null al invocar creación/edición.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_GUARDIAN_MISSING_CONTACT

**RN-GUARDIAN-008**
- Descripción: Debe ser mayor de edad (≥ 18 años).
- Condición: Guardian.age < 18 al invocar creación.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_GUARDIAN_UNDERAGE

**RN-GUARDIAN-009**
- Descripción: No puede modificarse vínculo si ha autorizado tratamientos.
- Condición: Guardian.autorizacionesPrevias > 0 al intentar modificar relationshipType.
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_GUARDIAN_CANNOT_MODIFY_RELATIONSHIP

**RN-GUARDIAN-010**
- Descripción: Desactivación requiere motivo obligatorio.
- Condición: desactivar(motivo) con motivo == null || motivo.isBlank().
- Consecuencia: Se rechaza operación.
- Error asociado: ERR_GUARDIAN_DEACTIVATION_REQUIRES_REASON

---

## Relación con ADRs
- ADR-25 (Dominio): Modelado de Persona en el dominio clínico - estructura base.
- ADR-30 (Dominio): Catálogo CRUD por rol - permisos para gestionar tutores.
- ADR-28 (Dominio): Conocimientos administrativos - aspectos legales.
- ADR-32 (Dominio): Implementación sistemática de reglas de negocio por agregado.
- ADR-34 (Dominio): Guardian de reglas de negocio - validación de autorizaciones.

---

## Eventos de Dominio
- GuardianRegistered: Al crear nuevo responsable legal.
- GuardianLinkedToPatient: Al vincular responsable a paciente.
- GuardianConsentGiven: Al autorizar tratamiento.
- GuardianConsentRevoked: Al revocar autorización.
- GuardianRelationshipUpdated: Al cambiar tipo de relación.
- GuardianDeactivated: Al desactivar responsable.
- GuardianResponsibilityTransferred: Al transferir tutela.

---

## Tipos de Relación Legal

**PADRE / MADRE (Parent)**
- Relación biológica o adoptiva.
- Responsabilidad legal automática hasta mayoría de edad.
- Prioridad máxima en decisiones clínicas.

**TUTOR (Legal Guardian)**
- Designado judicialmente.
- Responsabilidad legal plena sobre menores sin padres.
- Requiere documentación judicial.

**REPRESENTANTE_LEGAL (Legal Representative)**
- Adultos bajo curatela o tutela.
- Persona con discapacidad que requiere representación.
- Requiere documentación judicial/notarial.

**APODERADO (Attorney-in-Fact)**
- Poder legal específico para decisiones médicas.
- Temporal o permanente según documento.
- Requiere poder notariado.

**OTRO (Other)**
- Relaciones especiales autorizadas por ley.
- Requiere justificación y validación.

---

## Consentimiento Informado

**Proceso de Autorización:**

1. **Identificación del Paciente**: Validar vínculo activo con responsable.
2. **Presentación del Tratamiento**: Explicar procedimiento, riesgos, beneficios.
3. **Firma del Consentimiento**: Registro físico o digital.
4. **Validación del Sistema**: Verificar autorización antes de iniciar tratamiento.

```java
// Solicitar consentimiento
TreatmentConsent consent = TreatmentConsent.create(
    guardian: responsable,
    patient: pacienteMenor,
    treatment: tratamiento,
    risks: "Riesgos explicados...",
    benefits: "Beneficios explicados..."
);

// Confirmar consentimiento
responsable.confirmarConsentimiento(consent);

// El sistema registra:
// - Fecha y hora exacta
// - Firma digital o física
// - Documento firmado (PDF)
// - IP y ubicación (si es digital)
```

**Revocación de Consentimiento:**

```java
// Revocar antes de iniciar
if (tratamiento.getStatus() == TreatmentStatus.PENDIENTE) {
    responsable.revocarConsentimiento(
        tratamiento: tratamiento,
        motivo: "Cambio de opinión del tutor"
    );
} else {
    throw new InvalidGuardianException(
        "No se puede revocar consentimiento de tratamiento ya iniciado"
    );
}
```

---

## Transferencia de Responsabilidad

Cuando un responsable no puede continuar ejerciendo tutela:

```java
// Transferir responsabilidad
Guardian nuevoTutor = Guardian.register(
    fullName: FullName.of("Ana", "Gómez"),
    documentId: DocumentId.of("CC", "555666777"),
    relationshipType: RelationshipType.TIA
);

responsableActual.transferirResponsabilidad(
    nuevoGuardian: nuevoTutor,
    motivo: "Viaje prolongado del padre",
    autorizacion: documentoLegal
);

// El sistema:
// - Valida que nuevo tutor sea válido
// - Transfiere autorizaciones vigentes
// - Notifica a todas las partes
// - Registra auditoría completa
```

---

## Ejemplo de Uso Completo

```java
// Registrar paciente menor
Patient niño = Patient.register(
    fullName: FullName.of("Carlos", "Pérez"),
    documentId: DocumentId.of("TI", "111222333"),
    dateOfBirth: DateOfBirth.of(LocalDate.of(2015, 6, 10))
);

// Registrar padre como responsable
Guardian padre = Guardian.register(
    fullName: FullName.of("Roberto", "Pérez"),
    documentId: DocumentId.of("CC", "444555666"),
    dateOfBirth: DateOfBirth.of(LocalDate.of(1980, 3, 20)),
    relationshipType: RelationshipType.PADRE,
    phoneNumber: PhoneNumber.of("+57", "3112223344"),
    email: Email.of("roberto.perez@email.com")
);

// Vincular responsable a paciente
niño.vincularResponsable(padre);

// Solicitar autorización para tratamiento
Treatment ortodoncia = Treatment.create(
    patient: niño,
    dentist: drGomez,
    type: TreatmentType.ORTODONCIA,
    estimatedCost: Money.of(5000000, "COP")
);

// Validar autorización
if (padre.puedeAutorizarTratamiento(niño, ortodoncia)) {
    TreatmentConsent consent = TreatmentConsent.create(
        guardian: padre,
        patient: niño,
        treatment: ortodoncia
    );
    
    padre.confirmarConsentimiento(consent);
}
```

---

## Métricas de Gestión

**Tasa de Consentimientos**
```
Aprobación = (Consentimientos Confirmados / Consentimientos Solicitados) * 100
```

**Tiempo Promedio de Respuesta**
- Tiempo entre solicitud y confirmación de consentimiento.
- Identificar cuellos de botella en proceso.

**Revocaciones**
- Porcentaje de consentimientos revocados.
- Motivos más frecuentes de revocación.
- Impacto financiero de revocaciones.

---

## Validación Legal (Colombia)

**Código Civil Colombiano - Patria Potestad:**
- Padres ejercen patria potestad sobre hijos menores.
- Decisiones médicas requieren consentimiento de ambos padres (salvo urgencias).
- En desacuerdo, juez de familia decide.

**Ley 1098 de 2006 - Código de Infancia y Adolescencia:**
- Protección integral de niños, niñas y adolescentes.
- Derecho a la salud y atención médica.
- Consentimiento informado en tratamientos.

**Ley 1412 de 2010:**
- Autorización de actos médicos en menores.
- Procedimientos de emergencia sin consentimiento parental.