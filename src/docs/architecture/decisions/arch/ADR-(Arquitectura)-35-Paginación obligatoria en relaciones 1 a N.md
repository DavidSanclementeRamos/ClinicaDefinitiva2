# ADR-35 (Arquitectura): Paginación obligatoria en relaciones 1:N

- **Estado:** Aprobado
- **Fecha**: 2026-01-26
- **Categoría**: Arquitectura
- **Autor:** David Stiven Sanclemente


## Problema

Algunos métodos de UseCase devolvían un único objeto aunque la relación en el dominio era 1:N:
- `findByContractId()` devolvía un solo paciente (un contrato puede tener varios)
- `findByGuardianId()` devolvía un solo paciente (un guardián puede tener varios)

Esto causaba:
- Inconsistencias semánticas
- Incapacidad de devolver múltiples resultados
- No optimizado para grandes volúmenes

## Decisión

**Regla:** Métodos que representan relaciones 1:N SIEMPRE devuelven `Page<DTO>`.

**Excepción:** Si la cardinalidad máxima es conocida y ≤10, puede devolverse `List<DTO>`.

Aplicar en todas las capas:
- UseCase → `Page<DTO>`
- Adaptador → mapea `Page<Entity>` a `Page<DTO>`
- Puerto de salida → `Page<Entity>`
- Repositorio → Spring Data `Page<Entity>`
- Controlador REST → `PageResponse<T>` con metadatos

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| Devolver `List<DTO>` sin paginación | No escala. Con 1000+ registros, la respuesta HTTP es enorme |
| Paginación solo en infraestructura | La paginación es un requerimiento funcional, debe estar en el contrato del UseCase |
| Devolver el primer resultado | Semánticamente incorrecto. Oculta información |

## Consecuencias

**Ganamos:**
- Consistencia: todas las relaciones 1:N se manejan igual
- Escalabilidad: soporta miles de registros sin problemas
- Metadatos útiles: total de páginas, tamaño, etc.

**Perdemos:**
- Cambios en cascada: refactorizar controlador, adaptador, puerto, repositorio
- Mayor complejidad para casos triviales (ej. 2-3 registros)

## Implementación

```java
// 1. Repositorio (Spring Data)
public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {
    Page<PatientEntity> findByGuardianId(String guardianId, Pageable pageable);
}

// 2. Puerto de salida
public interface PatientRepository {
    Page<Patient> findByGuardianId(GuardianId guardianId, Pageable pageable);
}

// 3. Adaptador
@Component
public class PatientPersistenceAdapter implements PatientRepository {
    private final PatientJpaRepository jpaRepo;
    private final PatientMapper mapper;
    
    @Override
    public Page<Patient> findByGuardianId(GuardianId guardianId, Pageable pageable) {
        return jpaRepo.findByGuardianId(guardianId.value(), pageable)
            .map(mapper::toDomain);
    }
}

// 4. UseCase
public interface PatientUseCase {
    Page<PatientPageDto> findByGuardianId(GuardianId guardianId, Pageable pageable);
}

// 5. Application Service
@Service
public class PatientApplicationService implements PatientUseCase {
    private final PatientRepository repository;
    
    @Override
    public Page<PatientPageDto> findByGuardianId(GuardianId guardianId, Pageable pageable) {
        return repository.findByGuardianId(guardianId, pageable)
            .map(mapper::toPageDto);
    }
}

// 6. Controlador REST
@RestController
public class PatientController {
    private final PatientUseCase useCase;
    
    @GetMapping("/patients/guardian/{guardianId}")
    public PageResponse<PatientPageResponse> findByGuardianId(
        @PathVariable String guardianId,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<PatientPageDto> page = useCase.findByGuardianId(
            new GuardianId(guardianId), 
            pageable
        );
        
        return PageResponse.of(page, mapper::toResponse);
    }
}
```

## Mapper simplificado para paginación

Crear DTO específico para listados (menos campos que el DTO completo):

```java
// DTO completo (para detalle)
public record PatientDto(
    Long id,
    String firstName,
    String lastName,
    LocalDate birthDate,
    ContactInfo contactInfo,
    MedicalHistory medicalHistory
) {}

// DTO simplificado (para listados paginados)
public record PatientPageDto(
    Long id,
    String firstName,
    String lastName,
    String guardianId
) {}
```