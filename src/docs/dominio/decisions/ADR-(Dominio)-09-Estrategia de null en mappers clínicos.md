# ADR-09 (Dominio): Estrategia de null en mappers clínicos

- **Fecha**: 2026-01-30
- **Estado**: Aprobado
- **Categoría**: Dominio

## Problema

Al mapear entre dominio y entidades JPA, surgen valores null por:
1. **Campos opcionales legítimos** (ej. segundo apellido, teléfono alternativo)
2. **Violaciones de invariantes** (ej. FirstName obligatorio que llega null)

No está claro si los mappers deben:
- Validar null defensivamente en cada campo
- Asumir que el dominio garantiza invariantes

## Decisión

Adoptar **mapper estricto** con regla diferenciada por tipo de campo:

### Regla para campos obligatorios
**No validar null** → si llega null, que falle ruidosamente

```java
// ❌ NO hacer
public FullNameEntity toEntity(FullName fullName) {
    if (fullName == null) return null;
    if (fullName.firstName() == null) return new FullNameEntity("UNKNOWN", ...);
    // ...
}

// ✅ SÍ hacer
public FullNameEntity toEntity(FullName fullName) {
    return new FullNameEntity(
        fullName.firstName().value(),  // NPE si es null → BIEN
        fullName.lastName().value()
    );
}
```

**Justificación:** Un FullName con firstName null ES una violación clínica. Debe registrarse como error, no ocultarse.

### Regla para campos opcionales
**Usar Optional<T>** explícitamente

```java
public record ContactInfo(
    Email email,              // obligatorio
    Optional<PhoneNumber> alternativePhone  // opcional
) {}

public ContactInfoEntity toEntity(ContactInfo contactInfo) {
    return new ContactInfoEntity(
        contactInfo.email().value(),
        contactInfo.alternativePhone()
            .map(PhoneNumber::value)
            .orElse(null)  // OK, es opcional
    );
}
```

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Mapper defensivo (valida null siempre) | Oculta errores clínicos. Si un campo obligatorio es null, es un bug que debe detectarse |
| Usar valores por defecto ("UNKNOWN") | Contamina la BD con datos falsos. Dificulta auditoría |
| Lanzar excepción custom en mapper | El mapper no debe conocer reglas de negocio. Si hay null, que sea NPE claro |

## Consecuencias

**Ganamos:**
- Detección temprana de violaciones de invariantes
- Código de mapper más limpio (sin validaciones redundantes)
- Trazabilidad: errores por null inesperado se registran claramente

**Perdemos:**
- Menor tolerancia a datos incompletos (requiere dominio bien validado)
- NPE puede ser críptico si no se documenta el origen

## Estrategia de migración para datos legacy

Si existe data histórica con nulls en campos obligatorios:

```java
// En el repositorio de infraestructura
public Optional<Patient> findById(PatientId id) {
    return jpaRepo.findById(id.value())
        .filter(entity -> entity.getFirstName() != null)  // filtrar inválidos
        .map(entity -> {
            try {
                return mapper.toDomain(entity);
            } catch (NullPointerException e) {
                logger.error("Invalid legacy data for patient {}", id, e);
                return null;
            }
        });
}
```

Pero esto es **temporal**. La solución real es migrar los datos legacy.

## Invariante en Value Objects

Los Value Objects deben garantizar que campos obligatorios nunca sean null:

```java
public record FirstName(String value) {
    public FirstName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("FirstName cannot be null or blank");
        }
    }
}
```

Si el mapper recibe un FirstName, puede asumir que `value` no es null.

## Casos especiales

**¿Qué pasa con relaciones?**

```java
public record Patient(
    PatientId id,
    FullName name,
    Optional<GuardianId> guardianId  // puede no tener guardián
) {}
```

Si `guardianId` es `Optional.empty()`, el mapper persiste null en la FK:

```java
public PatientEntity toEntity(Patient patient) {
    var entity = new PatientEntity();
    entity.setGuardianId(
        patient.guardianId()
            .map(GuardianId::value)
            .orElse(null)  // FK nullable
    );
    return entity;
}
```

Esto es correcto: la FK puede ser null si la relación es opcional.