# Guía: Estrategia de Mappers por Operación

**Última actualización:** 2026-01-30  
**Tipo:** Guía de implementación

---

## Regla fundamental

**Un mapper/assembler específico por cada operación**

```
Entrada (Write DTO) → Assembler → Dominio (VOs)
Dominio (Agregados) → Mapper → Salida (Read DTO)
```

---

## Cuándo usar cada tipo

### Mapper de lectura (Domain → DTO)
**Función:** Convertir agregados y VOs en DTOs serializables

**Cuándo:** Al exponer datos hacia afuera (REST, UI, reportes)

```java
@Component
public class DentistReadMapper {
    public ReadDentistDto toDto(Dentist dentist) {
        return new ReadDentistDto(
            dentist.getId().value(),
            dentist.getName().fullName(),
            dentist.getEmail().value(),
            dentist.getLicenseNumber().value()
        );
    }
    
    // Para listados paginados: DTO simplificado
    public DentistPageDto toPageDto(Dentist dentist) {
        return new DentistPageDto(
            dentist.getId().value(),
            dentist.getName().fullName(),
            dentist.getSpecialization().name()
        );
    }
}
```

---

### Assembler de escritura (DTO → Domain)
**Función:** Convertir DTOs de entrada en VOs y agregados

**Cuándo:** Operaciones de creación o actualización

```java
@Component
public class DentistCreateAssembler {
    public Dentist toDomain(CreateDentistDto dto) {
        return Dentist.builder()
            .withId(DentistId.generate())
            .withPersonalInfo(new PersonalInfo(
                new Name(dto.firstName(), dto.lastName()),
                new BirthDate(dto.birthDate())
            ))
            .withContactInfo(new ContactInfo(
                new Email(dto.email()),
                new PhoneNumber(dto.phone())
            ))
            .withLicenseNumber(new LicenseNumber(dto.license()))
            .build();
    }
}
```

---

## Estrategia por operación

| Operación | DTO | Mapper/Assembler | Método del agregado |
|-----------|-----|------------------|---------------------|
| **Crear** | `CreateDentistDto` | `DentistCreateAssembler.toDomain()` | Constructor o `Dentist.create()` |
| **Actualizar contacto** | `UpdateContactDto` | `DentistUpdateContactAssembler.toVOs()` | `dentist.updateContactData()` |
| **Actualizar datos sensibles** | `UpdateSensitiveDto` | `DentistUpdateSensitiveAssembler.toVOs()` | `dentist.updateSensitiveData()` |
| **Leer detalle** | - | `DentistReadMapper.toDto()` | - |
| **Listar paginado** | - | `DentistReadMapper.toPageDto()` | - |

---

## Estructura de archivos

```
dentist/
├── application/
│   ├── dto/
│   │   ├── CreateDentistDto.java
│   │   ├── UpdateContactDto.java
│   │   ├── UpdateSensitiveDto.java
│   │   ├── ReadDentistDto.java
│   │   └── DentistPageDto.java
│   ├── mapper/
│   │   ├── DentistReadMapper.java          # Domain → Read DTO
│   │   └── DentistCreateAssembler.java     # Write DTO → Domain
│   └── assembler/
│       ├── DentistUpdateContactAssembler.java
│       └── DentistUpdateSensitiveAssembler.java
```

---

## Mappers estáticos vs inyectados

### Usar métodos estáticos cuando:
- Conversión trivial 1:1 (primitivo ↔ VO simple)
- Sin dependencias externas
- Sin composición de otros mappers

```java
public class NameMapper {
    public static String toValue(Name name) {
        return name.fullName();
    }
    
    public static Name toDomain(String firstName, String lastName) {
        return new Name(firstName, lastName);
    }
}
```

### Usar mapper inyectado cuando:
- Composición de múltiples VOs
- Dependencias de otros mappers
- Lógica adicional (validaciones, transformaciones)

```java
@Component
public class ActorMapper {
    private final ContactInfoMapper contactInfoMapper;
    private final AddressMapper addressMapper;
    
    public ActorMapper(
        ContactInfoMapper contactInfoMapper,
        AddressMapper addressMapper
    ) {
        this.contactInfoMapper = contactInfoMapper;
        this.addressMapper = addressMapper;
    }
    
    public Actor toDomain(ActorDto dto) {
        return new Actor(
            contactInfoMapper.toDomain(dto.contactInfo()),
            addressMapper.toDomain(dto.address())
        );
    }
}
```

---

## Patrones comunes

### Patrón 1: Crear agregado
```java
// 1. DTO de entrada
record CreatePatientDto(String firstName, String lastName, ...) {}

// 2. Assembler convierte a VOs
@Component
public class PatientCreateAssembler {
    public Patient toDomain(CreatePatientDto dto) {
        return Patient.builder()
            .withName(new Name(dto.firstName(), dto.lastName()))
            // ...
            .build();
    }
}

// 3. Application Service orquesta
public PatientDto create(CreatePatientDto dto) {
    Patient patient = createAssembler.toDomain(dto);
    Patient saved = repository.save(patient);
    return readMapper.toDto(saved);
}
```

### Patrón 2: Actualizar parcialmente
```java
// 1. DTO solo con campos a actualizar
record UpdateContactDto(String email, String phone) {}

// 2. Assembler convierte a VOs (NO a agregado completo)
@Component
public class PatientUpdateContactAssembler {
    public record ContactVOs(Email email, PhoneNumber phone) {}
    
    public ContactVOs toVOs(UpdateContactDto dto) {
        return new ContactVOs(
            new Email(dto.email()),
            new PhoneNumber(dto.phone())
        );
    }
}

// 3. Application Service obtiene agregado y lo modifica
public PatientDto updateContact(UpdateContactDto dto, PatientId id) {
    Patient patient = repository.findById(id).orElseThrow();
    
    var vos = updateContactAssembler.toVOs(dto);
    patient.updateContactData(vos.email(), vos.phone());
    
    return readMapper.toDto(repository.save(patient));
}
```

---

## Evitar anti-patrones

### ❌ NO: Mapper genérico para todo
```java
// MAL: un mapper para crear, actualizar y leer
public class PatientMapper {
    public Patient toDomain(PatientDto dto) { ... }  // ¿crear o actualizar?
    public PatientDto toDto(Patient patient) { ... }
}
```

### ✅ SÍ: Mappers específicos
```java
// BIEN: intención clara
PatientCreateAssembler    // DTO → Domain (crear)
PatientUpdateContactAssembler  // DTO → VOs (actualizar contacto)
PatientReadMapper         // Domain → DTO (leer)
```

---

### ❌ NO: Construir objetos artificiales
```java
// MAL: PersonData con nulls artificiales
PersonData data = new PersonData(
    null,  // no se actualiza
    dto.email(),
    dto.phone(),
    null   // no se actualiza
);
dentist.update(data);  // ¿qué se actualiza?
```

### ✅ SÍ: Parámetros explícitos
```java
// BIEN: claro qué se actualiza
Email email = new Email(dto.email());
PhoneNumber phone = new PhoneNumber(dto.phone());
dentist.updateContactData(email, phone);
```

---

## Resumen visual

```
┌─────────────────────────────────────────────────────────────┐
│                      FLUJO DE DATOS                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ENTRADA (Crear/Actualizar)                                │
│  ───────────────────────────                               │
│                                                             │
│  CreateDto  ──→  Assembler  ──→  VOs  ──→  Agregado       │
│  UpdateDto  ──→  Assembler  ──→  VOs  ──→  método()       │
│                                                             │
│  SALIDA (Leer)                                             │
│  ──────────────                                            │
│                                                             │
│  Agregado  ──→  ReadMapper  ──→  ReadDto                  │
│  Agregado  ──→  ReadMapper  ──→  PageDto (simplificado)   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Checklist de implementación

Al crear una nueva operación:

- [ ] Crear DTO específico en `/application/dto/`
- [ ] Decidir si necesita Mapper (lectura) o Assembler (escritura)
- [ ] Implementar mapper/assembler en `/application/mapper/` o `/assembler/`
- [ ] Decidir si es estático o inyectado según complejidad
- [ ] Usar el mapper en Application Service
- [ ] NO reutilizar DTOs entre operaciones diferentes
- [ ] NO construir objetos con nulls artificiales