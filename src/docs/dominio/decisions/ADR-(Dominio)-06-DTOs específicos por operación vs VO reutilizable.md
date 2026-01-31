# ADR-06 (Dominio): DTOs específicos por operación vs VO reutilizable

- **Fecha**: 2026-01-30
- **Estado**: Aprobado
- **Categoría**: Dominio

## Problema

Se diseñó `PersonData` como VO global reutilizable que encapsula atributos comunes (edad, dirección, teléfono).

Al usarlo en operaciones de actualización surgieron problemas:
1. Los métodos esperan `PersonData` completo aunque solo se actualicen 2-3 campos
2. Los DTOs de aplicación contienen primitivos, obligando a construir `PersonData` artificial
3. La semántica se pierde: no es claro qué campos se modifican

**Ejemplo problemático:**
```java
// DTO de aplicación
record UpdateDentistContactDto(String email, String phone) {}

// Método en agregado
public void updateContact(PersonData personData) { ... }

// En el Application Service
PersonData artificial = new PersonData(
    /* edad */ null,           // no se actualiza
    /* dirección */ null,      // no se actualiza  
    /* email */ dto.email(),   // SÍ se actualiza
    /* phone */ dto.phone()    // SÍ se actualiza
);
dentist.updateContact(artificial);  // ¿Qué se actualiza? No es claro
```

## Decisión

**Priorizar legibilidad y semántica sobre reducción de duplicación.**

### Regla de diseño

1. **DTOs específicos por operación** en capa de aplicación
    - `UpdateDentistContactDto` solo contiene email y phone
    - `UpdateDentistSensitiveDto` solo contiene datos sensibles
    - No reutilizar DTOs entre operaciones diferentes

2. **Métodos con parámetros explícitos** en agregados
   ```java
   public void updateContactData(Email email, PhoneNumber phone) {
       this.email = email;
       this.phone = phone;
   }
   ```

3. **VO global como núcleo de validación** (uso interno)
    - `PersonData` se mantiene para encapsular validaciones comunes
    - No se expone en métodos públicos del agregado

## Alternativas descartadas

| Alternativa | Por qué se descartó |
|-------------|---------------------|
| VO global en todas las operaciones | Obliga a construir objetos artificiales con nulls |
| Un DTO genérico para todo | Pierde semántica de la operación específica |
| Métodos con muchos parámetros Optional | Firma confusa: `update(Optional<Email> email, Optional<Phone> phone, ...)` |

## Consecuencias

**Ganamos:**
- **Legibilidad:** Cada operación muestra explícitamente qué modifica
- **Trazabilidad:** Los DTOs documentan la intención del caso de uso
- **Mantenibilidad:** Cambios en una operación no afectan otras

**Perdemos:**
- **Duplicación controlada:** Email, Phone aparecen en múltiples DTOs
- **Más clases:** Un DTO por operación (5-10 DTOs en lugar de 1-2)

## Trade-off consciente

**Duplicación < Claridad semántica**

En dominios sensibles (clínicos, financieros), la claridad vale más que la reducción de líneas de código.

## Implementación

```java
// ✅ Correcto: DTOs específicos
public record UpdateDentistContactDto(
    String email,
    String phone
) {}

public record UpdateDentistSensitiveDto(
    String licenseNumber,
    String specialization
) {}

// ✅ Correcto: métodos explícitos en agregado
public class Dentist {
    public void updateContactData(Email email, PhoneNumber phone) {
        // Validar que no se violen invariantes
        this.contactInfo = new ContactInfo(email, phone);
    }
    
    public void updateSensitiveData(
        LicenseNumber license, 
        Specialization specialization
    ) {
        // Validar permisos especiales si es necesario
        this.license = license;
        this.specialization = specialization;
    }
}

// ✅ Correcto: Application Service mapea DTO a VOs
@Service
public class DentistApplicationService {
    public DentistDto updateContactData(
        UpdateDentistContactDto dto, 
        DentistId id
    ) {
        Dentist dentist = repository.findById(id).orElseThrow();
        
        dentist.updateContactData(
            new Email(dto.email()),
            new PhoneNumber(dto.phone())
        );
        
        return mapper.toDto(repository.save(dentist));
    }
}
```

## Uso interno de PersonData

`PersonData` sigue siendo útil internamente:

```java
// VO interno para validaciones
record PersonData(
    Name name,
    Optional<BirthDate> birthDate,
    Optional<Address> address,
    ContactInfo contactInfo
) {
    // Validaciones comunes
    public void validateForClinicalRecord() {
        if (birthDate.isEmpty()) {
            throw new BusinessRuleViolationException("BirthDate required");
        }
    }
}

// Usado en constructor, no en updates
public static Dentist create(PersonData personData, LicenseNumber license) {
    personData.validateForClinicalRecord();
    return new Dentist(personData, license);
}
```

## Patrón recomendado

```
Operación → DTO específico → Método explícito → Validaciones internas con VOs
```

**Ejemplo:**
```
UpdateContactData → UpdateDentistContactDto → updateContactData(Email, Phone) → ContactInfo(email, phone)
```