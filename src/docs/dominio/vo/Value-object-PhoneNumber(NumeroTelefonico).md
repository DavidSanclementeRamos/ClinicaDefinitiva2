# Value Object: PhoneNumber

## Propósito

PhoneNumber encapsula el número telefónico de un actor clínico como una unidad semántica validada. Representa un dato sensible que debe cumplir reglas de formato, unicidad y trazabilidad. Este VO permite validar estructura, evitar duplicados y generar representaciones consistentes para contacto, notificaciones y auditoría.

## Motivación

El número telefónico es un dato clínico operativo que se usa para contacto directo, recuperación de cuenta, trazabilidad de responsables y validación de identidad. Usarlo como String sin validación genera ambigüedad, errores operativos y degeneración semántica.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en  [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public final class PhoneNumber {
  private static final Pattern VALID_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$");

  private final String value;

  public PhoneNumber(String value) {
    if (value == null) {
      throw new NullPhoneNumberException(ContextoEntidad.PHONE_NUMBER, "Phone number must not be null.");
    }
    if (isBlank(value)) {
      throw new BlankPhoneNumberException(ContextoEntidad.PHONE_NUMBER, "Phone number must not be empty.");
    }
    String normalized = value.trim().replaceAll("\\s+", "");
    if (!VALID_PATTERN.matcher(normalized).matches()) {
      throw new InvalidPhoneNumberException(ContextoEntidad.PHONE_NUMBER, "Invalid phone number format.");
    }
    this.value = normalized;
  }

  // methods semantic
  public boolean isInternational() {
    return value.startsWith("+");
  }

  public boolean isLocalTo(String countryCode) {
    return value.startsWith(countryCode);
  }

  public String masked() {
    int visibleDigits = Math.min(4, value.length());
    return "***" + value.substring(value.length() - visibleDigits);
  }

  public String asText() {
    return value;
  }

  private boolean isBlank(String input) {
    return input == null || input.trim().isEmpty();
  }

  // methods access
  public String Value() {
    return value;
  }

  // methods utility
  @Override
  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PhoneNumber)) return false;
    PhoneNumber that = (PhoneNumber) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }



}

```
## Reglas clínicas encapsuladas

- El número no puede estar vacío.
- Se normaliza eliminando caracteres no numéricos.
- Debe tener entre 7 y 15 dígitos.
- Puede validar si es móvil colombiano (startsWith("3")).
- Permite generar formato internacional para interoperabilidad.

## Uso en el modelo

- Paciente, Responsable, Odontólogo, Secretario → usan PhoneNumber como parte de su perfil clínico.
- Se utiliza en contacto directo, recuperación de cuenta, notificaciones y trazabilidad.
- Permite validaciones cruzadas para evitar duplicados en el sistema.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Mejora la integridad operativa del sistema.
- Evita ambigüedad y errores de formato.
- Permite interoperabilidad internacional.

## Proyección

Este VO será extendido con:

- Validación por país (prefijos, longitud).
- Integración con servicios de verificación (SMS, WhatsApp).
- Métodos para anonimización en reportes.
- Soporte para múltiples números por actor (personal, emergencia).

## Relación con ADR

- ADR-031: Implementación estratégica de VO
- ADR-030: Migración a arquitectura hexagonal
  `
