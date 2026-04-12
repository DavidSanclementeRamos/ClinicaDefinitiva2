# ADR-20 (Arquitectura) – Lección aprendida: Los tropiezos del módulo Actor y el verdadero significado del dominio

**Estado:** Lección aprendida (retrospectiva)  
**Fecha original:** 2024-12-24  
**Reescrito:** 2026-04-11

---

## El origen del error: cuando el dominio era solo una tabla más

El módulo Actor fue el primero que comencé a construir. Lo elegí creyendo que era el más familiar — al fin y al cabo, pacientes, odontólogos y recepcionistas son objetos del mundo real fáciles de imaginar. Me equivoqué. No porque el módulo fuera difícil, sino porque **no sabía lo que hacía**.

Venía de cursos donde el dominio era simplemente la base de datos con forma de objeto. Las clases eran POJOs con anotaciones JPA, y eso era todo. Así lucía una entidad típica de aquella época:

```java
@Entity
public class Paciente extends Persona implements Serializable {
    private boolean tiene_OS;
    private String tipoSangre;
    @OneToOne
    private Responsable unResponsable;
    @OneToMany(mappedBy="pacien")
    private List<Turno> listaTurnos;
    // getters y setters...
}
```

Todo era persistencia. Todo era JPA. El dominio no existía porque yo no sabía que el dominio era otra cosa.

## El choque con Hexagonal: el dominio no es la base de datos

Cuando empecé la migración a arquitectura hexagonal, ese modelo se derrumbó. Resultaba que lo que venía haciendo estaba mal: el dominio no era igual a la persistencia. Persistir era solo una consecuencia. Antes de llegar a eso, había un mundo de reglas, invariantes y responsabilidades que determinaban si los datos que se iban a persistir tenían sentido.

Para entender ese mundo, empecé a documentar reglas de negocio por agregado. Para Paciente, el descubrimiento tenía esta forma:

**CREACIÓN**
- Debe tener nombre completo, documento único y fecha de nacimiento válida.
- No puede crearse con estado INACTIVO.
- Debe registrar al menos un medio de contacto (email o teléfono).
- La edad calculada no puede ser negativa ni mayor a 120 años.
- Fecha de nacimiento no puede ser futura.
- Si es menor de edad, debe estar vinculado a un responsable (Guardian).

**EDICIÓN**
- No puede modificarse la fecha de nacimiento si ya tiene citas registradas.
- No puede eliminarse el documento ni el nombre.
- Cambios sensibles deben registrar fecha, responsable y motivo.

**DESACTIVACIÓN**
- No puede desactivarse si tiene citas activas o tratamientos en curso.
- La eliminación física está prohibida; se maneja como estado lógico.
- Debe registrar motivo obligatorio de desactivación.

Entender todas esas invariantes me permitió comprender qué es una **regla de negocio**. Pero también trajo un problema nuevo: reglas compartidas entre clases, reglas cuya responsabilidad no estaba clara, y descubrimientos enormes con decenas de reglas por agregado que se volvían insostenibles de mantener.

Fue ahí donde empecé a aprender conceptos como **agregados**, **Value Objects** y **servicios de dominio**, que me dieron estructura para organizar todo eso.

Así empezó a verse un agregado de verdad:

```java
public class Patient {
    private final PatientId patientId;
    private final UserIdentityId userIdentityId;
    private final GuardianId guardianId;
    private LocalDateTime lastUpdate;
    private ContractId contractId;
    private final List<TreatmentId> treatments;
    private Person person;

    private Patient(...) { ... }

    public static Patient registerPatient(
            Person data,
            UserIdentityId userIdentityId,
            GuardianId guardianId) {

        if (!data.getAge().isEligibleForRegistration()) {
            throw new DomainAggregateException(
                PatientError.ERR_PATIENT_INVALID_AGE,
                EntityContext.PATIENT);
        }
        if (!data.getAge().isAdult() && guardianId == null) {
            throw new BusinessRuleViolationException(
                PatientError.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN,
                EntityContext.PATIENT);
        }
        return new Patient(null, guardianId, data, null, userIdentityId, null);
    }

    public void updatePatientContact(
            Optional<Address> newAddress,
            Optional<PhoneNumber> newPhoneNumber) { ... }

    public void updateSensitiveData(...) { ... }
    public Outcome<Void> validateDeactivation() { ... }
}
```

Métodos semánticos (`registerPatient`, `updatePatientContact`), validaciones en el lugar correcto, responsabilidades claras. Pero todavía quedaba el problema más difícil del módulo.

## El caso que me rompió la cabeza: la desactivación de usuario

La desactivación de usuario parecería no ser responsabilidad del módulo Actor. Pero en ese entonces no lo tenía claro, especialmente con el agregado `Dentist`.

El primer error fue mezclar dos estados que son conceptualmente distintos:

- **Estado de disponibilidad operativa**: si el odontólogo está disponible para recibir turnos (puede estar en vacaciones, incapacidad, activo).
- **Estado del sistema**: si el usuario puede autenticarse y operar en el sistema (activo, inactivo, suspendido).

Los dos estados coexisten en el mismo profesional, pero son independientes y tienen ciclos de vida distintos. Confundirlos llevó a lógica duplicada, validaciones en el lugar equivocado, y agregados que sabían demasiado sobre cosas que no les correspondían.

Una vez que separé esos conceptos, apareció el problema real: aunque la desactivación de usuario es responsabilidad de `UserIdentity`, este no puede actuar de forma aislada. Un responsable con un paciente a cargo, un paciente con una cita activa, un odontólogo que debe atender una cita en las próximas horas — ninguno puede ser desactivado forzosamente sin que el sistema verifique esas condiciones. El módulo Actor tenía que tener voz en ese proceso.

Entender eso fue clave. Pero en la práctica fue mucho más complicado.

### Solución 1 (la más absurda): un servicio que lo preguntaba a todos

Se me ocurrió un `ActorDeactivationService` que recorría **todos** los agregados del módulo Actor para validar si el usuario podía desactivarse. La coordinación era centralizada: el servicio ejecutaba la desactivación si todas las validaciones eran exitosas.

El problema era obvio: para desactivar a un odontólogo, el servicio también le preguntaba al módulo de Pacientes, al de Responsables y al de Recepcionistas si estaban disponibles. Una consulta masiva sin criterio.

### Solución 2 (mejor, pero todavía con problemas)

Cada agregado validaba únicamente sus propias reglas mediante `assertCanBeDeactivated()`. Se evitaba recorrer todos los actores para desactivar uno. El agregado `UserIdentity` era quien ejecutaba la desactivación si las validaciones del actor lo permitían.

**Problema:** las validaciones que requerían coordinar con otros agregados (por ejemplo, verificar si el odontólogo tiene citas en las próximas 24 horas) terminaban acopladas dentro del propio agregado de Actor. Esa lógica cruzada no le pertenecía al agregado — pertenecía a un Domain Service.

### La solución correcta

Después de varios intentos, llegué a la solución documentada en ADR-38, ADR-39 y la implementación de `UserDeactivationPolicy`:

- **`UserAccessValidator`**: valida el acceso técnico del usuario (verificado, no bloqueado, activo). Responsabilidad técnica.
- **`UserDeactivationPolicy`**: orquesta las validaciones cruzadas entre agregados. Responsabilidad de negocio.
- **Application Service**: coordina ambos antes de ejecutar `userIdentity.deactivate()`.

**Regla de localización de validaciones:**

| Condición | Ubicación | Ejemplo |
|-----------|-----------|---------|
| Depende **solo de atributos internos** del agregado | Método en el agregado | `Guardian.validateDeactivation()` verifica si `patientList.isEmpty()` |
| Requiere **coordinar con otro agregado** | Domain Service especializado | `DentistDeactivationValidator` consulta `ScheduleRepository` |
| Involucra **múltiples agregados** | Policy orquestador | `UserDeactivationPolicy` invoca múltiples validadores |

```java
// Domain Service especializado (validación que cruza con otro agregado)
public class DentistDeactivationValidator {
    private final ScheduleRepository scheduleRepo;

    public Outcome<Void> validate(DentistId dentistId) {
        Schedule schedule = scheduleRepo.findByDentistId(dentistId);
        if (schedule.hasAppointmentsWithinHours(24)) {
            return Outcome.fail(DentistError.ERR_DENTIST_HAS_PENDING_APPOINTMENTS);
        }
        return Outcome.ok();
    }
}

// Policy como orquestador de múltiples validadores
public class UserDeactivationPolicy {
    private final DentistDeactivationValidator dentistValidator;
    private final PatientDeactivationValidator patientValidator;

    public Outcome<Void> validate(UserIdentity user) {
        return switch (user.getRole()) {
            case DENTIST  -> dentistValidator.validate(user.getId());
            case PATIENT  -> patientValidator.validate(user.getId());
            default       -> Outcome.ok();
        };
    }
}
```

## Lecciones aprendidas

**1. El dominio no es la base de datos.**  
Persistir es solo una consecuencia. Lo importante es modelar correctamente las reglas e invariantes del negocio.

**2. Los estados que parecen similares no siempre son el mismo estado.**  
`DentistAvailabilityStatus` y `UserStatus` coexisten en el mismo profesional pero son independientes. Confundirlos genera lógica duplicada y responsabilidades mal asignadas.

**3. Las validaciones de formato pertenecen a los Value Objects, no a los agregados.**  
Si un VO ya garantiza que no puede ser nulo o vacío, el agregado no necesita repetir esa validación.

**4. Las validaciones cruzadas tienen un lugar claro.**  
Lo que depende solo del agregado vive en el agregado. Lo que coordina con otro agregado vive en un Domain Service especializado. Lo que involucra múltiples agregados vive en un Policy orquestador.

**5. Una desactivación no es cambiar un flag.**  
Implica coordinar validaciones con múltiples agregados que tienen responsabilidades activas. No coloques toda esa lógica en un solo lugar sin criterio.

**6. Los errores de diseño que involucran múltiples agregados son los más costosos de corregir.**  
Una mala decisión en cómo dos agregados se relacionan no afecta solo a uno — afecta a ambos, y a todo lo que depende de esa relación.

## Reflexión final

Hubo un momento en este módulo donde me di cuenta de que el problema no era el código. Era que seguía descubriendo reglas antes de tener suficiente contexto para evaluarlas correctamente. Documentaba decenas de invariantes hipotéticas, construía sobre ellas, y días después descubría que la mitad no tenían sentido o que había una forma mejor de modelarlas. El ciclo se repetía indefinidamente.

Lo que aprendí no es que planear es malo. Es que **planear demasiado antes de tener contexto real es igual de costoso que no planear**. En un proyecto experimental, el contexto real llega cuando empiezas a implementar. Las relaciones entre agregados, las responsabilidades que emergen, los casos límite que nunca imaginaste — esos solo aparecen cuando el código existe.

La estrategia que funciona no es "documenta todo primero" ni "codifica sin pensar". Es: **construye una base mínima que resuelva el requerimiento más importante, y agrega complejidad solo cuando un caso real lo exija**. Especialmente cuando hay múltiples agregados involucrados: una mala implementación inicial en cómo se relacionan puede obligarte a reescribir ambos. Es mejor tener dos agregados simples que funcionen correctamente, que dos agregados perfectamente diseñados en papel que luego colisionan en la práctica.

El ADR-20 original era un inventario de reglas. Esto es lo que realmente aprendí construyéndolas.

---

*Ver decisiones vigentes: [ADR-38](../../architecture/decisions/arch/ADR-%28Arquitectura%29-38-UserDeactivationPolicy%20como%20orquestador%20de%20validaciones.md), [ADR-39](../../architecture/decisions/arch/ADR-%28Arquitectura%29-39-Ubicaci%C3%B3n%20de%20validaciones%20de%20desactivaci%C3%B3n.md)*

## Referencias

### Archivos de Descubrimiento Original

- [Dentist(odontologo).md](../initial-domain-discoveries/actores/Dentist%28odontologo%29.md)
- [Guardian(Reponsable).md](../initial-domain-discoveries/actores/Guardian%28Reponsable%29.md)
- [Patient(Paciente).md](../initial-domain-discoveries/actores/Patient%28Paciente%29.md)
- [Receptionist(Secretario).md](../initial-domain-discoveries/Receptionist%28Secretario%29.md)


### ADRs Relacionados
 

- [ADR-(Dominio)-01-Implementación de Value-Objects.md](../../architecture/decisions/domain/ADR-%28Dominio%29-01-Implementaci%C3%B3n%20de%20Value-Objects.md)
- [ADR-(Dominio)-02-Implementación de reglas de negocio.md](../../architecture/decisions/ADR-%28Dominio%29-02-Implementaci%C3%B3n%20de%20reglas%20de%20negocio.md)
- [ADR-(Actores)-02-Delegación de lógica Dentist DomainService.md](../../architecture/decisions/actor/ADR-%28Actores%29-02-Delegaci%C3%B3n%20de%20l%C3%B3gica%20Dentist%20DomainService.md)
- [ADR-(Actores)-03-Mantener la mutación local en Dentist.md](../decisions/actor/ADR-%28Actores%29-03-Mantener%20la%20mutaci%C3%B3n%20local%20en%20Dentist.md)
- [ADR-(Actores)-04-Separación de edición de datos de paciente y gobernanza.md](../../architecture/decisions/actor/ADR-%28Actores%29-04-Separaci%C3%B3n%20de%20edici%C3%B3n%20de%20datos%20de%20paciente%20y%20gobernanza.md)
- [ADR-(Actores)-06-Validar responsable en paciente.md](../../architecture/decisions/actor/ADR-%28Actores%29-06-Validar%20responsable%20en%20paciente.md)
- [ADR-(Actores)-09-Refactorización semántica canScheduleAt(...) .md](../../architecture/decisions/actor/ADR-%28Actores%29-09-Refactorizaci%C3%B3n%20sem%C3%A1ntica%20canScheduleAt%28...%29%20.md)
- [ADR-(Actores)-10-Modelado de Persona.md](../../architecture/decisions/domain/actor/ADR-%28Actores%29-10-Modelado%20de%20Persona.md)
- [ADR-(Actores)-11-Separación de estado entre User y Dentist.md](../../architecture/decisions/actor/ADR-%28Actores%29-11-Separaci%C3%B3n%20de%20estado%20entre%20User%20y%20Dentist.md)
- [ADR-(Actores)-12-Separación de AvailabilityStatus entre Dentist y Availability.md](../../architecture/decisions/actor/ADR-%28Actores%29-12-Separaci%C3%B3n%20de%20AvailabilityStatus%20entre%20Dentist%20y%20Availability.md)
- [ADR-(Atores)-15-Extrategia de desactivación de Usuarios y Actores.md](../../architecture/decisions/actor/ADR-%28Atores%29-15-Extrategia%20de%20desactivaci%C3%B3n%20de%20Usuarios%20y%20Actores.md)

