
# Retrospectiva de Migración: De Spring MVC tradicional a Arquitectura Hexagonal

**Fecha:** 2026-02-09
**Commit:** `85df08d94b6bbb32a3ee628ea591c6136a5faf7e`
**Archivos eliminados:** 133 (producción + tests)
**Tipo:** Refactorización arquitectónica mayor

> **Nota de alcance:** Este commit representa la eliminación más grande de código legacy en una sola operación, pero no es la eliminación completa. Otras clases del diseño anterior fueron removidas en commits previos y pendientes quedan algunas más. El log de este commit es la evidencia primaria de este documento.

---

## Resumen ejecutivo

Este documento registra la eliminación masiva de código legacy pre-hexagonal del proyecto ClinicaDefinitiva, como cierre de la etapa de migración arquitectónica iniciada en [ADR-01](docs/adr/ADR-01-Migración-progresiva-a-arquitectura-hexagonal.md).

**Resultado del commit `85df08d`:** 133 archivos eliminados, representando la estructura completa del diseño anterior basado en patrones de tutoriales de Spring Boot, junto con artefactos de transición que ya no tenían soporte en el nuevo núcleo hexagonal.

---

## Contexto de la migración

### Origen (marzo – agosto 2025)

El proyecto nació siguiendo patrones de tutoriales de YouTube:

- Estructura por capas técnicas: `controller/`, `service/`, `repository/`, `entity/`
- Spring Security con `@PreAuthorize` directo en controllers
- Roles y permisos hardcodeados en `RolesFactory.java`
- Entidad `Usuario.java` mezclando identidad y autorización
- Validaciones de enum delegadas a anotaciones personalizadas (`@RolValido`, `@SectorValido`, etc.)
- Value Objects en paquete `/vo/` acoplados al modelo de entidades JPA

### Destino (septiembre 2025 – febrero 2026)

Arquitectura hexagonal con DDD:

- Módulos por agregados de negocio (`identity`, `administration`, `appointment`, `patient`)
- Puertos y adaptadores explícitos
- Dominio rico con Value Objects y reglas de negocio encapsuladas
- Sistema de autorización contextual (RBAC + ABAC)
- Separación clara entre lógica técnica y de negocio

---

## Qué se eliminó en este commit

### 1. Entidades JPA legacy (18 archivos)

```
❌ Cita.java
❌ Disponibilidad.java
❌ DocumentoClinico.java
❌ EventoClinico.java
❌ Facturacion.java
❌ HistorialCambioResponsable.java
❌ HistorialClinico.java
❌ Odontologo.java
❌ Paciente.java
❌ Pago.java
❌ PermissionEntityxd.java
❌ Persona.java
❌ Responsable.java
❌ RolesEntity.java
❌ Secretario.java
❌ Servicio.java
❌ Turno.java
❌ Usuario.java
```

**Por qué:** Entidades anémicas acopladas directamente a JPA, sin lógica de dominio. `Usuario.java` mezclaba identidad con autorización. `PermissionEntityxd.java` es evidencia de una estructura de permisos improvisada.

**Reemplazado por:** Agregados de dominio (`Patient`, `Dentist`, `Guardian`, `Receptionist`, `UserIdentity`, `Appointment`) con Value Objects y reglas de negocio encapsuladas en el dominio.

---

### 2. DTOs de persistencia (18 archivos)

```
❌ HorarioDto.java, RolesDto.java, TurnoDto.java
❌ odontologoDto/: Create, Read, Update
❌ pacienteDto/: Create, Read, Update
❌ responsableDto/: CambioResponsable, CreateEndRead, Update
❌ secretarioDto/: Create, Read, Update
❌ usuarioDto/: Create, Read, Update
```

**Por qué:** DTOs agrupados por entidad técnica, no por intención de negocio. Los nombres como `CreateEndReadResponsableDto` evidencian un diseño que nunca fue revisado.

**Reemplazado por:** DTOs organizados por bounded context y separados en dos capas con responsabilidades distintas.

La capa de aplicación expone los datos que el dominio necesita para ejecutar cada caso de uso:

```java
// package com.example.ClinicaDefinitiva.application.actor.dto.patient
CreatePatientDto
PagePatientDto
ReadPatientDto
UpdatePatientContactDto
UpdatePatientSensitiveDto
```

La capa de infraestructura expone los contratos HTTP, diferenciando requests de responses:

```java
// package com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.patient
CreatePatientRequest
PagePatientResponse
ReadPatientResponse
UpdatePatientContactRequest
UpdatePatientSensitiveRequest
```

El nombre de cada DTO expresa la operación que representa, no la entidad a la que pertenece. `UpdatePatientContactDto` y `UpdatePatientSensitiveDto` son un ejemplo de esto: en lugar de un único `UpdatePacienteDto` que mezcla datos de contacto con datos sensibles, cada operación tiene su propio contrato.

---

### 3. Services legacy (interfaces + implementaciones, 14 archivos)

```
❌ HorarioService.java / HorarioImpl.java
❌ OdontologoService.java / OdontologoImpl.java
❌ PacienteServise.java / PacienteImpl.java   ← typo en el nombre original
❌ ResponsableService.java / ResponsableImpl.java
❌ SecretarioService.java / SecretarioImpl.java
❌ TurnoService.java / TurnoImpl.java
❌ UsuarioService.java / UsuarioImpl.java
```

**Por qué:** Orchestrators anémicos que solo delegaban a repositorios. El typo `PacienteServise` —que sobrevivió durante meses— es indicativo del estado del código.

**Reemplazado por:** Puertos de entrada (interfaces de casos de uso) con firmas que expresan intención de negocio y autorización explícita por parámetro, siguiendo ADR-48. Cada método recibe `requesterId` y `requesterRolId` directamente, sin depender de contexto estático:

```java
public interface ShiftUseCase {

    ReadShiftDto findById(
            ShiftId shiftId,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<PageShiftDto> findAll(
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    Page<PageShiftDto> findByDentist(
            DentistId dentistId,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadShiftDto assignShift(
            AssignShiftDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadShiftDto reschedule(
            ShiftId shiftId,
            RescheduleShiftDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadShiftDto cancel(
            ShiftId shiftId,
            String reason,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadShiftDto complete(
            ShiftId shiftId,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    boolean canAccommodateAppointment(
            ShiftId shiftId,
            CanAccommodateAppointmentDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );

    ReadShiftDto excludeBlock(
            ShiftId id,
            ExcludedBlockDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId
    );
}
```

Cada operación tiene un nombre que describe lo que hace en términos del negocio. No hay un `update()` genérico: hay `reschedule()`, `cancel()`, `complete()`, `excludeBlock()`. Esto es lo opuesto a `TurnoService`.

---

### 4. Sistema de enums y validación de enums (20 archivos)

```
Enums (6):
❌ Especialidades.java, Estado.java, Permisos.java
❌ Roles.java, Sector.java, TipoResponsable.java

Anotaciones de validación (7):
❌ AfeccionValido.java, EspecialidadesValido.java, EstadoValido.java
❌ RolValido.java, SectorValido.java, TipoResponsableValido.java
❌ TipoSangreValido.java

Validators de anotación (7):
❌ AfeccionValidator.java, EspecialidadesValidator.java, EstadoValidator.java
❌ RolValidator.java, SectorValidator.java, TipoResponsableValidator.java
❌ TipoSangreValidator.java
```

**Por qué:** El sistema usaba anotaciones `@Constraint` personalizadas para validar valores de enum en DTOs (patrón de tutorial). Esto generaba 20 archivos para cubrir lo que ahora maneja la lógica interna de los Value Objects. `TipoSangreValido` sugiere que hasta los datos clínicos pasaban por este mecanismo.

**Reemplazado por:** Value Objects que encapsulan el enum y su validación en una sola clase. El VO falla rápido en construcción con una excepción de dominio tipada, sin necesidad de anotaciones externas. Un ejemplo del módulo de pagos:

```java
public final class Payer {

    public enum PayerType {
        PATIENT("Paciente"),
        EPS("EPS"),
        INSURANCE("Aseguradora"),
        COMPANY("Empresa"),
        OTHER("Otro");

        private final String displayName;

        PayerType(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    private final PayerType type;
    private final String identifier; // NIT, documento, código
    private final String name;

    private Payer(PayerType type, String identifier, String name) {
        if (type == null) throw new ValueObjectValidationException(
            PaymentVoError.ERR_PAYMENT_PAYER_TYPE_NULL, VOContext.PAYMENT);
        if (name == null || name.isBlank()) throw new ValueObjectValidationException(
            PaymentVoError.ERR_PAYMENT_PAYER_NAME_NULL, VOContext.PAYMENT);

        this.type = type;
        this.identifier = identifier != null ? identifier.trim() : null;
        this.name = name.trim();
    }

    // Fábricas semánticas — el nombre del método reemplaza al enum explícito
    public static Payer patient(String patientName) {
        return new Payer(PayerType.PATIENT, null, patientName);
    }
    public static Payer eps(String epsName, String nit) {
        return new Payer(PayerType.EPS, nit, epsName);
    }
    public static Payer insurance(String insuranceName, String nit) {
        return new Payer(PayerType.INSURANCE, nit, insuranceName);
    }

    // Consultas semánticas
    public boolean isPatient()      { return type == PayerType.PATIENT; }
    public boolean isInstitutional(){ return !isPatient(); }

    // equals, hashCode, toString...
}
```

Un VO como `Payer` reemplaza a `@AfeccionValido` + `AfeccionValidator` colapsando validación, tipo y comportamiento en una sola clase cohesiva. El patrón escala: agregar un nuevo tipo de pagador es añadir un `PayerType` y una fábrica, no crear dos archivos nuevos.

---

### 5. Legacy Value Objects del paquete `/vo/` (9 archivos)

```
❌ Direccion.java
❌ Dni.java
❌ Edad.java
❌ Email.java
❌ EstadoCita.java
❌ EstadoHorario.java
❌ EstadoTurno.java
❌ NombreCompleto.java
❌ Telefono.java
```

**Por qué:** Existían VOs en el diseño original, pero estaban acoplados al modelo de entidades JPA y usaban las anotaciones de validación de enums del punto anterior. No tenían la riqueza semántica ni el aislamiento necesarios para el dominio hexagonal.

**Reemplazado por:** VOs rediseñados dentro de cada bounded context, con validación propia y sin dependencias de infraestructura.

---

### 6. Mappers (16 archivos en dos ubicaciones)

```
Paquete raíz /mapper/ (10):
❌ HorarioMapperResponse.java
❌ MapperAuxiliarOdontologo.java, MapperAuxiliarPaciente.java
❌ OdontologoMapperResponse.java, PacienteMapperResponse.java
❌ ResponsableMapperResponse.java, RolesMapper.java
❌ SecretarioMapperResponse.java, TurnoMapperResponse.java
❌ UsuarioMapperResponse.java

Paquete application/mapper/ —origen hexagonal— (6):
❌ AddressMapper.java, AppointmentMapper.java, EmailMapper.java
❌ InvoiceMapper.java, PhoneNumberMapper.java, ProvidedServiceMapper.java
```

**Por qué:** Los 10 del paquete raíz (`/mapper/`) convertían entidades JPA a DTOs sin expresar ninguna transformación de dominio: eran traducción mecánica entre capas que no deberían haberse acoplado directamente.

Los 6 de `application/mapper/` tienen una historia diferente. Nacieron dentro de la estructura hexagonal con una idea que en su momento parecía razonable: dado que varios agregados comparten VOs comunes (`Address`, `Email`, `PhoneNumber`), crear DTOs y mappers dedicados por VO para reutilizarlos entre bounded contexts. El problema es que la cantidad de VOs en el proyecto es grande, y sostener ese diseño significaba generar una clase de mapper y un DTO por cada VO, incluso para VOs que se usan en una sola operación. La relación costo-beneficio no cerraba.

La decisión fue simplificar: DTOs por operación, no por VO. Si una operación de creación de paciente necesita una dirección, esos campos van directamente en `CreatePatientDto`, sin pasar por un `AddressDto` intermediario con su `AddressMapper`. Eso volvió innecesarios a `AddressMapper`, `EmailMapper`, `PhoneNumberMapper` y los demás.

---

### 7. REST Controllers (7 archivos)

```
❌ HorarioController.java
❌ OdontologoController.java
❌ PacienteController.java
❌ ResponsableController.java
❌ SecretarioController.java
❌ TurnoController.java
❌ UsuarioController.java
```

**Por qué:** Controladores con `@PreAuthorize` hardcodeado, acoplados directamente a los services legacy.

---

### 8. Sistema de seguridad legacy (2 archivos)

```
❌ CustomUserDetailsService.java
❌ SecurityConfig.java
```

**Por qué:** Mezclaban autenticación con autorización. `SecurityConfig` contenía la lógica de acceso por roles directamente en la configuración de Spring, sin contexto de negocio.

**Reemplazado por:** `AuthenticationService`, `AuthorizationService` y el sistema de políticas (`OwnershipPolicy`, `SectorBasedPolicy`, etc.).

---

### 9. Configuración y utilidades (4 archivos)

```
❌ EdadMinimaConfig.java   — configuración de reglas de negocio en una @Configuration
❌ RolesFactory.java       — factory estática con permisos hardcodeados
❌ Paginacion.java         — helper de paginación sin contexto
❌ ValidarEdades.java      — validación de edad fuera del dominio
```

**Por qué:** En las etapas tempranas del proyecto no había claridad sobre qué capa debía ser responsable de cada decisión. El resultado fue que lógica de dominio terminó dispersa en clases utilitarias y beans de configuración.

`EdadMinimaConfig` es el caso más evidente: una regla de negocio —la edad mínima de un paciente— estaba externalizada en un `@Configuration` de Spring. `ValidarEdades` era su par en utilidades: la lógica de validación de edad implementada como método estático auxiliar. Ambas responsabilidades pertenecen al dominio, específicamente a un Value Object como `Edad` o a una política de registro de pacientes.

`RolesFactory` concentraba la asignación de permisos a roles como una factory estática hardcodeada. Si los permisos cambiaban, había que modificar código de infraestructura. Esa lógica migró al módulo de administración como parte del sistema de autorización.

`Paginacion` era un helper de paginación acoplado al modelo de entidades JPA. La paginación en la arquitectura actual se maneja en los puertos de salida y los adaptadores de repositorio, sin necesidad de una clase utilitaria transversal.

---

### 10. Artefactos de transición (2 archivos)

```
❌ DesactivarActorService.java   (en application/service/)
❌ ActorRepository.java          (en domain/actor/output/)
```

**Por qué:** Estos dos archivos merecen mención aparte porque vivían dentro de la estructura hexagonal (`application/` y `domain/`), pero representaban una abstracción que fue descartada: la noción de un `Actor` genérico que unificaba dentistas, secretarios y otros roles. Esta abstracción fue reemplazada por agregados separados por bounded context. Su eliminación en este commit marca el cierre definitivo de ese diseño intermedio.

---

### 11. Tests del código legacy (15 archivos)

```
TestDataFactory.java

Builders (6):
HorarioBuilder, IBuilder, OdontologoBuilder, PatientBuild, TurnoBuilder, UsuarioBuilder

Repository tests (7):
TestDisponibilidadRepository, TestOdontologoRespository, TestPacienteRepository,
TestResponsableRepository, TestSecretarioRepository, TestTurnoRepository, TestUsuarioRepository

Service tests (1):
UsuarioImplTest.java
```

**Por qué:** Tests acoplados a las entidades JPA y services eliminados. La presencia de solo `UsuarioImplTest` en services (con 7 implementations existentes) indica que la cobertura era mínima en el código legacy.

---

## Métricas del commit

| Categoría | Archivos |
|-----------|----------|
| Entidades JPA legacy | 18 |
| DTOs de persistencia | 18 |
| Enums + sistema de validación de enums | 20 |
| Legacy Value Objects (/vo/) | 9 |
| Mappers (raíz + application/mapper/) | 16 |
| Service interfaces + implementaciones | 14 |
| REST Controllers | 7 |
| Tests (builders + repository + service) | 15 |
| Config y utilidades | 4 |
| Seguridad legacy | 2 |
| Artefactos de transición | 2 |
| **TOTAL** | **133** |

> Los conteos de líneas por categoría no son verificables desde el log de git y fueron omitidos intencionalmente para mantener este documento como registro fiel del commit.

---

## Lo que el log revela sobre la evolución del proyecto

### El sistema de validación de enums fue una solución sobredimensionada

20 archivos para validar si un string corresponde a un valor de enum válido en los DTOs. El patrón `@RolValido`, `@SectorValido`, `@AfeccionValido` es un antipatrón frecuente en tutoriales que escala muy mal. Cada nuevo enum requería crear una anotación y un validator. El dominio hexagonal resuelve esto con VOs: si `Rol.of("INVALIDO")` lanza excepción, no se necesita nada más.

### Hubo una arquitectura hexagonal intermedia que también fue descartada

Los 6 archivos en `application/mapper/` (`AddressMapper`, `AppointmentMapper`, `EmailMapper`, etc.) y los 2 artefactos de transición (`ActorRepository`, `DesactivarActorService`) demuestran que existió al menos una versión intermedia de la arquitectura hexagonal antes de llegar al diseño actual. No fue una migración lineal de "legacy a hexagonal": hubo iteraciones dentro del nuevo diseño también.

### La cobertura de tests del legacy era casi nula

De 7 services con implementación, solo `UsuarioImplTest` tenía test. Los demás tenían únicamente repository tests de integración. Esto explica por qué muchos bugs del código legacy no eran visibles: no había nada que los detectara.

### El typo `PacienteServise` vivió durante meses

Este error ortográfico en el nombre de la interfaz sobrevivió sin ser corregido. En el código hexagonal nuevo, los casos de uso tienen nombres que expresan intención de negocio, lo que hace que errores como este sean más visibles y menos tolerables.

---

## Lecciones aprendidas

### La migración gradual fue clave

No se podría haber eliminado todo en un solo paso. La estrategia de convivencia definida en ADR-01 permitió mantener el sistema funcional durante meses, aprender arquitectura hexagonal mientras se construía en paralelo, y validar cada módulo antes de eliminar el código que reemplazaba.

### La arquitectura hexagonal también se iteró

El hecho de que `application/mapper/` y `domain/actor/` fueran eliminados en este mismo commit —siendo código ya hexagonal— es evidencia de que el destino no estaba claro desde el inicio. El diseño final emergió a través de la práctica, no fue planeado en un ADR inicial y ejecutado sin desviaciones.

### Documentar decisiones expuso qué mejorar

Cada ADR obligó a articular por qué el código anterior era insuficiente. Esto convirtió la refactorización en un proceso consciente: no se eliminaba código porque "se veía mal", sino porque había una decisión documentada que lo justificaba.

### El código eliminado no fue desperdiciado

Esas clases fueron el proyecto con el que se aprendió Spring Boot y la base para identificar qué cambios eran necesarios. `PermissionEntityxd.java` y `PacienteServise.java` no son vergüenzas: son evidencia de un proceso de aprendizaje real.

---


## Conclusión

133 archivos eliminados en un solo commit, respaldado por ADR-01 y meses de construcción paralela.

Lo que este commit demuestra:

- Capacidad de identificar limitaciones en diseños propios, incluyendo iteraciones del diseño nuevo
- Habilidad para migrar arquitecturas sin romper funcionalidad existente
- Disciplina para documentar decisiones técnicas antes de ejecutarlas
- Crecimiento desde patrones de tutoriales hacia soluciones con criterio propio

El código que se eliminó —incluyendo `PermissionEntityxd`, `PacienteServise`, y las 20 clases de validación de enums— enseñó tanto como el código que lo reemplazó.

---

## Referencias

- [ADR-01: Migración progresiva a arquitectura hexagonal](docs/adr/ADR-01-Migración-progresiva-a-arquitectura-hexagonal.md)
- [ADR-02: Sistema de autorización inicial basado en tutoriales de YouTube](docs/adr/ADR-02-Sistema-autorizacion-inicial-basado-YouTube.md)
- [ADR-14: Separación identidad del usuario con roles y permisos](docs/adr/ADR-14-Separación-identidad-del-usuario-con-roles-y-permisos.md)
- [STORY.md: Historia del proyecto](STORY.md)