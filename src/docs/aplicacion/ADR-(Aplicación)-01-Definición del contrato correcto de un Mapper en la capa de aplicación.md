
# ADR-01 (Aplicación): Definición del contrato correcto de un Mapper en la capa de aplicación

- **Fecha**: 2026-02-25
- **Estado**: Aprobado
- **Categoría**: Aplicación

---

## Contexto

Los mappers surgieron en el proyecto como una solución para desacoplar la capa de presentación del dominio. Su propósito original era claro: transformar DTOs en objetos de dominio y viceversa. Sin embargo, durante la evolución del sistema, los mappers acumularon responsabilidades que no les correspondían. Esto no ocurrió por descuido, sino por una comprensión gradual del rol de cada capa, propia de un sistema que madura con el tiempo.

Este ADR documenta los errores concretos que se cometieron, el aprendizaje que dejaron, y el contrato correcto que deben seguir todos los mappers del proyecto a partir de hoy.

---

## Problema

Se identificaron tres desviaciones distintas en la forma en que se construyeron los mappers. Cada una representa un tipo de acoplamiento diferente:

---

### Error 1: Mappers con validaciones de null

En una etapa temprana del proyecto, los mappers incluían validaciones defensivas de null, bajo la premisa de que una capa extra de protección no hacía daño. Con el tiempo, esas validaciones se redujeron, pero no se eliminaron por completo. Aún existen patrones como estos:

```java
// Validación condicional de null dentro del mapper
.contractId(dto.contractId() != null ? ContractId.of(dto.contractId()) : null)

// Guard de null al inicio del método
public Rate fromCreateDto(CreateRateDto dto) {
    if (dto == null) return null;
    ...
}
```

El problema es que esta responsabilidad ya está cubierta en otras capas. Los Value Objects como `ContractId` validan null en su propia construcción. Los DTOs son validados antes de llegar al mapper. El agregado garantiza la consistencia de sus atributos al construirse. Cuando el mapper también valida, no añade protección real; añade ruido semántico y una falsa sensación de seguridad que oscurece dónde reside la verdadera invariante.

---

### Error 2: Mapper acoplado a un servicio de dominio

Este fue el error de mayor impacto arquitectónico. Para el agregado `Shift`, la creación requería información de otro agregado (el dentista), por lo que se introdujo un servicio de dominio (`ShiftAssignmentService`) que encapsulara esa coordinación. Hasta ahí, el diseño era correcto.

El problema fue que, en lugar de invocar ese servicio desde el Application Service, se invocó desde el mapper:

```java
public class ShiftWriteMapper {

    private final ShiftAssignmentService assig;

    public Shift fromAssignDto(AssignShiftDto dto) {
        return assig.assignShift(
                DentistId.of(dto.dentistId()),
                LocalDate.MAX,
                LocalTime.MIN,
                LocalTime.MIN,
                ShiftType.valueOf(dto.type())
        );
    }
}
```

Y en el Application Service, el mapper se usó como si fuera un simple transformador:

```java
Shift shift = writeMapper.fromAssignDto(dto);
```

Lo que parecía una línea de mapeo era en realidad una llamada a lógica de dominio oculta detrás de una abstracción incorrecta. El mapper dejó de ser un transformador y se convirtió en un orquestador encubierto. Esto rompió la separación de capas: quien lee el Application Service no puede saber, sin entrar al mapper, que ahí se está coordinando la creación de un agregado con lógica de dominio. La trazabilidad del flujo queda fragmentada.

---

### Error 3: Mapper que invoca métodos del agregado para actualización

Al trabajar con agregados que tienen constructores privados y exponen sus operaciones a través de métodos semánticos (`update`, `addAttachment`, `deactivate`), surgió la tentación de usar esos métodos también desde el mapper para simplificar el Application Service. El razonamiento fue: si el agregado expone métodos de creación y es inmutable, el mapper puede usarlos para mapear directamente.

Eso derivó en patrones como este:

```java
// Mapper que aplica lógica de negocio sobre el agregado
public void toUpdateDto(UpdateAdministrativeReportDto dto, AdministrativeReport report) {
    report.updateInformation(
            Name.of(dto.title()),
            dto.notes()
    );
}

// Mapper que añade un documento al agregado
public void toDocument(DocumentDto dto, AdministrativeReport report) {
    report.addAttachment(Document.of(dto.name(), dto.url(), dto.type(), dto.size()));
}
```

Estos métodos no transforman datos; modifican el estado del agregado. Eso es lógica de aplicación, no mapeo. El mapper está tomando decisiones que corresponden al Application Service: cuándo actualizar el agregado, con qué datos y bajo qué condiciones. Además, los métodos del agregado como `updateInformation` o `addAttachment` son puntos de entrada a la lógica de negocio, y deben ser invocados desde la capa que tiene el contexto del caso de uso, no desde una capa de infraestructura de transformación.

---

## Decisión

**Un mapper es un transformador puro de estructuras de datos. Su única responsabilidad es construir objetos de dominio a partir de DTOs, o construir DTOs a partir de objetos de dominio. No valida, no orquesta, no modifica estado.**

### Reglas que definen el contrato correcto

**Regla 1: El mapper no valida null**

Las validaciones de null pertenecen a los Value Objects y a los agregados. Si `ContractId.of(...)` lanza una excepción cuando recibe null, esa es la capa correcta para esa validación. El mapper confía en que los datos que recibe ya fueron validados por quien corresponde.

```java
// ❌ Mapper con validación
.contractId(dto.contractId() != null ? ContractId.of(dto.contractId()) : null)

// ✅ Mapper sin validación
.contractId(ContractId.of(dto.contractId()))
```

**Regla 2: El mapper no tiene dependencias de servicios de dominio**

Si la creación de un agregado requiere coordinación con otro agregado, esa lógica pertenece a un servicio de dominio. Ese servicio debe ser invocado desde el Application Service, no desde el mapper. El mapper recibe un DTO y devuelve los parámetros que el servicio de dominio necesita, pero no lo invoca.

```java
// ❌ Mapper que orquesta
public class ShiftWriteMapper {
    private final ShiftAssignmentService assig;

    public Shift fromAssignDto(AssignShiftDto dto) {
        return assig.assignShift(DentistId.of(dto.dentistId()), ...);
    }
}

// ✅ Application Service que orquesta, usando el servicio de dominio directamente
public ReadShiftDto assignShift(AssignShiftDto dto, ...) {
    authorization.authorize(...);

    Shift shift = shiftAssignmentService.assignShift(
            DentistId.of(dto.dentistId()),
            dto.date(),
            dto.startTime(),
            dto.endTime(),
            ShiftType.valueOf(dto.type())
    );

    repository.save(shift);
    return readMapper.toReadDto(shift);
}
```

**Regla 3: El mapper no invoca métodos del agregado**

Para la creación, el mapper puede devolver el agregado usando su factory method, ya que eso es una transformación directa de un DTO a un objeto de dominio recién creado:

```java
// ✅ Mapper de creación: transformación pura
public AdministrativeReport fromCreateDto(CreateAdministrativeReportDto dto) {
    return AdministrativeReport.create(
            Name.of(dto.title()),
            Period.of(dto.period().start(), dto.period().end()),
            UserIdentityId.from(dto.createdBy())
    );
}
```

Para la actualización o cualquier otra operación sobre un agregado existente, el mapper no debe tocar el agregado. El mapper construye los objetos de dominio necesarios (Value Objects, IDs) y el Application Service los pasa al método del agregado:

```java
// ❌ Mapper que modifica el agregado
public void toUpdateDto(UpdateAdministrativeReportDto dto, AdministrativeReport report) {
    report.updateInformation(Name.of(dto.title()), dto.notes());
}

// ✅ Mapper que transforma datos
public Name toName(UpdateAdministrativeReportDto dto) {
    return Name.of(dto.title());
}

// Application Service que decide cuándo aplicar la operación
report.updateInformation(
        writeMapper.toName(dto),
        dto.notes()
);
```

---

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|---|---|
| Mantener validaciones de null en el mapper como capa defensiva adicional | Genera ruido semántico y oscurece dónde reside la verdadera invariante. Los VOs y los agregados ya son responsables de esto |
| Permitir que el mapper invoque servicios de dominio para simplificar el Application Service | Oculta lógica de orquestación en una capa que no tiene ese rol. Fragmenta la trazabilidad del flujo de un caso de uso |
| Usar métodos del agregado desde el mapper para centralizar la lógica de transformación | Mezcla transformación de datos con modificación de estado. El Application Service pierde visibilidad sobre qué operaciones de dominio se ejecutan en un caso de uso |

---

## Consecuencias

### Lo que ganamos

- **Trazabilidad completa del caso de uso**: quien lee el Application Service puede entender el flujo completo sin entrar al mapper. Las decisiones de qué operaciones de dominio se invocan y en qué orden están en el lugar correcto.
- **Mappers simples y predecibles**: un mapper sin dependencias externas ni lógica de dominio es trivial de entender, testear y mantener. Su único contrato es la transformación de tipos.
- **Separación real de responsabilidades**: el Application Service orquesta. El mapper transforma. El agregado protege sus invariantes. El servicio de dominio coordina entre agregados. Cada capa hace exactamente lo que le corresponde.

### Lo que asumimos

- **Application Services más explícitos**: al devolver el mapper solo objetos de dominio, el Application Service tiene que escribir más código para invocar los métodos del agregado. Ese código adicional no es ceremonia innecesaria; es claridad sobre qué hace el caso de uso.
- **Refactorización de mappers existentes**: los mappers que actualmente tienen validaciones de null o que invocan métodos de agregados deben ser revisados y ajustados conforme a este contrato.

---

## Relación con otros ADR

| ADR | Relación |
|---|---|
| ADR-12 (Validaciones en Value Objects) | Las validaciones de null que se eliminan del mapper ya están cubiertas por los VOs, como establece ese ADR |
| ADR-07 (Estrategia de construcción de objetos) | Los factory methods que usan los mappers para la creación siguen la estrategia definida en ese ADR |
| ADR sobre eliminación de validación de `id` en constructores | Refuerza el mismo principio: cada capa valida lo que le corresponde, sin duplicar responsabilidades hacia abajo |