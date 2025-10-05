# Value Object: Address (Dirección)

## Propósito

Dirección encapsula la información de ubicación de un actor clínico (Paciente, Responsable, Odontólogo, Secretario) como una unidad semántica validada. Representa no solo una dirección física, sino una estructura coherente que puede ser internacionalizable, trazable y defendible ante flujos clínicos y operativos.

Este VO evita el uso de cadenas planas (String) que no expresan intención ni validación, y permite delegar la responsabilidad de coherencia geográfica al dominio.

## Motivación

La dirección es un dato sensible que puede afectar la logística clínica, la asignación de turnos presenciales, la facturación y la trazabilidad de pacientes. Validar direcciones como texto plano genera ambigüedad, errores operativos y degeneración semántica.

Este VO fue introducido como parte de la migración hacia arquitectura hexagonal y documentado en [ADR-02-value-objects-(Vo)](ClinicaDefinitiva/src/docs/arquitetura/adr/ADR-02-value-objects-(Vo).md), que establece el uso sistemático de VO para encapsular lógica clínica.

## Estructura

```java
public final class Address {
    private final String street;
    private final String city;
    private final String state;
    private final String country;
    private final String postalCode;


    public Address(String street, String city, String state, String country, String postalCode) {
        if (street == null || city == null || state == null || country == null || postalCode == null) {
            throw new NullAddressException(ContextoEntidad.ADDRESS, "Address fields must not be null.");
        }
        if (street.isBlank() || city.isBlank() || state.isBlank() || country.isBlank() || postalCode.isBlank()) {
            throw new BlankAddressException(ContextoEntidad.ADDRESS, "Address fields must not be blank.");
        }

        this.street = street.trim();
        this.city = city.trim();
        this.state = state.trim();
        this.country = country.trim();
        this.postalCode = postalCode.trim();
    }

    //  methods semantic
    public boolean isInCountry(String expectedCountry) {
        return country.equalsIgnoreCase(expectedCountry.trim());
    }

    public boolean isLocalTo(String expectedCity) {
        return city.equalsIgnoreCase(expectedCity.trim());
    }

    public String fullAddress() {
        return street + ", " + city + ", " + state + ", " + country + " - " + postalCode;
    }

    public String postalZone() {
        return postalCode.substring(0, Math.min(3, postalCode.length()));
    }

    public String asText() {
        return fullAddress();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    // methods access

    public String Country() {
        return country;
    }

    public String City() {
        return city;
    }

    public String PostalCode() {
        return postalCode;
    }

    public String State() {
        return state;
    }

    public String Street() {
        return street;
    }

    // methods utility
    @Override
    public String toString() {
        return fullAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return street.equals(address.street) &&
                city.equals(address.city) &&
                state.equals(address.state) &&
                country.equals(address.country) &&
                postalCode.equals(address.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, state, country, postalCode);
    }




}
```
## Reglas clínicas encapsuladas

- Todos los campos son obligatorios.
- El código postal debe tener longitud válida.
- Se eliminan espacios innecesarios para evitar errores operativos.
- Se puede generar formato completo para informes, etiquetas o trazabilidad.

## Uso en el modelo

- Paciente, Responsable, Odontólogo, Secretario → usan Direccion como parte de su perfil clínico.
- Puede ser utilizado en informes, facturación, asignación de turnos presenciales.
- Permite internacionalización del sistema sin romper contratos.

## Ventajas

- Validación centralizada y coherente.
- Delegación semántica al dominio.
- Facilidad para test unitarios.
- Trazabilidad de errores clínicos.
- Preparación para internacionalización.
- Evita ambigüedad en datos sensibles.

## Proyección

Este VO será extendido con:

- Validación por país (formato de código postal, longitud de calle).
- Integración con servicios externos de geolocalización.
- Métodos para comparar direcciones y detectar duplicados.
- Posibilidad de representar direcciones temporales o de emergencia.

## Relación con ADR

- ADR-02: Implementación estratégica de VO
- ADR-01: Migración a arquitectura hexagonal
  