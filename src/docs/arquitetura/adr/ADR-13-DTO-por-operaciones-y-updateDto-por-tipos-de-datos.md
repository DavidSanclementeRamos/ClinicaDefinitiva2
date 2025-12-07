## ADR-13 (Arquitectura): Separar DTOs por operación y DTOs de Update por tipo de datos

- Estado: Aprobado
- Fecha: 2025-11-16
- Autores: David (proponente) y Equipo de Arquitectura

## Contexto
El dominio contiene agregados ricos (ej. Company, Guardian) que exponen operaciones semánticas como register, updateContactData y updateSensitiveData.  
Existen requisitos de seguridad y cumplimiento que obligan a tratar datos sensibles con mayor control y trazabilidad.  
Actualmente hay riesgo de sobrescritura accidental de campos al usar un único DTO de actualización con campos opcionales.  
Se busca claridad en los contratos API, facilidad de validación y menor superficie de error al exponer operaciones parciales.

## Decisión
Adoptar DTOs específicos por operación y, en particular, usar dos DTOs de actualización separados:
- UpdateContactDto para cambios de contacto y metadatos no sensibles.
- UpdateSensitiveDto para cambios que afectan datos sensibles o identificadores personales.

Los servicios de aplicación convertirán solo los valores mínimos necesarios y delegarán la aplicación de reglas al agregado (company.updateContactData(...), company.updateSensitiveData(...)).  
Los mappers se mantendrán puros y se usarán principalmente para Dominio → DTO (lectura).

## Justificación
- Principio de responsabilidad única: cada DTO representa un contrato claro y limitado a una operación concreta.
- Seguridad y privacidad: aislar datos sensibles facilita aplicar controles de autorización, logging y encriptación selectiva.
- Protección de invariantes: evita sobrescritura accidental; el agregado aplica las reglas.
- Validación específica: permite validaciones y mensajes de error más precisos.
- Evolución controlada: cambios en un contrato no afectan a otros consumidores.

## Ventajas
- Mayor seguridad.
- Claridad semántica.
- Menor complejidad en el agregado.
- Mejor trazabilidad.
- Facilidad de pruebas.

## Desventajas y Costes
- Más artefactos a mantener.
- Duplicación parcial de campos.
- Más endpoints o handlers.
- Necesidad de disciplina y gobernanza.

## Alternativas consideradas
1. Un único Update DTO con campos opcionales → Riesgo de sobrescritura accidental, validaciones complejas.
2. Mapper que reconstruye el agregado desde DTO → Rompe invariantes, desplaza reglas fuera del dominio.
3. Assembler centralizado → Mezcla capas, dificulta pruebas unitarias.

Ejemplos mínimos
```java
public record UpdateContactDto(
String id,
AddressDto address,
String phone,
String email,
String status
) {}

public record UpdateSensitiveDto(
String id,
String taxIdentificationNumber,
String legalRepresentative,
String taxRegime
) {}
```

```java
// Service con UpdateContactDto
Company company = repo.findById(CompanyId.of(dto.id())).orElseThrow(...);
company.updateContactData(AddressMapper.fromDto(dto.address()), Phone.of(dto.phone()), dto.email());
repo.save(company);

// Service con UpdateSensitiveDto
Company company = repo.findById(CompanyId.of(dto.id())).orElseThrow(...);
company.updateSensitiveData(dto.taxIdentificationNumber(), dto.legalRepresentative(), dto.taxRegime());
repo.save(company);
```

## Plan de migración
1. Definir DTOs y contratos en especificación OpenAPI.
2. Implementar endpoints nuevos que acepten UpdateContactDto y UpdateSensitiveDto.
3. Refactorizar servicios de aplicación para delegar reglas al agregado.
4. Actualizar mappers para mantener pureza (solo Dominio → DTO).
5. Añadir pruebas unitarias y de integración para cada operación.
6. Documentar en docs/arquitectura/dtos.md las convenciones de separación.

## Relación con otros ADR
- [ADR-05 (Arquitectura): Creación de un módulo independiente para Servicios.](ADR-05-Creación%20de%20un%20módulo%20independiente%20para%20Servicios.md)
- [ADR-06 (Arquitectura): Separación de Facturación y Pagos en módulos independientes.](ADR-06-Separación%20de%20Facturación%20y%20Pagos%20en%20módulos%20independientes.md)
- [ADR-07 (Arquitectura): Redefinición del módulo Administration.](ADR-07-Redefinición%20del%20módulo%20Administration.md) 
  

