# ADR-03 (Arquitectura): Jerarquía global de excepciones y excepciones para Value Objects de Persona

- Estado: Aceptado
- Fecha: 2025-10-02
- Autores: David

## Contexto
El proyecto ya tiene una excepción raíz que sirve como base de dominio.  
Varios agregados comparten VOs de persona como Age, FullName y Address.  
Es necesario garantizar consistencia, trazabilidad, justificación ética y mapeo estable a la API.

La decisión sobre la jerarquía de excepciones no se limita a los VOs: debe ser una convención global de la capa de dominio que facilite manejo coherente en todos los módulos.

## Decisión
Adoptar una jerarquía de excepciones global en el dominio que sirva como convención para:
- Excepciones de proyecto (raíz).
- Excepciones de validación/transversales de dominio (p. ej. VOs).
- Excepciones agrupadas por grupo semántico (p. ej. person).
- Excepciones de agregado o contexto concreto (p. ej. appointments).

## Reglas
- Mantener una excepción raíz del proyecto en el módulo común.
- Definir una base para validaciones de Value Objects: ValueObjectValidationException.
- Para grupos semánticos compartidos (como VOs de persona) crear una excepción intermedia: PersonValueObjectException.
- Para cada agregado mantener su propia excepción intermedia (ej. AppointmentBusinessRuleViolationException).
- Las excepciones concretas extienden la intermedia pertinente (global → dominio-vo → grupo semántico → agregado/contexto → concreta).
- Todas las excepciones deben llevar código estable, mensaje mínimo público y opcionalmente metadata estructurada (campo, valor inválido, adrId).

## Implementación (plantilla)
```java
// domain-common/exceptions/DomainRootException.java
public class DomainRootException extends RuntimeException {
private final String code;
public DomainRootException(String code, String message) {
super(message);
this.code = code;
}
public String getCode() { return code; }
}

// domain-common/exceptions/ValueObjectValidationException.java
public class ValueObjectValidationException extends DomainRootException {
public ValueObjectValidationException(String code, String message) {
super(code, message);
}
}

// shared/person/exceptions/PersonValueObjectException.java
public class PersonValueObjectException extends ValueObjectValidationException {
public PersonValueObjectException(String code, String message) {
super(code, message);
}
}

// shared/person/exceptions/InvalidAgeException.java
/
* Código: VOPERSONAGE_001
* ADR: ADR-2025-10-02-vo-person-exceptions
* Justificación: Garantiza que la edad registrada esté dentro de límites clínicos aceptables.
  */
  public class InvalidAgeException extends PersonValueObjectException {
  private final Integer invalidValue;
  public InvalidAgeException(Integer invalidValue) {
  super("VOPERSONAGE_001", "Edad inválida");
  this.invalidValue = invalidValue;
  }
  public Integer getInvalidValue() { return invalidValue; }
  }

// Ejemplo VO: shared/person/vo/Age.java
public final class Age {
private final int years;
private Age(int years) { this.years = years; }
public static Age of(int years) {
if (years < 0 || years > 150) {
throw new InvalidAgeException(years);
}
return new Age(years);
}
public int getYears() { return years; }
}
```

## Consecuencias
Positivas
- Convención única y global para excepciones facilita captura selectiva, métricas y trazabilidad.
- Reutilización sin duplicación entre agregados y VOs compartidos.
- Mejora la auditabilidad: códigos y ADRs rastreables desde cualquier error.

## Compensaciones
- Mayor número de clases; crear solo cuando aporte diferenciación en manejo, métricas o documentación.
- Paquetes compartidos introducen dependencias cruzadas; mitigar con un módulo domain-common versionado.

## Mapeos operativos y recomendaciones
- API: capturar DomainRootException y mapear a respuesta estructurada {code, messageKey, field?} con HTTP 400/422.
- Logging: registrar stack completo y metadata internamente; incluir aggregateId y correlationId.
- Catálogo de errores: registrar código, mensaje técnico, clave de localización, ADR id, justificación y ejemplos.
- Versionado: ubicar VOs y excepciones compartidas en domain-common para control de cambios.
- Pruebas: unitarias de fábricas VO que verifiquen excepciones y códigos; pruebas de contrato para el mapeo API.

## Plan de migración
1. Añadir la jerarquía global en paralelo a las implementaciones actuales.
2. Actualizar VOs y agregados para lanzar las nuevas excepciones tipadas.
3. Ajustar mappers API y catálogo de errores.
4. Reemplazar gradualmente throws ad-hoc por excepciones tipadas.
5. Ejecutar pruebas e instrumentar logs/metrics para validar la adopción.

## Relación con otros ADR
- [ADR-02 (Arquitectura): Catálogo de errores clínicos por operación](ADR-02-Catálogo%20de%20errores%20clínicos%20por%20operación.md)

## Notas finales
- La jerarquía propuesta es una convención global para todo el dominio, no solo para VOs.
- Crear excepciones nuevas solo cuando justifiquen manejo distinto, métricas o trazabilidad.
- Mantener mensajes mínimos en código y usar los códigos para localización y exposición segura.  
  