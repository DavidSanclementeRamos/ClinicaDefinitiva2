# ADR-012: Excepciones de negocio en capa de aplicación

- **Fecha**: 2026-01-26
- **Estado**: Aprobado
- **Categoría**: Arquitectura

## Problema

En arquitecturas tradicionales, los repositorios lanzan `EntityNotFoundException` directamente.

En arquitectura hexagonal no está claro:
- ¿Dónde se lanzan las excepciones de negocio?
- ¿Qué devuelven los repositorios?
- ¿Quién traduce excepciones a HTTP?

## Decisión

**Regla de responsabilidades:**

| Capa | Responsabilidad | Qué devuelve/lanza |
|------|-----------------|-------------------|
| **Infraestructura (repositorio)** | Solo persiste/consulta datos | `Optional<Entity>`, `Page<Entity>` |
| **Puerto de salida (dominio)** | Define contrato genérico | `Optional<Entity>`, `Page<Entity>` |
| **Application Service** | Interpreta resultado, aplica reglas | Lanza `NotFoundException` si `Optional.empty()` |
| **Controlador REST** | Traduce excepciones a HTTP | Captura con `@ExceptionHandler`, devuelve `ResponseEntity` |

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Repositorio lanza excepciones | Acopla infraestructura a reglas de negocio. ¿Qué es un "error" depende del caso de uso |
| Puerto de salida lanza excepciones | El contrato del dominio no debe definir comportamiento de infraestructura |
| Controlador lanza excepciones | Lógica de negocio ("este ID debe existir") no pertenece a capa de presentación |

## Consecuencias

**Ganamos:**
- Separación clara: infraestructura → datos, aplicación → reglas
- Infraestructura desacoplada de reglas de negocio
- Misma infraestructura puede servir casos de uso con diferentes reglas

**Perdemos:**
- Más código: cada Application Service debe validar `Optional.empty()`
- No se puede "asumir que existe" sin validación explícita

## Implementación

```java
// 1. Repositorio (infraestructura)
public interface JpaPatientRepository extends JpaRepository<PatientEntity, Long> {
    // Spring Data devuelve Optional automáticamente
}

// 2. Puerto de salida (dominio)
public interface PatientRepository {
    Optional<Patient> findById(PatientId id);
}

// 3. Adaptador (aplicación → infraestructura)
@Component
public class PatientPersistenceAdapter implements PatientRepository {
    private final JpaPatientRepository jpaRepo;
    private final PatientMapper mapper;
    
    @Override
    public Optional<Patient> findById(PatientId id) {
        return jpaRepo.findById(id.value())
            .map(mapper::toDomain);
    }
}

// 4. Application Service
@Service
public class PatientApplicationService {
    private final PatientRepository repository;
    
    public PatientDto findById(PatientId id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new NotFoundException(
                "Patient not found: " + id.value()
            ));
    }
}

// 5. Controlador REST
@RestController
public class PatientController {
    private final PatientApplicationService service;
    
    @GetMapping("/patients/{id}")
    public PatientDto getById(@PathVariable Long id) {
        return service.findById(new PatientId(id));
        // Si lanza NotFoundException, el @ExceptionHandler la captura
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }
}
```

## Casos especiales

**¿Y si el caso de uso PERMITE que no exista?**

```java
public Optional<PatientDto> findByIdIfExists(PatientId id) {
    return repository.findById(id)
        .map(mapper::toDto);
    // Devuelve Optional, no lanza excepción
}
```

El Application Service decide si la ausencia es error o no.