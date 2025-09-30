# Value Object: PhoneNumber

## Propósito

PhoneNumber encapsula el número telefónico de un actor clínico como una unidad semántica validada. Representa un dato sensible que debe cumplir reglas de formato, unicidad y trazabilidad. Este VO permite validar estructura, evitar duplicados y generar representaciones consistentes para contacto, notificaciones y auditoría.

## Motivación

El número telefónico es un dato clínico operativo que se usa para contacto directo, recuperación de cuenta, trazabilidad de responsables y validación de identidad. Usarlo como String sin validación genera ambigüedad, errores operativos y degeneración semántica.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en  [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public class PhoneNumber {

    private final String value;

    public PhoneNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new ClinicalValidationException("El número telefónico es obligatorio");
        }

        String normalized = value.replaceAll("[^\\d]", "");

        if (normalized.length() < 7 || normalized.length() > 15) {
            throw new ClinicalValidationException("Número telefónico inválido: " + value);
        }

        this.value = normalized;
    }

    public String getValue() {
        return value;
    }

    public boolean isColombianMobile() {
        return value.startsWith("3") && value.length() == 10;
    }

    public String formatInternational(String countryCode) {
        return "+" + countryCode + " " + value;
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
