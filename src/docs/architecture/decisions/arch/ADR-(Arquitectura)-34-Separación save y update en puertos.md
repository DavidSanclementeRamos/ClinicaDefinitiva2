# ADR-34 (Arquitectura): Separación save/update en puertos

- **Estado:** Aprobado
- **Fecha**: 2026-01-26
- **Autor:** David Stiven Sanclemente


## Problema

Los repositorios exponen solo métodos genéricos (`save`, `delete`, `find`), mientras que los UseCases definen métodos específicos (`updateContactData`, `updateSensitiveData`).

No está claro por qué no tener también `updateContact` o `updateSensitive` en el repositorio.

## Decisión

**Regla:**
- **Puertos de salida (repositorios)**: solo operaciones genéricas de persistencia
    - `save`, `delete`, `find`
    - El método `save` cubre inserciones Y actualizaciones
- **Puertos de entrada (UseCases)**: operaciones de negocio con nombres semánticos
    - `updateContactData`, `updateSensitiveData`, `updateAvailability`
    - Aplican lógica de negocio, luego llaman a `repository.save()`

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Repositorio con métodos específicos de actualización | Acopla infraestructura a reglas de negocio. ¿Qué es "datos sensibles" es decisión del dominio, no de persistencia |
| UseCase con solo `update` genérico | Pierde semántica. No queda claro QUÉ se está actualizando |
| Repositorio con `insert` y `update` separados | JPA/Hibernate maneja esto automáticamente con `save` |

## Consecuencias

**Ganamos:**
- Infraestructura desacoplada de reglas de negocio
- Puertos de salida simples y reutilizables
- UseCases expresivos y trazables

**Perdemos:**
- Cada UseCase debe invocar `save` explícitamente
- Más métodos en la interfaz del UseCase

## Implementación

```java
// Puerto de entrada (UseCase)
public interface DentistUseCase {
    DentistDto updateContactData(UpdateContactDto dto, DentistId id);
    DentistDto updateSensitiveData(UpdateSensitiveDto dto, DentistId id);
    DentistDto updateAvailability(UpdateAvailabilityDto dto, DentistId id);
}

// Puerto de salida (Repositorio)
public interface DentistRepository {
    Optional<Dentist> findById(DentistId id);
    Dentist save(Dentist dentist); // cubre insert Y update
    void deleteById(DentistId id);
}

// Application Service
@Service
public class DentistApplicationService implements DentistUseCase {
    private final DentistRepository repository;
    
    @Override
    public DentistDto updateContactData(UpdateContactDto dto, DentistId id) {
        Dentist dentist = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Dentist not found"));
        
        // Lógica de negocio en el agregado
        dentist.updateContactData(
            new Email(dto.email()),
            new PhoneNumber(dto.phone())
        );
        
        // Persistir cambios
        Dentist updated = repository.save(dentist);
        return mapper.toDto(updated);
    }
}
```

## Detección de insert vs update

La infraestructura (JPA/Hibernate) detecta automáticamente si debe hacer INSERT o UPDATE:
- Si la entidad tiene ID null o no existe en BD → INSERT
- Si la entidad tiene ID existente en BD → UPDATE

El repositorio no necesita saber la diferencia.

## Casos especiales

**¿Y si necesito saber si fue insert o update?**

El Application Service puede verificar antes:

```java
@Override
public DentistDto create(CreateDentistDto dto) {
    // Validar que NO exista
    if (repository.existsById(dto.licenseNumber())) {
        throw new BusinessRuleViolationException("License already exists");
    }
    
    Dentist dentist = Dentist.create(/* ... */);
    return mapper.toDto(repository.save(dentist));
}
```

La responsabilidad de decidir si es creación o actualización vive en la capa de aplicación, no en infraestructura.